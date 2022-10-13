package com.github.avpyanov.tools.testit.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Link {

    private String title;
    private String url;
    private String description;
    private String type;
    private Boolean hasInfo;
}