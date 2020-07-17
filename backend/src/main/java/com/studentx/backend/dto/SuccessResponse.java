package com.studentx.backend.dto;

public class SuccessResponse {

    private Integer status = 200;
    private String message;

    public SuccessResponse(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
