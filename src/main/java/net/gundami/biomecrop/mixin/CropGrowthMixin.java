package net.gundami.biomecrop.mixin;

import net.gundami.biomecrop.utils.GrowthUtil;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {CropBlock.class, CocoaBlock.class, StemBlock.class, SaplingBlock.class, SweetBerryBushBlock.class})
public abstract class CropGrowthMixin extends Block implements Fertilizable{
    public CropGrowthMixin(Settings settings){
        super(settings);
    }
    @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
    public void randomTickInject(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        GrowthUtil.randomTickInject(this, state, world, pos, random, ci);
    }

    @Inject(at = @At("HEAD"), method = "grow", cancellable = true)
    public void growInject(ServerWorld world, Random random, BlockPos pos, BlockState state, CallbackInfo ci) {
        GrowthUtil.growInject(this, world, random, pos, state, ci);
    }

    //@Inject(at = @At("HEAD"), method = "canPlaceAt", cancellable = true)
    //public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos, CallbackInfo ci) {
    //    if (GrowthUtil.getMultiplier(world.,pos,state))
    //    return (world.getBaseLightLevel(pos, 0) >= 8 || world.isSkyVisible(pos)) && super.canPlaceAt(state, world, pos);
    //}

}

