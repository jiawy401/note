package com.jiawy.springbootthread.thread2_1;


public class SynchronizedDemo {
    synchronized void demo(){

    }
    synchronized static  void demo2(){

    }
    void demo01(){
        synchronized (SynchronizedDemo.class){

        }
    }

    Object object = new Object();
    void demo3(){
        //
        synchronized (object){
            //线程安全性问题
        }
    }
    //锁的范围
    //实例锁：决定在当前实例的对象里面。
    //静态方法、类对象、类锁
    //代码块
    public static void main(String[] args) {
        SynchronizedDemo synchronizedDemo = new SynchronizedDemo();
        SynchronizedDemo synchronizedDemo2 = new SynchronizedDemo();

        //锁的互斥性,当t1获得了锁 ，在t1释放锁之前t2需要等待
        new Thread(() ->{
            synchronizedDemo.demo();
        } , "t1").start();
        new Thread(() ->{
            synchronizedDemo.demo();
        }, "t2").start();
    }

}
