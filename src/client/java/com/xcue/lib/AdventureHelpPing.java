package com.xcue.lib;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

import java.text.DecimalFormat;

public class AdventureHelpPing implements ClientTickEvents.EndTick {
    private static final KeyBinding keyBinding;
    private static final DecimalFormat formatter;

    static {
        formatter = new DecimalFormat("#.##");
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Adventure Ping", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_R, // The keycode of the key
                "Adventures" // The translation key of the keybinding's category.
        ));
    }


    @Override
    public void onEndTick(MinecraftClient client) {
        if (keyBinding.wasPressed()) {
            ClientPlayerEntity p = client.player;
            if (p == null) return;


            if (AdventureSession.isStarted() && AdventureSession.getAdventure() != null) {
                // Send ping in chat

                BlockPos pos = p.getBlockPos();

                String helpMsg = String.format("(!) Help in %s at %d | %d | %d %s / HP: %s",
                        AdventureSession.getAdventure().getName(),
                        pos.getX(), pos.getY(), pos.getZ(),
                        p.getMovementDirection().getName().toUpperCase(),
                        formatter.format(p.getHealth())
                );

                p.networkHandler.sendChatMessage(helpMsg);
            } else {
                p.sendMessage(Text.literal("Ping only works in adventures"));
            }
        }
    }
}
