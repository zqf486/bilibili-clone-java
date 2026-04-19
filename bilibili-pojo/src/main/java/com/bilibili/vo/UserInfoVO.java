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
public class UserInfoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID（主键）
     */
    private Long id;

    /**
     * 用户名（唯一）
     */
    private String username;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 性别：0未知 1男 2女
     */
    private Byte gender;

    /**
     * 状态：1正常 0封禁
     */
    private Byte status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
}
