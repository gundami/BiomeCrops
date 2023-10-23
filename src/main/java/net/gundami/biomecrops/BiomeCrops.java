package net.gundami.biomecrops;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.gundami.biomecrops.data.CropData;
import net.gundami.biomecrops.utils.CommandRegistry;
import net.gundami.biomecrops.utils.CropDataConfig;
import net.gundami.biomecrops.utils.ItemRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class BiomeCrops implements ModInitializer {
    public static final String MOD_ID = "biomecrops";
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static HashMap<Identifier, CropData> cropDataHashMap = new HashMap<>();
    public static Identifier UPDATE_CROP_DATA = new Identifier(MOD_ID,"update_crop_data");

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Biome Crops loading!");

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> {
            PacketByteBuf buf = PacketByteBufs.create();
            CropDataConfig.toBuf(buf);
            ServerPlayNetworking.send(player, UPDATE_CROP_DATA, buf);
        });

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new CropDataConfig());
        CommandRegistrationCallback.EVENT.register(CommandRegistry::registerCommands);
        ItemRegistry.register();

    }
}