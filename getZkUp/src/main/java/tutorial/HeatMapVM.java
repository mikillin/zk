package tutorial;

import com.lowagie.text.html.simpleparser.ALink;
import org.zkoss.bind.annotation.*;
import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONObject;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
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
    }


    @Command
    public void deleteActivity(@BindingParam("query") long activityId) {

        List<Activity> tmp = new ArrayList<Activity>();
        tmp.addAll(chartActivities);
        chartActivities.clear();
        for (Activity comparedActivity : tmp) {
            if (comparedActivity.getId() != (activityId))
                chartActivities.add(comparedActivity);
        }
    }


    @GlobalCommand
    @NotifyChange("chosenQuestion")
    public void sendActivity(@BindingParam("data") String activityId) {

        this.activityId = activityId;
        this.chosenQuestion = getActivityById(Integer.parseInt(activityId)).getName();
    }


    @Command
    public void renderChart() {


        // Clients.evalJavaScript(new ChartsUtil().compileChart("heatmap", "render"));
        if (isNotAllParametersEntered())
            return;


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

        JSONObject jsonData;
        JSONObject jsonItem;
        JSONObject jsonMain;

        HashSet<String> categoriesNames = new HashSet();
        for (Integer activityId : this.selectedActivitiesIds)
            categoriesNames.add(getActivityById(activityId).getCategory());

        for (String category : categoriesNames) {
            jsonMain = new JSONObject();

            for (Activity activity : chartActivities) {
                if (activity.getCategory().equals(category)) {
                    jsonData = new JSONObject();
                    jsonItem = new JSONObject();


                    jsonItem.put("value", activity.getValue());
                    jsonItem.put("date", new SimpleDateFormat("dd.MM.yyyy").format(activity.getDate()));

                    jsonData.put("item", jsonItem);
                    jsonData.put("id", activity.getId());

                    data.add(jsonData);

                }
            }

            jsonMain.put("type", category);
            jsonMain.put("data", data);
            src.add(jsonMain);
        }

        JSONObject result = new JSONObject();
        result.put("src", src);
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
    private void fillActivities() {

        this.activities.clear();
        try {
            this.activities.add(new Activity(11, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("14.09.2020"), null, null, 20));
            this.activities.add(new Activity(2, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("21.09.2020"), null, null, 30));
            this.activities.add(new Activity(3, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), null, null, 40));
            this.activities.add(new Activity(4, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2019"), null, null, 50));
            this.activities.add(new Activity(21, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("14.09.2020"), null, null, 4));
            this.activities.add(new Activity(6, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("21.09.2020"), null, null, 10));
            this.activities.add(new Activity(7, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), null, null, 15));
            this.activities.add(new Activity(8, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2019"), null, null, 11));
            this.activities.add(new Activity(31, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("14.09.2020"), null, null, 220));
            this.activities.add(new Activity(10, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("21.09.2020"), null, null, 140));
            this.activities.add(new Activity(11, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), null, null, 15));
            this.activities.add(new Activity(12, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2019"), null, null, 19));
            this.activities.add(new Activity(41, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("14.09.2020"), null, null, 220));
            this.activities.add(new Activity(14, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("21.09.2020"), null, null, 140));
            this.activities.add(new Activity(15, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), null, null, 15));
            this.activities.add(new Activity(16, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2019"), null, null, 19));


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