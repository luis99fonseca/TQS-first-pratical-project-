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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    @Mock
    private Api01MainResponse api01MainResponse;

    @Mock
    private Api02MainResponse api02MainResponse;

    @Test
    public void givenAQinRepository_whenAirQDetails_thenReturnsAirQInfo(){
        // TODO: check https://stackoverflow.com/questions/38567326/is-it-discouraged-to-use-spy-and-injectmocks-on-the-same-field
        //      dizer que é pelo principio do Single Responsibility Principle
        //      e que foi na confessao deste teste que se reparou nisso
//        AirQualityService spyAirQualityService = Mockito.spy(AirQualityService.class);
        given( airQualityRepository.findByCityName("viseu") ).
            willReturn(new AirQuality("viseu", "portugal", LocalDateTime.now(), Collections.emptyList() ));

        AirQuality temp_airQuality = surAirQualityService.getAirQuality("viseu");
        Assertions.assertThat( temp_airQuality.getCity()).isEqualTo("viseu") ;
    }

    @Test
    public void givenAQinApi01_whenAirQDetails_thenReturnsAirQInfo(){
        given( airQualityRepository.findByCityName("viseu") ).
                willReturn( null );

        api01MainResponse.setError(null);
        given( externalCaller.getFromApiOne( anyString() )  )
                .willReturn( api01MainResponse );

        given( api01MainResponse.getApi01Date())
                .willReturn("2020-04-07T02:00:00+01:00");


        AirQuality temp_airQuality = surAirQualityService.getAirQuality("viseu");
        Assertions.assertThat( temp_airQuality.getCity()).isEqualTo("viseu") ;
        // TODO: isto faz aprse automatico, dizer que p JUnit aparently trata disso
        Assertions.assertThat( temp_airQuality.getDate()).isEqualTo("2020-04-07T02:00:00");
    }

    @Test
    public void givenAQinApi02_whenAirQDetails_thenReturnsAirQInfo(){
        given( airQualityRepository.findByCityName("viseu") ).
                willReturn( null );

        Api01MainResponse.Api01Error temp_error01 = new Api01MainResponse.Api01Error();
        Api01MainResponse temp_main01 = new Api01MainResponse();
        temp_main01.setError(temp_error01);
        given( externalCaller.getFromApiOne( anyString() )  )
                .willReturn( temp_main01 );

        api02MainResponse.setError(null);

        given( externalCaller.getFromApiTwo( anyString() )  )
                .willReturn( api02MainResponse );

        given( api02MainResponse.getApi02Date())
                .willReturn("2020-04-04T14:00:00Z");

        AirQuality temp_airQuality = surAirQualityService.getAirQuality("viseu");
        Assertions.assertThat( temp_airQuality.getCity()).isEqualTo("viseu") ;
        Assertions.assertThat( temp_airQuality.getDate() ).isEqualTo("2020-04-04T14:00:00");
    }

    @Test
    public void givenAQnotIn_whenAirQDetails_thenReturnsNullObject(){
        given( airQualityRepository.findByCityName("viseu") ).
                willReturn( null );

        Api01MainResponse.Api01Error temp_error01 = new Api01MainResponse.Api01Error();
        Api01MainResponse temp_main01 = new Api01MainResponse();
        temp_main01.setError(temp_error01);
        given( externalCaller.getFromApiOne( anyString() )  )
                .willReturn( temp_main01 );

        Api02MainResponse.Api02Error temp_error02 = new Api02MainResponse.Api02Error();
        Api02MainResponse temp_main02 = new Api02MainResponse();
        temp_main02.setError(temp_error02);
        given( externalCaller.getFromApiTwo( anyString() )  )
                .willReturn( temp_main02 );

        AirQuality temp_airQuality = surAirQualityService.getAirQuality("viseu");
        Assertions.assertThat( temp_airQuality.getCity()).isEqualTo("unavailable") ;
    }

    // TODO: Dizer que se tentou dar override no system time mas not worth it. Embora s e sinta que testes como este
    //  nao sejam otimos .now() assim aqui, neste contexto serve para dar o timetravvel desejado
    //  e que fiz o que faria e a cache tivesse TTL no fim...
    //https://www.baeldung.com/java-override-system-time
    @Test
    public void givenAQInRepository_butOutdated_thenRequestNew(){
        AirQuality outdate_aq = new AirQuality("viseu", "portugal",
                LocalDateTime.now().plusMinutes(-50), Collections.emptyList());

        given( airQualityRepository.findByCityName("viseu") ).willReturn(
                outdate_aq
        );

        api01MainResponse.setError(null);
        given( externalCaller.getFromApiOne( anyString() )  )
                .willReturn( api01MainResponse );

        given( api01MainResponse.getApi01Date())
                .willReturn("2020-04-07T02:00:00+01:00");

        AirQuality temp_airQuality = surAirQualityService.getAirQuality("viseu");
        Assertions.assertThat( temp_airQuality.getDate()).isEqualTo("2020-04-07T02:00:00");


    }


    // TODO: ver se em vez de Exception, se é Null Object
    // TODO: also na pratica nao usa este objeto at all... acho que em cima ja faço isso
    //
    @Test
    public void getAirQDetails_whenDoesntExist_returnsException(){
        String non_existing_city = "no_city";

        given( airQualityRepository.findByCityName(non_existing_city) ).
                willThrow(new AirQualityNull());

        Assertions.assertThatThrownBy( () -> { surAirQualityService.getAirQuality(non_existing_city); } )
                .isInstanceOf(AirQualityNull.class);
    }

    @Test
    public void getStats_returnsStats(){
        List<Integer> integerList = new ArrayList<>(Arrays.asList(1,2,3));
        given (airQualityRepository.getStats()).willReturn(
                integerList
        );

        Assertions.assertThat(airQualityRepository.getStats()).containsExactly(1,2,3);
    }
}