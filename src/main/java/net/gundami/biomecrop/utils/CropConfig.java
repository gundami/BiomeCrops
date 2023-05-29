package net.gundami.biomecrop.utils;

import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.gundami.biomecrop.BiomeCrop;
import net.minecraft.item.Item;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class CropConfig implements SimpleSynchronousResourceReloadListener {

    private static HashMap<Identifier, CropBiomeConfig> biomeCropConfigMap = new HashMap<>();

    public static Float getBiomeCropMultiplier(Identifier cropIdentifier,Identifier biome){
        float modifier =1f;
        System.out.println(cropIdentifier);
        try {
            CropBiomeConfig config = biomeCropConfigMap.get(cropIdentifier);
            modifier = config.getModifier(biome);
        }catch (Exception ignored){

        }
        return modifier;
    }

    public static CropBiomeConfig getBiomeCropConfig(Identifier cropIdentifier){
        try {
            return biomeCropConfigMap.get(cropIdentifier);
        }catch (Exception e){
            return null;
        }
    }

    public static boolean isBiomeCrop(Identifier cropIdentifier){
        return biomeCropConfigMap.containsKey(cropIdentifier);
    }

    @Override
    public Identifier getFabricId() {
        return new ModIdentifier("crop_configs");
    }

    @Override
    public void reload(ResourceManager manager) {
        manager.findResources("biomecrop/crop", id -> id.getPath().endsWith(".json")).forEach((id, resource) -> {
            String[] split = id.getPath().split("/");
            Identifier cropIdentifier = new Identifier(id.getNamespace(), split[split.length-1].replace(".json", ""));
            try {
                CropBiomeConfig config = new CropBiomeConfig(JsonParser.parseReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)));
                biomeCropConfigMap.put(cropIdentifier, config);
            }catch(Exception e) {
                BiomeCrop.LOGGER.error("["+BiomeCrop.MOD_ID+"] Failed to load crop config for: "+cropIdentifier, e);
            }
        });
        if(!biomeCropConfigMap.isEmpty()) {
            BiomeCrop.LOGGER.info("["+BiomeCrop.MOD_ID+"] Successfully loaded "+biomeCropConfigMap.size()+" custom crop configs.");
        }
    }
}
