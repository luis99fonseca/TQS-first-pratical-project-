package tqs.ua.tqs01proj.entities;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.Collections;

public  class AirQualityNull extends AirQuality {

    public AirQualityNull(){
        super("unavailable", "unavailable", LocalDateTime.now(), Collections.emptyList());
    }
}
