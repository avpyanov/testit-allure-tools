package com.github.avpyanov.tools.allure;

import com.github.avpyanov.tools.Settings;
import com.github.avpyanov.tools.testit.client.dto.AutotestResults;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.TestResult;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.github.avpyanov.tools.allure.AllureResultsUtils.getResultsFromAllure;
import static com.github.avpyanov.tools.allure.TestRunUtils.getConfigurationId;

public class AllureTestLifecycleListener implements TestLifecycleListener {

    private static final Logger logger = LogManager.getLogger(AllureTestLifecycleListener.class);

    private final Settings settings = ConfigFactory.create(Settings.class);

    @Override
    public void afterTestStop(TestResult result) {
        if (settings.testRunId() != null) {
            final List<AutotestResults> autotestResultsList = new ArrayList<>();
            String configurationId = getConfigurationId(settings.testRunId());
            AutotestResults resultsFromAllure = getResultsFromAllure(result);
            resultsFromAllure.configurationId(configurationId);
            resultsFromAllure.autoTestExternalId(result.getFullName());
            autotestResultsList.add(resultsFromAllure);
            TestRunUtils.uploadTestResults(settings.testRunId(), autotestResultsList);
        } else {
            logger.warn("Не указана аннотация @AutotestId для {}", result.getFullName());
        }
    }
}