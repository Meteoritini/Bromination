package me.meteoritini.bromination.mixin.chat;

import me.meteoritini.bromination.ChatOptions;
import me.meteoritini.bromination.config.overrides.IChatHudLine;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMessage.Line.class)
public class ChatHudLineMixinVisible implements IChatHudLine {
    @Unique
    int reference = 0;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initializer(GuiMessage parent, FormattedCharSequence content, boolean endOfEntry, CallbackInfo ci) {
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
