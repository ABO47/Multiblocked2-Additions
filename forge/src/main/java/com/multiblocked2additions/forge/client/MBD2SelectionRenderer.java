package com.multiblocked2additions.forge.client;

import com.lowdragmc.lowdraglib.utils.ColorUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class MBD2SelectionRenderer {

    private static final int FROM_COLOR = 0x8f00ff00;
    private static final int TO_COLOR = 0x8fff0000;
    private static final int FRAME_COLOR = 0xffffffff;
    private static final int CONTROLLER_COLOR = 0xff00aaaa;
    private static final int FACE_COLOR = 0x2f0000ff;

    private MBD2SelectionRenderer() {
    }

    public static void render(Camera camera) {
        BlockPos first = MBD2MultiblockSelector.getFirstCorner();
        if (first == null) {
            return;
        }
        PoseStack view = new PoseStack();
        view.mulPose(camera.rotation());
        Vec3 projectedView = camera.getPosition();
        view.translate(-projectedView.x, -projectedView.y, -projectedView.z);
        RenderSystem.getModelViewStack().pushPose();
        RenderSystem.getModelViewStack().last().pose().set(view.last().pose());
        RenderSystem.applyModelViewMatrix();
        RenderSystem.disableDepthTest();
        PoseStack identity = new PoseStack();
        VertexConsumer buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
        drawFrame(buffer, identity, new AABB(first), FROM_COLOR);
        BlockPos second = MBD2MultiblockSelector.getSecondCorner();
        if (second != null) {
            drawFrame(buffer, identity, new AABB(second), TO_COLOR);
            drawFrame(buffer, identity, MBD2MultiblockSelector.getSelectionBox(), FRAME_COLOR);
        }
        BlockPos controller = MBD2MultiblockSelector.getController();
        if (controller != null) {
            drawFrame(buffer, identity, new AABB(controller), CONTROLLER_COLOR);
            drawFrame(buffer, identity, faceBox(controller, MBD2MultiblockSelector.getControllerFace()), FACE_COLOR);
        }
        Minecraft.getInstance().renderBuffers().bufferSource().endBatch(RenderType.lines());
        RenderSystem.enableDepthTest();
        RenderSystem.getModelViewStack().popPose();
        RenderSystem.applyModelViewMatrix();
    }

    private static void drawFrame(VertexConsumer buffer, PoseStack poseStack, AABB box, int color) {
        LevelRenderer.renderLineBox(poseStack, buffer, box,
                ColorUtils.red(color) / 255f, ColorUtils.green(color) / 255f, ColorUtils.blue(color) / 255f, ColorUtils.alpha(color) / 255f);
    }

    private static AABB faceBox(BlockPos pos, Direction face) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return switch (face) {
            case UP -> new AABB(x, y + 1, z, x + 1, y + 1, z + 1);
            case DOWN -> new AABB(x, y, z, x + 1, y, z + 1);
            case NORTH -> new AABB(x, y, z, x + 1, y + 1, z);
            case SOUTH -> new AABB(x, y, z + 1, x + 1, y + 1, z + 1);
            case WEST -> new AABB(x, y, z, x, y + 1, z + 1);
            case EAST -> new AABB(x + 1, y, z, x + 1, y + 1, z + 1);
        };
    }
}
