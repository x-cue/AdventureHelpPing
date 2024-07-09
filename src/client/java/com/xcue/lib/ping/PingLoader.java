package com.xcue.lib.ping;

import com.xcue.lib.Adventure;
import com.xcue.lib.AdventureSession;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class PingLoader {
    private PingLoader() {

    }

    private static Map<Adventure, Map<String, Ping>> pings;

    static {
        pings = new HashMap<>();
        for (Adventure adv : Adventure.values()) {
            pings.put(adv, new HashMap<>());
        }
    }

    public static void addPing(Ping ping, int seconds) {
        ping.setLifetime(seconds);
        pings.get(ping.adv()).put(ping.ign(), ping);
    }

    // Renders pings only from the player's current adventure
    public static void renderPings(WorldRenderContext context) {
        Map<String, Ping> advPings = PingLoader.pings.getOrDefault(AdventureSession.getAdventure(), new HashMap<>());
        List<Ping> pings = new ArrayList<>(advPings.values());

        for (Ping ping : pings) {
            ping.render(context, p -> advPings.remove(ping.ign()));
        }
    }
}
