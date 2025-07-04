package me.meteoritini.bromination.mixin.chat;

import me.meteoritini.bromination.config.overrides.IChatHudLine;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ChatHudLine.class)
public class ChatHudLineMixin implements IChatHudLine {
    @Unique
    int reference = 0;

    @Override
    public void bromination$setReference(int reference) {
        this.reference = reference;
    }

    @Override
    public int bromination$getReference() {
        return reference;
    }
}
