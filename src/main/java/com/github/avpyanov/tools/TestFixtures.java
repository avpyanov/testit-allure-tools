package com.github.avpyanov.tools;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.avpyanov.testit.client.dto.AutotestResultsStep;
import io.qameta.allure.model.TestResultContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.avpyanov.tools.AllureStepUtils.convertFixtureStepResultsToTestItStepResults;

public class TestFixtures {

    private static final Logger logger = LogManager.getLogger(TestFixtures.class);

    private static final List<String> PROCESSED_FILES = new CopyOnWriteArrayList<>();
    private static final Map<String, List<AutotestResultsStep>> classFixtures = new ConcurrentHashMap<>();
    private static final JsonMapper jsonMapper = JsonMapper.builder()
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
            .build();

    public static List<AutotestResultsStep> getFixtures(final String className) {

        List<AutotestResultsStep> steps = new ArrayList<>();
        List<String> containerFilesList = getContainerFilesList(AllureConfig.getAllureFolder());

        if (!containerFilesList.isEmpty()) {
            if (classFixtures.get(className) != null) {
                steps.addAll(classFixtures.get(className));
                steps.addAll(getMethodFixtures(containerFilesList, className));
            } else {
                steps.addAll(getClassAndMethodFixtures(containerFilesList, className));
            }
            List<AutotestResultsStep> methodFixtures = getMethodFixtures(containerFilesList, className);
            steps.addAll(methodFixtures);
        }
        return steps;
    }

    private static List<AutotestResultsStep> getClassAndMethodFixtures(final List<String> containerFilesList,
                                                                       final String className) {
        List<AutotestResultsStep> steps = new ArrayList<>();
        for (String container : containerFilesList) {
            String containerPath = String.format(AllureConfig.getAllureResultsPattern(), container);
            try {
                TestResultContainer testContainer = jsonMapper.readValue(
                        Paths.get(containerPath).toFile(), TestResultContainer.class);
                if (isClassContainer(testContainer.getName(), className)) {
                    List<AutotestResultsStep> classFixturesSteps = getSteps(testContainer, container);
                    classFixtures.putIfAbsent(className, classFixturesSteps);
                    steps.addAll(classFixturesSteps);
                } else if (isMethodContainer(testContainer.getName(), className)) {
                    List<AutotestResultsStep> methodFixtureSteps = getSteps(testContainer, container);
                    steps.addAll(methodFixtureSteps);
                }
            } catch (IOException e) {
                logger.error("Ошибка при чтении из файла-контейнера {}: {}", container, e);
            }
        }
        return steps;
    }

    private static List<AutotestResultsStep> getMethodFixtures(final List<String> containerFilesList,
                                                               final String className) {
        List<AutotestResultsStep> steps = new ArrayList<>();
        for (String containerFile : containerFilesList) {
            String containerPath = String.format(AllureConfig.getAllureResultsPattern(), containerFile);
            try {
                TestResultContainer testContainer = jsonMapper.readValue(
                        Paths.get(containerPath).toFile(), TestResultContainer.class);
                if (isMethodContainer(testContainer.getName(), className)) {
                    List<AutotestResultsStep> methodFixtureSteps = getSteps(testContainer, containerFile);
                    steps.addAll(methodFixtureSteps);
                }
            } catch (IOException e) {
                logger.error("Ошибка при чтении из файла-контейнера {}: {}", containerFile, e);
            }
        }
        return steps;
    }


    private static List<AutotestResultsStep> getSteps(final TestResultContainer container,
                                                      final String containerFileName) {
        List<AutotestResultsStep> autotestResultsSteps =
                convertFixtureStepResultsToTestItStepResults(container.getBefores());
        PROCESSED_FILES.add(containerFileName);
        return autotestResultsSteps;
    }


    private static boolean isClassContainer(String containerName, String className) {
        return containerName.equals(className);
    }

    private static boolean isMethodContainer(String containerName, String className) {
        return !containerName.equals(className) && containerName.contains(className);
    }


    private static List<String> getContainerFilesList(String allureDirectory) {
        try (Stream<Path> stream = Files.list(Paths.get(allureDirectory))) {
            List<String> collect = stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(s -> s.contains("-container.json"))
                    .collect(Collectors.toList());
            collect.removeAll(PROCESSED_FILES);
            return collect;
        } catch (IOException e) {
            logger.error("Ошибка при чтении из директории {}: {}", allureDirectory, e);
        }
        return new ArrayList<>();
    }

    private TestFixtures() {
    }
}