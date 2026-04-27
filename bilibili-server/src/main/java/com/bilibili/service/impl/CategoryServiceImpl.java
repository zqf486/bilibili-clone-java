package com.bilibili.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.constant.CacheConstant;
import com.bilibili.constant.MessageConstant;
import com.bilibili.constant.RedisConstant;
import com.bilibili.dto.CategoryCreateDTO;
import com.bilibili.entity.TbCategory;
import com.bilibili.exception.BusinessException;
import com.bilibili.mapper.CategoryMapper;
import com.bilibili.service.ICategoryService;
import com.bilibili.util.RedisUtil;
import com.bilibili.vo.CategoryVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, TbCategory> implements ICategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 创建分类
     * TODO: 设置默认图标与背景 redis缓存重建
     *
     * @param categoryCreateDTO
     */
    @Override
    public void create(CategoryCreateDTO categoryCreateDTO) {

        // 1.检查父分类id是否合法
        if (categoryCreateDTO.getPCategoryId() != null) {
            TbCategory pCategory = this.lambdaQuery()
                    .eq(TbCategory::getId, categoryCreateDTO.getPCategoryId())
                    .one();
            if (pCategory == null) {
                throw new BusinessException(MessageConstant.P_CATEGORY_NOT_EXISTS);
            }
            if (pCategory.getPCategoryId() != null) {
                throw new BusinessException(MessageConstant.P_CATEGORY_ILLEGAL);
            }
        }

        // 2.拷贝属性
        TbCategory tbCategory = BeanUtil.copyProperties(categoryCreateDTO, TbCategory.class);

        // 3.设置默认禁用状态
        tbCategory.setStatus((byte) 0);

        // 4.设置图标
        tbCategory.setIcon("");

        // 5.设置背景
        tbCategory.setBackground("");

        // 6.删除redis缓存
        redisUtil.delete(CacheConstant.CATEGORY_TREE_KEY);

        // 6.保存
        this.save(tbCategory);
    }

    /**
     * 获取完整分类树结构
     */
    @Override
    public List<CategoryVO> tree() {
        // 1.查询缓存
        List<CategoryVO> tree = (List<CategoryVO>) redisUtil.get(CacheConstant.CATEGORY_TREE_KEY, List.class);

        // 2.查询成功返回
        if (tree != null) {
            return tree;
        }

        // 3.查询不成功查数据库
        List<TbCategory> list = this.list();

        // 4.生成树
        tree = buildTree(list);

        // 5.设置redis缓存
        redisUtil.set(CacheConstant.CATEGORY_TREE_KEY, tree, RedisConstant.FOREVER_TTL);

        return tree;
    }

    /**
     * 根据TbCategory数据表生成树
     *
     * @param list List TbCategory
     * @return tree
     */
    private List<CategoryVO> buildTree(List<TbCategory> list) {

        if (list == null) {
            return null;
        }

        // 1.获取一级节点
        List<CategoryVO> parents = list.stream()
                .filter(tb -> tb.getPCategoryId() == null)
                .map(tb -> BeanUtil.copyProperties(tb, CategoryVO.class))
                .toList();

        // 2.为一级节点找子节点
        for (CategoryVO parent : parents) {
            List<CategoryVO> children = list.stream()
                    .filter(tb -> tb.getPCategoryId().equals(parent.getId()))
                    .map(tb -> BeanUtil.copyProperties(tb, CategoryVO.class))
                    .toList();

            parent.setChildren(children);
        }

        return parents;
    }
}
