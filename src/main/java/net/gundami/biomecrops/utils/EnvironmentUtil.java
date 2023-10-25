package net.gundami.biomecrops.utils;

import io.github.lucaargolo.seasons.utils.Season;
import net.gundami.biomecrops.data.CropData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static dev.architectury.hooks.level.biome.BiomeHooks.getBiomeProperties;
import static io.github.lucaargolo.seasons.FabricSeasons.getCurrentSeason;

public class EnvironmentUtil {
    private Biome biome;
    private Season season;
    private float envTemperature;
    private float envHumidity;
    public EnvironmentUtil(World world, BlockPos blockPos){
        this.biome=world.getBiome(blockPos).value();
        this.season = getCurrentSeason(world);
        this.envTemperature = getBiomeProperties(biome).getClimateProperties().getTemperature();
        this.envHumidity = getBiomeProperties(biome).getClimateProperties().getDownfall();
    }
    public boolean canGrow(CropData cropData){
        float cropTempMin = cropData.getTemperature()[0];
        float cropTempMax = cropData.getTemperature()[1];
        float cropHumidityMin = cropData.getDownfall()[0];
        float cropHumidityMax = cropData.getDownfall()[1];

        return (envTemperature >= cropTempMin && envTemperature <= cropTempMax &&
                envHumidity >= cropHumidityMin && envHumidity<= cropHumidityMax);
    }
    public float getEnvTemperature(){
        return envTemperature;
    }
    public float getEnvHumidity(){
        return envHumidity;
    }
    public float getReadableTemperature(){
        return new BigDecimal(envTemperature * 20).setScale(1, RoundingMode.HALF_DOWN).floatValue();
    }
    public float getReadableHumidity(){
        return new BigDecimal(envHumidity * 100).setScale(1, RoundingMode.HALF_DOWN).floatValue();
    }
    public float growSpeed(CropData cropData){
        float cropTempMin = cropData.getTemperature()[0];
        float cropTempMax = cropData.getTemperature()[1];
        float cropHumidityMin = cropData.getDownfall()[0];
        float cropHumidityMax = cropData.getDownfall()[1];

        float midT = (cropTempMax+cropTempMin)/2;
        float midH = (cropHumidityMax+cropHumidityMin)/2;

        if (canGrow(cropData)){
            return  (((1-(Math.abs(envTemperature-midT))/(cropTempMax-cropTempMin))+
                        (1-(Math.abs(envHumidity-midH))/(cropHumidityMax-cropHumidityMin)))/2) * 0.7f;
            //offset 0.7
        }else {
            return 0;
        }
    }
}
