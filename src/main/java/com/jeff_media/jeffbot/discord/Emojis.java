package com.jeff_media.jeffbot.discord;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Emojis {

    private static final Map<String,String> REPLACEMENTS = new LinkedHashMap<>();

    static {
        REPLACEMENTS.put(":)",":slight_smile:");
    }

    public static String apply(String string) {
        for(Map.Entry<String,String> replacement : REPLACEMENTS.entrySet()) {
            string = string.replace(replacement.getKey(),replacement.getValue());
        }
        return string;
    }

    public static String[] apply(String[] strings) {
        for(int i = 0; i < strings.length; i++) {
            strings[i] = apply(strings[i]);
        }
        return strings;
    }

    public static List<String> apply(List<String> strings) {
        for(int i = 0; i < strings.size(); i++) {
            strings.set(i, apply(strings.get(i)));
        }
        return strings;
    }

}
