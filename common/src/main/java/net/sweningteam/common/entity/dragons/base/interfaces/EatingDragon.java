package net.sweningteam.common.entity.dragons.base.interfaces;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.sweningteam.common.dragon.food.DragonFoodComponentType;

public interface EatingDragon {

    int getHunger();

    void setHunger(int hunger);

    int MaxHunger();

    int getSaturation();

    void setSaturation(int saturation);

    int MaxSaturation();

    int getRemainingHungerUpdateTime();

    void setRemainingHungerUpdateTime(int remainingHungerTimer);

    int MaxHungerUpdateTime();

    void updateSaturation();

    DragonFoodComponentType dragonFoodComponentType();

    boolean handleFood(Player player, Item item);

    default void addHungerSaveData(ValueOutput output){
        output.putInt("Hunger",getHunger());
        output.putInt("Saturation",getSaturation());
        output.putInt("HungerTime", getRemainingHungerUpdateTime());
    }

    default void readHungerSaveData(ValueInput input){
       setHunger(input.getIntOr("Hunger",MaxHunger()));
       setSaturation(input.getIntOr("Saturation", MaxSaturation()));
       setRemainingHungerUpdateTime(input.getIntOr("HungerTime", MaxHungerUpdateTime()));
    }

    default void startHungerUpdate(int food){
        startHungerTimer();
        setSaturation(food);
    }

    default void startHungerTimer(){
        setRemainingHungerUpdateTime(MaxHungerUpdateTime());
    }

    default void updateHungerTimer(){
        if(getRemainingHungerUpdateTime() > 0){
            setRemainingHungerUpdateTime(getRemainingHungerUpdateTime() -1);
        }else {
            if(getSaturation() > 0){
                setSaturation(getSaturation()-1);
                updateSaturation();
                startHungerTimer();
            }
        }
    }

    default boolean canEat(Item item){
        return dragonFoodComponentType().contains(item);
    }
    default boolean canIncreseSaturation(boolean other){
        return getHunger() < MaxHunger() || other;
    }
}