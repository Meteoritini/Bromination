package me.meteoritini.bromination.mixin.chat;

import me.meteoritini.bromination.ChatOptions;
import me.meteoritini.bromination.config.BrominationConfig;
import me.meteoritini.bromination.config.overrides.IChatHudLine;
import me.meteoritini.bromination.util.Miner;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ChatComponent.class, priority = 200)
public class MessageMixin {
    @Shadow @Final private List<GuiMessage> allMessages;

    @Shadow @Final private List<GuiMessage.Line> trimmedMessages;

    @ModifyVariable(method = "addServerSystemMessage", at = @At("HEAD"), argsOnly = true)
    public Component mixin_addMessage(Component message) {
        if(!BrominationConfig.getInstance().utilitiesConfig.collapseChat) {
            ChatOptions.nextReference = ++ChatOptions.referenceCounter;
            return message;
        }

        String key = Miner.rawString(message.getString());
        ChatOptions.MessageOccurrence occurrence = ChatOptions.collapse.get(key);
        if(occurrence == null || occurrence.time() + 60000 < System.currentTimeMillis()) {
            ChatOptions.collapse.put(key, new ChatOptions.MessageOccurrence(1, ChatOptions.nextReference = ++ChatOptions.referenceCounter, System.currentTimeMillis()));
            return message;
        }
        allMessages.removeIf(line -> ((IChatHudLine) (Object) line).bromination$getReference() == occurrence.reference());
        trimmedMessages.removeIf(line -> ((IChatHudLine) (Object) line).bromination$getReference() == occurrence.reference());
        message = message.copy().append(Component.literal(" (" + (occurrence.amount()+1) + ")").setStyle(Miner.STYLE_ERASE).withStyle(ChatFormatting.GRAY));
        ChatOptions.collapse.put(key, new ChatOptions.MessageOccurrence(occurrence.amount()+1, ChatOptions.nextReference = occurrence.reference(), System.currentTimeMillis()));
        return message;
    }

    @Inject(method = "addMessageToQueue", at = @At(value = "INVOKE", target = "Ljava/util/List;removeLast()Ljava/lang/Object;"), cancellable = true)
    private void mixin_addMessage(GuiMessage message, CallbackInfo ci) {
        if(BrominationConfig.getInstance().utilitiesConfig.unlimitedChat) ci.cancel();
    }

    @Inject(method = "addMessageToDisplayQueue", at = @At(value = "INVOKE", target = "Ljava/util/List;removeLast()Ljava/lang/Object;"), cancellable = true)
    private void mixin_addVisibleMessage(GuiMessage message, CallbackInfo ci) {
        if(BrominationConfig.getInstance().utilitiesConfig.unlimitedChat) ci.cancel();
    }

    @Inject(method = "clearMessages", at = @At("HEAD"), cancellable = true)
    private void mixin_clear(boolean history, CallbackInfo ci) {
        if(history && BrominationConfig.getInstance().utilitiesConfig.persistentChat) ci.cancel();
        else ChatOptions.reset();
    }
}