package com.bilibili.controller.admin;

import com.bilibili.annotation.LoginRequired;
import com.bilibili.dto.CategoryDTO;
import com.bilibili.result.Result;
import com.bilibili.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "CategoryAdminController")
@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryAdminController {

    @Resource
    private ICategoryService categoryService;

    /**
     * 新增分类
     *
     * @param categoryDTO
     * @return
     */
    @Operation(summary = "新增分类")
    @LoginRequired
    @PostMapping
    public Result add(@Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 修改分类
     *
     * @param categoryDTO
     * @return
     */
    @Operation(summary = "修改分类")
    @LoginRequired
    @PutMapping
    public Result update(@Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @Operation(summary = "删除分类")
    @LoginRequired
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }

    /**
     * 切换分类状态
     *
     * @param id
     * @return
     */
    @Operation(summary = "切换分类状态")
    @LoginRequired
    @PatchMapping("/{id}")
    public Result status(@PathVariable Integer id) {
        categoryService.toggleStatus(id);
        return Result.success();
    }

    /**
     * 获取全量分类列表
     *
     * @return
     */
    @Operation(summary = "获取全量分类列表")
    @LoginRequired
    @GetMapping
    public Result list() {
        return Result.success(categoryService.listAll());
    }
}
