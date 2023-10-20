package net.gundami.biomecrops.mixin;

import net.gundami.biomecrops.BiomeCrops;
import net.gundami.biomecrops.data.CropData;
import net.gundami.biomecrops.utils.EnvironmentUtil;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {CropBlock.class, CocoaBlock.class, StemBlock.class, SaplingBlock.class, SweetBerryBushBlock.class})
public abstract class CropGrowMixin extends Block implements Fertilizable {

    private static boolean seasons$shouldInject = true;
    public CropGrowMixin(Settings settings) {
        super(settings);
    }
    @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
    public void randomTickInject(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        randomTickInjector(this, state, world, pos, random, ci);
    }

    @Inject(at = @At("HEAD"), method = "grow", cancellable = true)
    public void growInject(ServerWorld world, Random random, BlockPos pos, BlockState state, CallbackInfo ci) {
        //growInjector(this, world, random, pos, state, ci);
        ci.cancel();
    }
    private static <F extends Block & Fertilizable> void randomTickInjector(F fertilizable, BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        EnvironmentUtil environmentUtil = new EnvironmentUtil(world,pos);
        Identifier crop = Registries.ITEM.getId(state.getBlock().asItem());
        if (BiomeCrops.cropDataHashMap.containsKey(crop)){
            CropData cropData = BiomeCrops.cropDataHashMap.get(crop);
            if (environmentUtil.canGrow(cropData)){
                if (seasons$shouldInject){
                    float multiplier = 1f + environmentUtil.growSpeed(cropData);
                    while(multiplier > 0f) {
                        multiplier -= 1f;
                        float rand = random.nextFloat();
                        if(multiplier >= rand) {
                            seasons$shouldInject = false;
                            fertilizable.randomTick(state, world, pos, random);
                            System.out.println("grow");
                            multiplier -= 1f;
                        }
                    }
                    seasons$shouldInject = true;
                    ci.cancel();
                }
            }
            else ci.cancel();
        }
    }

    private static <F extends Block & Fertilizable> void growInjector(F fertilizable, ServerWorld world, Random random, BlockPos pos, BlockState state, CallbackInfo ci) {
        EnvironmentUtil environmentUtil = new EnvironmentUtil(world,pos);
        Identifier crop = Registries.ITEM.getId(state.getBlock().asItem());
        if (BiomeCrops.cropDataHashMap.containsKey(crop)){
            CropData cropData = BiomeCrops.cropDataHashMap.get(crop);
            if (environmentUtil.canGrow(cropData)){
                if (seasons$shouldInject){

                    float multiplier = 1f + 0.1f;
                    while(multiplier > 0f) {
                        multiplier -= 1f;
                        float rand = random.nextFloat();
                        if(multiplier >= rand) {
                            seasons$shouldInject = false;
                            fertilizable.grow(world, random, pos, state);
                            System.out.println("fer");
                            multiplier -= 1f;
                        }
                    }
                    seasons$shouldInject = true;
                    ci.cancel();
                }
            }else ci.cancel();
        }
    }
}
