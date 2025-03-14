package com.example.t1Backend.api.technology.contracts;

import lombok.Getter;

@Getter
public class TechnologyDTO {
    private String name;
    private String category;
    private String ring;
    private String section;
    private String description;

    public TechnologyDTO(String name, String category, String ring, String section, String description) {
        this.name = name;
        this.category = category;
        this.ring = ring;
        this.section = section;
        this.description = description;
    }

    public TechnologyDTO() {
    }
}
