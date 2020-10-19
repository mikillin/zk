package tutorial;

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
    private Set<Integer> selectedActivitiesIds = new HashSet<Integer>();


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
        this.fillActivities();
        this.fillOptimalValues();
    }


    @Command
    public void deleteActivity(@BindingParam("ev") InputEvent event) {

        String deleteId = event.getValue();
        for (Integer id : selectedActivitiesIds)
            if (deleteId.equals(id.toString())) {
                selectedActivitiesIds.remove(id);
                break;
            }

        renderChart(false); // don't add any items. only to delete
    }


    @GlobalCommand
    @NotifyChange("chosenQuestion")
    public void sendActivity(@BindingParam("data") String activityId) {

        this.activityId = activityId;
        this.chosenQuestion = getActivityById(Integer.parseInt(activityId)).getName();
    }


    @Command
    public void renderChart() {
        renderChart(true);
    }

    public void renderChart(boolean addNewItem) {


        // Clients.evalJavaScript(new ChartsUtil().compileChart("heatmap", "render"));
        if (isNotAllParametersEntered())
            return;

        if (this.activityId != null && addNewItem)
            selectedActivitiesIds.add(Integer.parseInt(this.activityId));

        chartActivities.clear();
        for (Integer activityId : selectedActivitiesIds) {
            for (Activity activity : activities) {
                if (getActivityById(activityId).getCategory().equals(activity.getCategory())
                        && getActivityById(activityId).getName().equals(activity.getName()) &&
                        (chart1_db0 == null && chart1_db1 == null
                                || chart1_db0 == null && chart1_db1 != null && chart1_db1.after(activity.getDate())
                                || chart1_db0 != null && chart1_db0.before(activity.getDate()) && chart1_db1 == null
                                || chart1_db0 != null && chart1_db0.before(activity.getDate()) && chart1_db1 != null && chart1_db1.after(activity.getDate())
                        )) {
                    chartActivities.add(activity);
                }
            }
        }


        JSONArray src = new JSONArray();
        JSONArray data = new JSONArray();

        JSONObject jsonData = new JSONObject();
        JSONObject jsonItem = new JSONObject();
        JSONObject jsonMain = new JSONObject();

        //todo: take cat + name  + all dates
        HashSet<String> questions = new HashSet();
        for (Integer activityId : this.selectedActivitiesIds)
            questions.add(getActivityById(activityId).getName());

        String category = "";
        String question = "";


        for (Activity activity : this.chartActivities) {

            if (!category.equals(activity.getCategory()) || !question.equals(activity.getName())) {
                //new set of a definite activity
                //the set should be sorted


                if (data.size() > 0) {
                    jsonMain = new JSONObject();
                    jsonMain.put("category", category);
                    jsonMain.put("question", question);
                    jsonMain.put("data", data);
                    src.add(jsonMain);

                    jsonData = new JSONObject();
                    jsonItem = new JSONObject();
                    data = new JSONArray();
                }

                category = activity.getCategory();
                question = activity.getName();

            }


            jsonData = new JSONObject();
            jsonItem = new JSONObject();
            jsonItem.put("value", activity.getValue());
            jsonItem.put("date", new SimpleDateFormat("dd.MM.yyyy").format(activity.getDate()));

            jsonData.put("item", jsonItem);
            jsonData.put("id", activity.getId());

            data.add(jsonData);
        }


        // the last set of data
        if (data.size() > 0) {
            jsonMain = new JSONObject();
            jsonMain.put("category", category);
            jsonMain.put("question", question);
            jsonMain.put("data", data);
            src.add(jsonMain);

        }


        JSONObject result = new JSONObject();
        result.put("src", src);
        result.put("optimalValues", optimalValues);
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
        return this.activityId == null || (this.activityId != null && this.activityId.equals(""));
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
        try {
            this.activities.add(new Activity(11, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("14.05.2020"), null, null, 2));
            this.activities.add(new Activity(2, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("14.06.2020"), null, null, 3));
            this.activities.add(new Activity(3, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("14.07.2020"), null, null, 4));
            this.activities.add(new Activity(4, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("14.08.2020"), null, null, 3));
            this.activities.add(new Activity(5, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("14.09.2020"), null, null, 3));
            this.activities.add(new Activity(21, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("14.05.2020"), null, null, 4));
            this.activities.add(new Activity(6, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("14.06.2020"), null, null, 10));
            this.activities.add(new Activity(7, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("14.07.2020"), null, null, 15));
            this.activities.add(new Activity(8, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("14.08.2020"), null, null, 11));
            this.activities.add(new Activity(9, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("14.09.2020"), null, null, 11));
            this.activities.add(new Activity(31, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("14.05.2020"), null, null, 7));
            this.activities.add(new Activity(10, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("14.06.2020"), null, null, 9));
            this.activities.add(new Activity(11, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("14.07.2020"), null, null, 8));
            this.activities.add(new Activity(12, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("14.08.2020"), null, null, 8));
            this.activities.add(new Activity(16, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("14.09.2020"), null, null, 8));
            this.activities.add(new Activity(41, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("14.05.2020"), null, null, 10));
            this.activities.add(new Activity(14, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("14.06.2020"), null, null, 11));
            this.activities.add(new Activity(15, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("14.07.2020"), null, null, 7));
            this.activities.add(new Activity(16, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("14.08.2020"), null, null, 10));
            this.activities.add(new Activity(17, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("14.09.2020"), null, null, 10));
//            this.activities.add(new Activity(17, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("14.08.2020"), null, null, 15));


        } catch (ParseException e) {
            e.printStackTrace();
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