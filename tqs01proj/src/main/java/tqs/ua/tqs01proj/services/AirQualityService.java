package tqs.ua.tqs01proj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tqs.ua.tqs01proj.apis.Api02MainResponse;
import tqs.ua.tqs01proj.entities.AirQuality;
import tqs.ua.tqs01proj.apis.Api01MainResponse;
//import tqs.ua.tqs01proj.repos.AirQualityRepository;
import tqs.ua.tqs01proj.repos.AirQualityRepository2;
import tqs.ua.tqs01proj.utils.ConfigProperties;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AirQualityService {

        // TODO: tirar isto no futuro
//    private Map<String, AirQuality> temp_db;
    @Autowired
    private AirQualityRepository2 airQualityRepository;

    @Autowired
    private ConfigProperties configProperties;

    @Autowired  //from Main
    private WebClient.Builder webClientBuilder;

    public AirQuality getAirQuality(String place){
        System.out.println("[GET] for " + place + "; " + configProperties.getId01());
        // TODO: (tirar daqui, e possibly meter as singleton como no video do gajo;) plus isto muda os testes... bue
        //      https://www.baeldung.com/spring-5-webclient
        //      https://springframework.guru/spring-5-webclient/ -> testam os endpoints
        // TODO: https://stackoverflow.com/questions/60289283/webclient-map-nested-object -> passar pa objetos
        // dizer que é melhor que RestTemplate que vai tar decrepeat

        Api01MainResponse response = getFromApiOne(place);
        System.out.println("now 2");
        Api02MainResponse response2 = getFromApuTwo(place);
        for (Api02MainResponse.Api02Data.Api02Pollutants.Api02Pollutant p : response2.getData().getPollutants().getListPollutants()){
            System.out.println("<<< " + p.full_name + "; " + p.concentration.value);
        }
        assert response != null;        // TODO: ver se tiro isto, ou dou outro tipo de tolerancia (maybe a 2a API)
        for (Api01MainResponse.Api01Response.Api01Periods.Api01Pollutant ap: response.getAllPollutants()){
            System.out.println(">> "+ ap.getName() + "; " + ap.getValueUGM3());
        }                                                                           // TODO: por funçao na class pa dizer a data
        return new AirQuality(place, "pt", LocalDateTime.parse( response.getResponse().get(0).getPeriods().get(0).getDateTimeISO().split("\\+")[0] ));

//        return airQualityRepository.findByCity(place);
    };

    public AirQuality save(AirQuality airQuality) {
        return airQualityRepository.save(airQuality);
    }

    private Api01MainResponse getFromApiOne(String city){
        return webClientBuilder
                .baseUrl(configProperties.getUrl01())
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/airquality")
                        .queryParam("p", city + ",pt")
                        .queryParam("format", "json")
                        .queryParam("client_id", configProperties.getId01())
                        .queryParam("client_secret", configProperties.getSecret01())
                        .build()
                )
                .retrieve()
                .bodyToMono(Api01MainResponse.class)
                .block();
    }

    private Api02MainResponse getFromApuTwo(String city){
        // TODO: por dados na condifProperties
        return webClientBuilder
                .baseUrl(configProperties.getUrl02())
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/air-quality/v2/current-conditions")
                        .queryParam("lat", 48.857456)
                        .queryParam("lon", 2.354611)
                        .queryParam("key", configProperties.getSecret02())
                        .queryParam("features", "pollutants_concentrations")
                        .build()
                )
                .retrieve()
                .bodyToMono(Api02MainResponse.class)
                .block();
    }

}
