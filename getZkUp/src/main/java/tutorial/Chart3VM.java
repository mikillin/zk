package tutorial;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Chart3VM {
    private List<Activity> activities = new ListModelList<Activity>();
    private List<Activity> comparedActivities = new ListModelList<Activity>();
    private List<String> activitiesNames = new ListModelList<String>();
    private Set<String> selectedNames = new HashSet<String>();

    @Wire("#aktivitaet")
    private String aktivitaet;

    @Wire("#nearest_selected_date_begin")
    private Date nearestSelectedDateBegin;

    @Wire("#nearest_selected_date_end")
    private Date nearestSelectedDateEnd;

    @Wire("#chart1_db0")
    private Date chart1_db0;

    @Wire("#chart1_db1")
    private Date chart1_db1;

    @Wire("#compareMessage")
    private String compareMessage;

    @Init
    public void init() {
        activitiesNames.add("Aktivität");
        this.fillActivities();

    }


    @Command
    public void deleteActivity(@BindingParam("query") String name) {
        //selectedNames.remove(name);

        Set temp = new HashSet();
        temp.addAll(selectedNames);
        temp.remove(name);
        selectedNames.clear();
        selectedNames.addAll(temp);

        List<Activity> tmp = new ArrayList<Activity>();

        tmp.addAll(comparedActivities);
        comparedActivities.clear();

        for (Activity comparedActivity : tmp) {
            if (!comparedActivity.getName().equals(name))
                comparedActivities.add(comparedActivity);
        }


        this.fillActivitiesNames();
    }

    @Command
    public void renderChart() {

        if (this.aktivitaet == null || this.aktivitaet.equals("Aktivität") || this.chart1_db0 == null || this.chart1_db1 == null)
            return;

        selectedNames.add(this.aktivitaet);

        Activity beginActivity = null;
        Activity endActivity = null;
        long comparedBeginDateTime = 0;
        long comparedEndDateTime = 0;

        if (activities.size() > 0) {
            beginActivity = activities.get(0);
            endActivity = activities.get(0);

            comparedBeginDateTime = Math.abs(chart1_db0.getTime() - beginActivity.getDate().getTime());
            comparedEndDateTime = Math.abs(chart1_db1.getTime() - endActivity.getDate().getTime());
        } else {
            System.err.println(">>> Error: the size of activities is wrong");
            return;
        }

        for (Activity activity : activities) {

            if (aktivitaet.equals(activity.getName())) {
                if (Math.abs(chart1_db0.getTime() - activity.getDate().getTime()) < comparedBeginDateTime) {
                    comparedBeginDateTime = Math.abs(chart1_db0.getTime() - activity.getDate().getTime());
                    beginActivity = activity;
                }
                if (Math.abs(chart1_db1.getTime() - activity.getDate().getTime()) < comparedEndDateTime) {
                    comparedEndDateTime = Math.abs(chart1_db1.getTime() - activity.getDate().getTime());
                    endActivity = activity;
                }
            }
        }
        compareMessage = "";
        if (beginActivity.getDate().compareTo(this.chart1_db0) != 0) {
            compareMessage += "Im Vergliech wird als Startdatum " + beginActivity.getDate().toString() + " benutzt.\n";
        }

        if (endActivity.getDate().compareTo(this.chart1_db0) != 0) {
            compareMessage += "Im Vergliech wird als Enddatum " + beginActivity.getDate().toString() + " benutzt.";

        }

        Activity comparedActivity = new Activity(comparedActivities.size(), aktivitaet, null, (int) (((new Double(endActivity.getValue() - beginActivity.getValue())) / beginActivity.getValue()) * 100));
        comparedActivities.add(comparedActivity);


        this.fillActivitiesNames();
    }


    private void fillActivities() {

        this.activities.clear();
        try {
            this.activities.add(new Activity(1, "Biegen", new SimpleDateFormat("dd.MM.yyyy").parse("20.08.2020"), 20));
            this.activities.add(new Activity(2, "Biegen", new SimpleDateFormat("dd.MM.yyyy").parse("21.08.2020"), 30));
            this.activities.add(new Activity(3, "Biegen", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), 40));
            this.activities.add(new Activity(4, "Biegen", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2020"), 50));
            this.activities.add(new Activity(5, "Laufen", new SimpleDateFormat("dd.MM.yyyy").parse("20.08.2020"), 20));
            this.activities.add(new Activity(6, "Laufen", new SimpleDateFormat("dd.MM.yyyy").parse("21.08.2020"), 10));
            this.activities.add(new Activity(7, "Laufen", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), 5));
            this.activities.add(new Activity(8, "Laufen", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2020"), 1));


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


    // getters & setters
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

    public String getAktivitaet() {
        return aktivitaet;
    }

    public void setAktivitaet(String aktivitaet) {
        this.aktivitaet = aktivitaet;
    }

    public Date getNearestSelectedDateBegin() {
        return nearestSelectedDateBegin;
    }

    public void setNearestSelectedDateBegin(Date nearestSelectedDateBegin) {
        this.nearestSelectedDateBegin = nearestSelectedDateBegin;
    }

    public Date getNearestSelectedDateEnd() {
        return nearestSelectedDateEnd;
    }

    public void setNearestSelectedDateEnd(Date nearestSelectedDateEnd) {
        this.nearestSelectedDateEnd = nearestSelectedDateEnd;
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

    public String getCompareMessage() {
        return compareMessage;
    }

    public void setCompareMessage(String compareMessage) {
        this.compareMessage = compareMessage;
    }
}
