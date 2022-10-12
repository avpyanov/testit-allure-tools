package com.github.avpyanov.tools;

import com.github.avpyanov.testit.client.dto.AutotestResults;

import java.util.List;

public class TestRunUtils {

    public static String getConfigurationId(String testRunId){
        return AllureConfig.getTestItApiClient().testRunsApi().getTestRun(testRunId).getTestResults().get(0).getConfiguration().getId();
    }
    public static void uploadTestResults(String testRunId, List<AutotestResults> autotestResults){
        AllureConfig.getTestItApiClient().testRunsApi().setAutoTestsResults(testRunId,autotestResults);
    }

    private TestRunUtils() {
    }
}
