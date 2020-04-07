package tqs.ua.tqs01proj.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// TODO: por atributos privados :) ou tirar os Getters, pa ficar more POJO like? embora conflita pk precisamos de algumas funçoes especificas e preciamso
public class Api02MainResponse {

    public Api02Error error;
    public Api02Data data;

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
        return getData().getPollutants().getListPollutants();
    }

    public String getApi02Date(){
        return "";
    }

    public static class Api02Data {
        public String datetime;
        public Api02Pollutants pollutants;

        public String getDatetime() {
            return datetime;
        }

        public Api02Pollutants getPollutants() {
            return pollutants;
        }

        public static class Api02Pollutants {
            public Api02Pollutant co;
            public Api02Pollutant no2;
            public Api02Pollutant o3;
            public Api02Pollutant pm10;
            public Api02Pollutant so2;

            public List<Api02Pollutant> getListPollutants(){
                return new ArrayList<>(Arrays.asList(
                        co,
                        no2,
                        o3,
                        pm10,
                        so2
                ));
            }

            public static class Api02Pollutant {
                public String display_name;
                public String full_name;
                public Api02Concentration concentration;

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
                    public float value;
                    public String units;

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
