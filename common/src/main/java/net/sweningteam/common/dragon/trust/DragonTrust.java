package net.sweningteam.common.dragon.trust;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import net.sweningteam.NotEnoughDragons;

import java.lang.annotation.Native;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DragonTrust {
    Map<UUID, Double> Trust = Maps.newHashMap();
    Gson gson = new Gson();

    public void setTrust(UUID key, Double value){
        if(Trust.containsKey(key)){
            Trust.replace(key, value);
        }else {
            Trust.put(key, value);
        }
    }

    public void increaseTrust(UUID key, Double amount){
        if(Trust.containsKey(key)){
            Double oldTrust =Trust.get(key);
            Trust.replace(key, oldTrust+amount);
        }else {
            Trust.put(key, amount);
        }
    }

    public boolean hasTrust(UUID key){
        return Trust.containsKey(key);

    }

    public Double getTrust(UUID key){
        return Trust.getOrDefault(key, 0D);
    }



    public void clear(){
        Trust.clear();
    }

    @Override
    public String toString() {
        return gson.toJson(Trust);
    }

    public void getTrustFromString(String trust){
        this.Trust = gson.fromJson(trust,Trust.getClass());
    }

    public Map<UUID, Double> getTrustMap(){
        return Trust;
    }
}
