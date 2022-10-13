package com.github.avpyanov.tools.testit.client.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class WorkItem {

    private UUID id;
    private String globalId;
    private UUID projectId;
    private String entityTypeName;
    private String name;
    private List<Attachment> attachments;
    private List<WorkItemStep> steps;
    private List<WorkItemStep> postconditionSteps;
    private List<WorkItemStep> preconditionSteps;
    private List<WorkItemStep> sectionPreconditionSteps;
    private List<WorkItemStep> sectionPostconditionSteps;
    private UUID sectionId;
    private String description;
    private String state;
    private String priority;
    private Map<String, String> attributes;
    private List<Tag> tags;
    private List<Link> links;
    private Long duration;
    private List<AutotestDto> autoTests;
}