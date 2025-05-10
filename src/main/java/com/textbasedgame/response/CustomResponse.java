package com.textbasedgame.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CustomResponse<T> {
    private final ResponseEntity<BodyResponse<T>> response;

    public CustomResponse( HttpStatus status, BodyResponse<T> bodyResponse) {
        this.response = ResponseEntity.status(status).body(bodyResponse);
    }

    public CustomResponse(HttpStatus status, T bodyData){
        this.response = ResponseEntity.status(status).body(new BodyResponse<>(bodyData));
    }

    public CustomResponse(HttpStatus status, String bodyMessage, T bodyData){
        this.response = ResponseEntity.status(status).body(new BodyResponse<>(bodyMessage, bodyData));
    }

    public ResponseEntity<BodyResponse<T>> getResponse() {
        return response;
    }

}