package com.xcue.lib;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

public final class AdventureSession {
    private AdventureSession() {

    }

    static {
        sessionStart = null;
    }

    private static boolean isOnCosmicSky;
    private static ZonedDateTime sessionStart;
    private static Adventure adventure;

    public static void setServer(ServerInfo info) {
        if (info == null) {
            isOnCosmicSky = false;
            return;
        }

        isOnCosmicSky = info.address.equalsIgnoreCase("cosmicsky.net") && info.name.equalsIgnoreCase("cosmic sky");
    }

    public static boolean isOnCosmicSky() {
        return isOnCosmicSky;
    }

    /**
     * Starts an adventure session, enabling the rest of the mod
     */
    public static void startSession() {
        if (isOnCosmicSky() && sessionStart == null) {
            sessionStart = ZonedDateTime.now(ZoneId.systemDefault());

            // MinecraftClient.getInstance().player.sendMessage(Text.literal("Adventure session started"));
        }
    }

    /**
     * Terminates the adventure session immediately
     */
    public static void stopSession() {
        sessionStart = null;
    }

    /**
     * @return Whether an adventure session is in progress
     */
    public static boolean isStarted() {
        return sessionStart != null;
    }

    @Nullable
    public static ZonedDateTime getSessionStart() {
        return sessionStart;
    }

    @Nullable
    public static Adventure getAdventure() {
        return adventure;
    }

    public static void setAdventure(Adventure adv) {
        adventure = adv;
    }

    public static class JoinGameHandler implements ClientPlayConnectionEvents.Join {

        @Override
        public void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
            stopSession();
            setServer(handler.getServerInfo());
        }
    }
}
