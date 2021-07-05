package com;

public class Test {

    static  int a = 6;
    static int b = 8;

    public static int counta(int c){
        if( c%b == 0  ){
            return c/8 ;
        }

        if(c%b%a == 0 ){
            return c/b + c%b/a;
        }
        if(c%a==2){
            return c/a;
        }
        if(c%a%b == 0 ){
            return c/a + c%a/b;
        }

        if(c%a == 0 && c%b > 0 ){
            return c/a ;
        }


        return -1;
    }


    public static void main(String[] args) {

//        for(int i = 0 ; i <100 ; i ++){
//            System.out.println( i + "=========" + counta(i));
//        }
        System.out.println(true ^ true);
    }
}
