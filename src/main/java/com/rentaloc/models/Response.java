package com.rentaloc.models;

public class Response {

    String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Response(String message) {
        this.message = message;
    }

    public Response() {
    }

    @Override
    public String toString() {
        return
               "{" + '\"' + "message" + '\"' + ": " + '\"' + message + '\"' + "}";
    }
}
