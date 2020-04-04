package tqs.ua.tqs01proj.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AirQuality {

    private String city;
    private String country;

    // TODO: mudar para type Date(?)
    private LocalDateTime date;

    private List<Pollutants> pollutants;

    public AirQuality(){}

    public AirQuality(String city, String country) {
        this.city = city;
        this.country = country;
    }

    public AirQuality(String city, String country, LocalDateTime date) {
        this.city = city;
        this.country = country;
        this.date = date;
    }

    // Note: he key in JSON are not exactly the name of properties but in actual those are values of getter
    // methods after omitting the get from the method name. Even if we change the properties in the class but
    // keep the name of getter same we will not see any change in the JSON.
    // Nota: Mesmo no PostMan, funciona com o mesmo conceito :)
    // TODO: ver como fica no testing
    public String getCity() {
        return city;
    }


    public String getCountry() {
        return country;
    }


    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<Pollutants> getPollutants() {
        return pollutants;
    }
}
