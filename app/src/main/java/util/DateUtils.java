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


        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date createDate = null;
        try {
            createDate = sd.parse(createtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date currentDate=new Date();

        //用现在距离1970年的时间间隔new Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔
        long time = currentDate.getTime() - createDate.getTime();// 得出的时间间隔是毫秒

        int second = 1000;
        int minute = second * 60;
        int hour = minute * 60;


        int dayPast= 0;
        int yearPast=0;
        try {
            dayPast = compareDay(createDate,currentDate);
            yearPast=compareYear(createDate,currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String regularTime = null;
        if(dayPast==0){

            if(time/second<60){
                regularTime="刚刚";
            }else  if(time/minute<60){
                regularTime=time/minute+"分钟前";
            }else{
                regularTime=time/hour+"小时前";
            }

        } else if(dayPast==-1){
            regularTime="昨天"+parseToHM(createDate);
        }else if(dayPast==-2){
            regularTime="前天"+parseToHM(createDate);
        }else if(yearPast==0){
            regularTime=parseToMDHM(createDate);
        }else{

            regularTime=parseToYMDHM(createDate);

        }
        return regularTime;
    }


    //返回值为0就是今天，-1是昨天。-2是两天前
    public static int compareDay(Date createDate,Date currentDate) throws ParseException {

        Calendar pre = Calendar.getInstance();

        pre.setTime(createDate);

        Calendar cal = Calendar.getInstance();

        cal.setTime(currentDate);

        return cal.get(Calendar.DAY_OF_YEAR)
                - pre.get(Calendar.DAY_OF_YEAR);

    }

    //返回值为0就是今天，-1是去年。-2是两年前
    public static int compareYear(Date createDate,Date currentDate) throws ParseException {

        Calendar pre = Calendar.getInstance();

        pre.setTime(createDate);

        Calendar cal = Calendar.getInstance();

        cal.setTime(currentDate);

        return cal.get(Calendar.YEAR)
                - pre.get(Calendar.YEAR);

    }

    //时 分
    public static String parseToHM(Date date){
        return new SimpleDateFormat("H:mm").format(date);
    }

    //月 日 时 分
    public static String parseToMDHM(Date date){
        return new SimpleDateFormat("M-d H:mm").format(date);
    }

    //年 月 日 时 分
    public static String parseToYMDHM(Date date){
        return new SimpleDateFormat("yyyy-MM-dd H:mm").format(date);
    }





    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

}