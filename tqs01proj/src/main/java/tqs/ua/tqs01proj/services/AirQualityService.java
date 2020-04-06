package tqs.ua.tqs01proj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tqs.ua.tqs01proj.entities.Pollutant;
import tqs.ua.tqs01proj.utils.Api02MainResponse;
import tqs.ua.tqs01proj.entities.AirQuality;
import tqs.ua.tqs01proj.utils.Api01MainResponse;
import tqs.ua.tqs01proj.repos.AirQualityRepository;
import tqs.ua.tqs01proj.utils.ConfigProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class AirQualityService {

    @Autowired
    private AirQualityRepository airQualityRepository;

    @Autowired
    private ConfigProperties configProperties;

    @Autowired  //from Main
    private WebClient.Builder webClientBuilder;

    public AirQuality getAirQuality(String place){
//        System.out.println("[GET] for " + place + "; " + configProperties.getId01());
        // TODO: (tirar daqui, e possibly meter as singleton como no video do gajo;) plus isto muda os testes... bue
        //      https://www.baeldung.com/spring-5-webclient
        //      https://springframework.guru/spring-5-webclient/ -> testam os endpoints
        // TODO: https://stackoverflow.com/questions/60289283/webclient-map-nested-object -> passar pa objetos
        // dizer que é melhor que RestTemplate que vai tar decrepeat

        Api01MainResponse response01 = getFromApiOne(place);
        Api02MainResponse response02 = null;

        // those are instantiated for the purposes of Null Object Pattern, aka not returning null elements;
        List<Pollutant> pollutantList = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.now();

        if (response01.getError() != null){
            System.out.println("Couldn't get data from API01. Cause: " + response01.getError().getDescription());
            response02 = getFromApiTwo(place);

            // TODO: converter pa upg3 https://www2.dmu.dk/AtmosphericEnvironment/Expost/database/docs/PPM_conversion.pdf
            if (response02.getError() != null){
                System.out.println("Couldn't get data from API02. Cause: " + response02.getError().getDetail());

                // If both API's requests have problems, send Null Object
                return new AirQuality("unavailable", "unavailable", localDateTime, pollutantList );
            }

            // If API01 request doesn't work, but API02 does, use it
            for (Api02MainResponse.Api02Data.Api02Pollutants.Api02Pollutant p : response02.getAllPollutants()){
                pollutantList.add( new Pollutant(p.getDisplay_name().toLowerCase(), p.getFull_name().toLowerCase(), p.getConcentration().getValue() ) );
            }
            return new AirQuality(place, "portugal", LocalDateTime.parse(response02.getData().getDatetime().split("Z")[0] ) ,pollutantList);

        }

        // If API01 request works, use it
        for (Api01MainResponse.Api01Response.Api01Periods.Api01Pollutant p: response01.getAllPollutants()){
            pollutantList.add( new Pollutant(p.getType(), p.getName(), p.getValueUGM3() ));
        }
        return new AirQuality(place, "portugal", LocalDateTime.parse(response01.getApi01Date().split("\\+")[0])  ,pollutantList);
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
                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.empty())
                    .bodyToMono(Api01MainResponse.class)
                    .block();
    }

    private Api02MainResponse getFromApiTwo(String city){
            // https://stackoverflow.com/questions/59090105/webclient-block-is-throwing-nullpointerexception
            return webClientBuilder
                    .baseUrl(configProperties.getUrl02())
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/air-quality/v2/current-conditions")
                            .queryParam("lat", 49.857456)
                            .queryParam("lon", 2.354611)
                            .queryParam("key", configProperties.getSecret02())
                            .queryParam("features", "pollutants_concentrations")
                            .build()
                    )
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.empty())
                    .bodyToMono(Api02MainResponse.class)
                    .block();
    }

}
