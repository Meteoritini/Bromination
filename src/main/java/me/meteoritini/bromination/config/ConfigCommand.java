package me.meteoritini.bromination.config;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;

public class ConfigCommand {
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register(ClientCommands.literal("bromination").executes(context -> {
            Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(BrominationConfig.createGUI(Minecraft.getInstance().screen)));
            return 1;
        }));
    }
}