package com.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bilibili.dto.CategoryCreateDTO;
import com.bilibili.entity.TbCategory;

/**
 * 视频分区表
 */
public interface ICategoryService extends IService<TbCategory> {

    /**
     * 创建分类
     *
     * @param categoryDTO
     */
    void create(CategoryCreateDTO categoryDTO);
}
