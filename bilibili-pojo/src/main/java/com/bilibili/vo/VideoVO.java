package com.bilibili.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String description;
    private String url;
    private String cover;
    private Integer categoryId;
    private String categoryName;
    private Long userId;
    private String nickname;
    private String avatar;
    private Integer duration;
    private Byte status;
    private Integer views;
    private Integer likes;
    private Integer favorites;
    private Integer coins;
    private Integer comments;
    private String tags;
    private LocalDateTime createTime;
}
