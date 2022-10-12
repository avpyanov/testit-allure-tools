package com.github.avpyanov.tools;


import io.qameta.allure.model.Link;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.TestResult;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AllureUtils {

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

    private AllureUtils() {
    }
}
