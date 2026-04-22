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
}
