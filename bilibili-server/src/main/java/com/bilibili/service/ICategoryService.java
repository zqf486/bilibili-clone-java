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


}
