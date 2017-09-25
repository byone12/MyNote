package com.example.zhouzhou.mynote.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhouzhou on 2017/9/20.
 */

public class TextFormatUtil {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }

    public static  Date parseText(String text) throws ParseException {
        return dateFormat.parse(text);
    }

    public static String getNoteSummary(String content) {
        if (content.length() > 10) {
            StringBuilder sb = new StringBuilder(content.substring(0, 10));
            sb.append("...");
            return sb.toString();
        }
        return content;
    }
}
