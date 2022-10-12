package com.github.avpyanov.tools;

import com.github.avpyanov.testit.client.dto.AutotestResults;
import com.github.avpyanov.testit.client.dto.AutotestResultsStep;
import io.qameta.allure.model.TestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.github.avpyanov.tools.AllureResultsUtils.getResultsFromAllure;
import static com.github.avpyanov.tools.AllureUtils.*;

public class TestRunUtils {

    private static final Logger logger = LogManager.getLogger(TestRunUtils.class);

    public static String getConfigurationId(String testRunId) {
        return AllureConfig.getTestItApiClient().testRunsApi().getTestRun(testRunId).getTestResults().get(0).getConfiguration().getId();
    }

    public static void uploadTestResults(String testRunId, List<AutotestResults> autotestResults) {
        AllureConfig.getTestItApiClient().testRunsApi().setAutoTestsResults(testRunId, autotestResults);
    }

    public static void uploadTestResultsFromAllureResultFiles(String testRunId) {
        File[] allureReportFiles = getAllureReportFiles();
        if (allureReportFiles == null) {
            logger.error("Не удалось получить файлы из директории {}", AllureConfig.getAllureFolder());
        } else {
            String configurationId = getConfigurationId(testRunId);
            List<String> testResults = getAllureResults(allureReportFiles);
            List<AutotestResults> autotestResultsList = new ArrayList<>();
            for (String testResult : testResults) {
                TestResult result = getResultsFromFile(testResult);
                if (result == null) {
                    logger.error("Не удалось получить результаты для {}", testResult);
                } else {
                    final String testCaseId = getTestId(result, "autotest");
                    if (testCaseId.isEmpty()) {
                        logger.error("Не указана аннотация @AutotestId для {}", result.getFullName());
                    } else {
                        AutotestResults resultsFromAllure = getResultsFromAllure(result);
                        resultsFromAllure.configurationId(configurationId);
                        String externalId = getClassName(result);
                        List<AutotestResultsStep> fixtures = TestFixtures.getFixtures(externalId);
                        resultsFromAllure.setupResults(fixtures);
                        resultsFromAllure.autoTestExternalId(externalId);
                        autotestResultsList.add(resultsFromAllure);
                    }
                }
            }
            try {
                logger.info("Загрузка результатов тест-рана {}", autotestResultsList);
                uploadTestResults(testRunId, autotestResultsList);
            } catch (Exception e) {
                logger.error("Не удалось загрузить результаты тест-рана {}", e.getMessage());
            }
        }
    }

    private TestRunUtils() {
    }
}
