package tqs.ua.tqs01proj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tqs.ua.tqs01proj.entities.AirQuality;
import tqs.ua.tqs01proj.repos.AirQualityRepository;
import tqs.ua.tqs01proj.utils.ConfigProperties;

import java.util.Map;

@Service
public class AirQualityService {

        // TODO: tirar isto no futuro
//    private Map<String, AirQuality> temp_db;
    @Autowired
    private AirQualityRepository airQualityRepository;

    @Autowired
    private ConfigProperties configProperties;

    public AirQuality getAirQuality(String place){
//        System.out.println("[GET] for " + place + "; " + configProperties.getId01());
        // TODO: tirar daqui, e possibly meter as singleton como no video do gajo; plus isto muda os testes... bue
        //      https://www.baeldung.com/spring-5-webclient
        //      https://springframework.guru/spring-5-webclient/ -> testam os endpoints
        WebClient client = WebClient.builder() //create(configProperties.getUrl01());
                .get()
                .baseUrl(configProperties.getUrl01())


        return airQualityRepository.findByCity(place);
    };

    public AirQuality save(AirQuality airQuality) {
        return airQualityRepository.save(airQuality);
    }

}
