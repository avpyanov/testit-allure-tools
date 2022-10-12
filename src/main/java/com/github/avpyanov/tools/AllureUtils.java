package com.github.avpyanov.tools;


import com.google.gson.Gson;
import io.qameta.allure.model.Label;
import io.qameta.allure.model.Link;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.TestResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AllureUtils {

    private static final Logger logger = LogManager.getLogger(AllureUtils.class);

    public static String getTestId(TestResult result, String type) {
        Link link = result.getLinks()
                .stream()
                .filter(l -> l.getType().equals(type))
                .findFirst()
                .orElse(null);

        if (link != null) {
            return link.getName();
        } else return "";
    }

    public static String getClassName(TestResult result) {
        Label label = result.getLabels()
                .stream()
                .filter(l -> l.getName().equals("testClass"))
                .findFirst()
                .orElse(null);

        if (label != null) {
            return label.getValue();
        } else return "";
    }

    public static Long getDuration(Long stopTime, Long startTime) {
        return TimeUnit.MILLISECONDS.convert(stopTime -
                startTime, TimeUnit.MILLISECONDS);
    }

    public static String convertTimestampToDate(Long timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(timestamp);
    }

    public static String setTestStatus(String reportStatus) {
        if (reportStatus.equals("broken")) {
            return "Failed";
        } else return StringUtils.capitalize(reportStatus);
    }

    public static Map<String, String> getParameters(List<Parameter> parameters) {
        return parameters.stream()
                .collect(Collectors.toMap(Parameter::getName, Parameter::getValue));
    }


    public static TestResult getResultsFromAllureFile(String allureResultsPattern, final String fileName) {
        final String filePath = String.format(allureResultsPattern, fileName);
        try {
            return new Gson().fromJson(new FileReader(filePath), TestResult.class);
        } catch (FileNotFoundException e) {
            logger.error("Не удалось прочитать результат из  файла {}: {}", fileName, e.getMessage());
        }
        return null;
    }

    public static File[] getAllureReportFiles() {
        return new File(AllureConfig.getAllureFolder()).listFiles();
    }

    public static List<String> getAllureResults(File[] files) {
        return Stream.of(files)
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .filter(name -> name.contains("result"))
                .collect(Collectors.toList());
    }

    public static TestResult getResultsFromFile(final String fileName) {
        final String filePath = String.format(AllureConfig.getAllureResultsPattern(), fileName);
        try {
            return new Gson().fromJson(new FileReader(filePath), TestResult.class);
        } catch (FileNotFoundException e) {
            logger.error("Ошибка при чтении из файла {}: {}", fileName, e);
        }
        return null;
    }

    private AllureUtils() {
    }
}
