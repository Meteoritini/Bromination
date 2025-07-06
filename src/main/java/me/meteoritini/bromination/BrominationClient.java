package me.meteoritini.bromination;

import me.meteoritini.bromination.config.ConfigCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class BrominationClient implements ClientModInitializer {
    public static final String MOD_ID = "bromination", MOD_VERSION = "v1.2.1", MOD_NAME = "Bromination";

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(ConfigCommand::registerCommands);
        ClientPlayConnectionEvents.JOIN.register((a, b, c) -> UpdateCheck.check());
    }
}
