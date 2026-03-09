package net.sweningteam.common.registry;

import net.minecraft.world.item.Items;
import net.sweningteam.common.dragon.food.DragonFoodComponent;
import net.sweningteam.common.dragon.food.DragonFoodComponentType;
import net.sweningteam.common.dragon.food.custom.PoisonousFoodComponent;

public class ModDragonFoodComponentTypes {
    public static DragonFoodComponentType Night_fury = new DragonFoodComponentType()
            .add(Items.COD,new DragonFoodComponent(5,2D))
            .add(Items.SALMON,new DragonFoodComponent(7,3D))
            .add(Items.TROPICAL_FISH,new DragonFoodComponent(3,1D))
            .add(Items.PUFFERFISH, new PoisonousFoodComponent(1,-5D))
            .add(Items.COOKED_COD, new DragonFoodComponent(10,4D))
            .add(Items.COOKED_SALMON, new DragonFoodComponent(14,6D));
}
