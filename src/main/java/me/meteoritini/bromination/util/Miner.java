package me.meteoritini.bromination.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class Miner {
    public static void sendMessage(Text text) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
    }
    public static String rawString(String message) {return message.replaceAll("§([0-9]|[a-f]|r|[k-o])", "");}
    public static final Style STYLE_ERASE = Style.EMPTY.withBold(false).withItalic(false).withObfuscated(false).withStrikethrough(false).withUnderline(false);
}
