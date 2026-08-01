package com.multiblocked2additions.forge.mixin;

import com.lowdragmc.mbd2.common.gui.editor.multiblock.BlockPlaceholder;
import com.lowdragmc.mbd2.common.gui.editor.multiblock.widget.PatternLayerList;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.WeakHashMap;

@Mixin(value = PatternLayerList.class, remap = false)
public abstract class PatternLayerListMixin {

    private static final WeakHashMap<PatternLayerList, String> signatures = new WeakHashMap<>();

    @Inject(method = "reloadLayers", at = @At("HEAD"), cancellable = true)
    private void onReloadLayers(CallbackInfo cir) {
        PatternLayerList list = (PatternLayerList) (Object) this;
        String signature = buildSignature(list);
        if (signature.equals(signatures.get(list))) {
            cir.cancel();
        } else {
            signatures.put(list, signature);
        }
    }

    private static String buildSignature(PatternLayerList list) {
        BlockPlaceholder[][][] placeholders = list.getPanel().getProject().getBlockPlaceholders();
        Direction.Axis axis = list.getPanel().getProject().getLayerAxis();
        int length = placeholders.length;
        int width = length > 0 ? placeholders[0].length : 0;
        int height = width > 0 ? placeholders[0][0].length : 0;
        StringBuilder signature = new StringBuilder();
        signature.append(axis.name()).append(length).append('x').append(width).append('x').append(height);
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < width; y++) {
                for (int z = 0; z < height; z++) {
                    BlockPlaceholder placeholder = placeholders[x][y][z];
                    if (placeholder != null) {
                        for (Object predicate : placeholder.getPredicates()) {
                            signature.append(System.identityHashCode(predicate));
                        }
                        if (placeholder.isController()) {
                            signature.append('C');
                        }
                    } else {
                        signature.append('-');
                    }
                }
            }
        }
        return signature.toString();
    }
}
