package tqs.ua.tqs01proj.integration;

import org.junit.jupiter.api.AfterEach;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import tqs.ua.tqs01proj.Tqs01projApplication;
import tqs.ua.tqs01proj.entities.AirQuality;
import tqs.ua.tqs01proj.repos.AirQualityRepository;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Tqs01projApplication.class)
public class AirQualityRestControllerTemplateIT {

    @Autowired
    private AirQualityRepository airQualityRepository;

    @AfterEach
    public void resetCache(){
        airQualityRepository.resetCache();
    }

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void whenGetAirQByCity_thenReturnAirQ() throws Exception {
        String city_name = "viseu1";
        createAQEntry(city_name);

        AirQuality airQuality = webTestClient
                .get()
                .uri("airquality/"+city_name)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AirQuality.class).getResponseBody().blockFirst();

        Assertions.assertThat(airQuality.getCity()).isEqualTo(city_name);
    }

    private void createAQEntry(String name){
        airQualityRepository.save(new AirQuality(name, "portugal",  LocalDateTime.now(), Collections.emptyList() ));
    }

    private void createAQEntry(String name, LocalDateTime date){
        airQualityRepository.save(new AirQuality(name, "portugal",  date, Collections.emptyList() ));
    }
}
