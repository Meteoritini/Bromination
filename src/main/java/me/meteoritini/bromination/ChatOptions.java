package me.meteoritini.bromination;

import java.util.HashMap;

public class ChatOptions {
    public static HashMap<String, MessageOccurrence> collapse = new HashMap<>();
    public static int referenceCounter = 0, nextReference = referenceCounter;
    public static boolean ctrlPressed = false;

    public static void reset() {
        collapse.clear();
    }
    public record MessageOccurrence(int amount, int reference, long time) {}
}
