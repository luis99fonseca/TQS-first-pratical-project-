package tqs.ua.tqs01proj.apis;

import java.util.List;

public class Api01MainResponse {

    private String success;
    private String error;
    private List<Api01Response> response;

    public String getSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public List<Api01Response> getResponse() {
        return response;
    }

    public static class Api01Response {

        private String id;
        private List<Api01Periods> periods;

        public String getId() {
            return id;
        }

        public List<Api01Periods> getPeriods() {
            return periods;
        }

        public static class Api01Periods {

            private String dateTimeISO;
            private String dominant;
            private List<Api01Pollutant> pollutants;

            public String getDateTimeISO() {
                return dateTimeISO;
            }

            public String getDominant() {
                return dominant;
            }

            public List<Api01Pollutant> getPollutants() {
                return pollutants;
            }

            public static class Api01Pollutant {

                private String type;
                private String name;
                private float valueUGM3;
                private String category;

                public String getType() {
                    return type;
                }

                public String getName() {
                    return name;
                }

                public float getValueUGM3() {
                    return valueUGM3;
                }

                public String getCategory() {
                    return category;
                }
            }
        }
    }
}
