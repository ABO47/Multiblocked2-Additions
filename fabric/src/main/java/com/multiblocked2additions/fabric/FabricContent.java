package com.multiblocked2additions.fabric;

import com.multiblocked2additions.Multiblocked2Additions;
import com.multiblocked2additions.content.TemplateContent;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public final class FabricContent {
    private FabricContent() {
    }

    static void register() {
        Item item = new Item(new Item.Properties());
        TemplateContent.templateItem = item;
        Registry.register(BuiltInRegistries.ITEM,
                new ResourceLocation(Multiblocked2Additions.MOD_ID, TemplateContent.TEMPLATE_ITEM_ID), item);
    }
}
