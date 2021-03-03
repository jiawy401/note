package com.jiawy.springbootthread.thread3_1;

public class VolatileDemo {


    public volatile static  boolean stop = false;

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(()->{
            int i = 0  ;
            while(!stop){
                i++;
            }
            System.out.println("rs:" +i);
        });
        t.start();
        Thread.sleep(1000);
        stop = true;
    }
}
