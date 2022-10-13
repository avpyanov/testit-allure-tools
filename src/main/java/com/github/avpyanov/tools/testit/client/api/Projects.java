package com.github.avpyanov.tools.testit.client.api;

import com.github.avpyanov.tools.testit.client.dto.Attribute;
import com.github.avpyanov.tools.testit.client.dto.Section;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface Projects {

    @RequestLine("GET /api/v2/projects/{projectId}/attributes")
    List<Attribute> getAttributes(@Param("projectId") String projectId);

    @Headers("Content-Type: application/json")
    @RequestLine("POST /api/v2/projects/{projectId}/attributes")
    Attribute createAttribute(@Param("projectId") String projectId, Attribute attribute);

    @Headers("Content-Type: application/json")
    @RequestLine("GET /api/v2/projects/{projectId}/sections?SearchField={fieldName}&SearchValue={value}")
    List<Section> findSection(@Param("projectId") String projectId,
                              @Param("fieldName") String fieldName,
                              @Param("value") String value);
}