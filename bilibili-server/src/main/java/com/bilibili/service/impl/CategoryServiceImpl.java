package com.bilibili.service.impl;

import cn.hutool.Hutool;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.dto.CategoryCreateDTO;
import com.bilibili.entity.TbCategory;
import com.bilibili.exception.BusinessException;
import com.bilibili.mapper.CategoryMapper;
import com.bilibili.service.ICategoryService;
import com.bilibili.util.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, TbCategory> implements ICategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 创建分类
     * TODO: 设置默认图标与背景
     *
     * @param categoryCreateDTO
     */
    @Override
    public void create(CategoryCreateDTO categoryCreateDTO) {

        // 1.检查父分类id是否合法
        if (categoryCreateDTO.getPCategoryId() != null) {
            boolean exists = this.lambdaQuery()
                    .eq(TbCategory::getId, categoryCreateDTO.getPCategoryId())
                    .exists();
            if(!exists){
                throw new BusinessException("父分类不存在");
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

        this.save(tbCategory);
    }
}
