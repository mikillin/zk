package tutorial;

import db.entity.AssessmentEntity;
import db.service.WarningService;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chart5VM {

    private String surveyName = "";
    private String surveyDate= "";

    private List<AssessmentEntity> assessmentEntities = new ListModelList<>();


    @Init
    public void init(@ExecutionArgParam("map") Map map) {
        this.fillAssessmentEntities();
        if (assessmentEntities.size()>0) {
            this.surveyName = assessmentEntities.get(0).getSurveyName();
            this.surveyDate =new SimpleDateFormat("yyyy-MM-dd").format(assessmentEntities.get(0).getDate());
        }

    }

    @Command
    public void showSurvey(@BindingParam("date") long date) {


        Map args = new HashMap();


        args.put("getSurveyByDate", date);


        Window win = (Window) Executions.createComponents(
                "/survey.zul", null, args);
        win.doModal();
    }

    private void fillAssessmentEntities() {
        assessmentEntities.addAll(WarningService.getAllWarnings());
    }

    public List<AssessmentEntity> getAssessmentEntities() {
        return assessmentEntities;
    }

    public void setAssessmentEntities(List<AssessmentEntity> assessmentEntities) {
        this.assessmentEntities = assessmentEntities;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getSurveyDate() {
        return surveyDate;
    }

    public void setSurveyDate(String surveyDate) {
        this.surveyDate = surveyDate;
    }
}
