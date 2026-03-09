package net.sweningteam.common.entity.dragons;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.sweningteam.NotEnoughDragons;
import net.sweningteam.common.dragon.food.DragonFoodComponentType;
import net.sweningteam.common.entity.dragons.base.Dragon;
import net.sweningteam.common.registry.ModDragonFoodComponentTypes;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NightFury extends Dragon implements NeutralMob, OwnableEntity{
    private static final EntityDataAccessor<Boolean> AGGRO;
    private static final EntityDataAccessor<Boolean> SNEAKING;
    private static final EntityDataAccessor<Boolean> TAMED;

    @Override
    public DragonFoodComponentType dragonFoodComponentType() {
        return ModDragonFoodComponentTypes.Night_fury;
    }

    @Override
    public boolean tamingConditions(LivingEntity living) {
        return entityIsDanger(living);
    }

    @Override
    public int maxSaturation() {
        return 200;
    }

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    boolean isTamed = false;
    private EntityReference<LivingEntity> owner;

    boolean isAggro = false;
    private int AngerTime = 0;
    private UUID persistentAngerTarget;

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(AGGRO, false);
        builder.define(SNEAKING, false);
        builder.define(TAMED, false);
        super.defineSynchedData(builder);
    }

    private <A extends GeoAnimatable> PlayState eyeController(AnimationTest<A> event){
        event.controller().transitionLength(3);
    if(entityData.get(AGGRO)) {
        event.setAndContinue(RawAnimation.begin().thenPlay("animation.nightfury.face.aggro"));
        return PlayState.CONTINUE;
    }else{
    if(isEating()){
        event.setAndContinue(RawAnimation.begin().thenPlay("animation.nightfury.face.eat"));
        return PlayState.CONTINUE;
    } else {
        if (entityData.get(TAMED)) {
            event.setAndContinue(RawAnimation.begin().thenPlay("animation.nightfury.face.tamed"));
            return PlayState.CONTINUE;
        } else{
            event.setAndContinue(RawAnimation.begin().thenPlay("animation.nightfury.face.idle"));
            return PlayState.CONTINUE;
        }
    }
}


    }

    private <A extends GeoAnimatable> PlayState mainController(AnimationTest<A> event){
        event.controller().transitionLength(5);

        if(event.isMoving()){
            if(isShiftKeyDown()) {
                event.setAnimation(RawAnimation.begin().thenPlay("animation.nightfury.sneaking_walk"));
                return PlayState.CONTINUE;
            }else {
                event.setAnimation(RawAnimation.begin().thenPlay("animation.nightfury.walk"));
                return PlayState.CONTINUE;
            }
        }else {
            if(isShiftKeyDown()) {
                event.setAndContinue(RawAnimation.begin().thenPlay("animation.nightfury.sneaking"));
                return PlayState.CONTINUE;
            }else {
                event.setAndContinue(RawAnimation.begin().thenPlay("animation.nightfury.idle"));
                return PlayState.CONTINUE;
            }
        }
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1,new MeleeAttackGoal(this,1D,false));

        this.goalSelector.addGoal(2,new WaterAvoidingRandomStrollGoal(this,1.0));

        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4,new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0,new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10,true, false, this::isAngryAt));

        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Salmon.class, true, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Cod.class, true, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Skeleton.class, true, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Zombie.class, true, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, TropicalFish.class, true, false));
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

    public NightFury(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    private boolean isTamed(){
        return isTamed;
    }

    private void setTamed(boolean tamed){
        this.isTamed = tamed;
    }

    private void Tame(Player player){
        this.setOwner(player);
        this.setTamed(true);

        if (player instanceof ServerPlayer) {
            CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayer)player, this);
        }


        this.level().broadcastEntityEvent(this, (byte)7);
    }

    private void setOwner(@Nullable LivingEntity owner){

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putBoolean("tamed", isTamed());
        addPersistentAngerSaveData(output);
        EntityReference.store(this.owner, output, "Owner");
        super.addAdditionalSaveData(output);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        setTamed(input.getBooleanOr("tamed",false));
        readPersistentAngerSaveData(this.level(),input);
        this.owner = EntityReference.readWithOldOwnerConversion(input, "Owner", this.level());
        super.readAdditionalSaveData(input);
    }

    @Override
    protected boolean handleFood(Player player, Item item) {
        if(super.handleFood(player, item)){
            SetEatingProgress(35);
        }
        return super.handleFood(player, item);
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
        if(!this.level().isClientSide()) {
            this.updatePersistentAnger((ServerLevel) this.level(),true);
        }
        updateSwingTime();
    }

    public void MainBox1(){

    }

    public void AngerManager(){
        boolean Should_be_aggressive = false;
        boolean Should_be_sneaking = false;
        boolean Should_be_aggro = false;
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class,this.getHitbox().inflate(15)).stream().toList();
        for ( int i = 1; i < list.size(); i++){
            LivingEntity entity = list.get(i);
            if(entity instanceof Player && !((Player) entity).isCreative()) {
                if (trust.getTrust(entity.getUUID()) < 100 || trust.getTrust(entity.getUUID()) == null) {
                    Should_be_sneaking = true;
                }
                if (entity.getUUID().equals(getPersistentAngerTarget()) || (trust.getTrust(entity.getUUID()) < 50)) {
                    if (entityIsDanger(entity)) {
                       setTarget(entity);
                        Should_be_aggressive = true;
                    }
                }
            }
        }
        List<LivingEntity> list1 = this.level().getEntitiesOfClass(LivingEntity.class,this.getHitbox().inflate(5)).stream().toList();
        for(int i = 1; i<list1.size(); i++){
            LivingEntity entity = list1.get(i);
            if(entity instanceof Player && !((Player) entity).isCreative()) {
                if (trust.getTrust(entity.getUUID()) <= 50) {
                    Should_be_aggro = true;
                }
                if(trust.getTrust(entity.getUUID())< 50) {
                    setTarget(entity);
                }else if(trust.getTrust(entity.getUUID()) < 100){
                if(entityIsDanger(entity)) {
                    setTarget(entity);
                }
                }
            }

        }
        if(Should_be_sneaking){
            this.setShiftKeyDown(true);
        }else {
            this.setShiftKeyDown(false);
        }
        if(Should_be_aggro){
            entityData.set(AGGRO, true);
        }else {
            entityData.set(AGGRO, false);
        }
        if(Should_be_aggressive){
            setRemainingPersistentAngerTime(200);
        }
    }

    @Override
    public void tick() {
        if(!this.level().isClientSide()) {
            AngerManager();
            entityData.set(TAMED,this.isTamed());
        }
        super.tick();
    }

    @Override
    public boolean isShiftKeyDown() {
        if(super.isShiftKeyDown()){
            this.setSpeed(0.125F);
            entityData.set(SNEAKING,true);
        }else {
            this.setSpeed(0.25F);
            entityData.set(SNEAKING,false);
        }
        return super.isShiftKeyDown();
    }


    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        Entity entity= damageSource.getEntity();
        if(entity != null) {
            if(trust.getTrust(entity.getUUID()) < 100) {
                trust.increaseTrust(entity.getUUID(), (double) amount);
                setTarget((LivingEntity) entity);
            }else {
                trust.increaseTrust(entity.getUUID(), amount * -0.5);
                if(this.getHealth() < this.getHealth()*0.25){
                    setTarget((LivingEntity) entity);
                }
            }
        }
        return super.hurtServer(level, damageSource, amount);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if(!this.level().isClientSide()){
            if(player.isShiftKeyDown() && (stack.isEmpty() || stack.is(Items.AIR)) && this.trust.getTrust(player.getUUID()) >= 100){
                if(!this.isTamed()){
                    this.Tame(player);
                    return InteractionResult.SUCCESS;
                }else {
                    if(this.owner.getEntity(this.level(),LivingEntity.class).getName() != null) {
                        player.displayClientMessage(Component.translatable("text.not_enough_dragons.dragon.dragon.not_tame", this.owner.getEntity(this.level(), LivingEntity.class).getName()), true);

                    }else {
                        player.displayClientMessage(Component.translatable("text.not_enough_dragons.dragon.dragon.not_tame_no_name"), true);
                    }
                    return InteractionResult.FAIL;
                }
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return AngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {
        AngerTime = remainingPersistentAngerTime;
    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(200);
    }

    @Override
    public @Nullable EntityReference<LivingEntity> getOwnerReference() {
        return this.owner;
    }

    static {
        AGGRO = SynchedEntityData.defineId(NightFury.class, EntityDataSerializers.BOOLEAN);
        SNEAKING = SynchedEntityData.defineId(NightFury.class, EntityDataSerializers.BOOLEAN);
        TAMED = SynchedEntityData.defineId(NightFury.class, EntityDataSerializers.BOOLEAN);
    }
}
