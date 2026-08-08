package me.meteoritini.bromination;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.meteoritini.bromination.util.Log;
import me.meteoritini.bromination.util.Miner;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class UpdateCheck {
    private static int state = 0;
    private static String remote;

    private static void sendUpdateMessage() {
        Miner.sendMessage(Component.literal("[" + BrominationClient.MOD_NAME + "] New version available: ").withStyle(ChatFormatting.DARK_AQUA).append(Component.literal(remote).setStyle(Style.EMPTY.withClickEvent(new ClickEvent.OpenUrl(URI.create("https://github.com/Meteoritini/Bromination/releases/latest")))).withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD, ChatFormatting.UNDERLINE)));
    }

    public static void check() {
        if(state == 0) {
            state = 1;
            CompletableFuture.runAsync(() -> {
                try {
                    URL url = URI.create("https://api.github.com/repos/Meteoritini/Bromination/releases/latest").toURL();
                    JsonObject json = JsonParser.parseReader(new InputStreamReader(url.openStream())).getAsJsonObject();
                    remote = json.get("tag_name").getAsString();
                    Log.log("Remote has version " + remote);
                    state = remote.compareTo(BrominationClient.MOD_VERSION) > 0?2:3;
                } catch (IOException | NullPointerException e) {
                    Log.log("Error checking for updates: " + e.getMessage());
                }
                check();
            });
        } else if(state == 2) {
            sendUpdateMessage();
        }
    }
}
