package com.yilmaznaslan.notification;

import java.util.Objects;

public class SlackPostTO {

    private final String text;

    public SlackPostTO(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlackPostTO that = (SlackPostTO) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

    @Override
    public String toString() {
        return "SlackPostTO{" +
                "text='" + text + '\'' +
                '}';
    }
}
