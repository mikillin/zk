package fhdo;

import db.entity.AssessmentEntity;
import db.service.AssessmentService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import java.util.*;
public class TagesBuchVM {
    private List<Activity> activities = new ListModelList<Activity>();
    private List<Activity> comparedActivities = new ListModelList<Activity>();
    private Set<Integer> selectedActivitiesIds = new HashSet<Integer>();

    private String activityId;
    private Date chart1_db0;

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

        if (this.getActivityById(Integer.parseInt(activityId)) == null) {
            return;
        }

        //vielleicht wäre es besser nichts im Modalfenster zu zeigen, aber wann wird die Psychologin die verschiedene
        // Zeiträume zu sehen wollen?
        deleteFromComparedActivities(this.getActivityById(Integer.parseInt(activityId)));

        if (!selectedActivitiesIds.contains(Integer.parseInt(this.activityId)))
            selectedActivitiesIds.add(Integer.parseInt(this.activityId));

        Activity beginActivity;
        Activity endActivity;
        long bestComparedBeginDateTime;
        long bestComparedEndDateTime;

        if (activities.size() > 0) {
            beginActivity = activities.get(0);
            endActivity = activities.get(0);
            bestComparedBeginDateTime = Math.abs(chart1_db0.getTime() - beginActivity.getDate().getTime());
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

            }
        }


        Activity comparedActivity = new Activity(
                new Date().getTime(), //todo: need to generate id for delete
                this.getActivityById(Integer.parseInt(activityId)).getCategory(),
                this.getActivityById(Integer.parseInt(activityId)).getName(),
                null,
                beginActivity.getDate(),
                endActivity.getDate(),
                calculateCompareValue(beginActivity, endActivity),
                beginActivity.getValue(),
                endActivity.getValue(),
                null
        );


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

    private List<Activity> deleteFromComparedActivities(Activity activity) {
        List<Activity> tmpComparedActivities = new ArrayList<Activity>();
        tmpComparedActivities.addAll(comparedActivities);
        comparedActivities.clear();
        for (Activity comparedActivity : tmpComparedActivities) {
            if (comparedActivity.getName().equals(activity.getName())
                    && comparedActivity.getCategory().equals(activity.getCategory()))
                continue;
            comparedActivities.add(comparedActivity);
        }
        return comparedActivities;
    }


    private boolean isNotAllParametersEntered() {
        return this.activityId == null || (this.activityId != null && this.activityId.equals("")) || this.chart1_db0 == null ;
    }

    private int calculateCompareValue(Activity beginActivity, Activity endActivity) {

        int sum = 0;
        int amount = 0;
        for (Activity activity : activities) {
            if (activity.getName().equals(beginActivity.getName())
                    && activity.getCategory().equals(beginActivity.getCategory())
                    && activity.getDate().getTime() >= beginActivity.getDate().getTime()
                    && activity.getDate().getTime() <= endActivity.getDate().getTime()) {
                sum += activity.getValue();
                amount++;
            }
        }

        if (amount != 0)
            return sum / amount;
        else
            return -1;
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
            this.activities.add(new Activity(assessmentEntity.getCategoryId() * 100 + assessmentEntity.getQuestionId(), assessmentEntity.getCategoryName(),
                    assessmentEntity.getQuestion(), assessmentEntity.getDate(), null, null, assessmentEntity.getValue(), -1, -1, null));
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


    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }


}
