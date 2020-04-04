package tqs.ua.tqs01proj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tqs.ua.tqs01proj.entities.AirQuality;
import tqs.ua.tqs01proj.entities.TempClass;
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

    @Autowired  //from Main
    private WebClient.Builder webClientBuilder;

    public AirQuality getAirQuality(String place){
        System.out.println("[GET] for " + place + "; " + configProperties.getId01());
        // TODO: tirar daqui, e possibly meter as singleton como no video do gajo; plus isto muda os testes... bue
        //      https://www.baeldung.com/spring-5-webclient
        //      https://springframework.guru/spring-5-webclient/ -> testam os endpoints
        // TODO: https://stackoverflow.com/questions/60289283/webclient-map-nested-object -> passar pa objetos
        // dizer que é melhor que RestTemplate que vai tar decrepeat

        TempClass builder = webClientBuilder
                .baseUrl("https://api.aerisapi.com")
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/airquality")
                        .queryParam("p", "viseu,pt")
                        .queryParam("format", "json")
                        .queryParam("client_id", configProperties.getId01())
                        .queryParam("client_secret", configProperties.getSecret01())
                        .build()
                )
                .retrieve()
                .bodyToMono(TempClass.class)
                .block();
        System.out.println(">> " + builder.success);
        return airQualityRepository.findByCity(place);
    };

    public AirQuality save(AirQuality airQuality) {
        return airQualityRepository.save(airQuality);
    }

}
