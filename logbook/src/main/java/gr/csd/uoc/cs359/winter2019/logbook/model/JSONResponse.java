package gr.csd.uoc.cs359.winter2019.logbook.model;

public class JSONResponse {

    private String message;
    private int statusCode;
    private Object result;

    public JSONResponse(String message, int statusCode, Object result){
        this.message = message;
        this.statusCode = statusCode;
        this.result = result;
    }
}
