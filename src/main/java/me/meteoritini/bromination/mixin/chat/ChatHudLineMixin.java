package me.meteoritini.bromination.mixin.chat;

import me.meteoritini.bromination.ChatOptions;
import me.meteoritini.bromination.config.overrides.IChatHudLine;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.multiplayer.chat.GuiMessageSource;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMessage.class)
public class ChatHudLineMixin implements IChatHudLine {
    @Unique
    int reference = 0;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initializer(int addedTime, Component content, MessageSignature signature, GuiMessageSource source, GuiMessageTag tag, CallbackInfo ci) {
        reference = ChatOptions.nextReference;
    }

    @Override
    public void bromination$setReference(int reference) {
        this.reference = reference;
    }

    @Override
    public int bromination$getReference() {
        return reference;
    }
}
