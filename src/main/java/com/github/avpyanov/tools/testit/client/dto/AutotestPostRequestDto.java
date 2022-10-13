package com.github.avpyanov.tools.testit.client.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AutotestPostRequestDto {

    private String externalId;
    private String projectId;
    private String name;
    private String namespace;
    private String classname;
    private String title;
    private String description;
    private List<AutotestStep> steps;
    private List<AutotestStep> setup;
    private List<AutotestStep> teardown;
    private List<Link> links;
    private List<UUID> workItemIdsForLinkWithAutoTest;
    private boolean isFlaky;
    private boolean shouldCreateWorkItem;
}