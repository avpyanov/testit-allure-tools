package com.github.avpyanov.tools.allure;

import com.github.avpyanov.tools.testit.client.dto.AutotestResults;
import com.github.avpyanov.tools.testit.client.dto.AutotestResultsStep;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;

import java.util.List;

import static com.github.avpyanov.tools.allure.AllureStepUtils.convertStepResultsToTestItStepResults;
import static com.github.avpyanov.tools.allure.AllureUtils.*;
import static com.github.avpyanov.tools.allure.AttachmentsUtils.uploadAttachments;

public class AllureResultsUtils {

    public static AutotestResults getResultsFromAllure(TestResult testResult) {
        AutotestResults autotestResults = new AutotestResults();
        autotestResults.startedOn(convertTimestampToDate(testResult.getStart()))
                .completedOn(convertTimestampToDate(testResult.getStop()))
                .outcome(setTestStatus(testResult.getStatus().value()))
                .duration(getDuration(testResult.getStop(), testResult.getStart()));

        if (!testResult.getStatus().equals(Status.PASSED)) {
            autotestResults.message(testResult.getStatusDetails().getMessage())
                    .traces(testResult.getStatusDetails().getTrace());
        }

        if (!testResult.getAttachments().isEmpty()) {
            uploadAttachments(testResult.getAttachments());
        }
        List<AutotestResultsStep> autotestResultsSteps = convertStepResultsToTestItStepResults(testResult.getSteps());

        autotestResults.stepResults(autotestResultsSteps);
        return autotestResults;
    }

    private AllureResultsUtils() {
    }
}
