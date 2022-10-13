package com.github.avpyanov.tools.allure;

import com.github.avpyanov.tools.testit.client.dto.Attachment;
import com.github.avpyanov.tools.testit.client.dto.AutotestResultsStep;
import io.qameta.allure.model.FixtureResult;
import io.qameta.allure.model.StepResult;

import java.util.ArrayList;
import java.util.List;

import static com.github.avpyanov.tools.allure.AllureUtils.*;

public class AllureStepUtils {

    public static List<AutotestResultsStep> convertStepResultsToTestItStepResults(final List<StepResult> stepResults) {
        List<StepResult> flattenSteps = flattenSteps(stepResults);
        return getSteps(flattenSteps);
    }

    public static List<AutotestResultsStep> convertFixtureStepResultsToTestItStepResults(List<FixtureResult> fixtureResults) {
        List<StepResult> flattenSteps = flattenFixtureSteps((fixtureResults));
        return getSteps(flattenSteps);

    }

    private static List<AutotestResultsStep> getSteps(List<StepResult> steps) {
        List<AutotestResultsStep> autotestResultsSteps = new ArrayList<>();
        for (StepResult stepResult : steps) {
            AutotestResultsStep autotestResultsStep = new AutotestResultsStep();
            autotestResultsStep.setTitle(stepResult.getName());
            autotestResultsStep.setOutcome(setTestStatus(stepResult.getStatus().value()));
            autotestResultsStep.setStartedOn(convertTimestampToDate(stepResult.getStart()));
            autotestResultsStep.setCompletedOn(convertTimestampToDate(stepResult.getStop()));

            long duration = getDuration(stepResult.getStop(), stepResult.getStart());
            autotestResultsStep.setDuration(duration);

            if (!stepResult.getParameters().isEmpty()) {
                autotestResultsStep.setParameters(getParameters(stepResult.getParameters()));
            }

            if (!stepResult.getAttachments().isEmpty()) {
                List<Attachment> attachments = AttachmentsUtils.uploadAttachments(stepResult.getAttachments());
                autotestResultsStep.setAttachments(attachments);
            }
            autotestResultsSteps.add(autotestResultsStep);
        }
        return autotestResultsSteps;
    }


    private static List<StepResult> flattenSteps(final List<StepResult> steps) {
        final List<StepResult> flattenSteps = new ArrayList<>();
        for (StepResult step : steps) {
            if (step.getSteps().isEmpty()) {
                flattenSteps.add(step);
            } else {
                flattenSteps.add(step);
                flattenSteps.addAll(flattenSteps(step.getSteps()));
            }
        }
        return flattenSteps;
    }


    private static List<StepResult> flattenFixtureSteps(List<FixtureResult> fixtureSteps) {
        final List<StepResult> flattenStepsList = new ArrayList<>();
        for (FixtureResult fixtureStep : fixtureSteps) {
            List<StepResult> stetResults = fixtureStep.getSteps();
            flattenStepsList.addAll(flattenSteps(stetResults));
        }
        return flattenStepsList;
    }

    private AllureStepUtils() {
    }
}
