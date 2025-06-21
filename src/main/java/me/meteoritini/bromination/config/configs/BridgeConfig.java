package me.meteoritini.bromination.config.configs;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BridgeConfig {
    @SerialEntry
    public List<String> users = new ArrayList<>();

    @SerialEntry
    public Color prefixColor = new Color(0x00AAAA);

    @SerialEntry
    public Color nameColor = new Color(0xFF55FF);

    @SerialEntry
    public Color messageColor = new Color(0xFFFFFF);
}