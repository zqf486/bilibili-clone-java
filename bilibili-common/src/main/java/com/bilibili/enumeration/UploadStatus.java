package com.bilibili.enumeration;

public enum UploadStatus {

    INIT(0),
    UPLOADING(1),
    SUCCESS(2),
    FAILED(3),
    CANCELED(4),
    EXPIRED(5);

    private final int code;

    UploadStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}