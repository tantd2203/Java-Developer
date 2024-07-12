package com.programming.uit.javadeveloper.dto.respone;

import org.springframework.http.HttpStatus;

public class ResponseError extends  ResponseData {
    public ResponseError(int status, String message) {
        super(status, message);
    }
}
