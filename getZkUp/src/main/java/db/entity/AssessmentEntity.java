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
    private long id;
    @Column(name = "category")
    private int category;
    @Column(name = "question")
    private String question;
    @Column(name = "date")
    private Date date;
    @Column(name = "value")
    private int value;
    @Column(name = "categoryName")
    private String categoryName;


    public AssessmentEntity() {
    }

    public AssessmentEntity(long id, int category, String question, Date date, int value, String categoryName) {
        this.id = id;
        this.category = category;
        this.question = question;
        this.date = date;
        this.value = value;
        this.categoryName = categoryName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
