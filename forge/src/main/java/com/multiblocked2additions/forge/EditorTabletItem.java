package com.multiblocked2additions.forge;

import com.multiblocked2additions.forge.client.MBD2EditorOpener;
import com.multiblocked2additions.forge.client.MBD2TabletMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EditorTabletItem extends Item {

    public EditorTabletItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide() && MBD2TabletMode.get() == MBD2TabletMode.TabletMode.EDITOR) {
            MBD2EditorOpener.openEditor();
        }
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }
}
