package com.jiawy.springbootthread.thread2_2;

import java.util.Queue;

public class Product implements  Runnable{

    private Queue<String> msg;

    private int maxSize;

    public Product(Queue<String> msg , int maxSize){
        this.msg = msg;
        this.maxSize = maxSize;
    }

    @Override
    public void run() {

        int i = 0  ;
        while (true) {
            i++;
            synchronized (msg){
                while(msg.size() == maxSize){
                    try {
                        msg.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("生产者生产消息：" + i );
                msg.add("生产消息：" + i);
                msg.notify();
            }
        }
    }
}
