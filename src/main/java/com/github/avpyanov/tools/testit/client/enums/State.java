package com.github.avpyanov.tools.testit.client.enums;

public enum State {

    NEEDS_WORK("NeedsWork");

    private final String value;

    State(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
