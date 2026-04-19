package com.bilibili.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.constant.MessageConstant;
import com.bilibili.constant.RedisConstant;
import com.bilibili.dto.CategoryDTO;
import com.bilibili.entity.TbCategory;
import com.bilibili.exception.BusinessException;
import com.bilibili.mapper.CategoryMapper;
import com.bilibili.service.ICategoryService;
import com.bilibili.util.RedisUtil;
import com.bilibili.vo.CategoryVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, TbCategory> implements ICategoryService {

    @Resource
    private RedisUtil redisUtil;
    
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 获取启用的分类
     * 只读取redis缓存, 不操作db
     *
     * @return
     */
    @Override
    public List<CategoryVO> listWithStatus() {
        try {
            // 尝试从缓存获取
            String key = RedisConstant.REDIS_KEY_PREFIX + RedisConstant.CACHE_CATEGORY_KEY;
            String json = stringRedisTemplate.opsForValue().get(key);
            if (json == null || json.isEmpty()) {
                // 缓存不存在，重建缓存
                this.rebuildCache();
                json = stringRedisTemplate.opsForValue().get(key);
                if (json == null || json.isEmpty()) {
                    return new ArrayList<>();
                }
            }
            
            // 解析JSON为List
            List<?> rawList = objectMapper.readValue(json, List.class);
            List<CategoryVO> list = new ArrayList<>();
            for (Object item : rawList) {
                CategoryVO vo = objectMapper.convertValue(item, CategoryVO.class);
                list.add(vo);
            }
            
            // 过滤出状态为显示的分类（status == 1）
            return list.stream()
                    .filter(category -> category.getStatus() != null && category.getStatus() == 1)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("从缓存获取分类列表失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 缓存重建
     */
    @Override
    public void rebuildCache() {
        List<TbCategory> list = this.list();
        List<CategoryVO> voList = list.stream()
                .map(category -> BeanUtil.copyProperties(category, CategoryVO.class))
                .collect(Collectors.toList());
        redisUtil.set(RedisConstant.REDIS_KEY_PREFIX, RedisConstant.CACHE_CATEGORY_KEY, voList, RedisConstant.FOREVER_TTL);
    }

    /**
     * 新增分类并缓存重建
     *
     * @param categoryDTO
     */
    @Override
    @Transactional
    public void addCategory(CategoryDTO categoryDTO) {
        // 检查分类名称是否已存在
        boolean exists = lambdaQuery()
                .eq(TbCategory::getName, categoryDTO.getName())
                .count() > 0;
        if (exists) {
            throw new BusinessException(MessageConstant.CATEGORY_NAME_ALREADY_EXISTS);
        }
        
        TbCategory tbCategory = BeanUtil.copyProperties(categoryDTO, TbCategory.class);
        tbCategory.setStatus((byte) 0);
        tbCategory.setCreateTime(LocalDateTime.now());
        tbCategory.setUpdateTime(LocalDateTime.now());
        this.save(tbCategory);
        this.rebuildCache();
    }

    /**
     * 更新分类并缓存重建
     *
     * @param categoryDTO
     */
    @Override
    @Transactional
    public void updateCategory(CategoryDTO categoryDTO) {
        if (categoryDTO.getId() == null) {
            throw new BusinessException(MessageConstant.CATEGORY_ID_CANNOT_BE_NULL);
        }
        
        // 检查分类名称是否已存在（排除自身）
        boolean exists = lambdaQuery()
                .eq(TbCategory::getName, categoryDTO.getName())
                .ne(TbCategory::getId, categoryDTO.getId())
                .count() > 0;
        if (exists) {
            throw new BusinessException(MessageConstant.CATEGORY_NAME_ALREADY_EXISTS);
        }
        
        TbCategory tbCategory = BeanUtil.copyProperties(categoryDTO, TbCategory.class);
        tbCategory.setUpdateTime(LocalDateTime.now());
        
        boolean updated = this.updateById(tbCategory);
        if (!updated) {
            throw new BusinessException(MessageConstant.CATEGORY_NOT_EXISTS_OR_UPDATE_FAILED);
        }
        
        this.rebuildCache();
    }

    /**
     * 删除分类并缓存重建
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteCategory(Integer id) {
        if (id == null) {
            throw new BusinessException(MessageConstant.CATEGORY_ID_CANNOT_BE_NULL);
        }
        
        boolean removed = this.removeById(id);
        if (!removed) {
            throw new BusinessException(MessageConstant.CATEGORY_NOT_EXISTS_OR_DELETE_FAILED);
        }
        
        this.rebuildCache();
    }

    /**
     * 切换分类状态并缓存重建
     *
     * @param id
     */
    @Override
    @Transactional
    public void toggleStatus(Integer id) {
        if (id == null) {
            throw new BusinessException(MessageConstant.CATEGORY_ID_CANNOT_BE_NULL);
        }
        
        TbCategory category = this.getById(id);
        if (category == null) {
            throw new BusinessException(MessageConstant.CATEGORY_NOT_EXISTS);
        }
        
        Byte newStatus = (category.getStatus() == null || category.getStatus() == 0) ? (byte) 1 : (byte) 0;
        
        LambdaUpdateWrapper<TbCategory> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TbCategory::getId, id)
                    .set(TbCategory::getStatus, newStatus)
                    .set(TbCategory::getUpdateTime, LocalDateTime.now());
        
        this.update(updateWrapper);
        this.rebuildCache();
    }

    /**
     * 获取全量分类列表（管理后台用）
     *
     * @return
     */
    @Override
    public List<CategoryVO> listAll() {
        List<TbCategory> list = this.list();
        return list.stream()
                .map(category -> BeanUtil.copyProperties(category, CategoryVO.class))
                .collect(Collectors.toList());
    }
}
