package me.meteoritini.bromination;

import me.meteoritini.bromination.module.Bridge;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import java.io.IOException;

public class BrominationClient implements ClientModInitializer {
    public static final String MOD_ID = "bromination";

    @Override
    public void onInitializeClient() {
        try {
            Config.load();
        } catch (IOException ignored) {}

        ClientCommandRegistrationCallback.EVENT.register(Bridge::registerCommands);
    }
}
