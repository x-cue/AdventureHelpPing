package com.xcue.lib.ping;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

import java.util.HashMap;
import java.util.Map;

public final class PingLoader {
    private PingLoader() {

    }

    private static Map<String, Ping> pings;

    static {
        pings = new HashMap<>();
    }

    public static void addPing(Ping ping, int seconds) {
        pings.put(ping.ign(), ping);
        ping.setLifetime(seconds);
    }

    public static void renderPings(WorldRenderContext context) {
        pings.values().forEach(x -> x.render(context, ping -> {
            pings.remove(ping.ign());
        }));
    }
}
