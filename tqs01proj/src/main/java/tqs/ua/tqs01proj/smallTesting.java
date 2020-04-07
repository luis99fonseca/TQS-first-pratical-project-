package tqs.ua.tqs01proj;


import tqs.ua.tqs01proj.entities.AirQuality;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class smallTesting {
    public static void main(String[] args) {
        System.out.println("ola");
        LocalDateTime d1 = LocalDateTime.parse("2019-04-28T22:32:38.536");
        LocalDateTime d2 = LocalDateTime.now();
        LocalDateTime d3 = LocalDateTime.now();
        System.out.println(d2 + "; " + d3);
        System.out.println(d1.isBefore(d2));
        System.out.println(d1.isAfter(d2));

        System.out.println(d3.isAfter(d3));
        System.out.println(d3.isBefore(d3));
        System.out.println(d3.isAfter(d2));
        System.out.println(d3.isBefore(d2));

        LocalDateTime d4 = LocalDateTime.parse("2019-04-29T22:32:38.536");
        long minutes = ChronoUnit.MINUTES.between(d1, d4);
        System.out.println(minutes);
        minutes = ChronoUnit.MINUTES.between(d4, d1);
        System.out.println(minutes);
        List<AirQuality> ola = new ArrayList<>();
        ola.add(new AirQuality("aq1", "portugal",  LocalDateTime.parse("2019-04-28T22:32:38.536"), Collections.emptyList() ));
        ola.add(new AirQuality("aq2", "portugal",  LocalDateTime.parse("2010-04-28T22:32:38.536"), Collections.emptyList() ));

        ola.remove(new AirQuality("aq1", "portugal",  LocalDateTime.parse("2019-04-28T22:32:38.536"), Collections.emptyList() ));
        System.out.println(ola);

        String instantatual = "2014-12-22T10:15:30Z";
        Clock clock = Clock.fixed(Instant.parse(instantatual), ZoneId.of("UTC"));
        LocalDateTime d6 = LocalDateTime.now().plusMinutes(-50);
        System.out.println(">> " + d6);
        System.out.println(">>> " + Instant.now(clock));

    }
}
