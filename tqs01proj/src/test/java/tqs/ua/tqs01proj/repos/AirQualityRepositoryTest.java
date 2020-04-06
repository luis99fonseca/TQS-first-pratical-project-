package tqs.ua.tqs01proj.repos;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import tqs.ua.tqs01proj.entities.AirQuality;
import java.time.LocalDateTime;
import java.util.Collections;


// https://reflectoring.io/unit-testing-spring-boot/#dont-use-spring-in-unit-tests
class AirQualityRepositoryTest {

    private AirQualityRepository airQualityRepository;

    @BeforeEach
    public void setUp(){
        this.airQualityRepository = new AirQualityRepository();
    }

    @Test
    public void whenFindByCity_thenReturnAQuality(){
        AirQuality saved_airQuality = airQualityRepository.save(new AirQuality("aveiro", "portugal", LocalDateTime.now(), Collections.emptyList() ));

        AirQuality temp_airQuality = airQualityRepository.findByCity("aveiro");

        Assertions.assertThat( temp_airQuality.getCity()).isEqualTo(saved_airQuality.getCity());
    }

    @Test
    public void whenInvalidCity_thenReturnNull(){
        AirQuality no_existant = airQualityRepository.findByCity("doesNotExist");
        Assertions.assertThat(no_existant).isNull();
    }

    @Test
    public void whenSavingAQuality_ifFull_removeOldest(){
        airQualityRepository.save(new AirQuality("aq1", "portugal",  LocalDateTime.parse("2019-04-28T22:32:38.536"), Collections.emptyList() ));
        airQualityRepository.save(new AirQuality("aq2", "portugal",  LocalDateTime.parse("2010-04-28T22:32:38.536"), Collections.emptyList() ));
        airQualityRepository.save(new AirQuality("aq3", "portugal",  LocalDateTime.parse("2018-04-28T22:32:38.536"), Collections.emptyList() ));
        airQualityRepository.save(new AirQuality("aq4", "portugal",  LocalDateTime.parse("2011-04-28T22:32:38.536"), Collections.emptyList() ));
        airQualityRepository.save(new AirQuality("aq5", "portugal",  LocalDateTime.parse("2015-04-28T22:32:38.536"), Collections.emptyList() ));
        airQualityRepository.save(new AirQuality("aq6", "portugal",  LocalDateTime.parse("2015-04-28T22:32:38.536"), Collections.emptyList() ));

        AirQuality no_existant = airQualityRepository.findByCity("aq2");
        Assertions.assertThat(no_existant).isNull();

        AirQuality from_rep = airQualityRepository.findByCity("aq6");
        Assertions.assertThat(from_rep).isNotNull();

        from_rep = airQualityRepository.findByCity("aq1");
        Assertions.assertThat(from_rep).isNotNull();
    }

    @Test
    public void whenDeletedByCity_findReturnsNull(){
        String city_name = "aq1";
        AirQuality aq1 = airQualityRepository.save(new AirQuality(city_name, "portugal",  LocalDateTime.parse("2019-04-28T22:32:38.536"), Collections.emptyList() ));
        airQualityRepository.removeByCityName(city_name);

        AirQuality removed_aq = airQualityRepository.findByCity(city_name);
        Assertions.assertThat(removed_aq).isNull();
    }

    @Test
    public void givenRepositoryIsEmpty_returnsNull(){
        AirQuality removed_aq = airQualityRepository.findByCity( anyString() );
        Assertions.assertThat(removed_aq).isNull();
    }



}