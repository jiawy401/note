package com;

@SuppressWarnings("all")
public class PrintZigZag

{
    /**
     * 题目：用zigzag的方式打印矩阵，如下：
     * 00 01 02 03
     * 10 11 12 13
     * 20 21 22 23
     * 30 31 32 33
     * 打印结果：00 01 10 20 11 02 03 12 21 30 31
     * @param args
     */
    public static void main(String[] args) {


         int c = 4;
         int b = 5;
        String [][] a = new String[c][b];
        for (int i = 0; i <a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {

                a[i][j] = i+"-"+j;
            }
        }
        for (int i = 0; i <a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {

                System.out.println(a[i][j]);
            }
        }
        System.out.println("----------------------------------");
        int i = 0 ;
        int m =0 ;
//        if(c>b){
        while(true){
            try {
                        if(i-c>b) break;

                        if( (i - m)  < 0) {
                            i++;
                            m=0;
                        }

                        if(i%2==0){
                            System.out.println(a[i-m][m]);
                        }else{
                            System.out.println(a[m][i-m]);
                        }
            }catch(Exception e){
            }
            m++;
        }


//        }else{
//            while(true){
//                try {
//                    if(a.length<i) break;
//
//                    if( (i - m)  < 0) {
//                        i++;
//                        m=0;
//                    }
//
//                    if(i%2==0){
//                        System.out.println(a[m][i-m]);
//                    }else{
//                        System.out.println(a[i-m][m]);
//                    }
//                }catch(Exception e){
//                }
//                m++;
//            }
//        }


    }
}
