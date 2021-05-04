package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFormatInstance {
    //私有化构造器
    private static  DateFormatInstance a;
    private DateFormatInstance(){}
    private  static SimpleDateFormat dateTransfer;
    public static DateFormatInstance getInstance(){
        if (a == null) {
            synchronized(DateFormatInstance.class) {
                if (a == null) {
                    a = new DateFormatInstance();
                    dateTransfer=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
                }
            }
        }
        return a;
    }

    //將长整型转化成日志
    //transfer long to date
    public String parse_long(long timeLong)
    {

           return  dateTransfer.format(timeLong);


    }

}
