package tqs.ua.tqs01proj.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Api02MainResponse {

    private Api02Error error;
    private Api02Data data;

    public Api02Error getError() {
        return error;
    }

    public void setError( Api02Error api02Error){
        this.error = api02Error;
    }

    public Api02Data getData() {
        return data;
    }

    public List<Api02Data.Api02Pollutants.Api02Pollutant> getAllPollutants(){
        return this.getData().getPollutants().getListPollutants();
    }

    public String getApi02Date(){
        return this.getData().datetime;
    }

    public static class Api02Data {
        private String datetime;
        private Api02Pollutants pollutants;

        public String getDatetime() {
            return datetime;
        }

        public Api02Pollutants getPollutants() {
            return this.pollutants;
        }

        public static class Api02Pollutants {
            // must be public to work
            public Api02Pollutant co;
            public Api02Pollutant no2;
            public Api02Pollutant o3;
            public Api02Pollutant pm10;
            public Api02Pollutant so2;

            public List<Api02Pollutant> getListPollutants(){
                return new ArrayList<>(Arrays.asList(
                        this.co,
                        this.no2,
                        this.o3,
                        this.pm10,
                        this.so2
                ));
            }

            public static class Api02Pollutant {
                private String display_name;
                private String full_name;
                private Api02Concentration concentration;

                public String getDisplay_name() {
                    return display_name;
                }

                public String getFull_name() {
                    return full_name;
                }

                public Api02Concentration getConcentration() {
                    return concentration;
                }

                public static class Api02Concentration {
                    private float value;
                    private String units;

                    public float getValue() {
                        return value;
                    }

                    public String getUnits() {
                        return units;
                    }
                }
            }
        }
    }

    public static class Api02Error {
        private String code;
        private String title;
        private String detail;

        public String getCode() {
            return code;
        }

        public String getTitle() {
            return title;
        }

        public String getDetail() {
            return detail;
        }
    }

}
