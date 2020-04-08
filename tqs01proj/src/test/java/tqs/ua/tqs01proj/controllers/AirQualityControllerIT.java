package tqs.ua.tqs01proj.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tqs.ua.tqs01proj.entities.AirQuality;
import tqs.ua.tqs01proj.entities.AirQualityNull;
import tqs.ua.tqs01proj.services.AirQualityService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AirQualityController.class)
class AirQualityControllerIT {

    @Autowired
    MockMvc servlet;

    @MockBean
    AirQualityService airQualityService;

    @Test
    public void whenGetAirQByCity_thenReturnAirQ() throws Exception {
        given( airQualityService.getAirQuality( anyString()) ).willReturn(
                new AirQuality("viseu", "portugal", LocalDateTime.now(), Collections.emptyList() )
        );

        servlet.perform(MockMvcRequestBuilders.get("/airquality/viseu")).
                andExpect(status().isOk()).
                andExpect(jsonPath("city").value("viseu"))
        ;
    }

    // TODO: decidir what do here.. tem de tar de acorod com o de sevice
    @Test
    public void whenGetAirQByInexistingCity_thenReturnAirQ() throws Exception {
        String non_existing_city = "no_city";

        given( airQualityService.getAirQuality( non_existing_city )).willThrow(
                new AirQualityNull()
        );

        servlet.perform(MockMvcRequestBuilders.get("/airquality/".concat(non_existing_city))).
                andExpect(status().isNotFound())
                // TODO: ver se meto a ver que city é null :L
                //.andExpect(jsonPath("city").value("viseu"))
        ;
    }

    @Test
    public void whenGetStats_thenReturnStats() throws Exception {
        given (airQualityService.getStats()).willReturn(
                Collections.emptyList()
        );

        servlet.perform(MockMvcRequestBuilders.get("/airquality/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

}