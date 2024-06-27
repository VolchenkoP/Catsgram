package ru.yandex.practicum.catsgram.controller;

public class ErrorResponse {
    private String error;
    private String description;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getError() {
        return error;
    }
}
