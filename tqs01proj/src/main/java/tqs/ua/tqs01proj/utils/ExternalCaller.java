package tqs.ua.tqs01proj.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ExternalCaller {

    @Autowired  //from Main
    private WebClient.Builder webClientBuilder;

    @Autowired
    private ConfigProperties configProperties;

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

    public Api02MainResponse getFromApiTwo(String city){
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
