package com.bilibili.controller.admin;

import cn.hutool.db.PageResult;
import com.bilibili.dto.CategoryCreateDTO;
import com.bilibili.result.Result;
import com.bilibili.service.ICategoryService;
import com.bilibili.vo.CategoryAdminVO;
import com.bilibili.vo.CategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "CategoryAdminController")
@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryAdminController {

    @Resource
    private ICategoryService categoryService;

    /**
     * 分页查询分类列表
     *
     * @param name
     * @param status
     * @param page
     * @param size
     * @return
     */
    @Operation(summary = "分页查询分类")
    @GetMapping
    public Result<PageResult<CategoryAdminVO>> page(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Byte status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return Result.success();
    }

    /**
     * 创建分类
     *
     * @param categoryDTO
     * @return
     */
    @Operation(summary = "创建分类")
    @PostMapping
    public Result create(@RequestBody CategoryCreateDTO categoryDTO) {
        categoryService.create(categoryDTO);
        return Result.success();
    }

    /**
     * 修改分类
     *
     * @param id
     * @param categoryDTO
     * @return
     */
    @Operation(summary = "修改分类")
    @PutMapping("/{id}")
    public Result modify(
            @PathVariable Long id,
            @RequestBody CategoryCreateDTO categoryDTO
    ) {
        return Result.success();
    }

    /**
     * 启用禁用
     *
     * @param id
     * @return
     */
    @Operation(summary = "启用禁用")
    @PatchMapping("/{id}/status")
    public Result status(@PathVariable Long id) {
        categoryService.toggleStatus(id);
        return Result.success();
    }

    /**
     * 调整排序
     *
     * @return
     */
    @Operation(summary = "调整排序")
    @PatchMapping("/{id}/sort")
    public Result sort(@PathVariable Long id) {
        return Result.success();
    }

    /**
     * 获取完整树形结构
     *
     * @return
     */
    @Operation(summary = "获取完整树形结构")
    @GetMapping("/tree")
    public Result tree() {
        List<CategoryVO> tree = categoryService.tree();
        return Result.success(tree);
    }
}
