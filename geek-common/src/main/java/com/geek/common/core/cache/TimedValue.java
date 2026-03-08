package com.geek.common.core.cache;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 包装带有单次写入 TTL 的值。TTL 毫秒为相对时长，由 ExpiryPolicy 在创建/更新时读取。
 */
@Data
public final class TimedValue<T> implements Serializable {
    private final T data;
    private final long ttlMillis;
    private final Instant createTime;

    public TimedValue(T data, long ttlMillis) {
        this.data = data;
        this.ttlMillis = ttlMillis;
        this.createTime = com.geek.common.utils.DateUtils.getNowInstant();
    }

    @JsonCreator
    public TimedValue(
            @JsonProperty("data") T data,
            @JsonProperty("ttlMillis") long ttlMillis,
            @JsonProperty("createTime") Instant createTime) {
        this.data = data;
        this.ttlMillis = ttlMillis;
        this.createTime = createTime != null ? createTime : com.geek.common.utils.DateUtils.getNowInstant();
    }

    public boolean isExpired() {
        if (ttlMillis <= 0) {
            return false;
        }
        return System.currentTimeMillis() - createTime.toEpochMilli() >= ttlMillis;
    }
}
