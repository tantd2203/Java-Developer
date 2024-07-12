package com.programming.uit.javadeveloper.dto.respone;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payload {
    private final int status;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    public Payload(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public Payload(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
