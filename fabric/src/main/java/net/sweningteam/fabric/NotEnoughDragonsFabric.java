package net.sweningteam.fabric;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.sweningteam.NotEnoughDragons;
import net.sweningteam.common.registry.ModEntityTypes;

public final class NotEnoughDragonsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        NotEnoughDragons.init();
        ModEntityTypes.registerEntityAtributes(FabricDefaultAttributeRegistry::register);
    }
}
