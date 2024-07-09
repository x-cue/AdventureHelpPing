package com.xcue.mixin.client;

import com.xcue.lib.Adventure;
import com.xcue.lib.AdventureSession;
import com.xcue.lib.ping.Ping;
import com.xcue.lib.ping.PingInfo;
import com.xcue.lib.ping.PingLoader;
import joptsimple.util.RegexMatcher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.text.Text;
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

        Pattern pingP = Pattern.compile(String.format("^(?i).* (\\w+)\\: \\(!\\) Help in %s at (-?\\d+) \\| (\\d+) \\| (-?\\d+) (WEST|NORTH|EAST|SOUTH) \\/ HP: (\\d{1,2})$", advRegEx));
        Matcher pingM = pingP.matcher(msg);
        if (pingM.matches()) {
            String ign = pingM.group(1);
            Adventure adv = Adventure.forName(pingM.group(2));
            int x = Integer.parseInt(pingM.group(3));
            int y = Integer.parseInt(pingM.group(4));
            int z = Integer.parseInt(pingM.group(5));
            Direction dir = Direction.byName(pingM.group(6));
            double hp = Double.parseDouble(pingM.group(7));

            // Spawn ping for X seconds, by IGN
            Ping ping = new Ping(adv, new BlockPos(x, y -1, z), dir, ign, hp);
            PingLoader.addPing(ping, 10);
            // TODO Make a waypoint class that Ping extends
        }
    }
}