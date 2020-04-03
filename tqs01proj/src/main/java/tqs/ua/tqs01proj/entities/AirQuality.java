package tqs.ua.tqs01proj.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Map;

@Entity
public class AirQuality {

    @Id
    private String city;
    private String country;

    // TODO: mudar para type Date(?)
    private String date;

    // TODO: mudar para um outro objeto? Like Period
    // TODO: try to make it work it https://docs.oracle.com/javaee/6/api/javax/persistence/ElementCollection.html
//    private Map<String, String> periods;

    public AirQuality(){}

    public AirQuality(String city, String country) {
        this.city = city;
        this.country = country;
    }

    // Note: he key in JSON are not exactly the name of properties but in actual those are values of getter
    // methods after omitting the get from the method name. Even if we change the properties in the class but
    // keep the name of getter same we will not see any change in the JSON.
    // Nota: Mesmo no PostMan, funciona com o mesmo conceito :)
    // TODO: ver como fica no testing
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
