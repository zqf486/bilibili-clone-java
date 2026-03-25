package com.bilibili.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户登录表单
 */
@Data
public class LoginDTO {
    /**
     * 邮箱
     */
    @NotBlank
    @Email
    @Size(max = 150)
    private String email;
    /**
     * 密码
     */
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
    /**
     * 图片验证码 redis key
     */
    @NotBlank
    @Size(max = 36)
    private String checkCodeKey;
    /**
     * 图片验证码结果
     */
    @Size(max = 5)
    @NotBlank
    private String checkCode;
}