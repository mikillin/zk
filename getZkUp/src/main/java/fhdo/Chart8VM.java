package fhdo;

import db.entity.AssessmentEntity;
import db.service.AssessmentService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import java.util.*;


public class Chart8VM {
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
        this.fillActivities();
    }


    @GlobalCommand
    public void sendActivity(@BindingParam("data") String activityId) {
        this.activityId = activityId;
        renderChart();
    }


    @GlobalCommand
    public void fireRenderChart() {
              renderChart();
    }


    @Command
    public void deleteActivity(@BindingParam("activityId") long activityId) {

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

        if (isNotAllParametersEntered())
            return;

        selectedActivitiesIds.add(Integer.parseInt(this.activityId));

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
            if (getActivityById(Integer.parseInt(this.activityId)).getCategory().equals(activity.getCategory())
                    && getActivityById(Integer.parseInt(this.activityId)).getName().equals(activity.getName())) {
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

        if (this.getActivityById(Integer.parseInt(activityId)) == null) {
            return;
        }
        Activity comparedActivity = new Activity(
                new Date().getTime(), //todo: need to generate id for delete
                this.getActivityById(Integer.parseInt(activityId)).getCategory(),
                this.getActivityById(Integer.parseInt(activityId)).getName(),
                null,
                beginActivity.getDate(),
                endActivity.getDate(),
                calculateCompareValue(beginActivity, endActivity));

        if (!isAlreadyInCompare(comparedActivity))
            comparedActivities.add(comparedActivity);

    }


    private boolean isAlreadyInCompare(Activity activity) {
        for (Activity tmpActivity : this.comparedActivities)
            if (activity.getName().equals(tmpActivity.getName())
                    && activity.getCategory().equals(tmpActivity.getCategory()))
                return true;
        return false;
    }

    private List<Activity> deleteFromCompareList(Activity activity) {
        List<Activity> result = new ArrayList<>();
        for (Activity activityInCompare : comparedActivities) {
            if (activityInCompare.getCategory().equals(activity.getCategory())
                    && activityInCompare.getName().equals(activity.getName()))
                continue;
            else
                result.add(activityInCompare);
        }

        return result;
    }

    private boolean isNotAllParametersEntered() {
        return this.activityId == null || (this.activityId != null && this.activityId.equals("")) || this.chart1_db0 == null || this.chart1_db1 == null;
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

    /*DB + stub*/
    private void fillActivities() {

        this.activities.clear();

        List<AssessmentEntity> assessmentEntities = new AssessmentService().getAllAssessments();

        for (AssessmentEntity assessmentEntity : assessmentEntities) {
            this.activities.add(new Activity(assessmentEntity.getCategoryId()*100+assessmentEntity.getQuestionId(), assessmentEntity.getCategoryName(),
                    assessmentEntity.getQuestion(), assessmentEntity.getDate(), null, null, assessmentEntity.getValue()));
        }

//        try {
//            this.activities.add(new Activity(11, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("14.09.2020"), null, null, 20));
//            this.activities.add(new Activity(2, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("21.09.2020"), null, null, 30));
//            this.activities.add(new Activity(3, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), null, null, 40));
//            this.activities.add(new Activity(4, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2019"), null, null, 50));
//            this.activities.add(new Activity(21, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("14.09.2020"), null, null, 4));
//            this.activities.add(new Activity(6, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("21.09.2020"), null, null, 10));
//            this.activities.add(new Activity(7, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), null, null, 15));
//            this.activities.add(new Activity(8, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2019"), null, null, 11));
//            this.activities.add(new Activity(31, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("14.09.2020"), null, null, 220));
//            this.activities.add(new Activity(10, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("21.09.2020"), null, null, 140));
//            this.activities.add(new Activity(11, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), null, null, 15));
//            this.activities.add(new Activity(12, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2019"), null, null, 19));
//            this.activities.add(new Activity(41, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("14.09.2020"), null, null, 220));
//            this.activities.add(new Activity(14, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("21.09.2020"), null, null, 140));
//            this.activities.add(new Activity(15, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), null, null, 15));
//            this.activities.add(new Activity(16, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2019"), null, null, 19));
//
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
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
