package net.sweningteam.common.dragon.food;

import net.minecraft.world.item.Item;
import net.sweningteam.common.entity.dragons.base.Dragon;

public class DragonFoodComponent {
    int food_gained;
    double trust_gained;
    DragonFoodComponent food_component;
    public DragonFoodComponent(int food_gained, double trust_gained){
        this.food_gained = food_gained;
        this.trust_gained = trust_gained;
        this.food_component = this;
    }
    public DragonFoodComponent(int food_gained, double trust_gained, DragonFoodComponent food_component){
        this.food_gained = food_gained;
        this.trust_gained = trust_gained;
        this.food_component = food_component;
    }
    public void onEat(Dragon dragon){
        if(food_component != this){
            food_component.onEat(dragon);
        }
    }
    public int getFood_gained(){
        return this.food_gained;
    }

    public double getTrust_gained() {
        return this.trust_gained;
    }
}
