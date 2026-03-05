package net.sweningteam.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeafLitterBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.sweningteam.NotEnoughDragons;
import net.sweningteam.platform.Services;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ModBlocks {

    public static final ArrayList<Supplier<? extends Block>> BLOCKS = new ArrayList<>();

    public static final Supplier<Block> NIGHT_FURY_SCALES = registerBlockWithItem("night_fury_scales", ()->new LeafLitterBlock(baseProperties("night_fury_scales").sound(SoundType.DEEPSLATE).pushReaction(PushReaction.DESTROY).noCollision()));

    public static BlockBehaviour.Properties baseProperties(String id){
        return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(NotEnoughDragons.MOD_ID, id)));
    }

    public static <B extends Block> Supplier<B> registerBlockWithItem(String id, Supplier<B> block){
        Supplier<B> blockSupplier = registerBlock(id,block);
        Supplier<Item> item = ModItems.registerItem(id, ()-> new BlockItem(blockSupplier.get(), ModItems.baseProperties(id)));
        return blockSupplier;
    }

    public static <B extends Block> Supplier<B> registerBlock(String id, Supplier<B> block){
        Supplier<B> blockSupplier = register(id,block);
        BLOCKS.add(blockSupplier);
        return blockSupplier;
    }

    public static <B extends Block> Supplier<B> register(String id, Supplier<B> block){
        return Services.PLATFORM.getRegisterHelper().register(BuiltInRegistries.BLOCK, id, block);
    }

    public static void init(){
        NotEnoughDragons.LOGGER.info("Registering Blocks for {}", NotEnoughDragons.MOD_NAME);
    }
}
