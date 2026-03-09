package net.sweningteam.fabric.datagen.assets.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.sweningteam.NotEnoughDragons;
import net.sweningteam.common.registry.ModBlocks;
import net.sweningteam.common.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class DeDeLanguageProvider extends FabricLanguageProvider {
    public DeDeLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "de_de", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {
        // Items
        translationBuilder.add(ModItems.NIGHT_FURY.get(), "Nachtschatten");
        translationBuilder.add(ModItems.GOLD_COIN.get(), "Goldmünze");
        // Blocks
        translationBuilder.add(ModBlocks.NIGHT_FURY_SCALES.get().asItem(), "Nachtschatten-Schuppen");
        // CreativeModeTabs
        translationBuilder.add("itemGroup." + NotEnoughDragons.MOD_ID + "." + "ned_dragons", "NED Drachen");
        translationBuilder.add("itemGroup." + NotEnoughDragons.MOD_ID + "." + "ned_misc_itms", "NED Verschiedenes");
        // Texte
        translationBuilder.add("text.not_enough_dragons.dragon.get_trust", "Dein Vertrauen beträgt: ");
        translationBuilder.add("text.not_enough_dragons.dragon.dragon.not_eat", "Dieser Drache möchte gerade nicht fressen");
        translationBuilder.add("text.not_enough_dragons.dragon.dragon.not_tame", "Du kannst diesen Drachen nicht zähmen, da er bereits von {} gezähmt wurde!");
        translationBuilder.add("text.not_enough_dragons.dragon.dragon.not_tame_no_name", "Du kannst diesen Drachen nicht zähmen, da er bereits gezähmt wurde!");
    }
}