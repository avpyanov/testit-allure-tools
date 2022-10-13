package com.github.avpyanov.tools.testit.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Section {

    private UUID id;
    private String name;
    private UUID projectId;
    private UUID parentId;
}