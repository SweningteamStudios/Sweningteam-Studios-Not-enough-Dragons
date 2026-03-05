package net.sweningteam.common.util;

import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class AnimationHelper implements GeoEntity {
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
    }
    @SuppressWarnings("SameReturnValue")
    public static <A extends GeoEntity>PlayState loopAnim(String Anim, AnimationTest<A> event){
        event.controller().setAnimation(RawAnimation.begin().thenLoop(Anim));
        return PlayState.CONTINUE;
    }
}
