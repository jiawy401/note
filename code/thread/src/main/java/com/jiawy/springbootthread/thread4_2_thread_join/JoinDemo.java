package com.jiawy.springbootthread.thread4_2_thread_join;

public class JoinDemo {

    private static  int i = 10;

    public static void main(String[] args) {
        Thread t1 = new Thread(() ->{
            i  = 30;
        });

        t1.start();
        System.out.println("i:" + i);
    }
}
