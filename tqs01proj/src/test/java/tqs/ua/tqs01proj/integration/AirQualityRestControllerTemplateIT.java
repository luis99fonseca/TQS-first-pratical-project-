package tqs.ua.tqs01proj.integration;

import org.junit.jupiter.api.AfterEach;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import tqs.ua.tqs01proj.Tqs01projApplication;
import tqs.ua.tqs01proj.entities.AirQuality;
import tqs.ua.tqs01proj.repos.AirQualityRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureWebTestClient(timeout = "36000")
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
        String city_name = "viseu";
        createAQEntry(city_name);

        AirQuality airQuality = webTestClient
                .get()
                .uri("airquality/"+city_name)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AirQuality.class).getResponseBody().blockFirst();

        Assertions.assertThat(airQuality.getCity()).isEqualTo(city_name);
    }

    @Test
    public void whenGetAirQByInexistingCity_thenReturnNullAirQ() throws Exception {
        String no_existing_city = "no_city";

        AirQuality airQuality = webTestClient
                .get()
                .uri("airquality/"+no_existing_city)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AirQuality.class).getResponseBody().blockFirst();

        Assertions.assertThat(airQuality.getCity()).isEqualTo("unavailable");
    }

    @Test
    public void whenGetStats_afterGetAirQByCity_thenReturnStats() throws Exception {
        // checking correct hit
        String city_name = "viseu";
        createAQEntry(city_name);

        AirQuality airQuality = webTestClient
                .get()
                .uri("airquality/"+city_name)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AirQuality.class).getResponseBody().blockFirst();

        webTestClient
                .get()
                .uri("airquality/stats")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith(
                        result -> {
                            Assertions.assertThat(result.getResponseBody()).containsExactly(1,0,0);
                        }
                );
    }

    @Test
    public void whenGetStats_afterGetAirQByInexistingCity_thenReturnStats() throws Exception {
        // checking correct misses
        String city_name = "viseu";

        AirQuality airQuality = webTestClient
                .get()
                .uri("airquality/"+city_name)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AirQuality.class).getResponseBody().blockFirst();

        //TODO: code smell here!!
        AirQuality airQuality2 = webTestClient
                .get()
                .uri("airquality/"+city_name + "two")
                .exchange()
                .expectStatus().isOk()
                .returnResult(AirQuality.class).getResponseBody().blockFirst();


        webTestClient
                .get()
                .uri("airquality/stats")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith(
                        result -> {
                            Assertions.assertThat(result.getResponseBody()).containsExactly(0,2,0);
                        }
                );
    }

    @Test
    public void whenGetStats_afterGetAirQByAntiqueCity_thenReturnStats() throws Exception {
        // checking correct falseHits
        String city_name = "viseu";
        createAQEntry(city_name, LocalDateTime.now().plusMinutes(-51) );

        AirQuality airQuality2 = webTestClient
                .get()
                .uri("airquality/"+city_name)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AirQuality.class).getResponseBody().blockFirst();


        webTestClient
                .get()
                .uri("airquality/stats")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith(
                        result -> {
                            Assertions.assertThat(result.getResponseBody()).containsExactly(0,0,1);
                        }
                );
    }

    @Test
    public void whenGetStats_afterAllCases_thenReturnStats() throws Exception {
        // checking correct hits, misses and falseHits
        String antique_city = "viseu";
        String updated_city = "aveiro";
        String noExisting_city = "porto";

        createAQEntry(antique_city, LocalDateTime.now().plusMinutes(-51) );
        createAQEntry(updated_city);

        AirQuality airQuality2 = webTestClient
                .get()
                .uri("airquality/"+antique_city)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AirQuality.class).getResponseBody().blockFirst();

        webTestClient
                .get()
                .uri("airquality/"+updated_city)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AirQuality.class).getResponseBody().blockFirst();

        webTestClient
                .get()
                .uri("airquality/"+noExisting_city)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AirQuality.class).getResponseBody().blockFirst();

        webTestClient
                .get()
                .uri("airquality/stats")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith(
                        result -> {
                            Assertions.assertThat(result.getResponseBody()).containsExactly(1,1,1);
                        }
                );
    }

    @Test
    public void whenGetIncorrectInputCity_tryReturnClosestMatch() throws Exception {
        String city_name = "viseu";
        createAQEntry(city_name);

        AirQuality airQuality = webTestClient
                .get()
                .uri("airquality/"+city_name + "2")
                .exchange()
                .expectStatus().isOk()
                .returnResult(AirQuality.class).getResponseBody().blockFirst();

        Assertions.assertThat(airQuality.getPollutants()).isEmpty();
        Assertions.assertThat(airQuality.getCity()).isEqualTo("viseu");

    }

    private void createAQEntry(String name){
        airQualityRepository.save(new AirQuality(name, "portugal",  LocalDateTime.now(), Collections.emptyList() ));
    }

    private void createAQEntry(String name, LocalDateTime date){
        airQualityRepository.save(new AirQuality(name, "portugal",  date, Collections.emptyList() ));
    }
}
