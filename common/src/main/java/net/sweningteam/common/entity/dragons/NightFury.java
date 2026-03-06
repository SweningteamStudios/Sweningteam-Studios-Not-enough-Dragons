package net.sweningteam.common.entity.dragons;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sweningteam.NotEnoughDragons;
import net.sweningteam.common.dragon.food.DragonFoodComponentType;
import net.sweningteam.common.entity.dragons.base.Dragon;
import net.sweningteam.common.registry.ModDragonFoodComponentTypes;
import net.sweningteam.common.util.AnimationHelper;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationState;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

public class NightFury extends Dragon {
    private <A extends GeoAnimatable> PlayState eyeController(AnimationTest<A> event){
        event.controller().transitionLength(0);
        event.setAndContinue(RawAnimation.begin().thenPlay("animation.nightfury.face.idel"));
        return PlayState.CONTINUE;
    }



    private <A extends GeoAnimatable> PlayState mainController(AnimationTest<A> event){
        event.controller().transitionLength(5);
        if(event.isMoving()){
            event.setAnimation(RawAnimation.begin().thenPlay("animation.nightfury.walk"));

            return PlayState.CONTINUE;
        }else {
            event.setAndContinue(RawAnimation.begin().thenPlay("animation.nightfury.idle"));

            return PlayState.CONTINUE;
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1,new MeleeAttackGoal(this,0.4D,false));

        this.goalSelector.addGoal(2,new WaterAvoidingRandomStrollGoal(this,1.0));

        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4,new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAtributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH,80)
                .add(Attributes.MOVEMENT_SPEED,0.25)
                .add(Attributes.FLYING_SPEED,1)
                .add(Attributes.STEP_HEIGHT,1)
                .add(Attributes.ARMOR,1)
                .add(Attributes.ATTACK_DAMAGE,4)
                .add(Attributes.ATTACK_KNOCKBACK,1)
                .add(Attributes.FOLLOW_RANGE,20);
    }

    @Override
    public DragonFoodComponentType dragonFoodComponentType() {
        return ModDragonFoodComponentTypes.Night_fury;
    }
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public NightFury(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                new AnimationController<>("main",0,this::mainController),
                new AnimationController<>("eye",0,this::eyeController)
                );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    @Override
    public void aiStep() {
        super.aiStep();
        updateSwingTime();
    }
}
