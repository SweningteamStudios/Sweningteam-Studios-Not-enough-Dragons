package net.sweningteam.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.sweningteam.client.NotEnoughDragonsClient;
import net.sweningteam.common.registry.ModBlocks;

public final class NotEnoughDragonsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        NotEnoughDragonsClient.registerBlockRenderTypes(BlockRenderLayerMap::putBlock);
        NotEnoughDragonsClient.registerEntityRenderers(EntityRenderers::register);
    }
}
