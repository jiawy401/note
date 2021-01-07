package com.jiawy.springbootthread.thread2_1;

import org.springframework.transaction.annotation.Transactional;

public class App {

    public static  int count= 0;

    public static void incr(){
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count++;
    }

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0 ;i < 1000 ; i ++){
            new Thread(()-> App.incr()).start();    //5595008供热办
        }

        Thread.sleep(3000); //保证所有线程执行结束
        System.out.println("结果:" + count);
    }
}
