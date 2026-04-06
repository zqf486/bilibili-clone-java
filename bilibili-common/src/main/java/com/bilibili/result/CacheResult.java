package com.bilibili.result;

import lombok.AllArgsConstructor;import lombok.Data;import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class CacheResult<T> {
    private boolean exist;
    private boolean empty;
    private T data;
}
