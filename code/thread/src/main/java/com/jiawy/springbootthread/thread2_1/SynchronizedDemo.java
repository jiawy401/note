package com.jiawy.springbootthread.thread2_1;

import javax.management.ObjectName;

public class SynchronizedDemo {

    synchronized void demo(){

    }

    synchronized static  void demo2(){



    }

    Object object = new Object();

    void demo3(){
        synchronized (object){

        }
    }

}
