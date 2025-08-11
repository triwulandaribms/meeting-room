package com.java.mega_giga.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;


public record Response<T>(int code, String message, T data) {

    public static <T> ResponseEntity<Response<Map<String, Object>>> responseSuksesList(T data) {
        Map<String, Object> dataList = new HashMap<>();
        dataList.put("list", data);

        boolean isEmpty = (data == null) ||
                          (data instanceof java.util.Collection && ((java.util.Collection<?>) data).isEmpty()) ||
                          (data instanceof java.util.Map && ((java.util.Map<?, ?>) data).isEmpty());

        if (isEmpty) {
            return ResponseEntity.status(200)
                    .body(new Response<>(200, "Data tidak ditemukan", dataList));
        }

        return ResponseEntity.status(200)
                .body(new Response<>(200, "Sukses", dataList));
    }

    public static <T> ResponseEntity<Response<T>> responseCreateSukses(T data) {
        return ResponseEntity
                .status(201)
                .body(new Response<>(201, "sukses create", data));
    }

    public static <T> ResponseEntity<Response<T>> responseSukses(T data, String message) {
        return ResponseEntity
                .status(200)
                .body(new Response<>(200, message != null ? message : "sukses", data));
    }

    public static <T> ResponseEntity<Response<T>> responseBadRequest(String message) {
        return ResponseEntity
                .status(400)
                .body(new Response<>(400, message != null ? message : "Bad Request", null));
    }

    public static <T> ResponseEntity<Response<T>> responseError(T data, int status, String message) {
        return ResponseEntity
                .status(status)
                .body(new Response<>(status, message != null ? message : "Error", data));
    }

}
