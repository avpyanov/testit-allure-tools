package com.github.avpyanov.tools.testit.client.dto;

import lombok.Data;

import java.util.List;

@Data
public class AutotestDto {

    private String id;
    private String globalId;
    private boolean isDeleted;
    private String externalId;
    private String lastTestResultOutcome;
    private List<Link> links;
    private String projectId;
    private String name;
    private String namespace;
    private String classname;
    private List<AutotestStep> steps;
    private List<AutotestStep> setup;
    private List<AutotestStep> teardown;
    private String title;
    private String description;
    private boolean isFlaky;
}