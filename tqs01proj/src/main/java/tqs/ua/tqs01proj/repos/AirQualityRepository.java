package tqs.ua.tqs01proj.repos;

import org.springframework.stereotype.Component;
import tqs.ua.tqs01proj.entities.AirQuality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component("AQRepository")
public class AirQualityRepository {
    private List<AirQuality> table;
    private final int MAXSIZE;
    private int hits;
    private int misses;
    private int falseHits;

    public AirQualityRepository() {
        this.table = new ArrayList<>();
        this.MAXSIZE = 5;
        this.hits = 0;
        this.misses = 0;
        this.falseHits = 0;
    }

    public AirQualityRepository(int maxSize) {
        this.table = new ArrayList<>();
        this.MAXSIZE = maxSize;
        this.hits = 0;
        this.misses = 0;
        this.falseHits = 0;
    }

    public int getMaxSize(){
        return this.MAXSIZE;
    }

    public List<Integer> getStats(){
        return new ArrayList<>(
                Arrays.asList(
                        this.hits,
                        this.misses,
                        this.falseHits
                )
        );
    }

    public AirQuality save(AirQuality airQuality){
        // if a AQ of this name is already there, it is removed, else nothing happens
        if (this.table.remove(checkByCityName(airQuality.getCity()))){
            this.falseHits++;    // because it means we had to remove a old value
        }

        if (this.table.size() == this.MAXSIZE){
            removeOldest();
        }
        this.table.add(airQuality);
        return airQuality;
    }

    public AirQuality findByCityName(String city){
        for (AirQuality aq : this.table){
            if (aq.getCity().equals(city)){
                this.hits++;
                return aq;
            }
        }
        this.misses++;
        return null;
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

    private AirQuality checkByCityName(String city_name){
        for (AirQuality aq : this.table){
            if (aq.getCity().equals(city_name)){
                return aq;
            }
        }
        return null;
    }

}
