package com.xcue.mixin.client;

import com.xcue.lib.Adventure;
import com.xcue.lib.AdventureSession;
import joptsimple.util.RegexMatcher;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// Mixins are top-bottom in order of when they are called
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Unique
    private static final String advRegEx = "(" + Arrays.stream(Adventure.values()).map(x -> String.format("(?:%s)", x.getName())).collect(Collectors.joining("|")) + ")";

    @Inject(at = @At("TAIL"), method = "onGameMessage")
    public void startAdventureSession(GameMessageS2CPacket packet, CallbackInfo callbackInfo) {
        String msg = packet.content().getString().trim();

        Pattern pattern = Pattern.compile(String.format("^(?i)\\(!\\) Traveling to the %s Adventure!$", advRegEx));
        Matcher matcher = pattern.matcher(msg);

        if (!AdventureSession.isStarted() && matcher.matches()) {
            AdventureSession.startSession();
            AdventureSession.setAdventure(Adventure.forName(matcher.group(1)));
        }
    }
}