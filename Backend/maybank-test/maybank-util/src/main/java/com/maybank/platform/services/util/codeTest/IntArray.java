package com.maybank.platform.services.util.codeTest;

import java.util.ArrayList;
import java.util.List;

public class IntArray {
    public static void main(String[] args) {
        int[] intArray = {8,7,6,1,3,4,5,0,9};
        List<Integer> missNumbers = checkMissingNumber(intArray);
        System.out.println("The missing numbers are: "+missNumbers);
    }

    public static List<Integer> checkMissingNumber(int[] intArray) {
        boolean[] isPresent = new boolean[10];
        for(int num : intArray) {
            isPresent[num] = true;
        }

        List<Integer> missingNumbers = new ArrayList<>();
        for(int i=0; i< intArray.length; i++) {
            if(!isPresent[i]) {
                missingNumbers.add(i);
            }
        }
        return missingNumbers;
    }
}
