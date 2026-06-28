package me.meteoritini.bromination.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class Miner {
    public static void sendMessage(Component text) {
        Minecraft.getInstance().gui.getChat().addClientSystemMessage(text);
    }
    public static String rawString(String message) {return message.replaceAll("§([0-9]|[a-f]|r|[k-o])", "");}
    public static final Style STYLE_ERASE = Style.EMPTY.withBold(false).withItalic(false).withObfuscated(false).withStrikethrough(false).withUnderlined(false);
}
