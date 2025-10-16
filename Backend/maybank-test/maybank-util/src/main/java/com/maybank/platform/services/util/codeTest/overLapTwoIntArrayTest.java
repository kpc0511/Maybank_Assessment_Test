package com.maybank.platform.services.util.codeTest;

import java.util.stream.IntStream;

public class overLapTwoIntArrayTest {
    public static void main(String[] args) {
        int[] intArray1 = {3,7};
        int[] intArray2 = {5,10};

        if(isOverLap2(intArray1, intArray2)) {
            System.out.println("The range overlap");
        } else {
            System.out.println("The range do not overlap");
        }
    }

    public static boolean isOverLap(int[] array1, int[] array2) {
        int start1 = array1[0];
        int end1 = array1[1];

        int start2 = array2[0];
        int end2 = array2[1];

        return start1 <= end2 && start2 <= end1;
    }

    public static boolean isOverLap2(int[] array1, int[] array2) {
        return IntStream.rangeClosed(array1[0], array1[1]).anyMatch(
                i -> IntStream.rangeClosed(array2[0], array2[1]).anyMatch(j -> i == j)
        );
    }

}
