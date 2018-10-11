package wuxingxing.me.utils.date;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author xingxing.wu
 * @date 2018/7/4
 */
public class DateUtil {
    /**
     *  java8 LocalDate 转化成旧的日期
     * @param localDate
     * @return
     */
    public static Date localDate2Utildate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }


    /**
     * LocalDate对应的毫秒值
     * @param localDate
     * @return
     */
    public static long parseLocalDate2TimeMillis(LocalDate localDate) {
        return localDate2Utildate(localDate).getTime();
    }


    /**
     * 格式化时间，格式yyyyMMdd
     * @param localDate
     * @return
     */
    public static String getDateStrWithFormate(LocalDate localDate) {
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMdd");
        return dtf2.format(localDate);
    }


    /**
     * 格式化时间，格式yyyy-MM-dd hh:mm:ss
     * @param localDateTime
     * @return
     */
    public static String getDateStrWithFormate2(LocalDateTime localDateTime) {
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        return dtf2.format(localDateTime);
    }


    /**
     * 获取昨天日期的格式化字符形式
     * @return
     */
    public static String getYestedayDateStrWithFormate() {
        LocalDate yesterday = LocalDate.now().minusDays(1L);
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMdd");
        return dtf2.format(yesterday);
    }


    /**
     * 判断时间是否在某天内
     * @param localDateTime
     * @param localDate
     * @return
     */
    public static boolean ifDateTimeInDate(LocalDateTime localDateTime, LocalDate localDate) {
        LocalDate date = LocalDate.of(localDateTime.getYear(),
                localDateTime.getMonth(),
                localDateTime.getDayOfMonth());
        return localDate.compareTo(date) == 0;
    }


    /**
     * 判断某个时间是否在昨天范围
     * @param localDateTime
     * @return
     */
    public static boolean ifDateTimeInToday(LocalDateTime localDateTime) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return ifDateTimeInDate(localDateTime, yesterday);
    }


    /**
     * 毫秒时间戳转LocalDateTime
     * @param timeMillis 毫秒时间戳
     * @return LocalDateTimeO
     */
    public static LocalDateTime toLocalDateTime(long timeMillis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), ZoneId.systemDefault());
    }

    /**
     * 毫秒时间戳转LocalDate
     * @param timeMillis 毫秒时间戳
     * @return LocalDate
     */
    public static LocalDate toLocalDate(long timeMillis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 格式化时间，格式yyyy-MM-dd hh:mm:ss
     * @param localDateTime
     * @return
     */
    public static String getDateStrWithFormate(LocalDateTime localDateTime) {
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        return dtf2.format(localDateTime);
    }

    public static Instant toInstant(LocalDate localDate) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public static Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

}
