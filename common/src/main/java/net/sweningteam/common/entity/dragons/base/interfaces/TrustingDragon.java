package net.sweningteam.common.entity.dragons.base.interfaces;

import com.google.gson.Gson;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.sweningteam.common.dragon.trust.DragonTrust;
import net.sweningteam.common.entity.dragons.base.Dragon;

import java.util.HashMap;
import java.util.UUID;

public interface TrustingDragon {
    public DragonTrust trust = new DragonTrust();

    Gson gson = new Gson();

    default void readTrustSaveData(ValueInput input){
        trust.clear();
        trust.getTrustFromString(input.getStringOr("Trust",gson.toJson(new HashMap<String,Integer>(),trust.getTrustMap().getClass())));
    }
    default void addTrustSaveData(ValueOutput output){
        output.putString("Trust", trust.toString());
    }

    default void notifyOtherTrust( Dragon other, UUID entity, double trust){

    }

    default void getNotifiedTrust(Dragon sender, UUID entity, double trust){
        if(this instanceof Dragon){
            if(this != sender){
                
            }
        }

    }

}
