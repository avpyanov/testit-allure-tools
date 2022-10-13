package com.github.avpyanov.tools.testit.client.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class SectionPostDto {

    private String name;
    private UUID projectId;
    private UUID parentId;
    private List<WorkItemStep> preconditionSteps;
    private List<WorkItemStep> postconditionSteps;
}
