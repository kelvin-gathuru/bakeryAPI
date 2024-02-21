package com.threepmanagerapi.threepmanagerapi.settings.utility;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@Setter
public class ResponseData {
    private Object data;
    private String message;
    private int httpStatusCode;
    private boolean success;

    public ResponseData(Object data, String message, HttpStatus httpStatus, Boolean success) {
        this.data = data;
        this.message = message;
        this.httpStatusCode = httpStatus.value();
        this.success = success;
    }
}

