package com.jiawy.springbootthread.thread4_3_ThreadLocal;

public class ThreadLocalDemo {

    private static  int num =0;

    public static void main(String[] args) {
        Thread[] threads = new Thread[5];
        for (int i = 0; i <5 ; i ++){
            threads[i] = new Thread(() -> {
                num +=5;
                System.out.println(Thread.currentThread().getName() + "-" + num);
            });
        }

        for (int i = 0 ;i < 5; i ++){
            threads[i].start();
        }
    }
}
