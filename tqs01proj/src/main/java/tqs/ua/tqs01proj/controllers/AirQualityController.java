package tqs.ua.tqs01proj.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tqs.ua.tqs01proj.entities.AirQuality;
import tqs.ua.tqs01proj.services.AirQualityService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
public class AirQualityController {

    // https://stackoverflow.com/questions/50563524/requestmapping-works-on-private-methods
    @Autowired
    private AirQualityService airQualityService;

    // TODO: eventualmente mudar (e ver testes)
    //     private AirQuality getAirQuality(@RequestParam(value = "city", required = true) String city)
    // https://javarevisited.blogspot.com/2017/08/difference-between-restcontroller-and-controller-annotations-spring-mvc-rest.html
    // TODO: teste para .lowercase()?
    @GetMapping("/airquality/{city}")
    @ResponseBody
    private AirQuality getAirQuality(@PathVariable String city){

//        return new AirQuality("cepoes", "portugal");
        return airQualityService.getAirQuality(city);
    }

    @GetMapping("/airquality/stats")
    @ResponseBody
    private List<Integer> getCacheStats(){
        return airQualityService.getStats();
    }

    @RequestMapping("/index")
    private String frontPage(Model model){
        model.addAttribute("airQ", null);
        model.addAttribute("pollutants", null );
        return "index";
    }

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    private String frontPageResults(@RequestParam(name = "city_name") String city_name, Model model){
        AirQuality airQuality = airQualityService.getAirQuality(city_name);
        model.addAttribute("airQ", airQuality);
        model.addAttribute("pollutants", airQuality.getPollutants() );
        return "index";
    }

}
