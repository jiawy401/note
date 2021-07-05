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

        System.out.println(dye("RGRGR"));
        System.out.println(dye("GGGGGR"));
        System.out.println(dye("RGGGGG"));
        System.out.println(dye("GGGGG"));
    }

    public static int  dye(String str){

        int c = 0 ;
        int i = 0 ;
        int j = 0 ;
        int m =0 ;
        int n = 0;


        for (int k = 0; k < str.length();   k++) {
            char a = str.charAt(k);
            if(a == 'R'){
                i=+1;
                c=+1;
            }else{
                i = 0 ;
                j ++;
            }

            if(a == 'G'){
                m =+1;
            }else{
                m = 0 ;
                n ++;
            }

        }
        if(i == c ) return 0;
        if(j<n){
            m = j;
        }else{
            m = n ;
        }
        return m ;
    }
}
