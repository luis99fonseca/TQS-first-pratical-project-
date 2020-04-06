//package tqs.ua.tqs01proj.repos;
//
////import org.junit.jupiter.api.Assertions;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import tqs.ua.tqs01proj.entities.AirQuality;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//class AirQualityRepositoryTest {
//
//    @Autowired
//    private AirQualityRepository airQualityRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//    public void selectAirQ_returnsAirQDetails(){
//        AirQuality saved_airQuality = entityManager.persist(new AirQuality("aveiro", "portugal"));
//        entityManager.flush();
//
//        AirQuality temp_airQuality = airQualityRepository.findByCity("aveiro");
//
//        Assertions.assertThat( temp_airQuality.getCity()).isEqualTo(saved_airQuality.getCity());
//
//    }
//
//
//}