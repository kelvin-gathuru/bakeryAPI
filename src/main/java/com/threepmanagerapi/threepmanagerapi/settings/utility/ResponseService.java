package com.threepmanagerapi.threepmanagerapi.settings.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class ResponseService {

    public ResponseEntity formulateResponse(Object data, String message, HttpStatus httpStatus, MultiValueMap headers, boolean success) {
        return new ResponseEntity(new ResponseData(data, message, httpStatus,success), headers, httpStatus);

    }

}
