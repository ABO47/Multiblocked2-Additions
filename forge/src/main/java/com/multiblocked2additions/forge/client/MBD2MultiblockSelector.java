package com.multiblocked2additions.forge.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class MBD2MultiblockSelector {

    private static BlockPos firstCorner;
    private static BlockPos secondCorner;
    private static BlockPos controller;
    private static Direction controllerFace = Direction.NORTH;
    private static BlockPos lastPickPos;
    private static long lastPickTime;

    public enum CornerResult {
        FIRST, SECOND, IGNORED
    }

    private MBD2MultiblockSelector() {
    }

    public static CornerResult pickCorner(BlockPos pos) {
        long now = System.currentTimeMillis();
        if (pos.equals(lastPickPos) && now - lastPickTime < 1000) {
            return CornerResult.IGNORED;
        }
        lastPickPos = pos;
        lastPickTime = now;
        if (firstCorner == null) {
            firstCorner = pos;
            return CornerResult.FIRST;
        }
        if (secondCorner == null) {
            secondCorner = pos;
            return CornerResult.SECOND;
        }
        firstCorner = pos;
        secondCorner = null;
        return CornerResult.FIRST;
    }

    public static boolean pickController(BlockPos pos) {
        if (!contains(pos)) {
            return false;
        }
        controller = pos;
        controllerFace = readFace(pos);
        return true;
    }

    public static void clear() {
        firstCorner = null;
        secondCorner = null;
        controller = null;
        controllerFace = Direction.NORTH;
    }

    public static boolean hasSelection() {
        return firstCorner != null && secondCorner != null;
    }

    public static BlockPos getFirstCorner() {
        return firstCorner;
    }

    public static BlockPos getSecondCorner() {
        return secondCorner;
    }

    public static BlockPos getController() {
        return controller;
    }

    public static Direction getControllerFace() {
        return controllerFace;
    }

    public static AABB getSelectionBox() {
        if (!hasSelection()) {
            return null;
        }
        return new AABB(
                Math.min(firstCorner.getX(), secondCorner.getX()),
                Math.min(firstCorner.getY(), secondCorner.getY()),
                Math.min(firstCorner.getZ(), secondCorner.getZ()),
                Math.max(firstCorner.getX(), secondCorner.getX()) + 1,
                Math.max(firstCorner.getY(), secondCorner.getY()) + 1,
                Math.max(firstCorner.getZ(), secondCorner.getZ()) + 1);
    }

    private static Direction readFace(BlockPos pos) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return Direction.NORTH;
        }
        BlockState state = level.getBlockState(pos);
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            return state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        }
        if (state.hasProperty(BlockStateProperties.FACING)) {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            if (facing.getAxis().isHorizontal()) {
                return facing;
            }
        }
        return Direction.NORTH;
    }

    private static boolean contains(BlockPos pos) {
        AABB box = getSelectionBox();
        if (box == null) {
            return false;
        }
        return pos.getX() >= box.minX && pos.getX() < box.maxX
                && pos.getY() >= box.minY && pos.getY() < box.maxY
                && pos.getZ() >= box.minZ && pos.getZ() < box.maxZ;
    }
}
