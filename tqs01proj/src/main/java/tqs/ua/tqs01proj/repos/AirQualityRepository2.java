package tqs.ua.tqs01proj.repos;

import org.springframework.stereotype.Component;
import tqs.ua.tqs01proj.entities.AirQuality;

import java.util.ArrayList;
import java.util.List;

// TODO: ver se ponho aqui @Component
@Component
public class AirQualityRepository2 {
    private List<AirQuality> table;
    private final int MAXSIZE;

    public AirQualityRepository2() {
        this.table = new ArrayList<>();
        this.MAXSIZE = 5;
    }

    public AirQualityRepository2(int maxSize) {
        this.table = new ArrayList<>();
        this.MAXSIZE = maxSize;
    }

    public int getMaxSize(){
        return this.MAXSIZE;
    }

    public boolean exists(String city){
        for (AirQuality aq : this.table){
            if (aq.getCity().equals(city)){
                return true;
            }
        }
        return false;
    }

    public AirQuality save(AirQuality airQuality){
        this.table.add(airQuality);
        return airQuality;
    }

    public AirQuality findByCity(String city){
        for (AirQuality aq : this.table){
            if (aq.getCity().equals(city)){
                return aq;
            }
        }
        return null;    // TODO: Return Null object(?), ou null po Sercive saber
    }

}
