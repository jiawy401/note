package com.jiawy.springbootthread.thread1_2;

public class InterruptDemo2 implements Runnable {

    static volatile boolean interrupt = false;


    @Override
    public void run() {
        while(!interrupt){

        }
    }


    public static void main(String[] args) {
        interrupt = true;
    }
}
