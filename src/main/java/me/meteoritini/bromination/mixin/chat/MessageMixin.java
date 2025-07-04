package me.meteoritini.bromination.mixin.chat;

import me.meteoritini.bromination.ChatOptions;
import me.meteoritini.bromination.config.BrominationConfig;
import me.meteoritini.bromination.config.overrides.IChatHudLine;
import me.meteoritini.bromination.util.Miner;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ChatHud.class, priority = 100000)
public class MessageMixin {
    @Shadow @Final private List<ChatHudLine> messages;

    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;

    @Shadow @Final private MinecraftClient client;

    @Shadow private void logChatMessage(ChatHudLine line) {};
    @Shadow private void addVisibleMessage(ChatHudLine line) {};
    @Shadow private void addMessage(ChatHudLine line) {};

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"), cancellable = true)
    public void mixin_addMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        if(!BrominationConfig.getInstance().utilitiesConfig.collapseChat) return;

        String key = Miner.rawString(message.getString());
        ChatOptions.MessageOccurrence occurrence = ChatOptions.collapse.get(key);
        if(occurrence == null || occurrence.time() + 60000 < System.currentTimeMillis()) {
            inner_addMessage(message, signatureData, indicator, ++ChatOptions.referenceCounter);
            ChatOptions.collapse.put(key, new ChatOptions.MessageOccurrence(1, ChatOptions.referenceCounter, System.currentTimeMillis()));
            ci.cancel();
            return;
        }
        messages.removeIf(line -> ((IChatHudLine) (Object) line).bromination$getReference() == occurrence.reference());
        visibleMessages.removeIf(line -> ((IChatHudLine) (Object) line).bromination$getReference() == occurrence.reference());
        message = message.copy().append(Text.literal(" (" + (occurrence.amount()+1) + ")").formatted(Formatting.RESET).withColor(Colors.GRAY));

        inner_addMessage(message, signatureData, indicator, occurrence.reference());

        ChatOptions.collapse.put(key, new ChatOptions.MessageOccurrence(occurrence.amount()+1, occurrence.reference(), System.currentTimeMillis()));
        ci.cancel();
    }

    @Inject(method = "addMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V", at = @At(value = "INVOKE", target = "Ljava/util/List;remove(I)Ljava/lang/Object;"), cancellable = true)
    private void mixin_addMessage(ChatHudLine message, CallbackInfo ci) {
        if(BrominationConfig.getInstance().utilitiesConfig.unlimitedChat) ci.cancel();
    }

    @Inject(method = "addVisibleMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V", at = @At(value = "INVOKE", target = "Ljava/util/List;remove(I)Ljava/lang/Object;"), cancellable = true)
    private void mixin_addVisibleMessage(ChatHudLine message, CallbackInfo ci) {
        if(BrominationConfig.getInstance().utilitiesConfig.unlimitedChat) ci.cancel();
    }

    @Unique
    private void inner_addMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, int reference) {
        ChatHudLine first = messages.isEmpty()?null:messages.getFirst();
        ChatHudLine.Visible firstVisible = visibleMessages.isEmpty()?null:visibleMessages.getFirst();

        ChatHudLine chatHudLine = new ChatHudLine(client.inGameHud.getTicks(), message, signatureData, indicator);
        logChatMessage(chatHudLine);
        addVisibleMessage(chatHudLine);
        addMessage(chatHudLine);

        for(ChatHudLine line : messages) {
            if(line == first) break;
            ((IChatHudLine) (Object) line).bromination$setReference(reference);
        }
        for(ChatHudLine.Visible line : visibleMessages) {
            if(line == firstVisible) break;
            ((IChatHudLine) (Object) line).bromination$setReference(reference);
        }
    }
}