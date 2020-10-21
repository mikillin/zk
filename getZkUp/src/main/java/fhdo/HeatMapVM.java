package fhdo;

import db.entity.AssessmentEntity;
import db.service.AssessmentService;
import org.zkoss.bind.annotation.*;
import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONObject;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HeatMapVM {
    private List<Activity> activities = new ListModelList<Activity>();
    private List<Activity> chartActivities = new ListModelList<Activity>();
    private List<String> activitiesNames = new ListModelList<String>();
    private Set<Integer> selectedActivitiesIds = new LinkedHashSet<Integer>();


    private String activityId;
    private Date chart1_db0;
    private Date chart1_db1;
    private String chosenQuestion;
    private JSONArray optimalValues = new JSONArray();


    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireEventListeners(view, this);

    }


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


    @Command
    public void deleteActivity(@BindingParam("ev") InputEvent event) {

        String deleteId = event.getValue();
        for (Integer id : selectedActivitiesIds)
            if (deleteId.equals(id.toString())) {
                selectedActivitiesIds.remove(id);
                break;
            }

        renderChart(false); // don't add any items. only delete
    }


    @GlobalCommand
    @NotifyChange("chosenQuestion")
    public void sendActivity(@BindingParam("data") String activityId) {

        this.activityId = activityId;
        renderChart(true);
    }


    @Command
    public void renderChart() {
        renderChart(true);
    }

    @Command
    public void renderChartWithDates() {
        renderChart(false);
    }

    public void renderChart(boolean addNewItem) {


        // Clients.evalJavaScript(new ChartsUtil().compileChart("heatmap", "render"));
        if (isNotAllParametersEntered())
            return;

        if (addNewItem) //not delete
        {
            if (selectedActivitiesIds.contains(Integer.parseInt(this.activityId))) {
                selectedActivitiesIds.remove(Integer.parseInt(this.activityId));
            }
            selectedActivitiesIds.add(Integer.parseInt(this.activityId));
        }
        //invert
        ArrayList list = new ArrayList();
        for (Integer id : selectedActivitiesIds) {
            list.add(0, id);
        }
        selectedActivitiesIds.clear();
        selectedActivitiesIds.addAll(list);

        String whereClause = "";
        if (selectedActivitiesIds.size() > 0)
            whereClause += " AND ";
        if (selectedActivitiesIds.size() > 1)
            whereClause += "  (";
        for (Integer activityId : selectedActivitiesIds) {
            whereClause += "(categoryId = " + activityId / 100 + " and questionId = " + activityId % 100 + ") or ";
        }
        if (whereClause.length() > 3)
            whereClause = whereClause.substring(0, whereClause.length() - 3); //delete last "or"
        if (selectedActivitiesIds.size() > 1)
            whereClause += ")";
        whereClause += " ORDER BY AE.categoryId, AE.questionId, AE.date";

        List<AssessmentEntity> assessmentEntities = new AssessmentService().getAssessmentByDatesAndWhereClause(this.chart1_db0, this.chart1_db1, whereClause);


        JSONObject jsonData = new JSONObject();
        JSONObject names = new JSONObject();
        JSONObject goals = new JSONObject();
        JSONArray data = new JSONArray();

        Set<String> dates = new LinkedHashSet();
        for (Integer id : selectedActivitiesIds)
            for (AssessmentEntity assessmentEntity : assessmentEntities) {
                if ((assessmentEntity.getCategoryId() * 100 + assessmentEntity.getQuestionId()) == id.intValue()) {
                    jsonData = new JSONObject();
                    jsonData.put("id", assessmentEntity.getId());
                    jsonData.put("categoryId", assessmentEntity.getCategoryId());
                    jsonData.put("questionId", assessmentEntity.getQuestionId());
                    jsonData.put("date", new SimpleDateFormat("dd.MM.yyyy").format(assessmentEntity.getDate()));
                    jsonData.put("value", assessmentEntity.getValue());
                    dates.add(new SimpleDateFormat("yyyy.MM.dd").format(assessmentEntity.getDate()));
                    if (!names.containsKey(assessmentEntity.getCategoryId() * 100 + assessmentEntity.getQuestionId()))
                        names.put(assessmentEntity.getCategoryId() * 100 + assessmentEntity.getQuestionId(),
                                assessmentEntity.getCategoryName() + ":" + assessmentEntity.getQuestion());
                    if (!goals.containsKey(assessmentEntity.getCategoryId() * 100 + assessmentEntity.getQuestionId())) {
                        goals.put(assessmentEntity.getCategoryId() * 100 + assessmentEntity.getQuestionId(),
                                assessmentEntity.getGoal());
                    }
                    data.add(jsonData);
                }
            }

        //todo: refactor
        List<String> al = new ArrayList();
        al.addAll(dates);
        Collections.sort(al);
        dates.clear();
        for(String str: al) {
            try {
                dates.add(new SimpleDateFormat("dd.MM.yyyy").format(new SimpleDateFormat("yyyy.MM.dd").parse(str)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        JSONObject result = new JSONObject();
        JSONArray datesJSON = new JSONArray();
        for(String str: dates)
            datesJSON.add(str);
        result.put("src", data);
        result.put("names", names);
        result.put("goals", goals);
        result.put("dates", datesJSON);
        Clients.evalJavaScript(new ChartsUtil().compileChart("heatmap", "render", result.toJSONString()));
    }

    private boolean isAlreadyInCompare(Activity activity) {
        for (Activity activityInCompare : chartActivities) {
            if (activityInCompare.getCategory().equals(activity.getCategory())
                    && activityInCompare.getPeriodFrom().equals(activity.getPeriodFrom())
                    && activityInCompare.getPeriodTo().equals(activity.getPeriodTo())
                    && activityInCompare.getName().equals(activity.getName()))
                return true;

        }

        return false;
    }

    private List<Activity> getActivitiesWithCatAndName(Activity selectedActivity) {
        List<Activity> result = new ArrayList();
        for (Activity activity : this.activities) {

            if (selectedActivity.getCategory().equals(activity.getCategory())
                    && selectedActivity.getName().equals(activity.getName()))
                result.add(activity);
        }
        return result;
    }

    //todo: id shouldn't be 0
    private boolean isNotAllParametersEntered() {
        return this.activityId == null || (this.activityId != null && this.activityId.equals("")) || this.chart1_db0 == null || this.chart1_db1 == null;
    }


    private Activity getActivityById(int id) {
        for (Activity activity : this.activities)
            if (activity.getId() == id)
                return activity;
        return null;
    }

    /*stub*/
    private void fillOptimalValues() {

        JSONObject item = new JSONObject();
        item.put("category", "Essen/Trinken");
        item.put("question", "Wieviele Mahlzeiten haben Sie zu sich genommen?");
        item.put("value", 3);
        optimalValues.add(item);

        item = new JSONObject();
        item.put("category", "Wietere positive Aktivitäten");
        item.put("question", "Wie haben Sie sich dabei gefühlt?");
        item.put("value", 11);
        optimalValues.add(item);

        item = new JSONObject();
        item.put("category", "Schlaf");
        item.put("question", "Wielange waren Sie im Bett (in Stunden)?");
        item.put("value", 8);
        optimalValues.add(item);

        item = new JSONObject();
        item.put("category", "Entschpanungs- oder Atmenübungen");
        item.put("question", "Wie haben Sie sich dabei gefühlt?");
        item.put("value", 10);
        optimalValues.add(item);

    }


    private void fillActivities() {
        this.activities.clear();

        List<AssessmentEntity> assessmentEntities = new AssessmentService().getAssessmentByDatesAndCategoryIdQuestionId(chart1_db0, chart1_db1, Integer.parseInt(activityId) / 100, Integer.parseInt(activityId) % 100);
        for (AssessmentEntity assessmentEntity : assessmentEntities) {

            this.activities.add(new Activity(assessmentEntity.getCategoryId() * 100 + assessmentEntity.getQuestionId(), assessmentEntity.getCategoryName(),
                    assessmentEntity.getQuestion(), assessmentEntity.getDate(), chart1_db0, chart1_db1, assessmentEntity.getValue(), -1, -1, assessmentEntity.getGoal()));
        }

    }


    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Activity> getChartActivities() {
        return chartActivities;
    }

    public void setChartActivities(List<Activity> chartActivities) {
        this.chartActivities = chartActivities;
    }

    public List<String> getActivitiesNames() {
        return activitiesNames;
    }

    public void setActivitiesNames(List<String> activitiesNames) {
        this.activitiesNames = activitiesNames;
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


    public String getChosenQuestion() {
        return chosenQuestion;
    }

    public void setChosenQuestion(String chosenQuestion) {
        this.chosenQuestion = chosenQuestion;
    }


}