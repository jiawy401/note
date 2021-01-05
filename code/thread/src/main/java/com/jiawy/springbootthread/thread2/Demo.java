package com.jiawy.springbootthread.thread2;

import java.util.concurrent.TimeUnit;

public class Demo {


    public static void main(String[] args) {
        new Thread (()->{
            while(true){
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"STATUS_01").start();//阻塞状态

        new Thread(()->{
            while(true){
                synchronized (Demo.class){
                    try {
                        Demo.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"STATUS_02").start(); //阻塞状态


        new Thread(new BlockedDemo(), "BLOCKED-DEMO-01").start();
        new Thread(new BlockedDemo(), "BLOCKED-DEMO-02").start();


    }

    static class BlockedDemo extends Thread{
        @Override
        public void run(){
            synchronized (BlockedDemo.class){
                while(true){
                    try {
                        TimeUnit.SECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
