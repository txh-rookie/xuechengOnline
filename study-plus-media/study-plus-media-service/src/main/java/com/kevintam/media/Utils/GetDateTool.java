package com.kevintam.media.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/24
 */
public class GetDateTool {
    public static String getFileFolder(Date date, boolean year, boolean month, boolean day){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = dateFormat.format(date);
        String[] dateSplit = format.split("-");
        StringBuilder builder = new StringBuilder();
        if(year){
            String yearDate = dateSplit[0];
            builder.append(yearDate+"/");
        }
        if(month){
            String monthDate = dateSplit[1];
            builder.append(monthDate+"/");
        }
        if(day){
            String dayDate = dateSplit[2];
            builder.append(dayDate+"/");
        }
        return builder.toString();
    }
}
