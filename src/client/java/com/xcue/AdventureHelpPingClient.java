package com.xcue;

import com.xcue.lib.AdventureHelpPing;
import com.xcue.lib.AdventureSession;
import com.xcue.lib.render.RenderHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiling.jfr.event.PacketReceivedEvent;
import org.lwjgl.glfw.GLFW;

public class AdventureHelpPingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.

        // Session manager
        ClientPlayConnectionEvents.JOIN.register(new AdventureSession.JoinGameHandler());

        // Ping KeyBind
        ClientTickEvents.END_CLIENT_TICK.register(new AdventureHelpPing());

        WorldRenderEvents.END.register((context -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;

            if (player == null) return;

            player.sendMessage(Text.literal("Rendering lines"));
            Vec3d endPoint = new Vec3d(100.5, 100.5, 100.5);
            Vec3d[] points = new Vec3d[]{player.getBlockPos().toCenterPos(), endPoint};
            RenderHelper.renderLinesFromPoints(context, points, new float[]{0, 0, 0}, 1, 2, true);
            RenderHelper.renderWaypoint(context, new BlockPos(100, 100, 100), new float[]{1F, 0F, 0F}, 0.5F, true);
            //RenderHelper.renderFilledWithBeaconBeam(context, new BlockPos(100, 100, 100), new float[]{1F, 0F, 0F}, 0.5F, true);
        }));

        WorldRenderEvents.AFTER_TRANSLUCENT.register((context -> {
            RenderHelper.renderText(context, Text.literal("Waypoint"), new BlockPos(100, 100, 100).toCenterPos(),true);
        }));
    }
}