package com.jiawy.springbootthread.thread3_1;

public class DLCClass {


    //Double Lock Check
    // 不完整对象

    public  static  volatile DLCClass dlcClass ;

    private  DLCClass(){}

    public static DLCClass getInstance(){
        if(dlcClass == null) {
            synchronized (dlcClass){
                if(dlcClass == null ){
                    //三个指令 - 》  重排序
                    dlcClass = new DLCClass();
                }
            }
        }
        return dlcClass;
    }
}
