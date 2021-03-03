package com.jiawy.springbootthread.thread4_3_ThreadLocal;

public class HashDemo {

    private static final int HASH_INCREMENT = 0x61c88647;

    private static  void magicHash(int size){
        int hashCode =0 ;
        for(int i = 0 ; i < size ; i ++){
            hashCode = i * HASH_INCREMENT + HASH_INCREMENT;
            System.out.print((hashCode&(size -1 )) + "  ");
        }

        System.out.println();
    }

    public static void main(String[] args) {
        magicHash(16);
        magicHash(32);
    }
}
