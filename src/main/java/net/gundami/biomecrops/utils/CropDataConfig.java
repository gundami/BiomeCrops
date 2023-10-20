package net.gundami.biomecrops.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.gundami.biomecrops.BiomeCrops;
import net.gundami.biomecrops.data.CropData;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

public class CropDataConfig implements SimpleSynchronousResourceReloadListener {

    @Override
    public Identifier getFabricId() {
        return new Identifier(BiomeCrops.MOD_ID, "crops");
    }

    @Override
    public void reload(ResourceManager manager) {
        Set<Identifier> col = manager.findResources("crops", path -> path.getPath().endsWith(".json")).keySet();
        BiomeCrops.LOGGER.info("Attempting to register " + col.size() + " crops definitions... ");

        for (Identifier id : col) {
            String[] split = id.getPath().split("/");
            Identifier cropIdentifier = new Identifier(id.getNamespace(), split[split.length-1].replace(".json",""));
            try (InputStream stream = manager.getResource(id).get().getInputStream()) {

                String data = IOUtils.toString(stream, StandardCharsets.UTF_8);
                JsonObject object = new Gson().fromJson(data, JsonObject.class);

                //Temperature
                List<JsonElement> growTemperatureList = object.getAsJsonArray("growTemperature").asList();
                float[] growTemperature = new float[2];
                for (int i = 0; i < 2; i++)
                    growTemperature[i] = growTemperatureList.get(i).getAsFloat();
                //Humidity
                List<JsonElement> growHumidityList = object.getAsJsonArray("growHumidity").asList();
                float[] growHumidity = new float[2];
                for (int i = 0; i < 2; i++)
                    growHumidity[i] = growHumidityList.get(i).getAsFloat();


                BiomeCrops.cropDataHashMap.put(cropIdentifier, new CropData(growTemperature,growHumidity));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(!BiomeCrops.cropDataHashMap.isEmpty()) {
            BiomeCrops.LOGGER.info("Successfully loaded "+BiomeCrops.cropDataHashMap.size()+" custom crop configs.");
        }else {
            BiomeCrops.LOGGER.warn("No crop configs.");
        }

    }
}

