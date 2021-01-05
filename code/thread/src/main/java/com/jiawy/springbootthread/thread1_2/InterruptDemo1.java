package com.jiawy.springbootthread.thread1_2;

import java.util.concurrent.TimeUnit;

public class InterruptDemo1 implements  Runnable {

    @Override
    public void run() {
        while(Thread.currentThread().isInterrupted()){
            try {
                TimeUnit.SECONDS.sleep(200);
            } catch (InterruptedException e) { //JVM层面抛出的异常，触发了线程的复位
//                e.printStackTrace();
                //可以不做处理
                //继续中断
                Thread.currentThread().interrupt(); //再次中断。
            }

        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new InterruptDemo1());
        t1.start();
        t1.interrupt();//有关闭线程的作用

        Thread.interrupted();  //复位？
    }
}
