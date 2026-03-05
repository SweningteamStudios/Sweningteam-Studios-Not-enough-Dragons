package net.sweningteam.fabric.datagen.assets;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import net.sweningteam.common.registry.ModBlocks;
import net.sweningteam.common.registry.ModItems;

public class ModModelProvider extends FabricModelProvider {


    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.createLeafLitter(ModBlocks.NIGHT_FURY_SCALES.get());
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.createFlatItemModel(ModItems.NIGHT_FURY.get(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.createFlatItemModel(ModItems.GOLD_COIN.get(), ModelTemplates.FLAT_ITEM);
    }
}
