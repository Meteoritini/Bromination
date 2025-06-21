package me.meteoritini.bromination.config.category;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import me.meteoritini.bromination.config.BrominationConfig;
import net.minecraft.text.Text;

import java.awt.*;

public class BridgeCategory {
    public static ConfigCategory create(BrominationConfig defaults, BrominationConfig config) {
        return ConfigCategory.createBuilder()
                .name(Text.literal("Bridge Config"))
                .group(
                        ListOption.<String>createBuilder()
                                .name(Text.literal("Bridge Bot IGNs"))
                                .description(OptionDescription.of(Text.literal("Enter the IGNs of the Bridge Bots you want this mod to work with.")))
                                .binding(
                                        defaults.bridgeConfig.users,
                                        () -> config.bridgeConfig.users,
                                        newVal -> config.bridgeConfig.users = newVal
                                ).initial("")
                                .controller(StringControllerBuilder::create)
                                .build()
                ).group(
                        OptionGroup.createBuilder()
                                .name(Text.literal("Color settings"))
                                .description(OptionDescription.of(Text.literal("Change the colors used in the bridge message.")))
                                .option(
                                        Option.<Color>createBuilder()
                                                .name(Text.literal("Prefix"))
                                                .description(OptionDescription.of(Text.literal("Change the prefix color.")))
                                                .binding(
                                                        defaults.bridgeConfig.prefixColor,
                                                        () -> config.bridgeConfig.prefixColor,
                                                        newVal -> config.bridgeConfig.prefixColor = newVal
                                                )
                                                .controller(ColorControllerBuilder::create).build()
                                ).option(
                                        Option.<Color>createBuilder()
                                                .name(Text.literal("Name"))
                                                .description(OptionDescription.of(Text.literal("Change the name color.")))
                                                .binding(
                                                        defaults.bridgeConfig.nameColor,
                                                        () -> config.bridgeConfig.nameColor,
                                                        newVal -> config.bridgeConfig.nameColor = newVal
                                                )
                                                .controller(ColorControllerBuilder::create).build()
                                ).option(
                                        Option.<Color>createBuilder()
                                                .name(Text.literal("Message"))
                                                .description(OptionDescription.of(Text.literal("Change the message color.")))
                                                .binding(
                                                        defaults.bridgeConfig.messageColor,
                                                        () -> config.bridgeConfig.messageColor,
                                                        newVal -> config.bridgeConfig.messageColor = newVal
                                                )
                                                .controller(ColorControllerBuilder::create).build()
                                )
                                .build()
                ).build();
    }
}