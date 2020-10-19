package tutorial;

import db.entity.AssessmentEntity;
import db.service.SurveyService;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.SelectorParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SurveyVM extends Window {


    private List<AssessmentEntity> surveyAssessments = new ListModelList<>();


    @Init
    public void init() {
        this.fillAssessmentEntities();
    }


    private void fillAssessmentEntities() {
        Map args = Executions.getCurrent().getArg();
        Date date = null;
        if (args.containsKey("getSurveyByDate")) {
            date = new Date((Long) args.get("getSurveyByDate"));
        }
        if (date != null)
            surveyAssessments.addAll(new SurveyService().getSurveyByDate(date));
    }

    public List<AssessmentEntity> getSurveyAssessments() {
        return surveyAssessments;
    }

    public void setSurveyAssessments(List<AssessmentEntity> surveyAssessments) {
        this.surveyAssessments = surveyAssessments;
    }

//    public void onCancelClicked(@SelectorParam("#winModal") final Component window) {
    public void onCancelClicked() {
        this.detach();
        setVisible(false);
    }
}
