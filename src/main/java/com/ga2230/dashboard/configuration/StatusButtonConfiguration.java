package com.ga2230.dashboard.configuration;

public class StatusButtonConfiguration {

    private String text;
    private String stateFunction;
    private String clickFunction;

    public StatusButtonConfiguration(String text, String stateFunction, String clickFunction) {
        this.text = text;
        this.stateFunction = stateFunction;
        this.clickFunction = clickFunction;
    }

    public String getText() {
        return text;
    }

    public String getStateFunction() {
        return stateFunction;
    }

    public String getClickFunction() {
        return clickFunction;
    }
}
