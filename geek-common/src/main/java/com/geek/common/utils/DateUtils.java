package com.geek.common.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间工具类
 * 
 * @author geek
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM" };

    /**
     * 获取当前 UTC 时刻（推荐：与数据库 timestamptz 一致）
     */
    public static Instant getNowInstant() {
        return Instant.now();
    }

    /**
     * 获取当前Date型日期（兼容旧代码，新代码请用 getNowInstant）
     */
    @Deprecated
    public static Date getNowDate() {
        return new Date();
    }

    /** Instant 转 Date（系统默认时区解释） */
    public static Date toDate(Instant instant) {
        return instant == null ? null : Date.from(instant);
    }

    /** Date 转 Instant */
    public static Instant toInstant(Date date) {
        return date == null ? null : date.toInstant();
    }

    /**
     * 将「日期范围」参数转为 Instant（用于查询条件）。支持 yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss，按 UTC 解析。
     *
     * @param obj params 中的 beginXxx/endXxx（String 或 Date 或 Instant）
     * @return 若为 null 或空字符串返回 null；否则解析为 Instant
     */
    public static Instant parseToInstant(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Instant) {
            return (Instant) obj;
        }
        if (obj instanceof Date) {
            return ((Date) obj).toInstant();
        }
        String str = obj.toString().trim();
        if (str.isEmpty()) {
            return null;
        }
        try {
            if (str.length() <= 10) {
                return LocalDate.parse(str).atStartOfDay(ZoneId.of("UTC")).toInstant();
            }
            return LocalDateTime.parse(str.replace(" ", "T")).atZone(ZoneId.of("UTC")).toInstant();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     * 
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Instant date1, Instant date2) {
        return Math.toIntExact(Math.abs(ChronoUnit.DAYS.between(date1, date2)));
    }

    /**
     * 如果涉及“自然日”差异（很多系统需要）
     * 例如：
     *      2026-03-07 23:00
     *      2026-03-08 01:00
     * 实际只差：
     *      2小时
     * 但很多业务希望：
     *      相差1天
     * @param d1
     * @param d2
     * @param zone
     * @return
     */
    public static long differentDays(Instant d1, Instant d2, ZoneId zone) {
        LocalDate date1 = d1.atZone(zone).toLocalDate();
        LocalDate date2 = d2.atZone(zone).toLocalDate();
        return Math.abs(ChronoUnit.DAYS.between(date1, date2));
    }
    
    /**
     * 计算时间差
     *
     * @param endTime   最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static String timeDistance(Date endTime, Date startTime) {
        long nd = 1000L * 24 * 60 * 60;
        long nh = 1000L * 60 * 60;
        long nm = 1000L * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endTime.getTime() - startTime.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /** @deprecated */
    @Deprecated
    private DateUtils() {
    }
}
