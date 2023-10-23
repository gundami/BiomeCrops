package net.gundami.biomecrops.data;

import net.minecraft.network.PacketByteBuf;

public class CropData {
    float[] temperature;
    float[] downfall;
    int age;
    public CropData(float[] temperature, float[] downfall){
        this.temperature=temperature;
        this.downfall=downfall;
    }

    public float[] getDownfall() {
        return downfall;
    }

    public float[] getTemperature() {
        return temperature;
    }

    public void toBuf(PacketByteBuf buf) {
        buf.writeFloat(temperature[0]);
        buf.writeFloat(temperature[1]);
        buf.writeFloat(downfall[0]);
        buf.writeFloat(downfall[1]);
    }
    public static CropData fromBuf(PacketByteBuf buf) {
        float[] temp = new float[2], humi = new float[2];
        temp[0] = buf.readFloat();
        temp[1] = buf.readFloat();
        humi[0] = buf.readFloat();
        humi[1] = buf.readFloat();
        return new CropData(temp,humi);
    }
}
