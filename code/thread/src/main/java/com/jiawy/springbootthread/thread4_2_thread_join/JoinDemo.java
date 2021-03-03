package com.jiawy.springbootthread.thread4_2_thread_join;

public class JoinDemo {

    private static  int i = 10;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() ->{
            i  = 30;

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        //t1线程中的执行结果对于main 线程可见
        t1.join(); //Happens-Before模型
        /**
         * 类似于join()
         * Callable/Future(阻塞）
         */

        System.out.println("i:" + i);
    }
}
