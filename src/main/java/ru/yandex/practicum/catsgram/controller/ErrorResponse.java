package ru.yandex.practicum.catsgram.controller;

import lombok.Getter;

@Getter
public class ErrorResponse {
    // геттеры необходимы, чтобы Spring Boot мог получить значения полей
    String error;

    public ErrorResponse(String error) {
        this.error = error;
    }
}