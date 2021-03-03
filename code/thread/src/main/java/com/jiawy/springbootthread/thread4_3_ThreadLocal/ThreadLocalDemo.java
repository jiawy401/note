package com.jiawy.springbootthread.thread4_3_ThreadLocal;

public class ThreadLocalDemo {

    //    private static  int num =0;
    static ThreadLocal<Integer> local = new ThreadLocal<Integer>() {
        protected Integer initialValue() {
            return 0; //初始化的值是每个线程独有的
        }
    };

    public static void main(String[] args) {
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(() -> {

                int num = local.get();  //获得的值都是0
                local.set(num +=5);    //设置到local中
//                num += 5;
                System.out.println(Thread.currentThread().getName() + "-" + num);
            });
        }

        for (int i = 0; i < 5; i++) {
            threads[i].start();
        }
    }
}
