package tqs.ua.tqs01proj.repos;

import org.springframework.stereotype.Component;
import tqs.ua.tqs01proj.entities.AirQuality;

import java.util.ArrayList;
import java.util.List;

@Component("AQRepository")
public class AirQualityRepository {
    private List<AirQuality> table;
    private final int MAXSIZE;

    public AirQualityRepository() {
        this.table = new ArrayList<>();
        this.MAXSIZE = 5;
    }

    public AirQualityRepository(int maxSize) {
        this.table = new ArrayList<>();
        this.MAXSIZE = maxSize;
    }

    public int getMaxSize(){
        return this.MAXSIZE;
    }

    public AirQuality save(AirQuality airQuality){
        if (this.table.size() == this.MAXSIZE){
            removeOldest();
        }
        this.table.add(airQuality);
        return airQuality;
    }

    public AirQuality findByCity(String city){
        for (AirQuality aq : this.table){
            if (aq.getCity().equals(city)){
                return aq;
            }
        }
        return null;    // TODO: Return Null object(?), --> ou null po Sercive saber
    }

    public boolean removeByCityName(String name){
        AirQuality temp_airquality = null;
        for (AirQuality aq : this.table){
            if (aq.getCity().equals(name)){
                temp_airquality = aq;
            }
        }
        return this.table.remove(temp_airquality);
    }

    public boolean removeOldest(){
        AirQuality temp_airquality = this.table.get(0);
        for (AirQuality aq : this.table){
            if (temp_airquality.getDate().isAfter(aq.getDate())){
                temp_airquality = aq;
            }
        }
        return this.table.remove(temp_airquality);
    }

}
