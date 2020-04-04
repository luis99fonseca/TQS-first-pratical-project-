package tqs.ua.tqs01proj.apis;

import java.util.List;

public class Api01Periodsd {

    private String dateTimeISO;
    private String dominant;
    private List<Api01Pollutantd> pollutants;

    public String getDateTimeISO() {
        return dateTimeISO;
    }

    public String getDominant() {
        return dominant;
    }

    public List<Api01Pollutantd> getPollutants() {
        return pollutants;
    }
}
