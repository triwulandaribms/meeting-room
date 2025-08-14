package com.java.meeting_room.model.response;

public class JenisKonsumsiDto {
    private Integer id;
    private String name;
    private Integer maxPrice;

    public JenisKonsumsiDto(Integer id, String name, Integer maxPrice) {
        this.id = id;
        this.name = name;
        this.maxPrice = maxPrice;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }
}
