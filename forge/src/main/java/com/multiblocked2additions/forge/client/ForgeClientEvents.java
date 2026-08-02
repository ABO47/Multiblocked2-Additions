package com.multiblocked2additions.forge.client;

import com.multiblocked2additions.Multiblocked2Additions;
import com.multiblocked2additions.forge.ForgeContent;
import com.multiblocked2additions.forge.TranslationKeys;

import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

public final class ForgeClientEvents {

    private ForgeClientEvents() {
    }

    @Mod.EventBusSubscriber(modid = Multiblocked2Additions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static final class ModBus {
        private ModBus() {
        }

        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(MBD2KeyMappings.OPEN_EDITOR);
            event.register(MBD2KeyMappings.SWITCH_TABLET_MODE);
        }
    }

    @Mod.EventBusSubscriber(modid = Multiblocked2Additions.MOD_ID, value = Dist.CLIENT)
    public static final class ForgeBus {
        private ForgeBus() {
        }

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                if (MBD2KeyMappings.OPEN_EDITOR.consumeClick()) {
                    MBD2EditorOpener.openEditor();
                }
                if (MBD2KeyMappings.SWITCH_TABLET_MODE.consumeClick()) {
                    MBD2TabletMode.toggle();
                    if (MBD2TabletMode.get() == MBD2TabletMode.TabletMode.MULTIBLOCK_SELECTION) {
                        notifyPlayer(TranslationKeys.MESSAGE_SELECTION_HELP);
                    } else {
                        LocalPlayer player = Minecraft.getInstance().player;
                        if (player != null) {
                            player.displayClientMessage(Component.translatable(
                                    TranslationKeys.MESSAGE_TABLET_MODE,
                                    Component.translatable(MBD2TabletMode.get().getTranslationKey())), true);
                        }
                    }
                }
                MBD2EditorWindowView.applyPendingResize();
                MBD2ProjectAutosaver.tick();
            }
        }

        @SubscribeEvent
        public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
            MBD2EditorOpener.clearLastProject();
            MBD2MultiblockSelector.clear();
        }

        @SubscribeEvent
        public static void onScreenInit(ScreenEvent.Init.Post event) {
            MBD2EditorOpener.restoreLastSession(Editor.INSTANCE);
            MBD2EditorWindowView.onEditorOpened();
        }

        @SubscribeEvent
        public static void onMouseButton(InputEvent.MouseButton.Pre event) {
            if (Minecraft.getInstance().screen != null) {
                return;
            }
            if (MBD2TabletMode.get() != MBD2TabletMode.TabletMode.MULTIBLOCK_SELECTION) {
                return;
            }
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null || !isHoldingTablet(player)) {
                return;
            }
            if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                if (MBD2MultiblockSelector.hasSelection()) {
                    MBD2MultiblockSelector.clear();
                    notifyPlayer(TranslationKeys.MESSAGE_SELECTION_CLEARED);
                    event.setCanceled(true);
                }
            } else if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                HitResult hit = Minecraft.getInstance().hitResult;
                if (player.isShiftKeyDown()) {
                    if (hit == null || hit.getType() != HitResult.Type.BLOCK) {
                        return;
                    }
                    BlockPos pos = ((BlockHitResult) hit).getBlockPos();
                    if (MBD2MultiblockSelector.pickController(pos)) {
                        notifyPlayer(TranslationKeys.MESSAGE_CONTROLLER_SET);
                    } else {
                        notifyPlayer(TranslationKeys.MESSAGE_CONTROLLER_OUTSIDE);
                    }
                } else {
                    BlockPos pos = MBD2MultiblockSelector.getHoveredPosition();
                    if (pos == null) {
                        return;
                    }
                    MBD2MultiblockSelector.CornerResult result = MBD2MultiblockSelector.pickCorner(pos);
                    if (result == MBD2MultiblockSelector.CornerResult.FIRST) {
                        notifyPlayer(TranslationKeys.MESSAGE_CORNER_FIRST);
                    } else if (result == MBD2MultiblockSelector.CornerResult.SECOND) {
                        notifyPlayer(TranslationKeys.MESSAGE_CORNER_SECOND);
                    }
                }
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onRenderLevelStage(RenderLevelStageEvent event) {
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
                MBD2SelectionRenderer.render(event.getCamera(), event.getPoseStack());
            }
        }

        private static boolean isHoldingTablet(Player player) {
            return player.getMainHandItem().getItem() == ForgeContent.EDITOR_TABLET.get();
        }

        private static void notifyPlayer(String translationKey) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                player.displayClientMessage(Component.translatable(translationKey), true);
            }
        }
    }
}
