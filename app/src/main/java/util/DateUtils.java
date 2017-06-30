package util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间处理类
 */
public class DateUtils {

    public static String geRegularTime(String createtime)  { //传入的时间格式必须类似于2012-8-21 17:53:20这样的格式
        String regularTime = null;

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date d1 = sd.parse(createtime, pos);
        //用现在距离1970年的时间间隔new Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔
        long time = new Date().getTime() - d1.getTime();// 得出的时间间隔是毫秒

        int second = 1000;
        int minute = second * 60;
        int hour = minute * 60;
        int day = 24 * hour;
        int week = day * 7;


        int dayPast= 0;
        try {
            dayPast = compareDay(createtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(dayPast==0){

            if(time/second<60){
                regularTime="刚刚";
            }else  if(time/minute<60){
                regularTime=time/minute+"分钟前";
            }else{
                regularTime=time/hour+"小时前";
            }

        } else if(dayPast==-1){
            regularTime="昨天";
        }else if(dayPast==-2){
            regularTime="前天";
        }else if(dayPast>=-10){
            int i=Math.abs(dayPast);
            regularTime=i+ "天前";
        }else{

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            ParsePosition pos2 = new ParsePosition(0);
            Date d2 = (Date) sdf.parse(createtime, pos2);

            regularTime = sdf.format(d2);

        }
        return regularTime;
    }


    //返回值为0就是今天，-1是昨天。-2是两天前
    public static int compareDay(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        return cal.get(Calendar.DAY_OF_YEAR)
                - pre.get(Calendar.DAY_OF_YEAR);

    }


    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

}