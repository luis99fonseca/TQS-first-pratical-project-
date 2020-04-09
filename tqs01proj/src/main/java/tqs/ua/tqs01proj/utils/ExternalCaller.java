package tqs.ua.tqs01proj.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExternalCaller {

    @Autowired  //from Main
    private WebClient.Builder webClientBuilder;

    @Autowired
    private ConfigProperties configProperties;

    private Map<String, Api03MainResponse.Api03Result.Api03Geometry> geoMappings = new HashMap<>();

    public Api01MainResponse getFromApiOne(String city){
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

    // TODO: conseguir ao mandar localizaçao mal :D
    public Api02MainResponse getFromApiTwo(String city){
        if (!this.geoMappings.containsKey(city)){
            Api03MainResponse api03MainResponse = webClientBuilder
                    .baseUrl(configProperties.getUrl03())
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/geocode/v1/json")
                            .queryParam("q", city)
                            .queryParam("key", configProperties.getSecret03())
                            .queryParam("limit", 1)
                            .build()
                    )
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.empty())
                    .bodyToMono(Api03MainResponse.class)
                    .block();

            if (api03MainResponse.getCords() != null){
                this.geoMappings.put(city, api03MainResponse.getCords());
            }

        }

        if (this.geoMappings.containsKey(city)){
            // https://stackoverflow.com/questions/59090105/webclient-block-is-throwing-nullpointerexception
            return webClientBuilder
                    .baseUrl(configProperties.getUrl02())
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/air-quality/v2/current-conditions")
                            .queryParam("lat", this.geoMappings.get(city).getLat())
                            .queryParam("lon", this.geoMappings.get(city).getLat())
                            .queryParam("key", configProperties.getSecret02())
                            .queryParam("features", "pollutants_concentrations")
                            .build()
                    )
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.empty())
                    .bodyToMono(Api02MainResponse.class)
                    .block();
        }
        Api02MainResponse api02MainResponse = new Api02MainResponse();
        api02MainResponse.setError(new Api02MainResponse.Api02Error());
        return api02MainResponse;
    }
}
