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
    public abstract int maxSaturation();

    private static final EntityDataAccessor<Integer> EATING_PROGRESS;

    protected int hunger = maxSaturation();
    protected int hungerDecrease  = 20;

    public void SetEatingProgress(int value){
        this.entityData.set(EATING_PROGRESS,value);
    }
    public int getEatingProgress(){
        return this.entityData.get(EATING_PROGRESS);
    }

    protected DragonTrust trust = new DragonTrust();
    private int lastPassiveTrustIncrease = 0;

    static final TargetingConditions FIND_ENTITY_PREDICATE;
    private static final TargetingConditions.Selector TRUST_FILTER;

    public boolean isEating(){
        return this.entityData.get(EATING_PROGRESS)> 0;
    }


    protected Dragon(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(EATING_PROGRESS,0);
        super.defineSynchedData(builder);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putString("Trust",this.trust.toString());
        output.putInt("Hunger decrease", this.hungerDecrease);
        output.putInt("Hunger", this.hunger);
        super.addAdditionalSaveData(output);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        trust.clear();
        trust.getTrustFromString(input.getStringOr("Trust",gson.toJson(new HashMap<String,Integer>(),trust.getTrustMap().getClass())));
        hungerDecrease = input.getIntOr("Hunger decrease",80);
        hunger = input.getIntOr("Hunger", maxSaturation());
        super.readAdditionalSaveData(input);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        if(!this.level().isClientSide()) {
            if (dragonFoodComponentType().contains(item)) {
                if (this.getHealth() < this.getMaxHealth()){
                    if( handleFood(player, item)) {
                    stack.consume(1, player);
                    return InteractionResult.SUCCESS;
                }else{
                    return InteractionResult.SUCCESS;
                }}else {
                    player.displayClientMessage(Component.translatable("text.not_enough_dragons.dragon.dragon.not_eat"), true);
                    return InteractionResult.PASS;

            }} else if (stack.is(Items.STICK)) {
                NotEnoughDragons.LOGGER.info(trust.toString());
                player.displayClientMessage((Component.translatable("text.not_enough_dragons.dragon.get_trust").append(Component.literal(String.valueOf(trust.getTrust(player.getUUID()))))), true);
                return InteractionResult.SUCCESS;
            } else {
                return super.mobInteract(player, hand);
            }
        }else {
            return super.mobInteract(player,hand);
        }

    }

    public boolean entityIsDanger(LivingEntity entity){
        ItemStack MainStack = entity.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack OffStack = entity.getItemInHand(InteractionHand.OFF_HAND);
        return !entity.isShiftKeyDown() || !MainStack.is(ItemTags.SWORDS) || !MainStack.is(ItemTags.AXES) || !MainStack.is(Items.BOW) || !MainStack.is(Items.SHIELD) || !OffStack.is(ItemTags.SWORDS) || !OffStack.is(ItemTags.AXES) || !OffStack.is(Items.BOW) || !OffStack.is(Items.SHIELD);
    }

    protected boolean handleFood(Player player, Item item){
        boolean bl = false;
        if(dragonFoodComponentType().contains(item)){
            trust.increaseTrust(player.getUUID(),dragonFoodComponentType().get(item).getTrust_gained());
            dragonFoodComponentType().get(item).onEat(this);
            hunger = hunger + dragonFoodComponentType().get(item).getFood_gained();
            this.heal(2);
            bl = true;
        }
        return bl;
    }


    @Override
    public void tick() {
        if(!this.level().isClientSide()) {
            if (lastPassiveTrustIncrease > 0){
                lastPassiveTrustIncrease = lastPassiveTrustIncrease -1;
            }

            if (hungerDecrease > 0){
                hungerDecrease = hungerDecrease -1;
            }else if (hungerDecrease == 0){
                hungerDecrease = 80;
                if(!this.isSprinting() || !this.isFlapping() || this.getHealth() < this.getMaxHealth()){

                }
            }

            if (lastPassiveTrustIncrease == 0){
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class,this.getHitbox().inflate(4), (livingEntity)->{
                    return FIND_ENTITY_PREDICATE.test((ServerLevel) this.level(), this, livingEntity);
                });
                if(!list.isEmpty()){
                    for(int i = 0; i < list.size();i++){
                        if(trust.hasTrust(list.get(i).getUUID())){
                            LivingEntity entity = list.get(i);
                            if (trust.getTrust(entity.getUUID()) >= 20 && trust.getTrust(entity.getUUID()) <= 10){
                                if(tamingConditions(entity)){
                                    if(this.isLookingAtMe(entity,6,true,true,this.getEyeY())){
                                    trust.increaseTrust(entity.getUUID(),4D);
                                }else {
                                        trust.increaseTrust(entity.getUUID(),2D);
                                }
                            }
                        }
                    }
                }
            }}
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

    public boolean isOnServer(){
        if(this.level() instanceof ServerLevel){
            return true;
        }else {
            return false;
        }
    }

    static {
        TRUST_FILTER = (entity, level) -> {
            if (entity instanceof Player){
                if (((Player) entity).isCreative()){
                    return false;
                }else {
                    return true;
                }
            } else {
                return true;
            }
        };
        EATING_PROGRESS = SynchedEntityData.defineId(Dragon.class, EntityDataSerializers.INT);
        FIND_ENTITY_PREDICATE = TargetingConditions.forNonCombat().selector(TRUST_FILTER);
    }
}
