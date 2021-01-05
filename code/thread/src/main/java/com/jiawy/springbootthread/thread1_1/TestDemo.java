package com.jiawy.springbootthread.thread1_1;

public class TestDemo extends Thread{


    @Override
    public void run(){
        //线程会执行的指令


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Come in ");

    }


    public static void main(String[] args) {
        TestDemo t  = new TestDemo();
        System.out.println("Main in ");
        t.start();
    }

}
