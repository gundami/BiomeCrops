package net.gundami.biomecrop.utils;

import net.gundami.biomecrop.BiomeCrop;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

public class GrowthUtil {
    private static boolean seasons$shouldInject = true;

    @SuppressWarnings("deprecation")
    public static <F extends Block & Fertilizable> void randomTickInject(F fertilizable, BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if(seasons$shouldInject) {
            float multiplier = 1f + getMultiplier(world, pos, state);
            while(multiplier > 0f) {
                multiplier -= 1f;
                float rand = random.nextFloat();
                if(multiplier >= rand) {
                    seasons$shouldInject = false;
                    //BiomeCrop.LOGGER.info("randomTick");
                    fertilizable.randomTick(state, world, pos, random);
                    multiplier -= 1f;
                }
            }
            seasons$shouldInject = true;
            ci.cancel();
        }
    }
    public static <F extends Block & Fertilizable> void growInject(F fertilizable, ServerWorld world, Random random, BlockPos pos, BlockState state, CallbackInfo ci) {


        if(seasons$shouldInject) {
            float multiplier = 1f + getMultiplier(world, pos, state);
            while(multiplier > 0f) {
                multiplier -= 1f;
                float rand = random.nextFloat();
                if(multiplier >= rand) {
                    seasons$shouldInject = false;
                    //BiomeCrop.LOGGER.info("grow");
                    fertilizable.grow(world, random, pos, state);
                }
            }
            seasons$shouldInject = true;
            ci.cancel();
        }
    }
    public static float getMultiplier(ServerWorld world, BlockPos pos, BlockState state) {

        return CropConfig.getBiomeCropMultiplier(Registries.ITEM.getId(state.getBlock().asItem()),world.getBiome(pos).getKey().get().getValue());
    }


}
