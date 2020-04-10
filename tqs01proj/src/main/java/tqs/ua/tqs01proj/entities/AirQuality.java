package tqs.ua.tqs01proj.entities;

import java.time.LocalDateTime;
import java.util.List;

public class AirQuality {

    private String city;
    private String country;

    private LocalDateTime date;

    private List<Pollutant> pollutants;

    public AirQuality(){}

    public AirQuality(String city, String country, LocalDateTime date, List<Pollutant> pollutants) {
        this.city = city;
        this.country = country;
        this.date = date;
        this.pollutants = pollutants;
    }

    // Note: he key in JSON are not exactly the name of properties but in actual those are values of getter
    // methods after omitting the get from the method name. Even if we change the properties in the class but
    // keep the name of getter same we will not see any change in the JSON.
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

    public List<Pollutant> getPollutants() {
        return pollutants;
    }
}
