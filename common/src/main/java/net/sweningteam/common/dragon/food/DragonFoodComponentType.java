package net.sweningteam.common.dragon.food;

import net.minecraft.world.item.Item;
import net.sweningteam.NotEnoughDragons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DragonFoodComponentType {
    Map<Item,DragonFoodComponent> dragonFoodComponentMap = new HashMap<>();

    public DragonFoodComponentType add(Item key,DragonFoodComponent value){
        if(!this.dragonFoodComponentMap.containsKey(key)){
            this.dragonFoodComponentMap.put(key,value);
        }
        return this;
    }

    public boolean contains(Item item){
        boolean bl = false;
        if(dragonFoodComponentMap.containsKey(item)){
            bl = true;
        }
        return bl;
    }

    public DragonFoodComponent get(Item item){
        return dragonFoodComponentMap.get(item);
    }
}
