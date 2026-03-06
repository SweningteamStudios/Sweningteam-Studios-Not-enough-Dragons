package net.sweningteam.common.entity.dragons.base;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sweningteam.common.dragon.food.DragonFoodComponentType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;

public abstract class Dragon extends Animal implements GeoEntity {

    public boolean isMoving() {return getDeltaMovement().z() != 0 || getDeltaMovement().x() != 0;}

    public abstract DragonFoodComponentType dragonFoodComponentType();

    protected Dragon(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return dragonFoodComponentType().contains(stack.getItem());
    }


    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

}
