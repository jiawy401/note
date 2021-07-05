package com;

public class PointTest
{
    /**
     * 给定一个有序数组 arr，代表数轴上从左到右有 n 个点 arr[0]、arr[1] ... arr[n-1]，给定一个正数 L，代表一根长度为 L 的绳子，求绳子最多能覆盖其中的几个点？
     *
     * 思路：绳子左侧在第 i 个数时，能覆盖几个点，求最多的情况就可以。类似滑动窗口解决
     */

    static int[] arr = new int[]{0, 13, 24, 35, 46, 57, 60, 72, 87};

    public static void main(String[] args) {

        System.out.println(point(31 , new int[]{0, 13, 24, 35, 46, 57, 60, 72, 87}));
        System.out.println(point(3 , new int[]{1, 2 , 4 , 5 }));
    }

    public static int point(int l ,  int[] arr ){
        int result = 0;

        if(arr.length<0 || arr[0] >l ) return result;
        if( arr[arr.length-1] <=l ) return arr.length;

        int i =0 ;
        int maxCount = 0;
        int j = 0 ;
        while(true){
            if(j== arr.length) break;
            try{

                if(arr[i]<=arr[j]-arr[0]+l){
                    i ++;
                }else{
                    maxCount = i-j;
                    j ++ ;
                    i = j ;
                }

            }catch(Exception e){
                break;
            }
        }
        return maxCount;

    }

    public int pointa(int l ){

        while(true){

            if(arr[l] < l)
            return 0;
        }
    }
}
