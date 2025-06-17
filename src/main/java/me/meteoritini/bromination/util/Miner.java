package me.meteoritini.bromination.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class Miner {
    public static void sendMessage(Text text) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
    }
}
