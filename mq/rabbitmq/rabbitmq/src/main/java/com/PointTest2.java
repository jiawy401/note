package com;

import java.util.Arrays;

public class PointTest2 {

    public static int cordCoverMaxPoint(int[] arr, int L) {
        if (arr == null || arr.length == 0) return 0;

        System.out.println(Arrays.toString(arr));

        int max = 0;
        int curMax = 0;
        int startIndex = 0;
        for (int i = 0; i < arr.length; ) {
            if (arr[i] < arr[startIndex] + L) {
                curMax++;
                i++;
            } else {
                curMax--;
                startIndex++;
            }
            max = Math.max(max, curMax);
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println(cordCoverMaxPoint(new int[]{1, 3, 4, 5}, 4));
        System.out.println(cordCoverMaxPoint(new int[]{9, 13, 24, 35, 46, 57, 60, 72, 87}, 2));
    }
}
