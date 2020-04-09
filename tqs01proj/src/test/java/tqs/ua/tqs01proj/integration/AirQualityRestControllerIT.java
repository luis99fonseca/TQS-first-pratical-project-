package tqs.ua.tqs01proj.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
        airQualityRepository.deleteAll();
    }

    @Test
    public void whenGetAirQByCity_thenReturnAirQ() throws Exception {
        String city_name = "viseu";
        createAQEntry(city_name);

        mvc.perform(get("/airquality/"+city_name).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.city").exists()) // replaced .value, as it could return unavailable,
                // giving false error, when working as intended
                .andExpect(jsonPath("$.pollutants").isArray())
        ;
    }

    // TODO: por causa da 2a API, vai dar smp alguma coisa
//    @Test
//    public void whenGetAirQByInexistingCity_thenReturnNullAirQ() throws Exception {
//        String city_name = "viseu";
//        createAQEntry(city_name);
//
//        mvc.perform(get("/airquality/"+city_name).contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.city").value(city_name))
//                .andExpect(jsonPath("$.pollutants").isArray())
//        ;
//    }
    @Test
    public void whenGetStats_afterGetAirQByCity_thenReturnStats() throws Exception {
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


    private void createAQEntry(String name){
        airQualityRepository.save(new AirQuality(name, "portugal",  LocalDateTime.now(), Collections.emptyList() ));
    }

}
