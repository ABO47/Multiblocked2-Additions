package com.multiblocked2additions.forge.client;

import com.lowdragmc.lowdraglib.client.utils.RenderBufferUtils;
import com.lowdragmc.lowdraglib.utils.ColorUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.multiblocked2additions.forge.ForgeContent;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public final class MBD2SelectionRenderer {

    private static final int FROM_COLOR = 0x8f00ff00;
    private static final int TO_COLOR = 0x8fff0000;
    private static final int FACE_COLOR = 0x2f0000ff;
    private static final int FRAME_COLOR = 0xffffffff;
    private static final int CONTROLLER_COLOR = 0xff00aaaa;
    private static final int GHOST_COLOR = 0x99ffff00;

    private MBD2SelectionRenderer() {
    }

    public static void render(Camera camera, PoseStack poseStack) {
        BlockPos first = MBD2MultiblockSelector.getFirstCorner();

        poseStack.pushPose();
        Vec3 cameraPosition = camera.getPosition();
        poseStack.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();

        BufferBuilder buffer = Tesselator.getInstance().getBuilder();

        if (first != null) {
            BlockPos second = MBD2MultiblockSelector.getSecondCorner();
            BlockPos controller = MBD2MultiblockSelector.getController();

            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            buffer.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
            RenderBufferUtils.drawCubeFace(poseStack, buffer,
                    first.getX(), first.getY(), first.getZ(),
                    first.getX() + 1, first.getY() + 1, first.getZ() + 1,
                    ColorUtils.red(FROM_COLOR), ColorUtils.green(FROM_COLOR), ColorUtils.blue(FROM_COLOR), 0.2F, false);
            if (second != null) {
                RenderBufferUtils.drawCubeFace(poseStack, buffer,
                        second.getX(), second.getY(), second.getZ(),
                        second.getX() + 1, second.getY() + 1, second.getZ() + 1,
                        ColorUtils.red(TO_COLOR), ColorUtils.green(TO_COLOR), ColorUtils.blue(TO_COLOR), 0.2F, false);
            }
            if (controller != null) {
                drawControllerFace(poseStack, buffer, controller, MBD2MultiblockSelector.getControllerFace());
            }
            Tesselator.getInstance().end();

            RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
            buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
            RenderSystem.lineWidth(MBD2Config.getSelectionLineWidth());

            int minX = second == null ? first.getX() : Math.min(first.getX(), second.getX());
            int minY = second == null ? first.getY() : Math.min(first.getY(), second.getY());
            int minZ = second == null ? first.getZ() : Math.min(first.getZ(), second.getZ());
            int maxX = second == null ? first.getX() + 1 : Math.max(first.getX(), second.getX()) + 1;
            int maxY = second == null ? first.getY() + 1 : Math.max(first.getY(), second.getY()) + 1;
            int maxZ = second == null ? first.getZ() + 1 : Math.max(first.getZ(), second.getZ()) + 1;
            RenderBufferUtils.drawCubeFrame(poseStack, buffer,
                    minX, minY, minZ, maxX, maxY, maxZ,
                    ColorUtils.red(FRAME_COLOR), ColorUtils.green(FRAME_COLOR), ColorUtils.blue(FRAME_COLOR), ColorUtils.alpha(FRAME_COLOR));
            if (controller != null) {
                RenderBufferUtils.drawCubeFrame(poseStack, buffer,
                        controller.getX(), controller.getY(), controller.getZ(),
                        controller.getX() + 1, controller.getY() + 1, controller.getZ() + 1,
                        ColorUtils.red(CONTROLLER_COLOR), ColorUtils.green(CONTROLLER_COLOR), ColorUtils.blue(CONTROLLER_COLOR), ColorUtils.alpha(CONTROLLER_COLOR));
            }
            Tesselator.getInstance().end();
            RenderSystem.lineWidth(1);
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && player.getMainHandItem().getItem() == ForgeContent.EDITOR_TABLET.get()
                && MBD2TabletMode.get() == MBD2TabletMode.TabletMode.MULTIBLOCK_SELECTION) {
            BlockPos hover = MBD2MultiblockSelector.getHoveredPosition();
            if (hover != null) {
                RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
                buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
                RenderSystem.lineWidth(3);
                RenderBufferUtils.drawCubeFrame(poseStack, buffer,
                        hover.getX(), hover.getY(), hover.getZ(),
                        hover.getX() + 1, hover.getY() + 1, hover.getZ() + 1,
                        ColorUtils.red(GHOST_COLOR), ColorUtils.green(GHOST_COLOR), ColorUtils.blue(GHOST_COLOR), ColorUtils.alpha(GHOST_COLOR));
                Tesselator.getInstance().end();
                RenderSystem.lineWidth(1);
            }
        }

        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        poseStack.popPose();
    }

    private static void drawControllerFace(PoseStack poseStack, BufferBuilder buffer, BlockPos pos, Direction face) {
        float r = ColorUtils.red(FACE_COLOR);
        float g = ColorUtils.green(FACE_COLOR);
        float b = ColorUtils.blue(FACE_COLOR);
        float a = ColorUtils.alpha(FACE_COLOR);
        switch (face) {
            case UP -> RenderBufferUtils.drawCubeFace(poseStack, buffer,
                    pos.getX(), pos.getY() + 1, pos.getZ(),
                    pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, r, g, b, a, false);
            case DOWN -> RenderBufferUtils.drawCubeFace(poseStack, buffer,
                    pos.getX(), pos.getY(), pos.getZ(),
                    pos.getX() + 1, pos.getY(), pos.getZ() + 1, r, g, b, a, false);
            case NORTH -> RenderBufferUtils.drawCubeFace(poseStack, buffer,
                    pos.getX(), pos.getY(), pos.getZ(),
                    pos.getX() + 1, pos.getY() + 1, pos.getZ(), r, g, b, a, false);
            case SOUTH -> RenderBufferUtils.drawCubeFace(poseStack, buffer,
                    pos.getX(), pos.getY(), pos.getZ() + 1,
                    pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, r, g, b, a, false);
            case WEST -> RenderBufferUtils.drawCubeFace(poseStack, buffer,
                    pos.getX(), pos.getY(), pos.getZ(),
                    pos.getX(), pos.getY() + 1, pos.getZ() + 1, r, g, b, a, false);
            case EAST -> RenderBufferUtils.drawCubeFace(poseStack, buffer,
                    pos.getX() + 1, pos.getY(), pos.getZ(),
                    pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, r, g, b, a, false);
        }
    }
}
