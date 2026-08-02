package com.multiblocked2additions.forge.client;

import com.multiblocked2additions.forge.TranslationKeys;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class MBD2TabletMode {

    public enum TabletMode {
        EDITOR(TranslationKeys.MODE_EDITOR),
        MULTIBLOCK_SELECTION(TranslationKeys.MODE_MULTIBLOCK_SELECTION);

        private final String translationKey;

        TabletMode(String translationKey) {
            this.translationKey = translationKey;
        }

        public String getTranslationKey() {
            return translationKey;
        }
    }

    private static TabletMode current = TabletMode.EDITOR;

    private MBD2TabletMode() {
    }

    public static TabletMode get() {
        return current;
    }

    public static void toggle() {
        current = current == TabletMode.EDITOR ? TabletMode.MULTIBLOCK_SELECTION : TabletMode.EDITOR;
    }
}
