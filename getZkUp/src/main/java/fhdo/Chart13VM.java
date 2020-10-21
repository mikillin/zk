package fhdo;

import db.entity.AssessmentEntity;
import db.service.AssessmentService;
import org.zkoss.bind.annotation.*;
import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONObject;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import java.text.SimpleDateFormat;
import java.util.*;


public class Chart13VM {
    private List<Activity> activities = new ListModelList<Activity>();
    private List<Activity> comparedActivities = new ListModelList<Activity>();
    private Set<Integer> selectedActivitiesIds = new HashSet<Integer>();


    private String activityId;
    private Date chart1_db0;
    private Date chart1_db1;


    //refactor. das ist zu unakzeptabel
    private Integer sum = 0;


    @Command
    public void openModalQuestions() {

        Map args = new HashMap();
        Window win = (Window) Executions.createComponents(
                "/treeInOutput.zul", null, args);
        win.doModal();
    }


    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view) {

    }


    @GlobalCommand
    public void sendActivity(@BindingParam("data") String activityId) {

        this.activityId = activityId;
        renderChart();
    }

    @Command
    public void renderChart() {
        if (isNotAllParametersEntered())
            return;
        this.fillActivities();

        JSONObject map = new JSONObject();
//        this.activityId;


        // dieselbe Frage von verschiedene Fragebogen. Ist die Auswertung gleiche in diesem Fall?
        String title = "";
        Integer goal = -1;
        Integer average = -1;

        if (activities.size() > 0) {
            title = activities.get(0).getCategory() + ": " + activities.get(0).getName();
            goal = activities.get(0).getGoal();
            average = sum / activities.size();
        }

        //form js fields and
        JSONObject optionFromClass = new JSONObject();
        JSONObject legend = new JSONObject();
        legend.put("position", "bottom");
        optionFromClass.put("title", title); //todo: change the titel to activity name
        optionFromClass.put("curveType", "function");
        optionFromClass.put("legend", legend);
        optionFromClass.put("width", 530); //
        optionFromClass.put("height", 200);//

        map.put("option", optionFromClass);
        map.put("element", "document.getElementsByClassName('chart13')[0]");

        JSONArray data = new JSONArray();
        JSONArray titles = new JSONArray();
        titles.add("Date");
        if (goal != -1)
            titles.add("Ziel");
        titles.add("Durschnitt");
        titles.add("Auswertung");


        data.add(titles);

        JSONArray dataRow;

        for (Activity activity : activities) {
            dataRow = new JSONArray();
            dataRow.add(new SimpleDateFormat("dd-MM-YYYY").format(activity.getDate()));
            if (goal != -1)
                dataRow.add(goal);
            dataRow.add(average);
            dataRow.add(activity.getValue());
            data.add(dataRow);
        }

        if (activities.size()==0)
        {
            dataRow = new JSONArray();
            dataRow.add("");
            if (goal != -1)
                dataRow.add(0);
            dataRow.add(0);
            dataRow.add(0);
            data.add(dataRow);
        }

        map.put("data", data);

        String resultJS = new ChartsUtil().compileChart("line", "drawChart", map.toJSONString());
        Clients.evalJavaScript(resultJS);
    }


    private boolean isNotAllParametersEntered() {
        return this.activityId == null || (this.activityId != null && this.activityId.equals("")) || this.chart1_db0 == null || this.chart1_db1 == null;
    }


    private void fillActivities() {
//todo: here use also surveyId.. as for definite survey will  assessments  stable.
        this.activities.clear();
        this.sum = new Integer(0);
        List<AssessmentEntity> assessmentEntities = new AssessmentService().getAssessmentByDatesAndCategoryIdQuestionId(chart1_db0, chart1_db1, Integer.parseInt(activityId) / 100, Integer.parseInt(activityId) % 100);
        for (AssessmentEntity assessmentEntity : assessmentEntities) {
            this.sum += assessmentEntity.getValue();
            this.activities.add(new Activity(assessmentEntity.getId(), assessmentEntity.getCategoryName(),
                    assessmentEntity.getQuestion(), assessmentEntity.getDate(), chart1_db0, chart1_db1, assessmentEntity.getValue(), -1, -1, assessmentEntity.getGoal()));
        }
    }


    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Activity> getComparedActivities() {
        return comparedActivities;
    }

    public void setComparedActivities(List<Activity> comparedActivities) {
        this.comparedActivities = comparedActivities;
    }

    public Set<Integer> getSelectedActivitiesIds() {
        return selectedActivitiesIds;
    }

    public void setSelectedActivitiesIds(Set<Integer> selectedActivitiesIds) {
        this.selectedActivitiesIds = selectedActivitiesIds;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Date getChart1_db0() {
        return chart1_db0;
    }

    public void setChart1_db0(Date chart1_db0) {
        this.chart1_db0 = chart1_db0;
    }

    public Date getChart1_db1() {
        return chart1_db1;
    }

    public void setChart1_db1(Date chart1_db1) {
        this.chart1_db1 = chart1_db1;
    }
}
