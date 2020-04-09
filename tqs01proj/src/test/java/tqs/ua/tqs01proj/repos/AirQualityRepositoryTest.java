package tqs.ua.tqs01proj.repos;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import tqs.ua.tqs01proj.entities.AirQuality;
import java.time.LocalDateTime;
import java.util.Collections;

// TODO: diz que muitas alteraçoes teriam de ser feitas pa isto dar pa mais do k cities de portugal: comparaçao soa so
//  com city name e testes tbm por consequcnaia. Isto dar pa ver que muitas vezes somos negligentes e n pensamos como
//  escalar shit

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

        AirQuality temp_airQuality = airQualityRepository.findByCityName("aveiro");

        Assertions.assertThat( temp_airQuality.getCity()).isEqualTo(saved_airQuality.getCity());
    }

    @Test
    public void whenInvalidCity_thenReturnNull(){
        AirQuality no_existant = airQualityRepository.findByCityName("doesNotExist");
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

        AirQuality no_existant = airQualityRepository.findByCityName("aq2");
        Assertions.assertThat(no_existant).isNull();

        AirQuality from_rep = airQualityRepository.findByCityName("aq6");
        Assertions.assertThat(from_rep).isNotNull();

        from_rep = airQualityRepository.findByCityName("aq1");
        Assertions.assertThat(from_rep).isNotNull();
    }

    @Test
    public void whenDeletedByCity_findReturnsNull(){
        String city_name = "aq1";
        AirQuality aq1 = airQualityRepository.save(new AirQuality(city_name, "portugal",  LocalDateTime.parse("2019-04-28T22:32:38.536"), Collections.emptyList() ));
        airQualityRepository.removeByCityName(city_name);

        AirQuality removed_aq = airQualityRepository.findByCityName(city_name);
        Assertions.assertThat(removed_aq).isNull();
    }

    @Test
    public void givenRepositoryIsEmpty_returnsNull(){
        // Note: não se usa anyString() no teste itself
        AirQuality removed_aq = airQualityRepository.findByCityName( "anyString()" );
        Assertions.assertThat(removed_aq).isNull();
    }

    @Test
    public void whenAddSameNameCity_removeOlder(){
        airQualityRepository.save(new AirQuality("aq1", "portugal",  LocalDateTime.parse("2019-04-28T22:32:38.536"), Collections.emptyList() ));

        AirQuality from_rep = airQualityRepository.findByCityName("aq1");
        Assertions.assertThat(from_rep.getCountry()).isEqualTo("portugal");

        airQualityRepository.save(new AirQuality("aq1", "spain",  LocalDateTime.parse("2019-04-28T22:32:38.536"), Collections.emptyList() ));
        from_rep = airQualityRepository.findByCityName("aq1");
        Assertions.assertThat(from_rep.getCountry()).isEqualTo("spain");
    }

    @Test
    public void checkStatsList_whenOnlySavingNewAQ(){
        airQualityRepository.save(new AirQuality("aq1", "portugal",  LocalDateTime.parse("2019-04-28T22:32:38.536"), Collections.emptyList() ));
        airQualityRepository.save(new AirQuality("aq2", "portugal",  LocalDateTime.parse("2019-04-28T22:32:38.536"), Collections.emptyList() ));
        airQualityRepository.save(new AirQuality("aq3", "portugal",  LocalDateTime.parse("2019-04-28T22:32:38.536"), Collections.emptyList() ));

        Assertions.assertThat(airQualityRepository.getStats()).containsExactly(0, 0, 0);
    }

    @Test
    public void checkStatsList_whenSavingExistingAntiqueAQ(){
        // thus, checking falseHits
        airQualityRepository.save(new AirQuality("aq1", "portugal",  LocalDateTime.parse("2019-04-28T22:32:38.536"), Collections.emptyList() ));
        airQualityRepository.save(new AirQuality("aq2", "portugal",  LocalDateTime.parse("2019-04-28T22:32:38.536"), Collections.emptyList() ));
        airQualityRepository.save(new AirQuality("aq3", "portugal",  LocalDateTime.parse("2019-04-28T22:32:38.536"), Collections.emptyList() ));

        airQualityRepository.findByCityName("aq1");
        airQualityRepository.findByCityName("aq3");
        airQualityRepository.save(new AirQuality("aq1", "portugal",  LocalDateTime.parse("2019-04-28T22:32:38.536"), Collections.emptyList() ));
        airQualityRepository.save(new AirQuality("aq3", "portugal",  LocalDateTime.parse("2019-04-28T22:32:38.536"), Collections.emptyList() ));

        Assertions.assertThat(airQualityRepository.getStats()).containsExactly(0, 0, 2);
    }

    @Test
    public void checkStatsList_whenGettingNoExistingAQ(){
        // thus, checking misses
        airQualityRepository.findByCityName("no_city1");
        airQualityRepository.findByCityName("no_city2");
        airQualityRepository.findByCityName("no_city3");

        Assertions.assertThat(airQualityRepository.getStats()).containsExactly(0, 3, 0);
    }

    @Test
    public void checkStatsList_whenGettingExisting(){
        airQualityRepository.save(new AirQuality("aq1", "portugal",  LocalDateTime.parse("2019-04-28T22:32:38.536"), Collections.emptyList() ));
        airQualityRepository.findByCityName("aq1");

        Assertions.assertThat(airQualityRepository.getStats()).containsExactly(1,0,0);
    }
}