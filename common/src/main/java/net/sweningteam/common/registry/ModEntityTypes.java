package net.sweningteam.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.sweningteam.NotEnoughDragons;

import net.sweningteam.common.entity.dragons.NightFury;
import net.sweningteam.platform.Services;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModEntityTypes {
    public static final Supplier<EntityType<NightFury>> NIGHT_FURY = registerEntity("night_fury", NightFury::new , MobCategory.CREATURE, 1.5F, 1.6F,0.7F);

    private static <T extends Entity> Supplier<EntityType<T>> registerEntity(String id, EntityType.EntityFactory<T> factory, MobCategory category, float with, float hight, float eyeHeight ){
        return Services.PLATFORM.getRegisterHelper().register(BuiltInRegistries.ENTITY_TYPE, id, ()-> EntityType.Builder.of(factory, category).sized(with, hight).eyeHeight(eyeHeight).build(ResourceKey.create(Registries.ENTITY_TYPE,ResourceLocation.fromNamespaceAndPath(NotEnoughDragons.MOD_ID,id))));
    }

    public static void registerEntityAtributes(BiConsumer<EntityType<?extends LivingEntity>, AttributeSupplier> consumer){
        consumer.accept(NIGHT_FURY.get(), NightFury.createAtributes().build());
    }


    public static void init(){
        NotEnoughDragons.LOGGER.info("Registering EntityTypes for {}", NotEnoughDragons.MOD_NAME);
    }
}
