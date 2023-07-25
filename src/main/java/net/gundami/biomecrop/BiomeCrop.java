package net.gundami.biomecrop;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.gundami.biomecrop.utils.CropBiomeConfig;
import net.gundami.biomecrop.utils.CropConfig;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static net.minecraft.server.command.CommandManager.*;

public class BiomeCrop implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static final String MOD_ID = "biomecrop";


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new CropConfig());
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("biomecrop").then(literal("check")
				.executes(context -> {
					try {
						if (context.getSource().isExecutedByPlayer()) {
							ServerPlayerEntity player = context.getSource().getPlayer();
							Identifier biomeIdentifier = player.getWorld().getBiome(context.getSource().getPlayer().getBlockPos()).getKey().get().getValue();
							Identifier cropIdentifier = Registries.ITEM.getId(player.getStackInHand(player.getActiveHand()).getItem());
							float modifier =  CropConfig.getBiomeCropMultiplier(cropIdentifier,biomeIdentifier);
							CropBiomeConfig config = CropConfig.getBiomeCropConfig(cropIdentifier);
							if (config==null){
								context.getSource().sendMessage(Text.literal("§7Not a biome crop."));
							}else {
								context.getSource().sendMessage(Text.literal(
										(modifier !=0f ? "§6"+player.getStackInHand(player.getActiveHand()).getItem().toString()+" §agrow in this biome with speed §b§l"+modifier*100+"§a%\n"
												 : "§cThis crop cannot grow in this biome.\n") + mixup(config)
								));
							}
						}else {
							context.getSource().sendMessage(Text.literal("Only work for player!"));
						}
					}catch (Exception ignored){

					}
					return 1;
				}))));
	}

	private String mixup(CropBiomeConfig config){
		HashMap<Identifier, Float> map = config.getBiomeHashMap();
		StringBuilder result = new StringBuilder();
		result.append("§aThis crop could grow at these biomes. \n");
		for (Identifier biome: map.keySet()){
			result.append(biome+" §b§l"+map.get(biome)*100+"§a%\n");
		}
		return result.toString();
	}
}
