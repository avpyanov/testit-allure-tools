package com.github.avpyanov.tools;

import com.github.avpyanov.testit.client.TestItApiClient;

public class AllureConfig {

    private static TestItApiClient testItApiClient;
    private static String allureFolder;
    private static String allureResultsPattern;

    public static TestItApiClient getTestItApiClient() {
        return testItApiClient;
    }

    public static void setTestItApiClient(TestItApiClient testItApiClient) {
        AllureConfig.testItApiClient = testItApiClient;
    }

    public static String getAllureFolder() {
        return allureFolder;
    }

    public static void setAllureFolder(String allureFolder) {
        AllureConfig.allureFolder = allureFolder;
    }

    public static String getAllureResultsPattern() {
        return allureResultsPattern;
    }

    public static void setAllureResultsPattern(String allureResultsPattern) {
        AllureConfig.allureResultsPattern = allureResultsPattern;
    }

    private AllureConfig() {
    }
}
