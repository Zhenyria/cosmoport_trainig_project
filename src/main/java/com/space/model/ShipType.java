package com.space.model;

public enum ShipType {
    TRANSPORT("Transport"),
    MILITARY("Military"),
    MERCHANT("Merchant");

    private String fieldName;

    ShipType(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}