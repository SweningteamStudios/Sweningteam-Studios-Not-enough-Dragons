package net.sweningteam.common.entity.dragons.base.interfaces;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.sweningteam.common.dragon.food.DragonFoodComponentType;
import net.sweningteam.common.registry.ModDragonFoodComponentTypes;

public interface EatingDragon {


    int MaxSaturation();

    DragonFoodComponentType dragonFoodComponentType();


}
