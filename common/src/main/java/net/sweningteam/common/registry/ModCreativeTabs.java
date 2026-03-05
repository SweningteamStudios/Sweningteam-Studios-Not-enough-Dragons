package net.sweningteam.common.registry;


import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sweningteam.NotEnoughDragons;
import net.sweningteam.platform.Services;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ModCreativeTabs {
    private static ArrayList<Supplier<? extends Item>> NotEnoughDragonsDragonsContent(){
        ArrayList<Supplier<? extends Item>> NotEnoughDragonsDragonsContent = new ArrayList<>();
        NotEnoughDragonsDragonsContent.add(ModItems.NIGHT_FURY);
        NotEnoughDragonsDragonsContent.add(()-> ModBlocks.NIGHT_FURY_SCALES.get().asItem());
        return NotEnoughDragonsDragonsContent;
    }
    private static ArrayList<Supplier<? extends Item>> NotEnoughDragonsMiscItemsContent(){
        ArrayList<Supplier<? extends Item>> NotEnoughDragonsMiscItemsContent = new ArrayList<>();
        NotEnoughDragonsMiscItemsContent.add(ModItems.GOLD_COIN);
        return NotEnoughDragonsMiscItemsContent;
    }

    public static final ResourceKey<CreativeModeTab> NED_DRAGONS_TAB = createCreativeTab("ned_dragons",()-> ModItems.NIGHT_FURY.get().getDefaultInstance(),NotEnoughDragonsDragonsContent());
    public static final ResourceKey<CreativeModeTab> NED_MISC_ITEMS_TAB = createCreativeTab("ned_misc_itms",()-> ModItems.GOLD_COIN.get().getDefaultInstance(),NotEnoughDragonsMiscItemsContent());

    private static ResourceKey<CreativeModeTab> createCreativeTab(String name, Supplier<ItemStack> icon, ArrayList<Supplier<? extends Item>>... items) {
        Services.PLATFORM.getRegisterHelper().createCreativeTab(name,icon,items);
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(NotEnoughDragons.MOD_ID,name));
    }

    public static void init(){
        NotEnoughDragons.LOGGER.info("Registering Creative Mode Taps for {}", NotEnoughDragons.MOD_ID);
    }
}
