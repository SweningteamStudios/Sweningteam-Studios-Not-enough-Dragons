package net.sweningteam.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.sweningteam.NotEnoughDragons;
import net.sweningteam.platform.Services;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ModItems {
    public static final ArrayList<Supplier<? extends Item>> ITEMS = new ArrayList<>();

    public static final Supplier<Item> NIGHT_FURY = registerItem("night_fury", ()->new SpawnEggItem(baseProperties("night_fury").spawnEgg(ModEntityTypes.NIGHT_FURY.get())));
    public static final Supplier<Item> GOLD_COIN = registerItem("gold_coin",()-> new Item(baseProperties("gold_coin")));

    public static <I extends Item> Supplier<I> registerItem(String id, Supplier<I> item){
        Supplier<I> itemSupplier = register(id, item);
        ITEMS.add(itemSupplier);
        return itemSupplier;
    }

    public static Item.Properties baseProperties(String id){
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(NotEnoughDragons.MOD_ID, id)));
    }

    public static <I extends Item> Supplier<I> register(String id, Supplier<I> item) {
        return Services.PLATFORM.getRegisterHelper().register(BuiltInRegistries.ITEM,id,item);
    }

    public static void init(){
        NotEnoughDragons.LOGGER.info("Registering Items for {}", NotEnoughDragons.MOD_NAME);
    }
}
