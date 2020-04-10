package tqs.ua.tqs01proj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.ua.tqs01proj.entities.AirQualityNull;
import tqs.ua.tqs01proj.entities.Pollutant;
import tqs.ua.tqs01proj.utils.Api02MainResponse;
import tqs.ua.tqs01proj.entities.AirQuality;
import tqs.ua.tqs01proj.utils.Api01MainResponse;
import tqs.ua.tqs01proj.repos.AirQualityRepository;
import tqs.ua.tqs01proj.utils.ExternalCaller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Service
public class AirQualityService {

    @Autowired
    private AirQualityRepository airQualityRepository;

    @Autowired
    private ExternalCaller externalCaller;


    public AirQuality getAirQuality(String cityName){
        // Can't be done at the Controller, for some reason
        String place = cityName.toLowerCase().replaceAll("\\d","");
        // TODO: (tirar daqui, e possibly meter as singleton como no video do gajo;) plus isto muda os testes... bue
        //      https://www.baeldung.com/spring-5-webclient
        //      https://springframework.guru/spring-5-webclient/ -> testam os endpoints
        // TODO: https://stackoverflow.com/questions/60289283/webclient-map-nested-object -> passar pa objetos
        //  dizer que é melhor que RestTemplate que vai tar decrepeat
        AirQuality working_aq = airQualityRepository.findByCityName(place);
        if (working_aq != null){
            if ( ChronoUnit.MINUTES.between(working_aq.getDate(), LocalDateTime.now()) < 50 ){
                return working_aq;
            }
        }

        Api01MainResponse response01 = externalCaller.getFromApiOne(place);
        Api02MainResponse response02 = null;

        List<Pollutant> pollutantList = new ArrayList<>();

        if (response01.getError() != null){
            System.out.println("Couldn't get data from API01. Cause: " + response01.getError().getDescription());
            response02 = externalCaller.getFromApiTwo(place);

            // TODO: converter pa upg3 https://www2.dmu.dk/AtmosphericEnvironment/Expost/database/docs/PPM_conversion.pdf
            if (response02.getError() != null){
                System.out.println("Couldn't get data from API02. Cause: " + response02.getError().getDetail());

                // If both API's requests have problems, send Null Object
                return new AirQualityNull();
            }

            // If API01 request doesn't work, but API02 does, use it
            for (Api02MainResponse.Api02Data.Api02Pollutants.Api02Pollutant p : response02.getAllPollutants()){
                pollutantList.add( new Pollutant(p.getDisplay_name().toLowerCase(), p.getFull_name().toLowerCase(), p.getConcentration().getValue() ) );
            }

            working_aq = new AirQuality(place, "portugal", LocalDateTime.parse(response02.getApi02Date().split("Z")[0] ) ,pollutantList);
            airQualityRepository.save(working_aq);
            return working_aq;
        }

        // If API01 request works, use it
        for (Api01MainResponse.Api01Response.Api01Periods.Api01Pollutant p: response01.getAllPollutants()){
            pollutantList.add( new Pollutant(p.getType(), p.getName(), p.getValueUGM3() ));
        }
        working_aq = new AirQuality(place, "portugal", LocalDateTime.parse(response01.getApi01Date().split("\\+")[0])  ,pollutantList);
        airQualityRepository.save(working_aq);
        return working_aq;
    };

    public List<Integer> getStats() {
        return airQualityRepository.getStats();
    }
}
