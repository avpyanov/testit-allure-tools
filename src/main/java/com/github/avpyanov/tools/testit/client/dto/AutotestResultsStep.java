package com.github.avpyanov.tools.testit.client.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AutotestResultsStep {
    private String title;
    private String description;
    private String startedOn;
    private String completedOn;
    private Long duration;
    private String outcome;
    private List<Attachment> attachments;
    private Map<String, String> parameters;
}