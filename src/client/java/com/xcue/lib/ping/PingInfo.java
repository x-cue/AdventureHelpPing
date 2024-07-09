package com.xcue.lib.ping;

import com.xcue.lib.Adventure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public record PingInfo(Adventure adv, BlockPos pos, Direction dir, double hp) {
}
