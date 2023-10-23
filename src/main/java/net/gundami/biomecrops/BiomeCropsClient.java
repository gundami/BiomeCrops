package net.gundami.biomecrops;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.gundami.biomecrops.data.CropData;
import net.gundami.biomecrops.utils.CropDataConfig;
import net.minecraft.util.Identifier;

import java.util.HashMap;


public class BiomeCropsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(BiomeCrops.UPDATE_CROP_DATA, (client, handler, buf, responseSender) -> {
            HashMap<Identifier, CropData> receivedMap = CropDataConfig.fromBuf(buf);
            client.execute(() -> {
                BiomeCrops.cropDataHashMap.putAll(receivedMap);
                BiomeCrops.LOGGER.info("["+BiomeCrops.MOD_ID+"] Received dedicated server crops.");
            });

        });
    }
}
