package com.maybank.platform.services.util;

import com.maybank.platform.services.util.codeTest.OverLapTwoIntArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestOverlap {

    @Test
    public void testOverlappingRanges() {
        int[] range1 = {3,7};
        int[] range2 = {5,10};
        boolean result = OverLapTwoIntArray.isOverLap2(range1, range2);
        assertTrue(result, "The ranges is overlap.");
    }

    @Test
    public void testNonOverlappingRanges() {
        int[] range1 = {1,3};
        int[] range2 = {5,10};
        boolean result = OverLapTwoIntArray.isOverLap2(range1, range2);
        assertFalse(result, "The ranges is not overlap.");
    }

    @Test
    public void testTouchFirstOverlappingRanges() {
        int[] range1 = {1,5};
        int[] range2 = {5,10};
        boolean result = OverLapTwoIntArray.isOverLap2(range1, range2);
        assertTrue(result, "The ranges is overlap.");
    }

    @Test
    public void testSameRanges() {
        int[] range1 = {5,10};
        int[] range2 = {5,10};
        boolean result = OverLapTwoIntArray.isOverLap2(range1, range2);
        assertTrue(result, "The ranges is overlap.");
    }

    @Test
    public void testOnlyOnePointRange() {
        int[] range1 = {5,5};
        int[] range2 = {3,10};
        boolean result = OverLapTwoIntArray.isOverLap2(range1, range2);
        assertTrue(result, "The ranges is overlap.");
    }

    @Test
    public void testBeforeRange() {
        int[] range1 = {1,2};
        int[] range2 = {3,10};
        boolean result = OverLapTwoIntArray.isOverLap2(range1, range2);
        assertFalse(result, "The ranges is overlap.");
    }

    @Test
    public void testAfterRange() {
        int[] range1 = {8,9};
        int[] range2 = {3,7};
        boolean result = OverLapTwoIntArray.isOverLap2(range1, range2);
        assertFalse(result, "The ranges is overlap.");
    }
}
