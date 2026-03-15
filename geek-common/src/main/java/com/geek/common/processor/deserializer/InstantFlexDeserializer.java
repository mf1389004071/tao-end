package com.geek.common.processor.deserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 支持「仅日期」与「日期时间」的 Instant 反序列化器。
 * 前端传 "yyyy-MM-dd" 时按当日 00:00:00 UTC 解析；传 ISO-8601 日期时间则按标准解析。
 */
public class InstantFlexDeserializer extends JsonDeserializer<Instant> {

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        if (value == null || value.isBlank()) {
            return null;
        }
        String str = value.trim();
        try {
            if (str.length() <= 10) {
                return LocalDate.parse(str).atStartOfDay(ZoneId.of("UTC")).toInstant();
            }
            return LocalDateTime.parse(str.replace(" ", "T")).atZone(ZoneId.of("UTC")).toInstant();
        } catch (Exception e) {
            try {
                return Instant.parse(str);
            } catch (Exception e2) {
                throw new IOException("Cannot parse Instant from: " + str, e);
            }
        }
    }
}
