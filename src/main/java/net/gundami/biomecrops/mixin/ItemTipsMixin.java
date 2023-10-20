package net.gundami.biomecrops.mixin;

import net.gundami.biomecrops.BiomeCrops;
import net.gundami.biomecrops.data.CropData;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BlockItem.class)
public abstract class ItemTipsMixin {
	@Shadow public abstract Block getBlock();

	@Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
		if (BiomeCrops.cropDataHashMap.containsKey(Registries.ITEM.getId(this.getBlock().asItem()))){
			CropData cropData = BiomeCrops.cropDataHashMap.get(Registries.ITEM.getId(this.getBlock().asItem()));
			tooltip.add(Text.translatable("tooltip.biomecrops.temperature").styled(style -> style.withColor(0xFFD700)).append(": ").append(Text.literal(String.format("%.1f - %.1f°C",convertT(cropData.getTemperature()[0]),convertT(cropData.getTemperature()[1]))).styled(style -> style.withColor(0x32CD32))));
			tooltip.add(Text.translatable("tooltip.biomecrops.humidity").styled(style -> style.withColor(0xFFD700)).append(": ").append(Text.literal(String.format("%.1f - %.1f",convertH(cropData.getDownfall()[0]),convertH(cropData.getDownfall()[1]))).styled(style -> style.withColor(0x00CED1))));
		}
	}
	private float convertT(float t){
		return t*20;
	}
	private float convertH(float h){
		return h*100;
	}
}