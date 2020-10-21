package db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ASSESSMENT")
public class AssessmentEntity {

    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "category_id")
    private int categoryId;
    @Column(name = "question_id")
    private int questionId;
    @Column(name = "question")
    private String question;
    @Column(name = "date")
    private Date date;
    @Column(name = "value")
    private Integer value;
    @Column(name = "category_name")
    private String categoryName;
    @Column(name = "min_value")
    private String minValue;
    @Column(name = "max_value")
    private String maxValue;
    @Column(name = "survey_name")
    private String surveyName;
    @Column(name = "survey_id")
    private int surveyId;
    @Column(name = "status")
    private String status;
    @Column(name = "goal")
    private Integer goal;


    public AssessmentEntity() {
    }

    public AssessmentEntity(int id, int category, String question, Date date, Integer value, String categoryName,
                            String surveyName, int surveyId, String status, Integer goal) {
        this.id = id;
        this.categoryId = category;
        this.question = question;
        this.date = date;
        this.value = value;
        this.categoryName = categoryName;
        this.surveyName = surveyName;
        this.surveyId = surveyId;
        this.status = status;
        this.goal = goal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }
}
