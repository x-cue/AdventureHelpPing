package com.xcue;

import com.xcue.lib.AdventureHelpPing;
import com.xcue.lib.AdventureSession;
import com.xcue.lib.ping.PingLoader;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class AdventureHelpPingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.

        // Session manager
        ClientPlayConnectionEvents.JOIN.register(new AdventureSession.JoinGameHandler());

        // Ping KeyBind
        ClientTickEvents.END_CLIENT_TICK.register(new AdventureHelpPing());

        WorldRenderEvents.END.register(PingLoader::renderPings);
    }
}