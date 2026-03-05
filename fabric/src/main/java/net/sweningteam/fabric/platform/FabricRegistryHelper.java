package net.sweningteam.fabric.platform;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sweningteam.NotEnoughDragons;
import net.sweningteam.platform.RegisterHelper;

import java.util.ArrayList;
import java.util.function.Supplier;

public class FabricRegistryHelper implements RegisterHelper {
    @SafeVarargs
    @Override
    public final Supplier<CreativeModeTab> createCreativeTab(String name, Supplier<ItemStack> icon, ArrayList<Supplier<? extends Item>>... items) {
        CreativeModeTab tab = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(NotEnoughDragons.MOD_ID, name), FabricItemGroup.builder()
                .title(Component.translatable("itemGroup." + NotEnoughDragons.MOD_ID + "." + name))
                .icon(icon)
                .displayItems((entry, context) -> {
                    for (ArrayList<Supplier<? extends Item>> item : items)
                        item.forEach((item1) -> context.accept(item1.get()));
                })
                .build());
        return () -> tab;
    }

    @Override
    public <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> value) {
        T value1 = Registry.register(registry, ResourceLocation.fromNamespaceAndPath(NotEnoughDragons.MOD_ID,name), value.get());
        return () -> value1;
    }
}
