package com.jiawy.springbootthread.thread2_2;

import java.util.Queue;

public class Consumer implements  Runnable {


    private Queue<String> msg;

    private int maxSize;

    public Consumer(Queue<String> msg, int maxSize) {
        this.msg = msg;
        this.maxSize = maxSize;
    }

    @Override
    public void run() {

        while(true){
            synchronized (msg){
                while(msg.isEmpty()){
                    try {
                        msg.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("消费者消费消息"+msg.remove());
                msg.notify();
            }
        }
    }
}
