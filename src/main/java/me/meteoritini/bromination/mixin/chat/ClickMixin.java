package me.meteoritini.bromination.mixin.chat;

import me.meteoritini.bromination.ChatOptions;
import me.meteoritini.bromination.config.BrominationConfig;
import me.meteoritini.bromination.config.overrides.IChatHudLine;
import me.meteoritini.bromination.util.Miner;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class ClickMixin {

    @Shadow protected abstract int getMessageLineIndex(double chatLineX, double chatLineY);

    @Shadow protected abstract double toChatLineX(double x);

    @Shadow protected abstract double toChatLineY(double y);

    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;

    @Shadow @Final private List<ChatHudLine> messages;

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mouseClicked(double mouseX, double mouseY, CallbackInfoReturnable<Boolean> cir) {
        if(!BrominationConfig.getInstance().utilitiesConfig.copyChat || !ChatOptions.ctrlPressed) return;
        int i = getMessageLineIndex(toChatLineX(mouseX), toChatLineY(mouseY));
        if(i >= 0 && i < visibleMessages.size()) {
            int reference = ((IChatHudLine) (Object) visibleMessages.get(i)).bromination$getReference();
            for(ChatHudLine line : messages) {
                if(((IChatHudLine) (Object) line).bromination$getReference() == reference) {
                    client.keyboard.setClipboard(Miner.rawString(line.content().getString()));
                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                }
            }
        }
    }
}
