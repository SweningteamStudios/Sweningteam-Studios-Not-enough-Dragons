package net.sweningteam.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.sweningteam.NotEnoughDragons;
import net.sweningteam.fabric.datagen.assets.ModBlockModelDefinationsProvider;
import net.sweningteam.fabric.datagen.assets.ModItemDefinitionProvider;
import net.sweningteam.fabric.datagen.assets.ModModelProvider;
import net.sweningteam.fabric.datagen.assets.lang.DeDeLanguageProvider;
import net.sweningteam.fabric.datagen.assets.lang.EnUsLanguageProvider;
import net.sweningteam.fabric.datagen.data.Loot.ModBlockLootTableProvider;
import net.sweningteam.fabric.datagen.data.tags.ModItemTagProvider;

public class NotEnoughDragonsDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        NotEnoughDragons.LOGGER.info("Starting datagen");
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(EnUsLanguageProvider::new);
        pack.addProvider(DeDeLanguageProvider::new);
        pack.addProvider(ModModelProvider::new);
        pack.addProvider((FabricDataGenerator.Pack.Factory<ModItemDefinitionProvider>) ModItemDefinitionProvider::new);
        pack.addProvider((FabricDataGenerator.Pack.Factory<ModBlockModelDefinationsProvider>) ModBlockModelDefinationsProvider::new);

        pack.addProvider(ModItemTagProvider::new);

        pack.addProvider(ModBlockLootTableProvider::new);

    }
}
