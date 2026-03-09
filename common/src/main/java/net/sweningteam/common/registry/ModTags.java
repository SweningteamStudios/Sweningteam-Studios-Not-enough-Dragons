package net.sweningteam.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.sweningteam.NotEnoughDragons;

public class ModTags {
    public static final TagKey<Item> NIGHT_FURY_PROVOCATING =TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(NotEnoughDragons.MOD_ID,"night_fury_provocating"));
}
