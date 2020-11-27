package fhdo;

import java.util.HashMap;

public class KaviatAxis {

    private int id;
    private String name;
    private String value;
    private HashMap displayValues;
    private int min;
    private int max;
    private int step;


    public KaviatAxis() {
    }

    public KaviatAxis(int id, String name, String value, HashMap displayValues, int min, int max, int step) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.displayValues = displayValues;
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public HashMap getDisplayValues() {
        return displayValues;
    }

    public void setDisplayValues(HashMap displayValues) {
        this.displayValues = displayValues;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
