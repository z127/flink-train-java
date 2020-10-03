package com.peter.flink.java.utils;

import com.peter.flink.java.entity.House;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static java.lang.System.*;

public class Utils {

    public  static House transferToHouseObj(String info)
    {
        String[] item=info.split("\\|");
        String description=item[0].split(",")[0].trim();
        String name=item[0].split(",")[1]+item[0].split(",")[2].trim();
        String size=item[1]+"-"+item[0].split(",")[2].trim();
        String direction=item[2].trim();
        String decoration=item[3].trim();
        String floor=item[4].trim();
        String category=item[5].split(",")[0].trim();
        String total_price=item[5].split(",")[1].trim();
        String avg_price=item[5].split(",")[2].trim();
        String advantage=item[5].split(",")[3].trim();
        String id=item[5].split(",")[4].trim();
        String ts=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        String ds=new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        return  new House(ds,ts,description,name,size,direction,decoration,floor,category,total_price,avg_price,advantage,id);
    }

    public static void main(String[] args) {
        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        out.println(datetime);
    }
}
