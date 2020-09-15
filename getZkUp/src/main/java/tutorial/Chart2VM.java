package tutorial;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


public class Chart2VM extends SelectorComposer {


    public static final String AXIS_X_NAME = "'Date'";
    public static final String AXIS_Y_NAME = "'Auswertung'";
    public static final int CHART_WIDTH = 350;
    public static final int CHART_HEIGHT = 200;
    public static final String TAG_TO_INSERT_CHART = "document.getElementsByClassName('chartItem')[0]";
    public static final String TITLE_TEXT_COLOR = "'#333'";
    public static final String STUB_JSON_DATE_FORMAT = "dd.MM.yyyy";
    public static final String STUB_JSON_DATE_FIELD = "date";
    public static final String STUB_JSON_VALUE_FIELD = "value";
    public static final String STUB_JSON_DATA_FIELD = "data";
    public static final String STUB_JSON_ACTIVITY_FIELD = "activity";
    public static final String STUB_JSON_MAIN_FIELD = "fromDB";


    private List<Activity> activities = new LinkedList<Activity>();
    private List<Activity> comparedActivities = new LinkedList<Activity>();
    private List<String> activitiesNames = new LinkedList<String>();


    private int pickedIndex;

    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Wire("#xxx")
    private Date xxx;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Wire("#chart1_db0")
    private Datebox chart1_db0;

    @Wire("#chart1_db1")
    private Datebox chart1_db1;

    @Wire("#nearest_selected_date_begin")
    private Date nearestSelectedDateBegin;

    @Wire("#nearest_selected_date_end")
    private Date nearestSelectedDateEnd;


    @Wire("#aktivitaet")
    private String aktivitaet;


    @Wire
    private Textbox titleTextbox;


    @Init
    public void init() {

//        this.chart1_db0 = new Datebox();
//        chart1_db0.setValue(new Date());
        this.fillActivities();
    }


    @Command
    public void showTyping(@BindingParam("v") String value, @ContextParam(ContextType.TRIGGER_EVENT) InputEvent event) {
        message = value;
    }

    //getter and setter
    @Command
    public void showIndex(@BindingParam("index") @Default("0") Integer index) {
        this.index = index;
    }

    @Command
    public void woche() {

        this.getXxx().setYear(1999);
        System.out.println("hallo");

//        this.chart1_db1 = new Date();
//        this.chart1_db0 = new Date(chart1_db1.getValue().getTime() - 7 * 24 * 60 * 60 * 1000);

    }

    @Command
    public void monat() {
//        chart1_db1 = new Date();
//
//        chart1_db0 = new Date();
//        if (chart1_db0.getMonth() == 0) {
//            chart1_db0.setMonth(11);
//            chart1_db0.setYear(chart1_db0.getYear() - 1);
//
//        } else {
//            chart1_db0.setMonth(chart1_db0.getMonth() - 1);
//        }
    }

    @Command
    public void jahr() {
//        chart1_db1 = new Date();
//        chart1_db0.setYear(new Date().getYear() - 1);
    }

    @Command
    public void renderChart() {


        if (this.aktivitaet == null)
            return;

        Activity beginActivity = null;
        Activity endActivity = null;
        long comparedBeginDateTime = 0;
        long comparedEndDateTime = 0;

        if (activities.size() > 0) {
            beginActivity = activities.get(0);
            endActivity = activities.get(0);
        } else {
            System.err.println(">>> Error >>> the size of activities is wrong");
            return;
        }

        for (Activity activity : activities) {

            if (aktivitaet.equals(activity.getName())) {
                if (Math.abs(beginActivity.getDate().getTime() - activity.getDate().getTime()) > comparedBeginDateTime) {
                    comparedBeginDateTime = Math.abs(beginActivity.getDate().getTime() - activity.getDate().getTime());
                    beginActivity = activity;
                }
                if (Math.abs(endActivity.getDate().getTime() - activity.getDate().getTime()) > comparedEndDateTime) {
                    comparedEndDateTime = Math.abs(endActivity.getDate().getTime() - activity.getDate().getTime());
                    endActivity = activity;
                }
            }


        }

//        Activity comparedActivity = new Activity(comparedActivities.size(), aktivitaet, null, endActivity.getValue() / beginActivity.getValue() * 100);
//        comparedActivities.add(comparedActivity);

        if (!beginActivity.getDate().equals(chart1_db0)) {
            nearestSelectedDateBegin = beginActivity.getDate();
        } else {
            nearestSelectedDateBegin = null;

        }

        if (!beginActivity.getDate().equals(chart1_db0)) {
            nearestSelectedDateEnd = endActivity.getDate();
        } else {
            nearestSelectedDateEnd = null;

        }

        for (String activityName : activitiesNames) {
            if (activityName.equals(aktivitaet))
                activitiesNames.remove(activityName);
        }
    }


    private String getStubJSONString() {

        return "source";
    }

    private void processJS() {


    }

    private String getJavaScript(String headPart, String columns, String data, String options, String end) {
        return headPart + columns + data + options + end;
    }


    private String getTitleTextColor() {
        return TITLE_TEXT_COLOR;
    }


    private String getTagToInsertChart() {
        return TAG_TO_INSERT_CHART;
    }

    private int getChartHeight() {
        return CHART_HEIGHT;
    }

    private int getChartWidth() {
        return CHART_WIDTH;
    }


    private String getAxisYName() {
        return Chart2VM.AXIS_Y_NAME;
    }

    private String getAxisXName() {
        return Chart2VM.AXIS_X_NAME;
    }

    public Datebox getChart1_db0() {
        return chart1_db0;
    }

    public void setChart1_db0(Datebox chart1_db0) {
        this.chart1_db0 = chart1_db0;
    }

    public Datebox getChart1_db1() {
        return chart1_db1;
    }

    public void setChart1_db1(Datebox chart1_db1) {
        this.chart1_db1 = chart1_db1;
    }

    public String getAktivitaet() {
        return aktivitaet;
    }

    public void setAktivitaet(String aktivitaet) {
        this.aktivitaet = aktivitaet;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    private void fillActivities() {

        this.activities.clear();
//        try {
//            this.activities.add(new Activity(1, "Biegen", new Date(), 20));
//            this.activities.add(new Activity(2, "Biegen", new SimpleDateFormat("dd.MM.yyyy").parse("21.08.2020"), 30));
//            this.activities.add(new Activity(3, "Biegen", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), 40));
//            this.activities.add(new Activity(4, "Biegen", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2020"), 50));
//            this.activities.add(new Activity(5, "Laufen", new Date(), 20));
//            this.activities.add(new Activity(6, "Laufen", new SimpleDateFormat("dd.MM.yyyy").parse("21.08.2020"), 10));
//            this.activities.add(new Activity(7, "Laufen", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), 5));
//            this.activities.add(new Activity(8, "Laufen", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2020"), 1));


            this.fillActivitiesNames();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    private void fillActivitiesNames() {

        HashSet<String> names = new HashSet<>();

        this.activitiesNames.clear();

        for (Activity activity : this.activities) {
            names.add(activity.getName());
        }

        for (String name : names)
            this.activitiesNames.add(name);


    }

    public List<String> getActivitiesNames() {
        return activitiesNames;
    }

    public void setActivitiesNames(List<String> activitiesNames) {
        this.activitiesNames = activitiesNames;
    }

    private Date resetHoursMinutesSeconds(Date date) {
        Date result = null;
        if (date != null) {
            result = new Date(date.getTime());
            result.setHours(0);
            result.setMinutes(0);
            result.setSeconds(0);
        }
        return result;
    }

    private boolean isInTimePeriod(Date beginDate, Date endDate, Date date) {
        return (beginDate == null && endDate != null && endDate.after(date)) ||
                (endDate == null && beginDate != null && beginDate.before(date)) ||
                (beginDate != null && beginDate.before(date) &&
                        endDate != null && endDate.after(date));
    }

    public List<Activity> getComparedActivities() {
        return comparedActivities;
    }

    public void setComparedActivities(List<Activity> comparedActivities) {
        this.comparedActivities = comparedActivities;
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

    public int getPickedIndex() {
        return pickedIndex;
    }

    public void setPickedIndex(int pickedIndex) {
        this.pickedIndex = pickedIndex;
    }

    public Date getXxx() {
        return xxx;
    }

    public void setXxx(Date xxx) {
        this.xxx = xxx;
    }

    public Textbox getTitleTextbox() {
        return titleTextbox;
    }

    public void setTitleTextbox(Textbox titleTextbox) {
        this.titleTextbox = titleTextbox;
    }
}
