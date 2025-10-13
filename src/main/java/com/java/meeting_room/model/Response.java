package com.java.meeting_room.model;

import java.util.HashMap;
import java.util.Map;

public record Response<T>(String code, String message, T data) {

    public static <T> Response<T> create(String serviceCode, String responseCode, String message, T data) {
        return new Response<>(serviceCode + responseCode, message, data);
    }

    public static Response<Object> unauthenticated() {
        return new Response<>("0101", "Unauthenticated", null);
    }

    public static Response<Object> unauthorized() {
        return new Response<>("0201", "Unauthorized", null);
    }

    public static Response<Object> badRequest() {
        return new Response<>("0301", "Bad Request", null);
    }

    public static <T> Response<Map<String, Object>> suksesList(T data) {
        Map<String, Object> dataList = new HashMap<>();
        dataList.put("list", data);

        boolean isEmpty = (data == null) ||
                          (data instanceof java.util.Collection && ((java.util.Collection<?>) data).isEmpty()) ||
                          (data instanceof java.util.Map && ((java.util.Map<?, ?>) data).isEmpty());

        if (isEmpty) {
            return new Response<>("0404", "Data tidak ditemukan", dataList);
        }

        return new Response<>("0000", "Sukses", dataList);
    }

    public static <T> Response<T> createSuccess(T data) {
        return new Response<>("0001", "Sukses create", data);
    }

    public static <T> Response<T> ok(T data) {
        return new Response<>("0000", "Sukses", data);
    }

    public static <T> Response<T> error(String serviceCode, String responseCode, String message, T data) {
        return new Response<>(serviceCode + responseCode, message != null ? message : "Error", data);
    }
}
