package me.meteoritini.bromination;

import me.meteoritini.bromination.module.Bridge;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

import java.io.IOException;

public class BrominationClient implements ClientModInitializer {
    public static final String MOD_ID = "bromination", MOD_VERSION = "v1.0", MOD_NAME = "Bromination";

    @Override
    public void onInitializeClient() {
        try {
            Config.load();
        } catch (IOException ignored) {}

        ClientCommandRegistrationCallback.EVENT.register(Bridge::registerCommands);
        ClientPlayConnectionEvents.JOIN.register((a, b, c) -> UpdateCheck.check());
    }
}
