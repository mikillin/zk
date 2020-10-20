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

import java.util.*;


public class Chart13VM {
    private List<Activity> activities = new ListModelList<Activity>();
    private List<Activity> comparedActivities = new ListModelList<Activity>();
    private Set<Integer> selectedActivitiesIds = new HashSet<Integer>();


    private String activityId;
    private Date chart1_db0;
    private Date chart1_db1;


    //todo: >>>>>>> patientId - fom session, anyway Map should be


    @Command
    public void openModalQuestions() {

        Map args = new HashMap();
        Window win = (Window) Executions.createComponents(
                "/treeInOutput.zul", null, args);
        win.doModal();

    }


    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view) {

        //this.fillActivities();
    }


    @Command
    public void changeActivity(@BindingParam("activityId") long activityId) {

        List<Activity> tmp = new ArrayList<Activity>();
        tmp.addAll(comparedActivities);
        comparedActivities.clear();
        for (Activity comparedActivity : tmp) {
            if (comparedActivity.getId() != (activityId))
                comparedActivities.add(comparedActivity);
        }
    }


    @Command
    public void renderChart() {

        JSONObject map = new JSONObject();
        if (isNotAllParametersEntered())
            return;

        selectedActivitiesIds.add(Integer.parseInt(this.activityId));



        //stub

        //form js fields and
        JSONObject optionFromClass = new JSONObject();
        JSONObject legend = new JSONObject();
        legend.put("position", "bottom");
        optionFromClass.put("title", "Company Performance");
        optionFromClass.put("curveType", "function");
        optionFromClass.put("legend", legend);

        map.put("option", optionFromClass);
        map.put("element", "document.getElementsByClassName('chart13')[0]");

        JSONArray data = new JSONArray();
        JSONArray titles = new JSONArray();
        titles.push("Year");
        titles.push("Sales");
        titles.push("Expenses");
        titles.push("Add");
       
        data.push(titles);

        JSONArray dataRow = new JSONArray();
        dataRow.push("2003");
        dataRow.push(1000);
        dataRow.push(400);
        dataRow.push(500);
        data.push(dataRow);


         dataRow = new JSONArray();
        dataRow.push("2005");
        dataRow.push(1100);
        dataRow.push(600);
        dataRow.push(500);
        data.push(dataRow);

        dataRow = new JSONArray();
        dataRow.push("2005");
        dataRow.push(1700);
        dataRow.push(500);
        dataRow.push(500);
        data.push(dataRow);


        map.put("dataFromClass", data);


        String resultJS = new ChartsUtil().compileChart("line", "drawChart", map.toJSONString());
        Clients.evalJavaScript(resultJS);
    }


    private boolean isNotAllParametersEntered() {
        return this.activityId == null || (this.activityId != null && this.activityId.equals("")) || this.chart1_db0 == null || this.chart1_db1 == null;
    }


    private void fillActivities() {

        this.activities.clear();
        List<AssessmentEntity> assessmentEntities = new AssessmentService().getAssessmentByDatesAndQuestionId(chart1_db0, chart1_db1, Integer.parseInt(activityId));
        for (AssessmentEntity assessmentEntity : assessmentEntities) {
            this.activities.add(new Activity(assessmentEntity.getId(), assessmentEntity.getCategoryName(),
                    assessmentEntity.getQuestion(), assessmentEntity.getDate(), null, null, assessmentEntity.getValue()));
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

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

}
