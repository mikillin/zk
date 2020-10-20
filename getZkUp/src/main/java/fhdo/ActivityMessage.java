package fhdo;

import java.util.Date;

public class ActivityMessage {

    private int id;
    private String description;
    private Date date;
    private int surveyId;
    private String surveyName;

    public ActivityMessage() {
    }

    public ActivityMessage(int id, String description, Date date, int surveyId, String surveyName) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.surveyId = surveyId;
        this.surveyName = surveyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }
}
