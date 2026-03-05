package net.sweningteam.common.dragon.food.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.sweningteam.common.dragon.food.DragonFoodComponent;
import net.sweningteam.common.entity.dragons.base.Dragon;

public class PoisonousFoodComponent extends DragonFoodComponent {
    public PoisonousFoodComponent(double food_gained, double trust_gained) {
        super(food_gained, trust_gained);
    }

    @Override
    public void onEat(Dragon dragon) {
        dragon.addEffect(new MobEffectInstance(MobEffects.POISON,400));
        super.onEat(dragon);
    }
}
