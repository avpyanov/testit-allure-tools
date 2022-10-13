package com.github.avpyanov.tools.testit.client.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class IdDto {

    private UUID id;

    public IdDto(UUID id) {
        this.id = id;
    }
}