package tqs.ua.tqs01proj.utils;

import java.util.List;

public class Api03MainResponse {

    private String documentation;
    private Api03Status status;
    private List<Api03Result> results;

    public String getDocumentation() {
        return documentation;
    }

    public Api03Status getStatus() {
        return status;
    }

    public void setStatus(Api03Status api03Status){
        this.status = api03Status;
    }

    public List<Api03Result> getResults() {
        return results;
    }

    public Api03MainResponse.Api03Result.Api03Geometry getCords(){
        return !this.results.isEmpty() ? this.results.get(0).getGeometry() : null;
    }


    public static class Api03Result {

        private Api03Geometry geometry;


        public Api03Geometry getGeometry() {
            return geometry;
        }

        public static class Api03Geometry {

            private String lat;
            private String lng;

            public String getLat() {
                return lat;
            }

            public String getLng() {
                return lng;
            }
        }
    }

    public static class Api03Status {
        private String code;
        private String message;

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
