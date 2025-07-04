package me.meteoritini.bromination.mixin.bridge;

import me.meteoritini.bromination.config.BrominationConfig;
import me.meteoritini.bromination.util.Miner;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(value = ChatHud.class, priority = 800)
public abstract class ChatMixin {
    @Shadow public abstract void addMessage(Text message);

    @Unique
    private static final Pattern pattern = Pattern.compile("(?s)^(Guild|Officer) > (?:\\[[A-Z]+\\+*] )?((?:[A-z]|[0-9]|_){3,16})(?: \\[(?:[A-z]|[0-9]|_)+])?: ([^:>]+)(?::| >) (.*)");
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"), cancellable = true)
    public void addMessage(Text message, @Nullable MessageSignatureData signatureData, @Nullable MessageIndicator indicator, CallbackInfo ci) {
        String msg = Miner.rawString(message.getString());
        Matcher matcher = pattern.matcher(msg);
        if (!matcher.find() || matcher.groupCount() < 4) return;
        for (String user : BrominationConfig.getInstance().bridgeConfig.users) {
            if (user.equalsIgnoreCase(matcher.group(2))) {
                addMessage(Text.literal(matcher.group(1).equals("Officer") ? "Bridge (Staff) > " : "Bridge > ")
                        .withColor(BrominationConfig.getInstance().bridgeConfig.prefixColor.getRGB())
                        .append(Text.literal(matcher.group(3))
                                .withColor(BrominationConfig.getInstance().bridgeConfig.nameColor.getRGB())
                                .append(Text.literal(": ").formatted(Formatting.GRAY)
                                        .append(Text.literal(matcher.group(4))
                                                .withColor(BrominationConfig.getInstance().bridgeConfig.messageColor.getRGB())))));
                ci.cancel();
                return;
            }
        }
    }
}