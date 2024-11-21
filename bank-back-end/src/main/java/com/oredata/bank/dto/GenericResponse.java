package com.oredata.bank.dto;

import org.springframework.http.ResponseEntity;

public class GenericResponse {
    private String message;
    private Object body;

    private Object headers;

    private int statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Object getHeaders() {
        return headers;
    }

    public void setHeaders(Object headers) {
        this.headers = headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public GenericResponse(String message, Object body, Object headers, int statusCode) {
        this.message = message;
        this.body = body;
        this.headers = headers;
        this.statusCode = statusCode;
    }
}
