package tqs.ua.tqs01proj.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.ua.tqs01proj.Tqs01projApplication;
import tqs.ua.tqs01proj.entities.AirQuality;
import tqs.ua.tqs01proj.repos.AirQualityRepository;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Tqs01projApplication.class)
@AutoConfigureMockMvc
public class AirQualityRestControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AirQualityRepository airQualityRepository;

    @AfterEach
    public void resetCache(){
        airQualityRepository.resetCache();
    }

    @Test
    public void whenGetAirQByCity_thenReturnAirQ() throws Exception {
        String city_name = "viseu";
        createAQEntry(city_name);

        mvc.perform(get("/airquality/"+city_name).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.city").value(city_name))
                // giving false error, when working as intended
                .andExpect(jsonPath("$.pollutants").isArray())
        ;
    }

    @Test
    public void whenGetAirQByInexistingCity_thenReturnNullAirQ() throws Exception {
        String no_existing_city = "invalidcity";

        mvc.perform(get("/airquality/"+no_existing_city).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.city").value("unavailable"))
                .andExpect(jsonPath("$.pollutants").isArray())
        ;
    }
    @Test
    public void whenGetStats_afterGetAirQByCity_thenReturnStats() throws Exception {
        // checking correct hit
        String city_name = "viseu";
        createAQEntry(city_name);

        mvc.perform(get("/airquality/"+city_name).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        mvc.perform(get("/airquality/stats").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value(1))
                .andExpect(jsonPath("$[1]").value(0))
                .andExpect(jsonPath("$[2]").value(0))
        ;

    }

    @Test
    public void whenGetStats_afterGetAirQByInexistingCity_thenReturnStats() throws Exception {
        // checking correct misses
        String city_name = "viseu";

        mvc.perform(get("/airquality/"+city_name).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        mvc.perform(get("/airquality/"+city_name+"two").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));


        mvc.perform(get("/airquality/stats").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value(0))
                .andExpect(jsonPath("$[1]").value(2))
                .andExpect(jsonPath("$[2]").value(0))
        ;
    }

    @Test
    public void whenGetStats_afterGetAirQByAntiqueCity_thenReturnStats() throws Exception {
        // checking correct falseHits
        String city_name = "viseu";
        createAQEntry(city_name, LocalDateTime.now().plusMinutes(-51) );

        mvc.perform(get("/airquality/"+city_name).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));


        mvc.perform(get("/airquality/stats").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value(0))
                .andExpect(jsonPath("$[1]").value(0))
                .andExpect(jsonPath("$[2]").value(1))
        ;
    }

    @Test
    public void whenGetStats_afterAllCases_thenReturnStats() throws Exception {
        // checking correct hits, misses and falseHits
        String antique_city = "viseu";
        String updated_city = "aveiro";
        String noExisting_city = "porto";

        createAQEntry(antique_city, LocalDateTime.now().plusMinutes(-51) );
        createAQEntry(updated_city);

        mvc.perform(get("/airquality/"+antique_city).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        mvc.perform(get("/airquality/"+updated_city).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        mvc.perform(get("/airquality/"+noExisting_city).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        mvc.perform(get("/airquality/stats").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value(1))
                .andExpect(jsonPath("$[1]").value(1))
                .andExpect(jsonPath("$[2]").value(1))
        ;
    }

    @Test
    public void whenGetIncorrectInputCity_tryReturnClosestMatch() throws Exception {
        String city_name = "viseu";
        createAQEntry(city_name);

        mvc.perform(get("/airquality/"+city_name+"2").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.city").value("viseu"))
                // giving false error, when working as intended
                .andExpect(jsonPath("$.pollutants").isEmpty())
        ;
    }

    private void createAQEntry(String name){
        airQualityRepository.save(new AirQuality(name, "portugal",  LocalDateTime.now(), Collections.emptyList() ));
    }

    private void createAQEntry(String name, LocalDateTime date){
        airQualityRepository.save(new AirQuality(name, "portugal",  date, Collections.emptyList() ));
    }

}
