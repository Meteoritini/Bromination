package me.meteoritini.bromination.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import me.meteoritini.bromination.config.category.BridgeCategory;
import me.meteoritini.bromination.config.configs.BridgeConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BrominationConfig {
    public static final ConfigClassHandler<BrominationConfig> HANDLER = ConfigClassHandler.createBuilder(BrominationConfig.class)
            .id(Identifier.of("bromination", "bromination"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("bromination.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    static {
        HANDLER.load();
    }

    @SerialEntry
    public BridgeConfig bridgeConfig = new BridgeConfig();

    public static BrominationConfig getInstance() {
        return HANDLER.instance();
    }

    public static Screen createGUI(Screen parent) {
        return YetAnotherConfigLib.create(HANDLER, (defaults, config, builder) ->
                builder.title(Text.literal("Bromination Configuration"))
                        .category(BridgeCategory.create(defaults, config))
        ).generateScreen(parent);
    }
}