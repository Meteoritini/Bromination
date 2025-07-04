package me.meteoritini.bromination.mixin.chat;

import me.meteoritini.bromination.ChatOptions;
import me.meteoritini.bromination.config.overrides.IChatHudLine;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHudLine.Visible.class)
public class ChatHudLineMixinVisible implements IChatHudLine {
    @Unique
    int reference = 0;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initializer(int i, OrderedText orderedText, MessageIndicator messageIndicator, boolean bl, CallbackInfo ci) {
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
