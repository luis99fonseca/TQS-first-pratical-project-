package tqs.ua.tqs01proj.entities;

public class Pollutant {

    private String type;
    private String name;
    private float valueUGM3;
    private String unit;

    public Pollutant(String type, String name, float valueUGM3, String unit) {
        this.type = type;
        this.name = name;
        this.valueUGM3 = valueUGM3;
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public float getValueUGM3() {
        return valueUGM3;
    }
}
