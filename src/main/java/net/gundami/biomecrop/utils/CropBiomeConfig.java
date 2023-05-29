package net.gundami.biomecrop.utils;

import com.google.gson.JsonElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CropBiomeConfig {
    private HashMap<Identifier,Float> BiomeHashMap = new HashMap<>();

    public CropBiomeConfig(JsonElement json){
        Set<Map.Entry<String, JsonElement>> es = json.getAsJsonObject().entrySet();
        for (Map.Entry<String, JsonElement> en : es)
        {
            BiomeHashMap.put(new Identifier(en.getKey()),en.getValue().getAsFloat());
        }
    }

    public Float getModifier(Identifier identifier){
        System.out.println(identifier.toString());
        if (BiomeHashMap.get(identifier)!=null){
            return  BiomeHashMap.get(identifier);
        }else {
            return 1f;
        }
    }

    public HashMap<Identifier,Float> getBiomeHashMap(){
        return BiomeHashMap;
    }

}
