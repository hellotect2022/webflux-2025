package com.mpole.imp.api.exception;

import com.mpole.imp.api.dto.type.ErrorCode;
import com.mpole.imp.api.dto.type.Status;

public class StatusOkException extends CustomException{
    public StatusOkException(String message) {
        super(message);
    }

    public StatusOkException(ErrorCode errorCode) {
        super(errorCode);
    }

    public StatusOkException(Status status, ErrorCode errorCode, String message) {
        super(status, errorCode, message);
    }
}
