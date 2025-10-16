package com.maybank.platform.services.util;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {
    public static final String DATE_COMMON_SHORT_FORMAT = "yyyyMMdd";
    public static final String DATE_COMMON_SHORT_DASH_FORMAT = "yyyy-MM-dd";
    public static final String DATE_COMMON_SHORT_MONTH_FORMAT = "yyyyMM";
    public static final String DATE_COMMON_SHORT_MONTH_DASH_FORMAT = "yyyy-MM";
    public static final String DATE_COMMON_MONTH_FORMAT = "MM";
    public static final String DATE_COMMON_DAY_FORMAT = "dd";
    public static final String DATE_COMMON_HOUR_FORMAT = "HH";
    public static final String DATE_COMPLETE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_COMPRESS_FORMAT = "yyyyMMddHHmmss";
    public static final String DATE_CSV_FORMAT = "dd/MM/yyyy HH:mm:ss";
    private static final Map<String, Format> formatMap = new ConcurrentHashMap<>();

    public DateUtil() {
    }

    public static Date format(String dateTime, String format) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.parse(dateTime);
        } catch (Exception var4) {
            Logger logger = LoggerFactory.getLogger(DateUtil.class);
            logger.error("parse time fail:" + dateTime + ", format :" + format, var4);
            return null;
        }
    }

    public static String format(String format, Object time) {
        try {
            return getFormat(format).format(time);
        } catch (Exception var4) {
            Logger logger = LoggerFactory.getLogger(DateUtil.class);
            logger.error("parse time fail:" + time + ", format :" + format, var4);
            return null;
        }
    }

    public static String getCurrentYearMonth(String format, Date currentDate) {
        SimpleDateFormat yearMonthFormat = new SimpleDateFormat(format);
        return yearMonthFormat.format(currentDate);
    }

    public static int extractYear(String input) {
        // Define the pattern matching the input format "yyyy-MM"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_COMMON_SHORT_DASH_FORMAT);
        YearMonth yearMonth = YearMonth.parse(input, formatter);
        return yearMonth.getYear();
    }

    public static int extractMonth(String input) {
        // Define the pattern matching the input format "yyyy-MM"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_COMMON_SHORT_DASH_FORMAT);
        YearMonth yearMonth = YearMonth.parse(input, formatter);
        return yearMonth.getMonthValue();
    }

    public static int extractAllDays(String input) {
        // Define the pattern matching the input format "yyyy-MM"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_COMMON_SHORT_DASH_FORMAT);
        YearMonth yearMonth = YearMonth.parse(input, formatter);
        return yearMonth.lengthOfMonth();
    }

    public static String getMonthAndYear(String additionalInput, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);

        int week = Integer.parseInt(additionalInput.replace("week", "").trim());

        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        int month = calendar.get(Calendar.MONTH) + 1;
        int extractedYear = calendar.get(Calendar.YEAR);

        // Return the result in yyyy-MM format
        return String.format("%d-%02d-01", extractedYear, month);
    }

    public static String format(String format, String timeZone, Date time) {
        try {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(time.toInstant(), TimeZone.getTimeZone(timeZone).toZoneId());
            return getFormat("zone" + format).format(localDateTime);
        } catch (Exception var5) {
            Logger logger = LoggerFactory.getLogger(DateUtil.class);
            logger.error("parse time fail:" + time + ", format :" + format, var5);
            return null;
        }
    }

    public static <T> T parse(String format, String time) {
        try {
            return (T) getFormat(format).parseObject(time);
        } catch (Exception var4) {
            Logger logger = LoggerFactory.getLogger(DateUtil.class);
            logger.error("parse time fail:" + time + ", format :" + format, var4);
            return null;
        }
    }

    private static Format getFormat(String format) {
        Format formatInstance = (Format)formatMap.get(format);
        if (formatInstance == null) {
            if (format.startsWith("zone")) {
                formatInstance = DateTimeFormatter.ofPattern(format.substring(5)).toFormat();
            } else {
                formatInstance = new SimpleDateFormat(format);
            }

            formatMap.put(format, formatInstance);
        }

        return (Format)formatInstance;
    }

    public static long parseMillis(String format, String time) {
        try {
            Object date = getFormat(format).parseObject(time);
            if (date instanceof Long) {
                return (Long)date;
            }

            if (date instanceof Date) {
                return ((Date)date).getTime();
            }
        } catch (Exception var4) {
            Logger logger = LoggerFactory.getLogger(DateUtil.class);
            logger.error("parse parseMillis fail:" + time + ", format :" + format, var4);
        }

        return -1L;
    }

    public static ZoneId getDateTimeZone(String timeZone) {
        return TimeZone.getTimeZone(timeZone).toZoneId();
    }

    public static String dateFormatCommonShort(Date currentTime) {
        return format("yyyyMMdd", currentTime);
    }

    public static String dateFormatCommonShort(long currentTime) {
        return format("yyyyMMdd", currentTime);
    }

    public static String dateFormatCommonShortMonth(long currentTime) {
        return format("yyyyMM", currentTime);
    }

    public static String dateFormatCommonMonth(Date currentTime) {
        return format("MM", currentTime);
    }

    public static String dateFormatCommonMonth(long currentTime) {
        return format("MM", currentTime);
    }

    public static String dateFormatCommonDay(Date currentTime) {
        return format("dd", currentTime);
    }

    public static String dateFormatCommonDay(long currentTime) {
        return format("dd", currentTime);
    }

    public static String dateFormatCommonHour(Date currentTime) {
        return format("HH", currentTime);
    }

    public static String dateFormatCommonHour(long currentTime) {
        return format("HH", currentTime);
    }

    public static String dateFormat(Date currentTime) {
        return format("yyyy-MM-dd HH:mm:ss", currentTime);
    }

    public static String dateFormat(long currentTime) {
        return format("yyyy-MM-dd HH:mm:ss", currentTime);
    }

    public static long stringToLong(String string) {
        return parseMillis("yyyy-MM-dd HH:mm:ss", string);
    }

    public static String dateFormat() {
        return format("yyyyMMddHHmmss", System.currentTimeMillis());
    }

    public static long getCurrentSeconds(Calendar calendar) {
        return calendar.getTimeInMillis() / 1000L;
    }

    public static long getCurrentSeconds() {
        return getCurrentMillis() / 1000L;
    }

    public static long getCurrentMillis(Calendar calendar) {
        return calendar.getTimeInMillis();
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static Date getCurrentDate(String timeZone) {
        return Date.from(LocalDateTime.now().atZone(getDateTimeZone(timeZone)).toInstant());
    }

    public static long getCurrentMillis() {
        return System.currentTimeMillis();
    }

    public static long getCurrentMillis(String timeZone) {
        return getCurrentDate(timeZone).getTime();
    }

    public static Long getTodayTimeMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        Long todayTimeMillis = calendar.getTimeInMillis();
        return todayTimeMillis;
    }

    public static Date getTodayDate(int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.add(5, amount);
        Date todayDate = calendar.getTime();
        return todayDate;
    }

    public static Date getTodayDate() {
        return getTodayDate(0);
    }

    public static Integer getDateToDel(Long startDate, Long endDate) {
        long time = (startDate - endDate) / 1000L;
        return time > 0L ? (int)time : (int)Math.abs(time);
    }

    public static Date getLastMonthDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(2, -1);
        Date date = calendar.getTime();
        return date;
    }

    public static Date[] getStartAndEndDate(Integer mode, String additionalInput) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = calendar.getTime();

        switch (mode) {
            case 0:
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                startDate = calendar.getTime();
                break;
            case 1:
                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                startDate = calendar.getTime();
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                endDate = calendar.getTime();
                break;
            case 2:
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                startDate = calendar.getTime();
                calendar.add(Calendar.DAY_OF_WEEK, 6);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                endDate = calendar.getTime();
                break;
            case 3:
                calendar.add(Calendar.WEEK_OF_YEAR, -2);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                startDate = calendar.getTime();
                calendar.add(Calendar.DAY_OF_WEEK, 13);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                endDate = calendar.getTime();
                break;
            case 4:
                calendar.add(Calendar.HOUR_OF_DAY, -1);
                startDate = calendar.getTime();
                break;
            case 5: // TIME (e.g. YYYY:mm:dd HH:mm)
                startDate = dateTimeFormat.parse(additionalInput);
                calendar.setTime(startDate);
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                endDate = calendar.getTime();
                break;
            case 6: // DATE (e.g. YYYY:mm:dd)
                startDate = dateFormat2.parse(additionalInput);
                calendar.setTime(startDate);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                startDate = calendar.getTime();
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                endDate = calendar.getTime();
                break;
            case 7: // WEEK (e.g. week36)
                int week = Integer.parseInt(additionalInput.replace("week", ""));
                calendar.set(Calendar.WEEK_OF_YEAR, week);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                startDate = calendar.getTime();
                calendar.add(Calendar.DAY_OF_WEEK, 6);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                endDate = calendar.getTime();
                break;
            case 8: // MONTH (e.g. YYYY:mm)
                SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM-dd");
                startDate = monthFormat.parse(additionalInput);
                calendar.setTime(startDate);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                startDate = calendar.getTime();
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                endDate = calendar.getTime();
                break;
            case 9: // QUARTER (e.g. Q3)
                int quarter = Integer.parseInt(additionalInput.replace("Q", ""));
                int startMonth = (quarter - 1) * 3; // Q1 = Jan, Q2 = Apr, Q3 = Jul, Q4 = Oct
                calendar.set(Calendar.MONTH, startMonth);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                startDate = calendar.getTime();
                calendar.add(Calendar.MONTH, 2);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                endDate = calendar.getTime();
                break;
            case 10: // YEAR (e.g. YYYY)
                int year = extractYear(additionalInput);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                startDate = calendar.getTime();
                calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                endDate = calendar.getTime();
                break;
            default:
                throw new IllegalArgumentException("Invalid date mode");
        }
        return new Date[]{startDate, endDate};
    }

    public static void main(String[] args) throws ParseException {
        Date[] dates5 = getStartAndEndDate(5, "2024-09-01 13:20:10");
        System.out.println("5 Start Date: " + dates5[0] + " End Date: " + dates5[1]);

        Date[] dates6 = getStartAndEndDate(6, "2024-09-02");
        System.out.println("6 Start Date: " + dates6[0] + " End Date: " + dates6[1]);

        Date[] dates7 = getStartAndEndDate(7, "week36");
        System.out.println("7 Start Date: " + dates7[0] + " End Date: " + dates7[1]);

        Date[] dates8 = getStartAndEndDate(8, "2024-09-01");
        System.out.println("8 Start Date: " + dates8[0] + " End Date: " + dates8[1]);

        Date[] dates9 = getStartAndEndDate(9, "Q3");
        System.out.println("9 Start Date: " + dates9[0] + " End Date: " + dates9[1]);

        Date[] dates10 = getStartAndEndDate(10, "2024");
        System.out.println("10 Start Date: " + dates10[0] + " End Date: " + dates10[1]);
//        for (ModeType mode : ModeType.values()) {
//            Date[] dates = getStartAndEndDate(mode.getMode());
//            System.out.println(mode.getLabel() + " Start Date: " + dates[0] + " End Date: " + dates[1]);
//        }
//        System.out.println(DateUtil.format("20/08/2024 19:00:00", DATE_CSV_FORMAT));
        System.out.println(DateUtil.getCurrentYearMonth(DATE_COMMON_SHORT_MONTH_DASH_FORMAT, new Date()));
    }

    public static Date getRandomDate() {
        long now = System.currentTimeMillis();
        long randomTime = now - RandomUtils.nextLong(0, 365L * 24 * 60 * 60 * 1000); // within the last year
        return new Date(randomTime);
    }

    public static int getYear(Date dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(Date dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        return calendar.get(Calendar.MONTH) + 1;
    }
}
