package com.github.avpyanov.tools.allure;

import com.github.avpyanov.tools.Settings;
import com.github.avpyanov.tools.testit.client.TestItApiClient;
import com.github.avpyanov.tools.testit.client.dto.AutotestResults;
import com.github.avpyanov.tools.testit.client.dto.TestResult;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.github.avpyanov.tools.allure.AllureResultsUtils.getResultsFromAllure;
import static com.github.avpyanov.tools.allure.AllureUtils.*;

public class TestRunUtils {

    private static final Logger logger = LogManager.getLogger(TestRunUtils.class);
    private static final Settings settings = ConfigFactory.create(Settings.class);
    private static final TestItApiClient testItClient = new TestItApiClient(settings.endpoint(), settings.token());

    public static String getConfigurationId(String testRunId) {
        return testItClient.testRunsApi().getTestRun(testRunId).getTestResults().get(0).getConfiguration().getId();
    }

    public static void uploadTestResults(String testRunId, List<AutotestResults> autotestResults) {
        try {
            logger.info("Загрузка результатов тест-рана {}", autotestResults);
            testItClient.testRunsApi().setAutoTestsResults(testRunId, autotestResults);
        } catch (RuntimeException e) {
            logger.error("Не удалось загрузить результаты тест-рана: {}", e.getMessage());
        }
    }

    public static void uploadTestResultsFromAllureResultFiles(String testRunId) {
        File[] allureReportFiles = getAllureReportFiles();
        if (allureReportFiles == null) {
            String allureFolder = settings.allureFolder();
            logger.error("Не удалось получить файлы из директории {}", allureFolder);
        } else {
            String configurationId = getConfigurationId(testRunId);
            List<String> testResults = getAllureResults(allureReportFiles);
            List<AutotestResults> autotestResultsList = new ArrayList<>();
            for (String testResult : testResults) {
                io.qameta.allure.model.TestResult result = getResultsFromFile(testResult);
                if (result == null) {
                    logger.error("Не удалось получить результаты для {}", testResult);
                } else {
                    final String testCaseId = getTestId(result, "autotest");
                    if (testCaseId.isEmpty()) {
                        logger.error("Не указана аннотация @AutotestId для {}", result.getFullName());
                    } else {
                        AutotestResults resultsFromAllure = getResultsFromAllure(result);
                        resultsFromAllure.configurationId(configurationId);
                        resultsFromAllure.autoTestExternalId(result.getFullName());
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

    public static List<String> getAutotestIdsFromTestRun(String testRunId) {
        final List<String> testIds = new ArrayList<>();
        logger.info("Получение тестов для testRunId:{}", testRunId);
        try {

            List<TestResult> testResults = testItClient.testRunsApi()
                    .getTestRun(testRunId)
                    .getTestResults();
            for (TestResult result : testResults) {
                testIds.add(result.getAutoTest().getGlobalId());
            }
            logger.info("Тесты для запуска: {}", testIds);
        } catch (Exception e) {
            logger.error("Ошибка при получении списка тестов {}", e.getMessage());
        }
        return testIds;
    }

    private TestRunUtils() {
    }
}