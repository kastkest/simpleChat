package ru.gb.simplechat.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestsTest {
    private Tests t = new Tests();

    @Test
    public void afterArray() {
        Integer[] result = t.afterArray(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        assertEquals(new Integer[]{5, 6, 7}, result);
    }

    @Test
    public void afterArrayWithZero() {
        Integer[] result = t.afterArray(new Integer[]{0, 0, 0, 0, 0, 0, 0});
        assertEquals(null, result);
    }

    @Test
    public void afterArrayWithNull() {
        Integer[] result = t.afterArray(null);
        assertEquals(null, result);
    }


    @Test
    public void checkArray() {
        Boolean result = t.checkArray(new Integer[]{1, 1, 1, 1, 1, 1, 1});
        assertEquals(true, result);
    }

    @Test
    public void checkArrayWithNegative() {
        Boolean result = t.checkArray(new Integer[]{-1, -1, -1});
        assertEquals(false, result);
    }
    @Test
    public void checkArrayWithExcept() {
        Boolean result = t.checkArray(new Integer[]{-1, 0, 3});
        assertEquals(false, result);
    }
}