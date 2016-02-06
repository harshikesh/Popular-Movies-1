package com.example.harshikesh.nanodegree_02_spotifier.model;

public enum Language {

    LANGUAGE_EN("en"),
    LANGUAGE_Hi("hi");

    private final String value;

    private Language(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
