package net.sweningteam.common.entity.dragons.base;

import com.google.gson.Gson;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.sweningteam.NotEnoughDragons;
import net.sweningteam.common.dragon.food.DragonFoodComponentType;
import net.sweningteam.common.dragon.trust.DragonTrust;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;

import java.util.HashMap;
import java.util.List;

public abstract class Dragon extends Animal implements GeoEntity {
    Gson gson = new Gson();

    public abstract DragonFoodComponentType dragonFoodComponentType();
    public abstract boolean tamingConditions(LivingEntity living);

    private static final EntityDataAccessor<Integer> EATING_PROGRESS;


    public void SetEatingProgress(int value){
        this.entityData.set(EATING_PROGRESS,value);
    }

    public boolean isEating(){
        return this.entityData.get(EATING_PROGRESS)> 0;
    }

    protected Dragon(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    public boolean isMoving() {return getDeltaMovement().z() != 0 || getDeltaMovement().x() != 0;}

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(EATING_PROGRESS,0);
        super.defineSynchedData(builder);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    public boolean entityIsDanger(LivingEntity entity){
        ItemStack MainStack = entity.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack OffStack = entity.getItemInHand(InteractionHand.OFF_HAND);
        return !entity.isShiftKeyDown() || !MainStack.is(ItemTags.SWORDS) || !MainStack.is(ItemTags.AXES) || !MainStack.is(Items.BOW) || !MainStack.is(Items.SHIELD) || !OffStack.is(ItemTags.SWORDS) || !OffStack.is(ItemTags.AXES) || !OffStack.is(Items.BOW) || !OffStack.is(Items.SHIELD);
    }

    @Override
    public void tick() {
        if(!this.level().isClientSide()) {
            if (this.entityData.get(EATING_PROGRESS) > 0) {
                this.entityData.set(EATING_PROGRESS,this.entityData.get(EATING_PROGRESS)- 1);
            }

        }
        super.tick();
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }


    static {
        EATING_PROGRESS = SynchedEntityData.defineId(Dragon.class, EntityDataSerializers.INT);
    }
}
