package com.github.avpyanov.tools.testit.client.api;

import com.github.avpyanov.tools.testit.client.dto.AutotestResults;
import com.github.avpyanov.tools.testit.client.dto.TestRun;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface TestRuns {

    @Headers("Content-Type: application/json")
    @RequestLine("POST /api/v2/testRuns/{testRunId}/testResults")
    void setAutoTestsResults(@Param("testRunId") String testRunId, List<AutotestResults> autoTestsResults);

    @Headers("Content-Type: application/json")
    @RequestLine("GET /api/v2/testRuns/{testRunId}")
    TestRun getTestRun(@Param(value = "testRunId") String testRunId);
}
