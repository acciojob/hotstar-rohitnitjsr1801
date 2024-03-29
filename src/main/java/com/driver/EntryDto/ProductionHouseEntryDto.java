package com.driver.EntryDto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ProductionHouseEntryDto {

    private String name;

    @JsonCreator
    public ProductionHouseEntryDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
