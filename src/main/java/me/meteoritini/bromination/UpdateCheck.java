package me.meteoritini.bromination;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.meteoritini.bromination.util.Log;
import me.meteoritini.bromination.util.Miner;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class UpdateCheck {
    private static int state = 0;
    private static String remote;

    private static void sendUpdateMessage() {
        Miner.sendMessage(Text.literal("[" + BrominationClient.MOD_NAME + "] New version available: " + remote).formatted(Formatting.DARK_AQUA));
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
