package com.bilibili.controller.web;

import com.bilibili.result.Result;
import com.bilibili.service.ICategoryService;
import com.bilibili.vo.CategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "CategoryController")
@Slf4j
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Resource
    private ICategoryService categoryService;

    /**
     * 获取展示列表（仅返回启用的分类）
     *
     * @return
     */
    @Operation(summary = "获取分类列表")
    @GetMapping
    public Result list() {
        log.info("获取分类列表");
        List<CategoryVO> categories = categoryService.listWithStatus();
        return Result.success(categories);
    }

    /**
     * 根据ID获取分类详情
     *
     * @param id 分类ID
     * @return
     */
    @Operation(summary = "根据ID获取分类详情")
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据ID获取分类详情: {}", id);
        CategoryVO category = categoryService.getCategoryById(id);
        if (category == null) {
            return Result.success();
        }
        return Result.success(category);
    }
}
