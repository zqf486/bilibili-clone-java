package com.bilibili.constant;

public class TbVideoTaskConstant {

    public enum Status {
        UPLOADING(0, "上传中"),
        MERGING(1, "合并中"),
        FINISHED(2, "上传完成"),
        FAILED(3, "上传失败");

        private final Integer code;
        private final String desc;

        Status(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() { return code; }
        public String getDesc() { return desc; }
    }
}
