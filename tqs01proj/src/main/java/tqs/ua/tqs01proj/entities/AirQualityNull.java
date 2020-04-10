package tqs.ua.tqs01proj.entities;

import java.time.LocalDateTime;
import java.util.Collections;

public  class AirQualityNull extends AirQuality {

    public AirQualityNull(){
        super("unavailable", "unavailable", LocalDateTime.now(), Collections.emptyList());
    }
}
