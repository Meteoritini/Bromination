package me.meteoritini.bromination.util;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;

public class Chroma {

    private static final Map<String, Integer> colours = new HashMap<>();
    static {
        for(Formatting format : Formatting.values()) {
            if(format.getColorValue() != null) {
                colours.put(format.name(), 0xFF000000 + format.getColorValue());
            }
        }
    }

    public static boolean isValidColour(String s) {
        if(s == null) return false;
        if(s.startsWith("#")) {
            return s.matches("^#([0-9]|[A-F]){6}$");
        } else {
            return colours.containsKey(s);
        }
    }

    public static int getColour(String s) {
        if(!isValidColour(s)) return 0xFFFFFFFF;
        if(s.startsWith("#")) return toARGB(s);
        return colours.get(s);

    }
        private static int toARGB(String hex) {
        return 0xFF000000 + HexFormat.fromHexDigits(hex.substring(1));
    }

    public static void suggestColours(SuggestionsBuilder build) {
        for(String s : colours.keySet()) if(Commander.shouldSuggest(build, s)) build.suggest(s);
    }
}
