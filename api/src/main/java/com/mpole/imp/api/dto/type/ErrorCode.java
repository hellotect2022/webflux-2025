package com.mpole.imp.api.dto.type;

public enum ErrorCode {
    INVALID_REQUEST("Invalid request"),
    NOT_FOUND("Not found"),
    UNEXPECTED_ERROR("An unexpected error occurred"),
    FORBIDDEN("Forbidden"),
    UNAUTHORIZED("Unauthorized"),
    SOME_ERROR("Some error occurred"),
    ;
    private String description;
    ErrorCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
