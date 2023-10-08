package com.yilmaznaslan.enums;

public enum Section3FormElements {

    TIME_SLOT("xi-sel-3", "dd_zeiten");

    private final String id;
    private final String name;

    Section3FormElements(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
