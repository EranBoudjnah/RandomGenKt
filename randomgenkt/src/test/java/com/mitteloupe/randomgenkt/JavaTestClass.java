package com.mitteloupe.randomgenkt;

import java.util.List;

public class JavaTestClass {
    Byte byteField;

    Short shortField;

    Integer intField;

    Long longField;

    Float floatField;
    Double doubleField;

    Boolean booleanField;

    String stringField;

    Object objectField;

    List<Object> objects;

    public JavaTestClass(
            Byte byteField,
            Short shortField,
            Integer intField,
            Long longField,
            Float floatField,
            Double doubleField,
            Boolean booleanField,
            String stringField,
            Object objectField,
            List<Object> objects
    ) {
        this.byteField = byteField;
        this.shortField = shortField;
        this.intField = intField;
        this.longField = longField;
        this.floatField = floatField;
        this.doubleField = doubleField;
        this.booleanField = booleanField;
        this.stringField = stringField;
        this.objectField = objectField;
        this.objects = objects;
    }
}
