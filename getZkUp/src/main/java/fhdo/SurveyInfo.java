package fhdo;

import java.util.ArrayList;
import java.util.Date;

public class SurveyInfo {

    private int id;
    private Date date;
    private String dateString;
    private String name;
    private ArrayList<KaviatAxis> axisData = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<KaviatAxis> getAxisData() {
        return axisData;
    }

    public void setAxisData(ArrayList<KaviatAxis> axisData) {
        this.axisData = axisData;
    }
}
