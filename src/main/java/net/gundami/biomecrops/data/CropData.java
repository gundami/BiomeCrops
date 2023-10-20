package net.gundami.biomecrops.data;

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
}
