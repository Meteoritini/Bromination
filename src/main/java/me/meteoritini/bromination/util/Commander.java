package me.meteoritini.bromination.util;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;

public class Commander {
    public static boolean shouldSuggest(SuggestionsBuilder builder, String arg) {
        return arg.toLowerCase().startsWith(builder.getRemainingLowerCase());
    }
}
