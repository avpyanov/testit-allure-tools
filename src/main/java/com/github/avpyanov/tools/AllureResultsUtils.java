package com.github.avpyanov.tools;

import com.github.avpyanov.testit.client.dto.AutotestResults;
import com.github.avpyanov.testit.client.dto.AutotestResultsStep;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;

import java.util.List;

import static com.github.avpyanov.tools.AllureStepUtils.convertStepResultsToTestItStepResults;
import static com.github.avpyanov.tools.AllureUtils.*;
import static com.github.avpyanov.tools.AttachmentsUtils.uploadAttachments;

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
