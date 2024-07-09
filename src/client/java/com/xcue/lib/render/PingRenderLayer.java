package com.xcue.lib.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;


public class PingRenderLayer {
    private static final RenderPhase.Transparency DEFAULT_TRANSPARENCY = new RenderPhase.Transparency("default_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }, RenderSystem::disableBlend);
    public static final RenderLayer.MultiPhase FILLED_THROUGH_WALLS = RenderLayer.of("filled_through_walls", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.TRIANGLE_STRIP, RenderLayer.CUTOUT_BUFFER_SIZE, false, true, RenderLayer.MultiPhaseParameters.builder()
            .program(RenderPhase.COLOR_PROGRAM)
            .cull(RenderPhase.DISABLE_CULLING)
            .layering(RenderPhase.POLYGON_OFFSET_LAYERING)
            .transparency(DEFAULT_TRANSPARENCY)
            .depthTest(RenderPhase.ALWAYS_DEPTH_TEST)
            .build(false));
}
