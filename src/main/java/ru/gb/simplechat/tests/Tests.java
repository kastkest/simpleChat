package ru.gb.simplechat.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tests {

    public static void main(String[] args) {
        Tests test = new Tests();
        System.out.println(test.checkArray(new Integer[]{1, 1, 4, 4}));
        System.out.println(Arrays.asList((test.afterArray(new Integer[]{1, 4, 7, 5, 10, 5}))));
    }

    public Integer[] afterArray(Integer[] array) {
        Integer[] newArray = new Integer[0];
        List<Integer> list = new ArrayList(Arrays.asList(array));

        if (list.contains(4)) {
            newArray = Arrays.copyOfRange(array, list.indexOf(4) + 1, list.size());
        }
        return newArray;
    }

    public Boolean checkArray(Integer[] array) {
        List<Integer> list = new ArrayList(Arrays.asList(array));
        return list.contains(1) || list.contains(4);
    }


}
