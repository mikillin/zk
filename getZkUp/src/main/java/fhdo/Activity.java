package fhdo;

import java.util.Date;

public class Activity {

    private long id;
    private String category;
    private String name;
    private Date date;
    private Date periodFrom;
    private Date periodTo;
    private int value;
    private int valueFrom;
    private int valueTo;

    public Activity() {
    }

    public Activity(long id, String category, String name, Date date, Date periodFrom, Date periodTo, int value, int valueFrom, int valueTo) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.date = date;
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
        this.value = value;
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(Date periodFrom) {
        this.periodFrom = periodFrom;
    }

    public Date getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(Date periodTo) {
        this.periodTo = periodTo;
    }

    public int getValueFrom() {
        return valueFrom;
    }

    public void setValueFrom(int valueFrom) {
        this.valueFrom = valueFrom;
    }

    public int getValueTo() {
        return valueTo;
    }

    public void setValueTo(int valueTo) {
        this.valueTo = valueTo;
    }
}
