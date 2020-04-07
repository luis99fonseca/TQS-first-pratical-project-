package tqs.ua.tqs01proj.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tqs.ua.tqs01proj.entities.AirQuality;
import tqs.ua.tqs01proj.services.AirQualityService;

@Controller
public class AirQualityController {

    // https://stackoverflow.com/questions/50563524/requestmapping-works-on-private-methods
    @Autowired
    private AirQualityService airQualityService;

    // TODO: eventualmente mudar (e ver testes)
    //     private AirQuality getAirQuality(@RequestParam(value = "city", required = true) String city)
    // https://javarevisited.blogspot.com/2017/08/difference-between-restcontroller-and-controller-annotations-spring-mvc-rest.html
    @GetMapping("/airquality/{city}")
    @ResponseBody
    private AirQuality getAirQuality(@PathVariable String city){

//        return new AirQuality("cepoes", "portugal");
        return airQualityService.getAirQuality(city);
    }

    @RequestMapping("/index")
    private String frontPage(Model model){
        AirQuality aq = getAirQuality("viseu");
        model.addAttribute("messagem", aq.getPollutants());
        return "index";
    }

    // TODO: por to get stats do Rep

    // TODO: remove later, for testing only
    // TODO: por try catch, pa enviar HttpStatus.ERROR ors someshit
    @PostMapping("/airquality")
    private ResponseEntity<AirQuality> CreateAirQuality(@RequestBody AirQuality airQuality){
        HttpStatus httpStatus = HttpStatus.CREATED;
        AirQuality saved = airQualityService.save(airQuality);
        return new ResponseEntity<>(saved, httpStatus);
    }


}
