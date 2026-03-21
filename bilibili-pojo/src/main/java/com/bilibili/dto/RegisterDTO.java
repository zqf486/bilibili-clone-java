package com.bilibili.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 用户登录表单
 */
@Data
public class RegisterDTO {
    /**
     * 密码正则: 最少8位，至少1个大写，1个小写，1个数字
     */
    private static final String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";

    /**
     * 邮箱
     */
    @NotNull
    @Email
    @Size(max = 150)
    private String email;
    /**
     * 用户名
     */
    @NotBlank
    @Size(max = 20)
    private String nickName;
    /**
     * 密码
     */
    @NotNull
    @Pattern(regexp = REGEX_PASSWORD)
    private String registerPassword;
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
