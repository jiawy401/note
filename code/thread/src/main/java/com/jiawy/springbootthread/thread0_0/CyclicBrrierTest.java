package com.jiawy.springbootthread.thread0_0;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CyclicBarrier指定了计数器为2，线程A执行cyclicBarrier.await();到达屏障点阻塞，此时计数器等于2-1=1，
 * 线程B也执行cyclicBarrier.await();到达屏障点阻塞，此时计数器等于0，CyclicBarrier里的任务会立即执行，执行完成后，线程A和B也被同时唤醒继续执行。
 * ————————————————
 */
public class CyclicBrrierTest {

    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(2, new Runnable() {
        @Override
        public void run() {
            System.out.println("汇总线程： " + Thread.currentThread().getName() + "任务合并。");
        }
    });

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程A：" + Thread.currentThread().getName()  + "执行任务" ) ;
                System.out.println("线程A：到达屏障点");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("线程A：退出屏障点");
            }
        });


        executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程B ：" +Thread.currentThread().getName() + "执行任务");
                System.out.println("线程B ： 到达屏障点");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("线程B:退出屏障点");
            }
        });
        executorService.shutdown();
    }
}
