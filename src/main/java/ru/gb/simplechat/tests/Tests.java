package ru.gb.simplechat.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tests {

    public static void main(String[] args) {
        System.out.println(checkArray(new int[]{1, 1, 4, 4}));
        System.out.println(Arrays.asList((afterArray(new int[]{1, 2, 7, 5, 10, 5}))));
    }

    public static int[] afterArray(int[] array) {
        int[] newArray = new int[0];
        List list = new ArrayList(Arrays.asList(array));

        if (list.contains(4)) {
            newArray = Arrays.copyOfRange(array, list.indexOf(4), list.size());
        }
        return newArray;
    }

    public static Boolean checkArray(int[] array){
        List list = new ArrayList(Arrays.asList(array));
        if (list.contains(4) && list.contains(1)) {
            return true;
        }
        return false;
    }
}
