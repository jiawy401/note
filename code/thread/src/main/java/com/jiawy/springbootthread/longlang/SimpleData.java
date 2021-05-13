package com.jiawy.springbootthread.longlang;

import java.util.Calendar;

public class SimpleData {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.HOUR_OF_DAY) +"---" + calendar.get(Calendar.HOUR));
    }
}
