package com.jiawy.springbootthread.thread2;

public class InterruptDemo implements  Runnable
{


    private int i = 1;
    @Override
    public void run() {
        //Thread.currentThread().isInterrupted()=false;
        //表示一个中断的标记 , interrupted = false;

        while(!Thread.currentThread().isInterrupted()){

            System.out.println("test:" +i++);
        }
    }


    public static void main(String[] args) {
        Thread t = new Thread(new InterruptDemo());
        t.start();
        t.interrupt(); //设置interrupted = true
    }
}
