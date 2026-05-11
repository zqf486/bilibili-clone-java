use `bilibili-clone`;

CREATE TABLE `tb_user`
(
    `id`              BIGINT       NOT NULL COMMENT '用户ID（主键）',
    `username`        VARCHAR(50)  NOT NULL COMMENT '用户名（唯一）',
    `password`        VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    `salt`            varchar(32)  NOT NULL COMMENT '盐',
    `email`           VARCHAR(100) NOT NULL COMMENT '邮箱 (唯一) ',
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
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY uk_email (email)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='用户表';

CREATE TABLE `tb_category`
(
    `id`            INT(11)     NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `code`          VARCHAR(50) NOT NULL COMMENT '分类编码',
    `name`          VARCHAR(50) NOT NULL COMMENT '分类名称',
    `p_category_id` INT(11)              DEFAULT NULL COMMENT '父分类ID',
    `icon`          VARCHAR(255)         DEFAULT NULL COMMENT '图标',
    `background`    VARCHAR(255)         DEFAULT NULL COMMENT '背景图',
    `sort`          INT                  DEFAULT 0 COMMENT '排序',
    `status`        TINYINT              DEFAULT 1 COMMENT '1启用 0禁用',
    `create_time`   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`),
    KEY `idx_parent_id` (`p_category_id`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='视频分区表';

CREATE TABLE `tb_video`
(
    `id`            BIGINT       NOT NULL COMMENT '视频ID（主键）',
    `title`         VARCHAR(100) NOT NULL COMMENT '视频标题',
    `description`   VARCHAR(2000)         DEFAULT NULL COMMENT '视频简介',
    `cover`         VARCHAR(500)          DEFAULT NULL COMMENT '封面图',
    `category_id`   INT(11)               DEFAULT NULL COMMENT '分类ID',
    `user_id`       BIGINT       NOT NULL COMMENT '发布者ID',
    `tags`          VARCHAR(500)          DEFAULT NULL COMMENT '标签（逗号分隔）',
    `episode_count` INT(11)               DEFAULT 1 COMMENT '总集数',
    `status`        TINYINT               DEFAULT 0 COMMENT '状态：0审核中 1已发布 2审核不通过 3私密',
    `views`         INT(11)               DEFAULT 0 COMMENT '播  放量',
    `likes`         INT(11)               DEFAULT 0 COMMENT '点赞数',
    `favorites`     INT(11)               DEFAULT 0 COMMENT '收藏数',
    `coins`         INT(11)               DEFAULT 0 COMMENT '硬币数',
    `comments`      INT(11)               DEFAULT 0 COMMENT '评论数',
    `is_delete`     TINYINT               DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='视频主表';

CREATE TABLE `tb_video_episode`
(
    `id`               BIGINT   NOT NULL COMMENT '分集ID（主键）',
    `video_id`         BIGINT   NOT NULL COMMENT '所属投稿ID',
    `episode_index`    INT(11)  NOT NULL COMMENT '集数 (从1开始)',
    `title`            VARCHAR(100)      DEFAULT NULL COMMENT '分集标题',
    `duration`         INT(11)           DEFAULT 0 COMMENT '时长（秒）',
    `views`            INT(11)           DEFAULT 0 COMMENT '播放量',
    `transcode_status` TINYINT           DEFAULT 0 COMMENT '转码状态：0待处理 1处理中 2完成 3失败',
    `error_msg`        VARCHAR(500)      DEFAULT NULL COMMENT '转码错误信息',
    `is_delete`        TINYINT           DEFAULT 0 COMMENT '逻辑删除',
    `create_time`      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    KEY `idx_video_id` (`video_id`),
    KEY `idx_transcode_status` (`transcode_status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='视频分集表';

CREATE TABLE `tb_video_stream`
(
    `id`          BIGINT       NOT NULL COMMENT '流ID（主键）',
    `episode_id`  BIGINT       NOT NULL COMMENT '所属分集ID',
    `quality`     TINYINT      NOT NULL COMMENT '清晰度：1=480P 2=720P 3=1080P 4=4K SDR 5=4K HDR',
    `url`         VARCHAR(500) NOT NULL COMMENT '播放地址',
    `file_size`   BIGINT                DEFAULT 0 COMMENT '文件大小（字节）',
    `width`       INT(11)               DEFAULT 0 COMMENT '视频宽度',
    `height`      INT(11)               DEFAULT 0 COMMENT '视频高度',
    `bitrate`     INT(11)               DEFAULT 0 COMMENT '码率（kbps）',
    `codec`       VARCHAR(50)           DEFAULT NULL COMMENT '编码格式',
    `status`      TINYINT               DEFAULT 0 COMMENT '状态：0转码中 1可播放',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    KEY `idx_episode_id` (`episode_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='视频流清晰度表';

CREATE TABLE `tb_upload_task`
(
    `id`            BIGINT   NOT NULL COMMENT '任务ID（主键）',
    `user_id`       BIGINT   NOT NULL COMMENT '用户ID',
    `md5`           CHAR(32) NOT NULL COMMENT '文件MD5',
    `file_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
    `mime_type`     VARCHAR(100)      DEFAULT NULL COMMENT '文件MIME类型',
    `file_size`     BIGINT   NOT NULL COMMENT '文件总大小',
    `chunk_size`    INT      NOT NULL COMMENT '分片大小（字节）',
    `total_chunks`  INT      NOT NULL COMMENT '总分片数',
    `upload_id`     VARCHAR(255)      DEFAULT NULL COMMENT 'MinIO multipart upload ID',
    `object_name`   VARCHAR(500)      DEFAULT NULL COMMENT 'MinIO 对象名',
    `status`        TINYINT  NOT NULL DEFAULT 0 COMMENT '状态：0初始化 1上传中 2已完成 3失败 4已取消 5已过期',
    `expire_time`   DATETIME          DEFAULT NULL COMMENT '任务过期时间',
    `finish_time`   DATETIME          DEFAULT NULL COMMENT '完成时间',
    `error_message` VARCHAR(500)      DEFAULT NULL COMMENT '失败原因',
    `create_time`   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_md5` (`md5`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='上传任务表';
