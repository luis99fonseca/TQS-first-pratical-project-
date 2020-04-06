//package tqs.ua.tqs01proj.services;
//
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import tqs.ua.tqs01proj.entities.AirQuality;
//import tqs.ua.tqs01proj.entities.AirQualityNull;
//import tqs.ua.tqs01proj.repos.AirQualityRepository;
//
////import static org.junit.jupiter.api.Assertions.*;
//
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.willReturn;
//import org.assertj.core.api.Assertions;
//@ExtendWith(MockitoExtension.class)
//class AirQualityServiceTest {
//
//    @Mock   // same as Mockito.mock()
//    private AirQualityRepository airQualityRepository;
//
//    // System Under Test
//    @InjectMocks
//    private AirQualityService surAirQualityService;
//
//    @Test
//    public void getAirQDetails_thenReturnsAirQInfo(){
//        given( airQualityRepository.findByCity("viseu") ).
//            willReturn(new AirQuality("viseu", "portugal"));
//        AirQuality temp_airQuality = surAirQualityService.getAirQuality("viseu");
//        Assertions.assertThat( temp_airQuality.getCity()).isEqualTo("viseu") ;
//    }
//
//    // TODO: ver se em vez de Exception, se é Null Object
//    // TODO: also na pratica nao usa este objeto at all...
//    @Test
//    public void getAirQDetails_whenDoesntExist_returnsException(){
//        String non_existing_city = "no_city";
//
//        given( airQualityRepository.findByCity(non_existing_city) ).
//                willThrow(new AirQualityNull());
//
//        Assertions.assertThatThrownBy( () -> { surAirQualityService.getAirQuality(non_existing_city); } )
//                .isInstanceOf(AirQualityNull.class);
//    }
//}