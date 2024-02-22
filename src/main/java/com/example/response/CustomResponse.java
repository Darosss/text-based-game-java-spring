package com.example.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CustomResponse<T> {
    private final ResponseEntity<BodyResponse<T>> responseEntity;

    public CustomResponse( HttpStatus status, BodyResponse<T> bodyResponse) {
        this.responseEntity = ResponseEntity.status(status).body(bodyResponse);
    }

    public CustomResponse(HttpStatus status, String bodyMessage){
        this.responseEntity = ResponseEntity.status(status).body(new BodyResponse<>(bodyMessage));
    }
    public CustomResponse(HttpStatus status, T bodyData){
        this.responseEntity = ResponseEntity.status(status).body(new BodyResponse<>(bodyData));
    }

    public CustomResponse(HttpStatus status, String bodyMessage, T bodyData){
        this.responseEntity = ResponseEntity.status(status).body(new BodyResponse<>(bodyMessage, bodyData));
    }

    public ResponseEntity<BodyResponse<T>> getResponseEntity() {
        return responseEntity;
    }

}