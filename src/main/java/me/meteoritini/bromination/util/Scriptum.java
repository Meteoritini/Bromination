package me.meteoritini.bromination.util;

import java.util.List;

public class Scriptum {
    private static int index(List<String> list, String element) {
        String low = element.toLowerCase();
        for(int i = 0; i < list.size(); ++i) {
            if(list.get(i).toLowerCase().equals(low)) return i;
        }
        return -1;
    }
    public static boolean contains(List<String> list, String element) {
        return index(list, element) != -1;
    }

    public static void remove(List<String> list, String element) {
        int i = index(list, element);
        if(i != -1) list.remove(i);
    }
}
