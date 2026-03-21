use `bilibili-clone`;

CREATE TABLE `tb_user`
(
    `id`              BIGINT       NOT NULL COMMENT '用户ID（主键）',
    `username`        VARCHAR(50)  NOT NULL COMMENT '用户名（唯一）',
    `password`        VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    `salt`            varchar(32)  NOT NULL COMMENT '盐',
    `email`           VARCHAR(100)          DEFAULT NULL COMMENT '邮箱',
    `phone`           VARCHAR(20)           DEFAULT NULL COMMENT '手机号',
    `avatar`          VARCHAR(255)          DEFAULT NULL COMMENT '头像URL',
    `nickname`        VARCHAR(50)           DEFAULT NULL COMMENT '昵称',
    `signature`       VARCHAR(255)          DEFAULT NULL COMMENT '个性签名',
    `gender`          TINYINT               DEFAULT 0 COMMENT '性别：0未知 1男 2女',
    `status`          TINYINT               DEFAULT 1 COMMENT '状态：1正常 0封禁',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `last_login_time` DATETIME              DEFAULT NULL COMMENT '最后登录时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='用户表';