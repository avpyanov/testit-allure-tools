package com.github.avpyanov.tools.testit.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Attribute {

    private String id;
    private String name;
    private String type;
    private boolean enabled;
    private boolean required;
    private boolean isGlobal;
}