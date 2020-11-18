package com.marcojan11.wonder.kernel.common;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class Dates {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd");

    private static final SimpleDateFormat CN_SDF = new SimpleDateFormat("yyyy-MM-dd");

    public static final String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};


    //将字符转换成时间类型
    public static Date getDateByStringYYYY_MM_DD(String dateStr) {
        if (Strings.isNullOrEmpty(dateStr)) {
            return null;
        }
        try {
            return SDF.parse(dateStr);
        } catch (ParseException e) {
            log.error("时间转换发生异常:{}", e);
            return null;
        }
    }


    /**
     * yyyy-MM-dd
     */
    public static final String CN_DATE_FORMAT = "yyyy-MM-dd";
    public static final String CN_DATE_FORMAT_MMDDHHMM = "MM-dd HH:mm";
    public static final String CN_DATE_FORMAT_M = "yyyy-MM";
    public static final String CN_DATE_FORMAT_Md = "MM-dd";
    public static final String CN_HOUR_MINUTE_FORMAT = "HH:mm";
    public static final String CN_DATE_FORMAT_C = "yyyy年MM月dd日";
    public static final String CN_DATE_FORMAT_D = "dd";


    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String CN_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String CN_DATETIME_FORMAT_MINUTE = "yyyy-MM-dd HH:mm";


    public static final String CN_DATE_FORMAT_SLASH = "yyyy/MM/dd";
    public static final String CN_DATETIME_FORMAT_SLASH = "yyyy/MM/dd HH:mm:ss";

    /**
     * yyyyMMddHHmmss
     */
    public static final String STRING_DATE_FORMAT = "yyyyMMddHHmmss";

    /**
     * yyyyMMddHHmmssSSS
     */
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

    /**
     * yyyyMMddHHmmssSSS
     */
    public static final String YYYYMMDD = "yyyyMMdd";

    public static final String YEAR = "yyyy";

    /**
     * yyyy/MM/dd
     */
    public static final String CN_SPRIT_DATE_FORMAT_DAY = "yyyy/MM/dd";

    /**
     * yyyy/MM/dd hh
     */
    public static final String CN_SPRIT_DATE_FORMAT_HOUR = "yyyy/MM/dd HH";

    /**
     * yyyy/MM/dd hh
     */
    public static final String CN_SPRIT_DATE_FORMAT_MINUTE = "yyyy/MM/dd HH:mm";

    /**
     * yyyy/M/d
     */
    public static final String CN_SPRIT_DATE_FORMAT_SIMPLE_DAY = "yyyy/M/d";

    public static final String TS_YMD = "yyyyMMdd";

    public static final String TS_YMDHMS = "yyyyMMddHHmmss";

    private static final int HOURS = 24;

    private static final int MINUTES = 60;

    private static final int SECONDS = 60;

    private static final int MILLION = 1000;

    public static final String TIME_SUFFIX = " 00:00:00";
    /**
     * 日期排序类型-升序
     */
    public static final int DATE_ORDER_ASC = 0;

    /**
     * 日期排序类型-降序
     */
    public static final int DATE_ORDER_DESC = 1;

    /**
     * 是否周末
     *
     * @param writeOffEndDate
     */
    public static boolean isWeekend(Date writeOffEndDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(writeOffEndDate);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * 获取当前年
     *
     * @return
     */
    public static String getCurrentYear() {
        return formatStrDate(new Date(), YEAR);
    }

    /**
     * 获取年
     *
     * @return
     */
    public static String getYear(Date date) {
        return formatStrDate(date, YEAR);
    }

    /**
     * 根据格式获取指定日期格式的字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 转换时间为秒
     *
     * @param date
     * @return
     */
    public static Long getSecond(Date date) {
        Long milliSecond = date.getTime();
        String milliSecondStr = milliSecond.toString();
        Long Second = Long.parseLong(milliSecondStr.substring(0, milliSecondStr.length() - 3));
        return Second;
    }

    /**
     * 获取yyyy-MM-dd形式的字符串
     *
     * @param date
     * @return
     */
    public static String formateCNDateByString(Date date) {
        return CN_SDF.format(date);
    }

    /**
     * 获取一个yyyy/MM/dd的类型的日期字符串
     *
     * @param date
     * @return
     */
    public static String formateBySDF(Date date) {
        return SDF.format(date);
    }

    /**
     * 根据格式获取指定日期格式的字符串
     *
     * @return
     */
    public static Date formatDateByLongWithMillisecond(Long dateLong) {
        if (dateLong == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateLong);
        return cal.getTime();
    }

    /**
     * 根据格式获取指定日期格式的字符串
     *
     * @return
     */
    public static Date formatDateByLongWithSecond(Long dateLong) {
        if (dateLong == null) {
            return null;
        }
        Date d = new Date(dateLong * 1000);
        return d;
    }

    /**
     * 根据格式获取指定日期格式的字符串
     *
     * @param pattern
     * @return
     */
    public static String formatDate(Long dateLong, String pattern) {
        if (dateLong == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateLong);
        Date date = cal.getTime();
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 根据格式获取指定日期格式的字符串
     *
     * @param pattern
     * @return
     */
    public static String formatDateWithSecond(Long dateLong, String pattern) {
        if (dateLong == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateLong * 1000);
        Date date = cal.getTime();
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 获取当前毫秒数
     *
     * @return
     */
    public static long getCurrentTime() {

        return new Date().getTime();
    }

    /**
     * 根据格式获取指定当前日期格式的字符串
     *
     * @param pattern
     * @return
     */
    public static String formatDate(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date());
    }

    /**
     * 获取当前日期
     *
     * @param formatStr 日期格式
     * @return 日期格式当前日期
     */
    public static Date getCurrentDate(String formatStr) {
        java.util.Date currentDate = null;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormate = new SimpleDateFormat(formatStr);
        String dateStr = dateFormate.format(cal.getTime());
        try {
            currentDate = dateFormate.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
        return currentDate;
    }

    /**
     * 获取指定日期减少的天数
     *
     * @param date
     * @param day
     * @return
     */
    public static Date subtractDay(java.util.Date date, int day) {
        return addDay(date, -day);
    }

    /**
     * 获取指定日期添加的天数
     *
     * @param date
     * @param day
     * @return
     */
    public static Date addDay(Date date, int day) {
        if (date == null) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 获取指定日期添加的月数
     *
     * @param date
     * @return
     */
    public static Date addMonth(Date date, int month) {
        if (date == null) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * 判断日期大小
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int compare(Date date1, Date date2) {
        try {
            if (date1.getTime() > date2.getTime()) {
                return 1;
            } else if (date1.getTime() == date2.getTime()) {
                return 0;
            } else {
                return -1;
            }
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 日期相减
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int sub(Date date1, Date date2) {
        try {
            Long l = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);
            int i = Integer.parseInt(l.toString());
            return i;
        } catch (Exception e) {
            log.error("sub error", e);
        }
        return 0;
    }

    /**
     * 获取当前日期，YYYYMMDD格式转为long型
     *
     * @param
     * @param
     * @return
     */
    public static long getCurrentDay() {
        String dayStr = formatDate(YYYYMMDD);
        return Long.valueOf(dayStr);
    }

    @SuppressWarnings("deprecation")
    public static long getFormatDate(String time) {
        Date date = new Date(Long.parseLong(time));
        String s = "";
        StringBuffer sb = new StringBuffer();
        sb.append(date.getYear()).append(date.getMonth()).append(date.getDay()).append(date.getHours())
                .append(date.getMinutes()).append(date.getSeconds());
        s = sb.toString();
        return Long.parseLong(s);

    }

    /**
     * 计算CPD实际投放天数
     *
     * @param searchStartDate
     * @param searchEndDate
     * @return
     */
    public static Integer countLeaveDays(Date searchStartDate, Date searchEndDate, Date startDateTwo, Date endDateTwo) {

        // 查询结束日期大于投放的开始日期 查询开始日期小于投放的结束日期， 无交集
        if (searchEndDate.compareTo(startDateTwo) < 0 || searchStartDate.compareTo(endDateTwo) > 0) {

            return 0;
        }

        // 投放时间完全在查询条件之内，无需计算
        if (searchStartDate.compareTo(startDateTwo) <= 0 && searchEndDate.compareTo(endDateTwo) >= 0) {

            long diff = endDateTwo.getTime() - startDateTwo.getTime();
            return (int) ((diff / (1000 * 60 * 60 * 24)) + 1);
        }

        int days = 0; // 记录天数
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        // 获取不在投放期间的天数
        startDate.setTime(startDateTwo);
        endDate.setTime(endDateTwo);
        endDate.add(Calendar.DAY_OF_MONTH, 1);
        while (startDate.before(endDate)) {

            Date date = startDate.getTime();

            Boolean isEquals = date.compareTo(searchStartDate) == 0 || date.compareTo(searchEndDate) == 0;

            if ((date.compareTo(searchStartDate) > 0 && date.compareTo(searchEndDate) < 0) || isEquals) {

                days++;
            }

            startDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;
    }

    /**
     * String 类型日期格式化
     *
     * @param
     * @param
     * @return
     * @throws ParseException
     */
    public static Date formatDate(String date, String pattern) throws ParseException {
        SimpleDateFormat dateFormate = new SimpleDateFormat(pattern);
        return dateFormate.parse(date);
    }

    /**
     * String 类型日期格式化
     *
     * @param
     * @param
     * @return
     * @throws ParseException
     */
    public static Date formatDateByStr(String date, String pattern) {
        try {
            SimpleDateFormat dateFormate = new SimpleDateFormat(pattern);
            return dateFormate.parse(date);
        } catch (Exception e) {
            log.error("时间转换发生异常:{}", e);
            return null;
        }
    }

    /**
     * String 类型日期格式化
     *
     * @param
     * @param
     * @return
     * @throws ParseException
     */
    public static String formatStrDate(String date, String pattern) throws ParseException {
        SimpleDateFormat dateFormate = new SimpleDateFormat(STRING_DATE_FORMAT);
        return formatDate(dateFormate.parse(date), pattern);
    }

    /**
     * String 类型日期格式化
     *
     * @param
     * @param
     * @return
     * @throws ParseException
     */
    public static String formatStrDate(Date d, String pattern) {
        SimpleDateFormat dateFormate = new SimpleDateFormat(pattern);
        return dateFormate.format(d);
    }

    /**
     * @param dates
     * @param isNeedOrderBy
     * @param orderBy       0升序1降序
     * @return
     */
    public static List<List<Date>> groupDates(List<Date> dates, boolean isNeedOrderBy, int orderBy) {
        List<List<Date>> result = new ArrayList<List<Date>>();
        if (isNeedOrderBy) {
            // 按照升序排序
            orderDate(dates, orderBy);
        }
        // 临时结果
        List<Date> tempDates = null;
        // 上一组最后一个日期
        Date lastDate = null;
        // 当前读取日期
        Date cdate = null;
        for (int i = 0; i < dates.size(); i++) {
            cdate = dates.get(i);
            // 第一次增加
            if (tempDates == null) {
                tempDates = new ArrayList<Date>();
                tempDates.add(cdate);
                result.add(tempDates);
            } else {
                /**
                 * 差距为1是继续在原有的列表中添加，大于1就是用新的列表
                 */
                lastDate = tempDates.get(tempDates.size() - 1);
                long days = getDiffDays(lastDate, cdate);
                if (days == 1) {
                    tempDates.add(cdate);
                } else {
                    tempDates = new ArrayList<Date>();
                    tempDates.add(cdate);
                    result.add(tempDates);
                }
            }
        }
        return result;
    }

    /**
     * @param dates
     * @return
     */
    public static List<List<String>> groupDates(List<String> dates, String pattern) {
        List<List<String>> result = new ArrayList<List<String>>();
        // 临时结果
        List<Date> tempDates = null;
        List<String> tempDateStringList = null;
        // 上一组最后一个日期
        Date lastDate = null;
        // 当前读取日期
        Date cdate = null;
        for (int i = 0; i < dates.size(); i++) {
            try {
                cdate = Dates.formatDate(dates.get(i), pattern);
                // 第一次增加
                if (tempDates == null) {
                    tempDates = new ArrayList<Date>();
                    tempDateStringList = new ArrayList<String>();
                    tempDates.add(cdate);
                    tempDateStringList.add(dates.get(i));
                    result.add(tempDateStringList);
                } else {
                    /**
                     * 差距为1是继续在原有的列表中添加，大于1就是用新的列表
                     */
                    lastDate = tempDates.get(tempDates.size() - 1);
                    long days = getDiffDays(lastDate, cdate);
                    if (days == 1) {
                        tempDates.add(cdate);
                        tempDateStringList.add(dates.get(i));
                    } else {
                        tempDates = new ArrayList<Date>();
                        tempDateStringList = new ArrayList<String>();
                        tempDates.add(cdate);
                        tempDateStringList.add(dates.get(i));
                        result.add(tempDateStringList);
                    }
                }
            } catch (ParseException e) {
                log.error("", e);
            }
        }
        return result;
    }

    /**
     * 获取两个时间间隔的天数
     *
     * @return
     */
    public static int getDiffDays(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        long difftime = endDate.getTime() - startDate.getTime();
        return (int) (difftime / (HOURS * MINUTES * SECONDS * MILLION));
    }

    public static List<Date> orderDate(List<Date> dates, int orderType) {
        DateComparator comp = new DateComparator(orderType);
        Collections.sort(dates, comp);
        return dates;
    }

    /**
     * 计算当前时间与某个时间点的差(单位:秒)
     */
    public static String getCostTimeStrByStartTime(long startTime) {
        float seconds = (System.currentTimeMillis() - startTime) / 1000F;
        return Float.toString(seconds);
    }

    /**
     * 计算两个时间点的差(单位:秒)
     */
    public static String getCostTimeStrByStartTime(long startTime, long endTime) {
        float seconds = (endTime - startTime) / 1000F;
        return Float.toString(seconds);
    }

    static class DateComparator implements Comparator<Date> {
        int orderType;

        public DateComparator(int orderType) {
            this.orderType = orderType;
        }

        public int compare(Date d1, Date d2) {
            if (d1.getTime() > d2.getTime()) {
                if (orderType == Dates.DATE_ORDER_ASC) {
                    return 1;
                } else {
                    return -1;
                }
            } else {
                if (d1.getTime() == d2.getTime()) {
                    return 0;
                } else {
                    if (orderType == Dates.DATE_ORDER_DESC) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        }
    }

    /**
     * 获取两个时间之间的日期
     *
     * @param date1
     * @param date2
     * @param pattern
     * @return
     */
    public static List<String> process(String date1, String date2, String pattern) {

        List<String> dateList = new ArrayList<String>();

        if (date1.equals(date2)) {
            dateList.add(date1);
            return dateList;
        }
        SimpleDateFormat dateFormate = new SimpleDateFormat(pattern);

        String tmp = null;
        if (date1.compareTo(date2) > 0) { // 确保 date1的日期不晚于date2
            tmp = date1;
            date1 = date2;
            date2 = tmp;
        }

        try {
            tmp = dateFormate.format(dateFormate.parse(date1).getTime());
        } catch (ParseException e1) {
            log.error("process error", e1);
        }

        int num = 0;
        while (tmp.compareTo(date2) <= 0) {
            dateList.add(tmp);
            num++;
            try {
                tmp = dateFormate.format(dateFormate.parse(tmp).getTime() + 3600 * 24 * 1000);
            } catch (ParseException e) {
                log.error("process error", e);
            }
        }

        if (num == 0) {

            dateList.add(date1);
            dateList.add(date2);
        }

        return dateList;
    }


    /**
     * 根据客保规则获取失效日期
     *
     * @param currentTime
     * @return
     */
    public static Date getExpireTime(Date currentTime, int dayNum) {
        GregorianCalendar calender = new GregorianCalendar();
        calender.setTime(currentTime);
        calender.add(GregorianCalendar.DAY_OF_MONTH, dayNum);
        Date expireTime = calender.getTime();
        return expireTime;
    }

    /**
     * 根据客保规则获取失效日期
     *
     * @param currentTime
     * @return
     */
    public static String getExpireTime4str(Date currentTime, int dayNum) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(CN_DATE_FORMAT);
            Date fromDate = sdf.parse(sdf.format(currentTime));
            GregorianCalendar calender = new GregorianCalendar();
            calender.setTime(fromDate);
            calender.add(GregorianCalendar.DAY_OF_MONTH, dayNum);
            return new SimpleDateFormat(CN_DATE_FORMAT).format(calender.getTime());
        } catch (Exception e) {
            log.error("getExpireTime4str error", e);
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 获得剩余客保天数
     *
     * @param userExpireTime
     * @return
     */
    public static Integer getLeftDays(Date userExpireTime) {

        if (userExpireTime == null) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(CN_DATE_FORMAT);
            Date fromDate = sdf.parse(sdf.format(new Date()));
            Date toDate = sdf.parse(sdf.format(userExpireTime));
            Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(toDate);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24) + 1;
            return Integer.valueOf(String.valueOf(between_days));
        } catch (Exception e) {
            log.error("getLeftDays error", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(CN_DATE_FORMAT);
        String currentDate = sdf.format(date);
        return currentDate;
    }

    /**
     * 获取昨天日期
     *
     * @return
     */
    public static String getYesterday() {
        Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        calendar.add(Calendar.DATE, -1);    //得到前一天
        String yesterday = new SimpleDateFormat(CN_DATE_FORMAT).format(calendar.getTime());
        return yesterday;
    }

    public static Date getTodayMidNightTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    //字符串unix时间(秒)，转换为指定格式（pattern）时间
    public static String unixToFormat(String unix, String pattern) {

        Long unixTime = Long.parseLong(unix);
        String dateString = formatDateWithSecond(unixTime, pattern);
        return dateString;

    }

    public static String getWeek(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }
}
