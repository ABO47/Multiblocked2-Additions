package com.multiblocked2additions.forge;

import com.multiblocked2additions.Multiblocked2Additions;
import com.multiblocked2additions.content.TemplateContent;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ForgeContent {
    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Multiblocked2Additions.MOD_ID);

    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Multiblocked2Additions.MOD_ID);

    private static final RegistryObject<Item> TEMPLATE_ITEM = ITEMS.register(
            TemplateContent.TEMPLATE_ITEM_ID,
            () -> {
                Item item = new Item(new Item.Properties());
                TemplateContent.templateItem = item;
                return item;
            });

    public static final RegistryObject<Item> EDITOR_TABLET = ITEMS.register(
            "editor_tablet",
            () -> new EditorTabletItem(new Item.Properties().stacksTo(1)));

    private static final RegistryObject<CreativeModeTab> MAIN_TAB = TABS.register(
            "main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable(TranslationKeys.ITEM_GROUP))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(() -> EDITOR_TABLET.get().getDefaultInstance())
                    .displayItems((parameters, output) -> output.accept(EDITOR_TABLET.get()))
                    .build());

    private ForgeContent() {
    }

    static void register(IEventBus modBus) {
        ITEMS.register(modBus);
        TABS.register(modBus);
    }
}
