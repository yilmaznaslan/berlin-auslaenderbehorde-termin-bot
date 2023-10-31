package com.yilmaznaslan.formhandlers;

import java.util.Objects;

public class CountryOption {

    private final String value;
    private final String text;

    public CountryOption(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryOption that = (CountryOption) o;
        return Objects.equals(value, that.value) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, text);
    }

    @Override
    public String toString() {
        return "CountryOption{" +
                "value='" + value + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
