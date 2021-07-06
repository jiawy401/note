package com;

public class Dye {
    /**
     * 牛牛有一些排成一行的正方形。每个正方形已经被染成红色或者绿色。牛牛现在可以选择任意一个正方形然后用这两种颜色的任意一种进行染色，这个正方形的颜色将会被覆盖。牛牛的目标是在完成染色之后，每个红色 R 都比每个绿色 G 距离最左侧近（也就是绿色 G 在右侧）。牛牛想知道他最少需要涂染几个正方形。
     *
     * 如样例所示：s = RGRGR
     * 我们涂染之后变成 RRRGG 满足要求了：涂染的个数为2，没有比这个更好的涂染方案。
     *
     * 例如：s = GGGGGR
     * 涂染后变成 GGGGGG：个数为 1。
     *
     *
     * rrrgrrrrgggggggr
     */

    public static void main(String[] args) {

//        System.out.println(dye("RGRGR"));
//        System.out.println(dye("GGGGGR"));
//        System.out.println(dye("RGGGGG"));
        System.out.println(minPaint ("RRRRRRGGGGGR"));
        System.out.println(minPaint2("GGRRRGGG"));
    }


    /**
     * 思路一：循环统计
     * @param s
     * @return
     */
    public static int minPaint(String s) {
        char[] arr = s.toCharArray();

        int time = -1;

        for (int i = 0; i <= arr.length; i++) {
            int cTime = 0;
            for (int j = 0; j < arr.length; j++) {
                if (j < i && arr[j] == 'G' || j > i && arr[j] == 'R') {
                    cTime++;
                }
            }
            time = time == -1 ? cTime : Math.min(cTime, time);
        }
        return time;
    }

    public static int minPaint2(String s) {
        char[] arr = s.toCharArray();

        // 统计左侧的 G
        int[] gArr = new int[arr.length];
        // 统计右侧的 R
        int[] rArr = new int[arr.length];

        for (int i = 0; i <= arr.length; i++) {
            if (i < arr.length)
                if (arr[i] == 'G') {
                    gArr[i] = i == 0 ? 1 : gArr[i - 1] + 1;
                } else {
                    gArr[i] = i == 0 ? 0 : gArr[i - 1];
                }
            int j = arr.length - 1 - i;
            if (j >= 0)
                if (arr[j] == 'R') {
                    rArr[j] = i == 0 ? 1 : rArr[j + 1] + 1;
                } else {
                    rArr[j] = i == 0 ? 0 : rArr[j + 1];
                }
        }

        int time = rArr[0];

        for (int i = 1; i < arr.length - 1; i++) {
            time = Math.min(time, rArr[i] + gArr[i - 1]);
        }

        return Math.min(time, gArr[gArr.length - 1]);
    }


    public static int  dye(String str){

        int b = 0 ;
        int c = 0 ;
        int i = 0 ;
        int j = 0 ;
        int m =0 ;
        int n = 0;
        for(char a : str.toCharArray()){
            if(String.valueOf(a).equals("R")){
                c+=1;
            }
            if(String.valueOf(a).equals("G")){
                b+=1;
            }
        }

        //GGRRRGG :G n=0 m=1 j=1 i = 0, G n=0 m=2 j=2 i = 0, R i=1 m=2 j=2 n=1 , R i2 m2 j2 n2 R i3 m2 j2 n3 G i3 m3 j3 n3  G i3 m4 j4 n3
        //GRGRGR : G n0 m1 j1 i0 R n1 m1 j1 i1 G n1 m2 j2 i1 R n2 m2 j2 i2


        for (int k = 0; k < str.length();   k++) {
            char a = str.charAt(k);

            if(String.valueOf(a).equals("R")) {
                m=0;
                i+=1;
            } else {
                i = 0;
                m += 1;
            }

            if(String.valueOf(a).equals("R")){
//                if(m==0) {
//                    i+=1;
//                }
                if(i==c ) {
                    return 0;
                }

            }else{
//                i =0 ;
                if(m==0 ) {
                    j ++;
                }
            }

            if(String.valueOf(a).equals("G")){
//                if(i==0) {
//                    m += 1;
//                }
            }else{
//                m = 0 ;
                if(i==0 ) {
                    n ++;
                }
            }

        }
//        if(i == c ) return 0;
        if(j<n){
            return   j;
        }else{
            return   n ;
        }

    }
}
