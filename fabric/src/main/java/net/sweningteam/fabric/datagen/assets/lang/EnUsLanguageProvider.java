package net.sweningteam.fabric.datagen.assets.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
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
    //Texts
    translationBuilder.add("text.not_enough_dragons.dragon.get_trust", "Your trust is: ");
    translationBuilder.add("text.not_enough_dragons.dragon.dragon.not_eat", "This Dragon doesn't want to eat right now");
    translationBuilder.add("text.not_enough_dragons.dragon.dragon.not_tame","You can't tamed this Dragon because it is already tamed by {}!");
    translationBuilder.add("text.not_enough_dragons.dragon.dragon.not_tame_no_name", "You can't tame this Dragon because it is already tamed!");
    }
}
