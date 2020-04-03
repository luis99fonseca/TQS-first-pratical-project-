package tqs.ua.tqs01proj.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.ua.tqs01proj.entities.AirQuality;

// https://howtodoinjava.com/spring-boot2/h2-database-example/
@Repository
public interface AirQualityRepository extends JpaRepository<AirQuality, String> {

    public AirQuality findByCity(String name);
}
