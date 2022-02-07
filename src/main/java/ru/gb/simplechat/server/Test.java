package ru.gb.simplechat.server;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        String[] split = {"мама", "мыла", "раму", "кукусь," , "незачто", "шлеп", "жмяк", "чмафки"};
        String[] privateMessage = new String[split.length - 2];
        System.out.println(Arrays.toString(split));
        System.arraycopy(split, 2, privateMessage, 0, split.length - 2);
        System.out.println(Arrays.toString(privateMessage));
        String pm = String.join(" ", privateMessage);
        System.out.println(pm);
    }
}
