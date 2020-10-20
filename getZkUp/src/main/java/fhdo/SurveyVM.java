package fhdo;

import db.entity.AssessmentEntity;
import db.service.SurveyService;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SurveyVM {

    Window thisWin;
    private List<AssessmentEntity> surveyAssessments = new ListModelList<>();
    private String surveyName;
    private int surveyId;

    @Init
    public void init(@ContextParam(ContextType.VIEW) Component thisWin) {
        this.thisWin = (Window) thisWin;
        this.fillAssessmentEntities();
    }


    private void fillAssessmentEntities() {
        Map args = Executions.getCurrent().getArg();

        if (args.containsKey("getSurveyByDate")) {
            Date date = new Date((Long) args.get("getSurveyByDate"));
            if (date != null)
                surveyAssessments.addAll(new SurveyService().getSurveyByDate(date));
        }

        if (args.containsKey("getSurveyById")) {
            Integer surveyId = (Integer) args.get("getSurveyById");
            if (surveyId != null)
                surveyAssessments.addAll(new SurveyService().getSurveyById(surveyId));
        }

        if (surveyAssessments.size() > 0) {
            AssessmentEntity assessmentEntity = surveyAssessments.get(0);
            surveyName = assessmentEntity.getSurveyName();
            surveyId = assessmentEntity.getSurveyId();


        }


    }

    public List<AssessmentEntity> getSurveyAssessments() {
        return surveyAssessments;
    }

    public void setSurveyAssessments(List<AssessmentEntity> surveyAssessments) {
        this.surveyAssessments = surveyAssessments;
    }

    public void onCancelClicked() {
        thisWin.detach();
        thisWin.setVisible(false);
    }

    public Window getThisWin() {
        return thisWin;
    }

    public void setThisWin(Window thisWin) {
        this.thisWin = thisWin;
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
}
