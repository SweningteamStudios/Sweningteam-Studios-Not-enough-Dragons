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
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
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
import net.sweningteam.common.entity.dragons.base.interfaces.EatingDragon;
import net.sweningteam.common.entity.dragons.base.interfaces.TrustingDragon;
import net.sweningteam.common.registry.ModDragonFoodComponentTypes;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class NightFury extends Dragon implements NeutralMob, OwnableEntity, TrustingDragon, EatingDragon {
    Random random = new Random();

    private static final EntityDataAccessor<Boolean> AGGRO;
    private static final EntityDataAccessor<Boolean> SNEAKING;
    private static final EntityDataAccessor<Boolean> TAMED;
    private static final EntityDataAccessor<Boolean> BITING;
    private static final EntityDataAccessor<Boolean> BLINKING;
    private static final TargetingConditions.Selector TRUST_FILTER;

    static final TargetingConditions FIND_ENTITY_PREDICATE;

    private int lastPassiveTrustIncrease = 0;

    int hunger = MaxHunger();

    @Override
    public int getHunger() {
        return hunger;
    }

    @Override
    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    @Override
    public int MaxHunger() {
        return 200;
    }

    int Saturation = MaxSaturation();

    @Override
    public int getSaturation() {
        return Saturation;
    }

    @Override
    public void setSaturation(int saturation) {
        this.Saturation = saturation;
    }

    @Override
    public int MaxSaturation() {
        return 200;
    }

    int HungerUpdateTime = MaxHungerUpdateTime();

    @Override
    public int getRemainingHungerUpdateTime() {
        return this.HungerUpdateTime;
    }

    @Override
    public void setRemainingHungerUpdateTime(int remainingHungerTimer) {
    this.HungerUpdateTime = remainingHungerTimer;
    }

    @Override
    public int MaxHungerUpdateTime() {
        return 15;
    }

    @Override
    public void updateSaturation() {
        if(this.getHealth() < this.getMaxHealth()){
            this.heal(1);
        } else if (this.getHunger() < this.MaxHunger()) {
            setHunger(getHunger() +1);
        }
    }

    @Override
    public DragonFoodComponentType dragonFoodComponentType() {
        return ModDragonFoodComponentTypes.Night_fury;
    }

    @Override
    public boolean tamingConditions(LivingEntity living) {
        return entityIsDanger(living);
    }

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    boolean isTamed = false;
    private EntityReference<LivingEntity> owner;

    private int AngerTime = 0;
    private UUID persistentAngerTarget;

    int HungerDecreseTimer = 200;

    boolean oldSwinging = false;
    int swingTime = 0;

    int blinkTimer = 200;
    int blinkProgress = 0;

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(AGGRO, false);
        builder.define(SNEAKING, false);
        builder.define(TAMED, false);
        builder.define(BITING, false);
        builder.define(BLINKING,false);
        super.defineSynchedData(builder);
    }

    private <A extends GeoAnimatable> PlayState faceController(AnimationTest<A> event){
        event.controller().transitionLength(3);
        if(this.entityData.get(BITING)){
            event.setAndContinue(RawAnimation.begin().then("animation.nightfury.face.bite", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        }else {
            if(entityData.get(AGGRO)) {
                event.setAndContinue(RawAnimation.begin().thenPlay("animation.nightfury.face.aggro"));
                return PlayState.CONTINUE;
            }else{
                if(isEating()){
                    event.setAndContinue(RawAnimation.begin().thenPlay("animation.nightfury.face.eat"));
                    return PlayState.CONTINUE;
                } else {
                    if(this.entityData.get(BLINKING)){
                        event.setAndContinue(RawAnimation.begin().thenPlay("animation.nightfury.face.blink"));
                        return PlayState.CONTINUE;
                    }else {
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
        this.owner = EntityReference.of(owner);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        addTrustSaveData(output);
        output.putBoolean("tamed", isTamed());
        addPersistentAngerSaveData(output);
        EntityReference.store(this.owner, output, "Owner");
        addHungerSaveData(output);
        output.putInt("HungryDecreaseTime", HungerDecreseTimer);
        output.putInt("BlinkTime", blinkTimer);
        super.addAdditionalSaveData(output);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        readTrustSaveData(input);
        setTamed(input.getBooleanOr("tamed",false));
        readPersistentAngerSaveData(this.level(),input);
        this.owner = EntityReference.readWithOldOwnerConversion(input, "Owner", this.level());
        readHungerSaveData(input);
        HungerDecreseTimer = input.getIntOr("HungryDecreaseTime",200);
        blinkTimer = input.getIntOr("BlinkTime",200);
        super.readAdditionalSaveData(input);
    }

    @Override
    public boolean handleFood(Player player, Item item) {
        boolean bl = false;
        if(dragonFoodComponentType().contains(item)){
            trust.increaseTrust(player.getUUID(),dragonFoodComponentType().get(item).getTrust_gained());
            dragonFoodComponentType().get(item).onEat(this);
            startHungerUpdate(this.dragonFoodComponentType().get(item).getFood_gained());
            SetEatingProgress(35);
            bl = true;
        }
        return bl;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                new AnimationController<>("main",0,this::mainController),
                new AnimationController<>("eye",0,this::faceController)
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
        if(!this.level().isClientSide()) {
            this.updatePersistentAnger((ServerLevel) this.level(),true);
        }
    }

    public void updateSwinging(){
        if(this.swingTime >0){
            this.swingTime = this.swingTime -1;
            this.entityData.set(BITING,true);
        }else {
            this.entityData.set(BITING, false);
        }
        if(this.swinging && !this.oldSwinging){
            this.swingTime = 18;
        }
        this.oldSwinging = this.swinging;
    }

    public void updateBlinking(){
        if(this.blinkTimer > 0){
            this.blinkTimer = blinkTimer -1;
        }else {
            this.blinkTimer = 100 + random.nextInt(150);
            this.blinkProgress = 11;
        }
        if(this.blinkProgress > 0){
            this.blinkProgress = this.blinkProgress -1;
            this.entityData.set(BLINKING,true);
        }else {
            this.blinkProgress = 0;
            this.entityData.set(BLINKING,false);
        }
    }

    public void BigBox(){
        boolean Should_be_sneaking = false;
        boolean Should_be_aggressive = false;
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class,this.getHitbox().inflate(30)).stream().toList();
        for ( int i = 1; i < list.size(); i++){
            LivingEntity entity = list.get(i);
            if(entity instanceof Player && !((Player) entity).isCreative()) {
                if (TrustingDragon.trust.getTrust(entity.getUUID()) < 100 || trust.getTrust(entity.getUUID()) == null) {
                    Should_be_sneaking = true;
                }
                if (entity.getUUID().equals(getPersistentAngerTarget()) || (trust.getTrust(entity.getUUID()) < 50)) {
                    if (!entity.isShiftKeyDown()) {
                        setTarget(entity);
                        Should_be_aggressive = true;
                    }
                }
            }
        }
        if(Should_be_sneaking){
            this.setShiftKeyDown(true);
        }else {
            this.setShiftKeyDown(false);
        }
        if(Should_be_aggressive){
            setRemainingPersistentAngerTime(200);
        }
    }

    public void SmallBox(){
        boolean Should_be_aggro = false;
        List<LivingEntity> list1 = this.level().getEntitiesOfClass(LivingEntity.class,this.getHitbox().inflate(10)).stream().toList();
        for(int i = 1; i<list1.size(); i++){
            LivingEntity entity = list1.get(i);
            if(entity instanceof Player && !((Player) entity).isCreative()) {
                if (trust.getTrust(entity.getUUID()) <= 50) {
                    Should_be_aggro = true;
                }
                if(trust.getTrust(entity.getUUID())< 50) {
                    setTarget(entity);
                }else if(trust.getTrust(entity.getUUID()) < 100){
                    if(!entity.isShiftKeyDown()) {
                        setTarget(entity);
                    }
                }
            }
        }
        if(Should_be_aggro){
            entityData.set(AGGRO, true);
        }else {
            entityData.set(AGGRO, false);
        }
    }

    public void AngerManager(){
    this.BigBox();
    this.SmallBox();
    }

    @Override
    public void tick() {
        super.tick();
        if(!this.level().isClientSide()){
            if(this.HungerDecreseTimer <= 200){
                if(isFlapping()){
                    this.HungerDecreseTimer = this.HungerDecreseTimer +3;
                } else if (isFallFlying()) {
                    this.HungerDecreseTimer = this.HungerDecreseTimer +2;
                } else if (isSprinting()) {
                    this.HungerDecreseTimer = this.HungerDecreseTimer +2;
                } else if (isMoving() && !isShiftKeyDown()) {
                    this.HungerDecreseTimer = this.HungerDecreseTimer +1;
                }
            }else {
                this.HungerDecreseTimer = 0;
                this.setHunger(this.getHunger() -1);
            }
            updateHungerTimer();
            if (lastPassiveTrustIncrease > 0){
                lastPassiveTrustIncrease = lastPassiveTrustIncrease -1;
            }
            if(!this.level().isClientSide()) {
                AngerManager();
                entityData.set(TAMED,this.isTamed());
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
                }
            }
            this.updateSwinging();
            this.updateBlinking();
        }

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
        Item item = stack.getItem();
        if(!this.level().isClientSide()){
                if (canEat(item)){
                    if(canIncreseSaturation(this.getHealth() < this.getMaxHealth())){
                    if( handleFood(player, item)) {
                        stack.consume(1, player);
                        return InteractionResult.SUCCESS;
                    }}else {
                        player.displayClientMessage(Component.translatable("text.not_enough_dragons.dragon.dragon.not_eat"), true);
                        return InteractionResult.PASS;
                    }}
            if(player.isShiftKeyDown() && (stack.isEmpty() || stack.is(Items.AIR)) && this.trust.getTrust(player.getUUID()) >= 100){
                if(!this.isTamed()){
                    this.Tame(player);
                    return InteractionResult.SUCCESS;
                } else {
                      player.displayClientMessage(Component.translatable("text.not_enough_dragons.dragon.dragon.not_tame_no_name"), true);
                    return InteractionResult.FAIL;
                }
            } else if (stack.is(Items.STICK)) {
                NotEnoughDragons.LOGGER.info(trust.toString());
                player.displayClientMessage((Component.translatable("text.not_enough_dragons.dragon.get_trust").append(Component.literal(String.valueOf(trust.getTrust(player.getUUID()))))), true);
                return InteractionResult.SUCCESS;
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
        AGGRO = SynchedEntityData.defineId(NightFury.class, EntityDataSerializers.BOOLEAN);
        SNEAKING = SynchedEntityData.defineId(NightFury.class, EntityDataSerializers.BOOLEAN);
        TAMED = SynchedEntityData.defineId(NightFury.class, EntityDataSerializers.BOOLEAN);
        BITING = SynchedEntityData.defineId(NightFury.class, EntityDataSerializers.BOOLEAN);
        BLINKING = SynchedEntityData.defineId(NightFury.class,EntityDataSerializers.BOOLEAN);
        FIND_ENTITY_PREDICATE = TargetingConditions.forNonCombat().selector(TRUST_FILTER);
    }
}