package net.sweningteam.client.entity.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sweningteam.NotEnoughDragons;
import net.sweningteam.common.entity.dragons.base.Dragon;
import software.bernie.geckolib.animatable.processing.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class NightfuryModel <E extends Dragon> extends GeoModel<E> {
    @Override
    public ResourceLocation getModelResource(GeoRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(NotEnoughDragons.MOD_ID,"entity/dragon/night_fury.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(NotEnoughDragons.MOD_ID, "textures/entity/dragon/night_fury/night_fury.png");
    }

    @Override
    public ResourceLocation getAnimationResource(E animatable) {
        return ResourceLocation.fromNamespaceAndPath(NotEnoughDragons.MOD_ID, "entity/dragon/night_fury.animation.json");
    }
    @Override
    public void setCustomAnimations(AnimationState<E> animationState) {
        super.setCustomAnimations(animationState);
        GeoBone head = getAnimationProcessor().getBone("HeadRoot");
        GeoBone Neck = getAnimationProcessor().getBone("NeckRoot");
        GeoBone NeckBase = getAnimationProcessor().getBone("NeckBaseRoot");
        if(head != null && Neck != null && NeckBase != null){
            float HeadPitch = animationState.getData(DataTickets.ENTITY_PITCH);
            float HeadYaw = animationState.getData(DataTickets.ENTITY_YAW);
            NeckBase.setRotX(Mth.clamp(HeadPitch,-12.5F,12.5F));
            NeckBase.setRotY(Mth.clamp (HeadYaw, -15.0F,15.0F));
            Neck.setRotX(Mth.clamp (HeadPitch, -10.0F,10.0F));
            Neck.setRotY(Mth.clamp (HeadYaw,-7.5F,7.5F));
            head.setRotY(Mth.clamp (HeadYaw,-5.0F, 5.0F));
        }
    }
}
