package com.github.avpyanov.tools;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"classpath:testit-allure.properties",
        "system:env"})
public interface Settings extends Config {

    String endpoint();

    String token();

    String testRunId();

    String testPlanId();

    String configurationId();

    String allureFolder();

    String allureResultsPattern();
}