package com.xcue.lib.ping;

import com.xcue.lib.Adventure;
import com.xcue.lib.AdventureSession;
import com.xcue.lib.render.RenderHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.time.ZonedDateTime;
import java.util.function.Consumer;

public final class Ping {
    private final Adventure adv;
    private final BlockPos pos;
    private final Direction dir;
    private final String ign;
    private final double hp;

    public Ping(Adventure adv, BlockPos pos, Direction dir, String ign, double hp) {
        this.adv = adv;
        this.pos = pos;
        this.dir = dir;
        this.ign = ign;
        this.hp = hp;
    }

    public Adventure adv() {
        return this.adv;
    }

    public BlockPos pos() {
        return this.pos;
    }

    public Direction dir() {
        return this.dir;
    }

    public String ign() {
        return this.ign;
    }

    public double hp() {
        return this.hp;
    }

    public int getLifetime() {
        return this.lifetime;
    }

    public void setLifetime(int seconds) {
        this.lifetime = seconds;
    }

    private int lifetime;
    // If unset, it will wait for first render to "start the timer"
    private ZonedDateTime timeSpawned = ZonedDateTime.now();

    public void render(WorldRenderContext ctx, Consumer<Ping> killCb) {
        if (AdventureSession.isStarted()) {
            RenderHelper.renderWaypoint(ctx, this.pos, new float[]{1F, 0F, 0F}, 0.5F, true);

            if (ZonedDateTime.now().isAfter(timeSpawned.plusSeconds(5))) {
                // Cleanup function
                killCb.accept(this);
            }
        }
    }
}
