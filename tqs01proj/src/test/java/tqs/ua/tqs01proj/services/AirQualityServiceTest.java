package tqs.ua.tqs01proj.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.ua.tqs01proj.entities.AirQuality;
import tqs.ua.tqs01proj.entities.AirQualityNull;

//import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.spy;

import tqs.ua.tqs01proj.repos.AirQualityRepository;
import tqs.ua.tqs01proj.utils.Api01MainResponse;
import tqs.ua.tqs01proj.utils.Api02MainResponse;
import tqs.ua.tqs01proj.utils.ExternalCaller;

import java.time.LocalDateTime;
import java.util.Collections;

// https://stackoverflow.com/questions/40961057/how-to-use-mockito-with-junit5
@ExtendWith(MockitoExtension.class)
class AirQualityServiceTest {

    @Mock   // same as Mockito.mock()
    private AirQualityRepository airQualityRepository;

    // System Under Test
    @InjectMocks
    private AirQualityService surAirQualityService;

    @Mock
    private ExternalCaller externalCaller;

    @Test
    public void givenAQinRepository_whenAirQDetails_thenReturnsAirQInfo(){
        // TODO: check https://stackoverflow.com/questions/38567326/is-it-discouraged-to-use-spy-and-injectmocks-on-the-same-field
        //      dizer que é pelo principio do Single Responsibility Principle
        //      e que foi na confessao deste teste que se reparou nisso
//        AirQualityService spyAirQualityService = Mockito.spy(AirQualityService.class);
        given( airQualityRepository.findByCityName("viseu") ).
            willReturn(new AirQuality("viseu", "portugal", LocalDateTime.now(), Collections.emptyList() ));

//        Api01MainResponse.Api01Error temp_error01 = new Api01MainResponse.Api01Error();
//        Api01MainResponse temp_main01 = new Api01MainResponse();
//        temp_main01.setError(temp_error01);
//        given( externalCaller.getFromApiOne( anyString() )  )
//                .willReturn( temp_main01 );
//
//        Api02MainResponse.Api02Error temp_error02 = new Api02MainResponse.Api02Error();
//        Api02MainResponse temp_main02 = new Api02MainResponse();
//        temp_main02.setError(temp_error02);
//
//        given( externalCaller.getFromApiTwo( anyString() )  )
//                .willReturn( temp_main02 );

        AirQuality temp_airQuality = surAirQualityService.getAirQuality("viseu");
        Assertions.assertThat( temp_airQuality.getCity()).isEqualTo("viseu") ;
    }

    // TODO: ver se em vez de Exception, se é Null Object
    // TODO: also na pratica nao usa este objeto at all...
    @Test
    public void getAirQDetails_whenDoesntExist_returnsException(){
        String non_existing_city = "no_city";

        given( airQualityRepository.findByCityName(non_existing_city) ).
                willThrow(new AirQualityNull());

        Assertions.assertThatThrownBy( () -> { surAirQualityService.getAirQuality(non_existing_city); } )
                .isInstanceOf(AirQualityNull.class);
    }
}