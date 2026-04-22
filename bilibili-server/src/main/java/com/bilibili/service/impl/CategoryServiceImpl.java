package com.bilibili.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.constant.CacheConstant;
import com.bilibili.constant.MessageConstant;
import com.bilibili.constant.RedisConstant;
import com.bilibili.dto.CategoryDTO;
import com.bilibili.entity.TbCategory;
import com.bilibili.exception.BusinessException;
import com.bilibili.mapper.CategoryMapper;
import com.bilibili.service.ICategoryService;
import com.bilibili.util.RedisUtil;
import com.bilibili.vo.CategoryVO;
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

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, TbCategory> implements ICategoryService {

    @Resource
    private RedisUtil redisUtil;
}
