package com.github.avpyanov.tools.testit.client.enums;

public enum EntityType {

    TEST_CASE("TestCases");

    private final String entity;

    EntityType(final String entityType) {
        this.entity = entityType;
    }

    public String type() {
        return entity;
    }
}
