package fhdo;

import db.entity.AssessmentEntity;
import db.service.AssessmentService;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyLastActivitiesVM {

    private List<ActivityMessage> activityMessages = new ListModelList<ActivityMessage>();

    @Init
    public void init() {
        this.fillActivityMessages();
    }

    private void fillActivityMessages() {

        this.activityMessages.clear();

        for (AssessmentEntity assessmentEntity : new AssessmentService().getAssessmentEventsDateDesc()) {
            this.activityMessages.add(
                    new ActivityMessage(assessmentEntity.getId(), assessmentEntity.getStatus(), assessmentEntity.getDate(),
                            assessmentEntity.getSurveyId(), assessmentEntity.getSurveyName())
            );
        }
    }

    @Command
    public void showSurvey(@BindingParam("surveyId") int surveyId) {

        Map args = new HashMap();
        args.put("getSurveyById", surveyId);

        Window win = (Window) Executions.createComponents(
                "/survey.zul", null, args);
        win.doModal();
    }



    public List<ActivityMessage> getActivityMessages() {
        return activityMessages;
    }

    public void setActivityMessages(List<ActivityMessage> activityMessages) {
        this.activityMessages = activityMessages;
    }
}
