package net.sweningteam.client;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.sweningteam.client.entity.renderer.NightFuryRendrer;
import net.sweningteam.common.registry.ModBlocks;
import net.sweningteam.common.registry.ModEntityTypes;

import java.util.function.BiConsumer;

public class NotEnoughDragonsClient {
    public static void registerBlockRenderTypes(BiConsumer<Block, ChunkSectionLayer> consumer){
        consumer.accept(ModBlocks.NIGHT_FURY_SCALES.get(),ChunkSectionLayer.CUTOUT);
    }
    public static void registerEntityRenderers(BiConsumer<EntityType< ?extends Entity>, EntityRendererProvider> consumer){
        consumer.accept(ModEntityTypes.NIGHT_FURY.get() , NightFuryRendrer::new);
    }
}
