package me.meteoritini.bromination;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class BrominationClient implements ClientModInitializer {
    public static final String MOD_ID = "bromination", MOD_VERSION = "v1.0", MOD_NAME = "Bromination";

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((a, b, c) -> UpdateCheck.check());
    }
}
