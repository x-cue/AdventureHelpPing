package com.xcue.mixin.client;

import com.xcue.lib.Adventure;
import com.xcue.lib.AdventureSession;
import com.xcue.lib.ping.Ping;
import com.xcue.lib.ping.PingInfo;
import com.xcue.lib.ping.PingLoader;
import joptsimple.util.RegexMatcher;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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
    private static final String advRegEx = "(" + Arrays.stream(Adventure.values()).map(Adventure::getName).collect(Collectors.joining("|")) + ")";

    @Inject(at = @At("TAIL"), method = "onGameMessage")
    public void startAdventureSession(GameMessageS2CPacket packet, CallbackInfo callbackInfo) {
        String msg = packet.content().getString().trim();

        Pattern pattern = Pattern.compile(String.format("^(?i)\\(!\\) Traveling to the %s Adventure!$", advRegEx));
        Matcher matcher = pattern.matcher(msg);

        if (!AdventureSession.isStarted() && matcher.matches()) {
            AdventureSession.startSession();
            AdventureSession.setAdventure(Adventure.forName(matcher.group(1)));
            return;
        }

        Pattern pingP = Pattern.compile(String.format("^(?i)\\(!\\) Help in %s at (\\d+) \\| (\\d+) \\| (\\d+) (WEST|NORTH|EAST|SOUTH) \\/ HP: (\\d{1,2})$", advRegEx));
        Matcher pingM = pingP.matcher(msg);
        if (pingM.matches()) {
            Adventure adv = Adventure.forName(pingM.group(1));
            int x = Integer.parseInt(pingM.group(2));
            int y = Integer.parseInt(pingM.group(3));
            int z = Integer.parseInt(pingM.group(4));
            Direction dir = Direction.byName(pingM.group(5));
            double hp = Double.parseDouble(pingM.group(6));

            // TODO get ign

            // Spawn ping for X seconds, by IGN
            Ping ping = new Ping(adv, new BlockPos(x, y, z), dir, "placeholder", hp);
            PingLoader.addPing(ping, 5);
        }
    }
}