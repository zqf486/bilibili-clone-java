package com.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bilibili.dto.CategoryDTO;
import com.bilibili.entity.TbCategory;
import com.bilibili.vo.CategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 视频分区表
 */
public interface ICategoryService extends IService<TbCategory> {

    /**
     * 获取启用的分类
     * 只读取redis缓存, 不操作db
     *
     * @return
     */
    List<CategoryVO> listWithStatus();

    /**
     * 缓存重建
     */
    void rebuildCache();

    /**
     * 新增分类并缓存重建
     *
     * @param categoryDTO
     */
    void addCategory(@Valid CategoryDTO categoryDTO);

    /**
     * 更新分类并缓存重建
     *
     * @param categoryDTO
     */
    void updateCategory(@Valid CategoryDTO categoryDTO);

    /**
     * 删除分类并缓存重建
     *
     * @param id
     */
    void deleteCategory(Integer id);

    /**
     * 切换分类状态并缓存重建
     *
     * @param id
     */
    void toggleStatus(Integer id);

    /**
     * 获取全量分类列表（管理后台用）
     *
     * @return
     */
    List<CategoryVO> listAll();
}
