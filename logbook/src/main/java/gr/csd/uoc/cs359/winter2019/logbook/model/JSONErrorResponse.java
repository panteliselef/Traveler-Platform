package gr.csd.uoc.cs359.winter2019.logbook.model;

public class JSONErrorResponse {

    private String message;
    private int statusCode;

    public JSONErrorResponse(String message, int statusCode){
        this.message = message;
        this.statusCode = statusCode;
    }
}
