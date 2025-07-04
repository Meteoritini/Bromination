package me.meteoritini.bromination.config.configs;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

public class UtilitiesConfig {
    @SerialEntry
    public boolean collapseChat = false;

    @SerialEntry
    public boolean copyChat = true;

    @SerialEntry
    public boolean unlimitedChat = true;
}
