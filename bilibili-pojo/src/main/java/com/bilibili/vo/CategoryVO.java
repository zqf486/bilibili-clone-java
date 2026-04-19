package com.bilibili.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID（主键）
     */
    private Integer id;

    /**
     * 分类名称（如：科技、游戏）
     */
    private String name;

    /**
     * 排序字段（数值越小越靠前）
     */
    private Integer sort;

    /**
     * 状态：1显示 0隐藏
     */
    private Byte status;
}
