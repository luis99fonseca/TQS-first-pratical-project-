package tqs.ua.tqs01proj.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tqs.ua.tqs01proj.entities.AirQuality;
import tqs.ua.tqs01proj.services.AirQualityService;

import java.util.List;

@Controller
public class AirQualityController {

    // https://stackoverflow.com/questions/50563524/requestmapping-works-on-private-methods
    @Autowired
    private AirQualityService airQualityService;

    // https://javarevisited.blogspot.com/2017/08/difference-between-restcontroller-and-controller-annotations-spring-mvc-rest.html
    @GetMapping("/airquality/{city}")
    @ResponseBody
    public AirQuality getAirQuality(@PathVariable String city){
        return airQualityService.getAirQuality(city);
    }

    @GetMapping("/airquality/stats")
    @ResponseBody
    public List<Integer> getCacheStats(){
        return airQualityService.getStats();
    }

    @GetMapping(value = "/index")
    public String frontPage(Model model){
        model.addAttribute("airQ", null);
        model.addAttribute("pollutants", null );
        return "index";
    }

    @PostMapping(value = "/info")
    public String frontPageResults(@RequestParam(name = "city_name") String cityName, Model model){
        AirQuality airQuality = airQualityService.getAirQuality(cityName);
        model.addAttribute("airQ", airQuality);
        model.addAttribute("pollutants", airQuality.getPollutants() );
        return "index";
    }

}
