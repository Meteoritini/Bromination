package me.meteoritini.bromination.module;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import me.meteoritini.bromination.Config;
import me.meteoritini.bromination.util.Chroma;
import me.meteoritini.bromination.util.Commander;
import me.meteoritini.bromination.util.Scriptum;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bridge {
    public static List<String> users = new ArrayList<>();
    public static String[] colourConf = {"DARK_AQUA", "LIGHT_PURPLE", "WHITE"};

    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(ClientCommandManager.literal("bridge")
            .then(ClientCommandManager.literal("add")
                .then(ClientCommandManager.argument("Username", StringArgumentType.string()).executes(context -> {
                    String name = StringArgumentType.getString(context, "Username");
                    if(Scriptum.contains(users, name)) {
                        context.getSource().sendError(Text.literal("User already is a registered bridge bot!"));
                    } else {
                        users.add(name);
                        try {
                            Config.save();
                        } catch (IOException e) {
                            context.getSource().sendError(Text.literal("Saving config failed: " + e.getMessage()));
                            return 1;
                        }
                        context.getSource().sendFeedback(Text.literal("Registered " + name + " as a bridge bot."));
                    }
                    return 0;
                }
                ))
            ).then(ClientCommandManager.literal("remove")
                .then(ClientCommandManager.argument("Username", StringArgumentType.string())
                    .suggests((ctx, build) -> {
                        for(String s : users) if(Commander.shouldSuggest(build, s)) build.suggest(s);
                        return build.buildFuture();
                    }).executes(context -> {
                        String name = StringArgumentType.getString(context, "Username");
                        if(Scriptum.contains(users, name)) {
                            Scriptum.remove(users, name);
                            try {
                                Config.save();
                            } catch (IOException e) {
                                context.getSource().sendError(Text.literal("Saving config failed: " + e.getMessage()));
                                return 1;
                            }
                            context.getSource().sendFeedback(Text.literal("Deregistered " + name + " as a bridge bot."));
                        } else {
                            context.getSource().sendError(Text.literal("User is not a registered bridge bot!"));
                        }
                        return 0;
                    })
                )
            ).then(ClientCommandManager.literal("colour")
                .then(ClientCommandManager.literal("prefix")
                    .executes(context -> commandGetColour(context, "Prefix", 0)).then(ClientCommandManager.argument("colour", StringArgumentType.string())
                        .suggests((ctx, build) -> {
                            Chroma.suggestColours(build);
                            return build.buildFuture();
                        }).executes(context -> {return commandSetColour(context, "Prefix", 0);})
                    )
                ).then(ClientCommandManager.literal("name")
                    .executes(context -> commandGetColour(context, "Name", 1)).then(ClientCommandManager.argument("colour", StringArgumentType.string())
                        .suggests((ctx, build) -> {
                            Chroma.suggestColours(build);
                            return build.buildFuture();
                        }).executes(context -> {return commandSetColour(context, "Name", 1);})
                    )
                ).then(ClientCommandManager.literal("message")
                    .executes(context -> commandGetColour(context, "Message", 2)).then(ClientCommandManager.argument("colour", StringArgumentType.string())
                        .suggests((ctx, build) -> {
                            Chroma.suggestColours(build);
                            return build.buildFuture();
                        }).executes(context -> {return commandSetColour(context, "Message", 2);})
                    )
                )
            )
        );
    }

    private static int commandSetColour(CommandContext<FabricClientCommandSource> context, String name, int key) {
        String c = StringArgumentType.getString(context, "colour").toUpperCase();
        if(!Chroma.isValidColour(c)) {
            context.getSource().sendError(Text.literal(c + " is not a valid colour!"));
            return 1;
        }
        colourConf[key] = c;
        try {
            Config.save();
        } catch (IOException e) {
            context.getSource().sendError(Text.literal("Saving config failed: " + e.getMessage()));
            return 1;
        }
        context.getSource().sendFeedback(Text.literal(name + " colour is now ").append(Text.literal(colourConf[key]).withColor(Chroma.getColour(colourConf[key]))));
        return 0;
    }

    private static int commandGetColour(CommandContext<FabricClientCommandSource> context, String name, int key) {
        context.getSource().sendFeedback(Text.literal(name + " colour is ").append(Text.literal(colourConf[key]).withColor(Chroma.getColour(colourConf[key]))));
        return 0;
    }
}
