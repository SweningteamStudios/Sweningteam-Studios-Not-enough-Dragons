package net.sweningteam.fabric.datagen.assets.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.sweningteam.NotEnoughDragons;
import net.sweningteam.common.registry.ModBlocks;
import net.sweningteam.common.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class EnUsLanguageProvider extends FabricLanguageProvider {
    public EnUsLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us",registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {
    //Items
    translationBuilder.add(ModItems.NIGHT_FURY.get(),"Night Fury");
    translationBuilder.add(ModItems.GOLD_COIN.get(), "Gold Coin");
    //Blocks
    translationBuilder.add(ModBlocks.NIGHT_FURY_SCALES.get().asItem(), "Night Fury scales");
    //CreativeModeTaps
    translationBuilder.add("itemGroup." + NotEnoughDragons.MOD_ID + "."+"ned_dragons","NED Dragons");
    translationBuilder.add("itemGroup." + NotEnoughDragons.MOD_ID + "."+"ned_misc_itms","NED Misc Items");
    }
}
