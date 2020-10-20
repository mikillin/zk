package fhdo;

import db.entity.AssessmentEntity;
import db.service.SurveyService;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyFilterVM {

    private String keyword="";
    private List<AssessmentEntity> surveysList = new ListModelList<AssessmentEntity>();


    @Init
    public void init() {
        this.search();
    }

    @Command
    public void search() {
        surveysList.clear();
        surveysList.addAll(new SurveyService().getSurveyBySearchWord(keyword));
    }

    @Command
    public void showSurvey(@BindingParam("surveyId") int surveyId) {

        Map args = new HashMap();
        args.put("getSurveyById", surveyId);

        Window win = (Window) Executions.createComponents(
                "/survey.zul", null, args);
        win.doModal();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<AssessmentEntity> getSurveysList() {
        return surveysList;
    }

    public void setSurveysList(List<AssessmentEntity> surveysList) {
        this.surveysList = surveysList;
    }
}
