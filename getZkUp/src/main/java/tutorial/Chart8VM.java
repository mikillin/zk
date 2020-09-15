package tutorial;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Chart8VM {
    private List<Activity> activities = new ListModelList<Activity>();
    private List<Activity> comparedActivities = new ListModelList<Activity>();
    private List<String> activitiesNames = new ListModelList<String>();
    private Set<String> selectedNames = new HashSet<String>();

    @Wire("#activity")
    private String activityId;


    @Wire("#chart1_db0")
    private Date chart1_db0;

    @Wire("#chart1_db1")
    private Date chart1_db1;


    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        this.fillActivities();
//        this.thisWin = (Window) view;
    }


    @Command
    public void deleteActivity(@BindingParam("query") int activityId) {

        Set temp = new HashSet();
        temp.addAll(selectedNames);
        temp.remove(getActivityById(activityId).getName()); //todo:
        selectedNames.clear();
        selectedNames.addAll(temp);

        List<Activity> tmp = new ArrayList<Activity>();
        tmp.addAll(comparedActivities);
        comparedActivities.clear();
        for (Activity comparedActivity : tmp) {
            if (!comparedActivity.getName().equals(activityId))
                comparedActivities.add(comparedActivity);
        }
        this.fillActivitiesNames();
    }

    @Command
    public void renderChart() {

        if (isAllParametersEntered())
            return;

        selectedNames.add(this.getActivityById(Integer.parseInt(this.activityId)).getName());

        Activity beginActivity;
        Activity endActivity;
        long bestComparedBeginDateTime;
        long bestComparedEndDateTime;

        if (activities.size() > 0) {
            beginActivity = activities.get(0);
            endActivity = activities.get(0);
            bestComparedBeginDateTime = Math.abs(chart1_db0.getTime() - beginActivity.getDate().getTime());
            bestComparedEndDateTime = Math.abs(chart1_db1.getTime() - endActivity.getDate().getTime());
        } else {
            System.err.println(">>> Error: the size of activities is wrong");
            return;
        }

        for (Activity activity : activities) {
            if(Integer.parseInt(this.activityId) == activity.getId()){
                if (Math.abs(chart1_db0.getTime() - activity.getDate().getTime()) < bestComparedBeginDateTime) {
                    bestComparedBeginDateTime = Math.abs(chart1_db0.getTime() - activity.getDate().getTime());
                    beginActivity = activity;
                }
                if (Math.abs(chart1_db1.getTime() - activity.getDate().getTime()) < bestComparedEndDateTime) {
                    bestComparedEndDateTime = Math.abs(chart1_db1.getTime() - activity.getDate().getTime());
                    endActivity = activity;
                }
            }
        }

        //TODO: round till?
        Activity comparedActivity = new Activity(Integer.parseInt(activityId), this.getActivityById(Integer.parseInt(activityId)).getCategory(), getActivityById(Integer.parseInt(activityId)).getName(), null, calculateCompareValue(beginActivity, endActivity));
        comparedActivities.add(comparedActivity);


        this.activityId = ""; // in purpose not to use twice the same parameters
        this.fillActivitiesNames();
    }


    //todo: id shouldn't be 0
    private boolean isAllParametersEntered() {
        return this.activityId.equals("") || this.chart1_db0 == null || this.chart1_db1 == null;
    }

    private int calculateCompareValue(Activity beginActivity, Activity endActivity) {
        return (int) (((new Double(endActivity.getValue() - beginActivity.getValue())) / beginActivity.getValue()) * 100);
    }


    private Activity getActivityById(int id) {
        for (Activity activity : this.activities)
            if (activity.getId() == id)
                return activity;
        return null;
    }

    private void fillActivities() {

        this.activities.clear();
        try {
            this.activities.add(new Activity(1, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("20.08.2020"), 20));
            this.activities.add(new Activity(2, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("21.08.2020"), 30));
            this.activities.add(new Activity(3, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), 40));
            this.activities.add(new Activity(4, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2020"), 50));
            this.activities.add(new Activity(5, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("20.08.2020"), 4));
            this.activities.add(new Activity(6, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("21.08.2020"), 10));
            this.activities.add(new Activity(7, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), 15));
            this.activities.add(new Activity(8, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2020"), 11));
            this.activities.add(new Activity(9, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("20.08.2020"), 220));
            this.activities.add(new Activity(10, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("21.08.2020"), 140));
            this.activities.add(new Activity(11, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), 15));
            this.activities.add(new Activity(12, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2020"), 19));
            this.activities.add(new Activity(13, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("20.08.2020"), 220));
            this.activities.add(new Activity(14, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("21.08.2020"), 140));
            this.activities.add(new Activity(15, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), 15));
            this.activities.add(new Activity(16, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2020"), 19));


            this.fillActivitiesNames();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void fillActivitiesNames() {

        HashSet<String> names = new HashSet<>();

        this.activitiesNames.clear();

        for (Activity activity : this.activities) {
            if (selectedNames.contains(activity.getName()))
                continue;
            names.add(activity.getName());
        }

        for (String name : names)
            this.activitiesNames.add(name);
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

    public List<String> getActivitiesNames() {
        return activitiesNames;
    }

    public void setActivitiesNames(List<String> activitiesNames) {
        this.activitiesNames = activitiesNames;
    }

    public Set<String> getSelectedNames() {
        return selectedNames;
    }

    public void setSelectedNames(Set<String> selectedNames) {
        this.selectedNames = selectedNames;
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
