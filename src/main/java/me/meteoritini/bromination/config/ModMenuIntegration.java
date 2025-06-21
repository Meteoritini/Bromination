package me.meteoritini.bromination.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi
{
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return BrominationConfig::createGUI;
    }
}