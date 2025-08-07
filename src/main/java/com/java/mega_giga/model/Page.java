package com.java.mega_giga.model;

import java.util.List;

public record Page<T>(long totalData, long totalPage, int page, int saze, List<T> data) {
}