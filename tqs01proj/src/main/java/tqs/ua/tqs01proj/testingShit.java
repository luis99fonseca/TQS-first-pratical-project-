package tqs.ua.tqs01proj;


import java.time.LocalDateTime;

public class testingShit {
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

    }
}
