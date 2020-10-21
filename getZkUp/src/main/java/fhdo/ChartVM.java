package fhdo;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ChartVM {


    public static final String AXIS_X_NAME = "'Datum'";
    public static final String AXIS_Y_NAME = "'Auswertung'";
    public static final int CHART_WIDTH = 330;
    public static final int CHART_HEIGHT = 250;
    public static final String TAG_TO_INSERT_CHART = "document.getElementsByClassName('chartItem')[0]";
    public static final String TITLE_TEXT_COLOR = "'#333'";
    public static final String STUB_JSON_DATE_FORMAT = "dd.MM.yyyy";
    public static final String STUB_JSON_DATE_FIELD = "date";
    public static final String STUB_JSON_VALUE_FIELD = "value";
    public static final String STUB_JSON_DATA_FIELD = "data";
    public static final String STUB_JSON_ACTIVITY_FIELD = "activity";
    public static final String STUB_JSON_MAIN_FIELD = "fromDB";

    private String activityId;
    private String chosenQuestion;
    private List<Activity> activities = new ListModelList<Activity>();


    @Wire("#chart1_db0")
    private Date chart1_db0;

    @Wire("#chart1_db1")
    private Date chart1_db1;

    @Wire("#aktivitaet")
    private String aktivitaet;

    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        this.fillActivities();
    }


    @Command
    public void openModalQuestions() {

        Map args = new HashMap();
        Window win = (Window) Executions.createComponents(
                "/treeInOutput.zul", null, args);
        win.doModal();

    }

    @GlobalCommand
    @NotifyChange("chosenQuestion")
    public void sendActivity(@BindingParam("data") String activityId) {

        this.activityId = activityId;
        this.chosenQuestion = getActivityById(Integer.parseInt(activityId)).getName();
        renderChart();
    }

    private Activity getActivityById(int id) {
        for (Activity activity : this.activities)
            if (activity.getId() == id)
                return activity;
        return null;
    }

    @Command
    public void renderChart() {
        if (isNotAllParametersEntered())
            return;

        processJS();
    }

    //todo: id shouldn't be 0
    private boolean isNotAllParametersEntered() {
        return this.activityId == null || (this.activityId != null && this.activityId.equals(""));
    }

    private JsonElement getJSON() {
        return new JsonParser().parse(this.getStubJSONString());
    }


    private String getStubJSONString() {
        String source = "{\"fromDB\": [{\n" +
                "\"activity\":\"Laufen\",\n" +
                "\"data\": [\n" +
                "{\"date\":\"01.01.2019\",\n" +
                "\"value\":\"1\"},\n" +
                "{\"date\":\"01.02.2019\",\n" +
                "\"value\":\"2\"},\n" +
                "{\"date\":\"01.03.2020\",\n" +
                "\"value\":\"3\"},\n" +
                "{\"date\":\"01.04.2020\",\n" +
                "\"value\":\"2\"},\n" +
                "{\"date\":\"01.06.2020\",\n" +
                "\"value\":\"5\"},\n" +
                "{\"date\":\"07.06.2020\",\n" +
                "\"value\":\"5\"}\n" +
                "]\n" +
                "},\n" +
                "{\n" +
                "\"activity\":\"Schlafen\",\n" +
                "\"data\": [\n" +
                "{\"value\":\"7\",\n" +
                "\"date\":\"01.03.2020\"},\n" +
                "{\"value\":\"8\",\n" +
                "\"date\":\"01.07.2020\"},\n" +
                "{\"value\":\"12\",\n" +
                "\"date\":\"01.05.2020\"},\n" +
                "{\"value\":\"9\",\n" +
                "\"date\":\"01.06.2020\"},\n" +
                "{\"value\":\"10\",\n" +
                "\"date\":\"07.06.2020\"}\n" +
                "]\n" +
                "},\n" +
                "{\n" +
                "\"activity\":\"Biegen\",\n" +
                "\"data\": [\n" +
                "{\"value\":\"76\",\n" +
                "\"date\":\"01.01.2020\"},\n" +
                "{\"value\":\"87\",\n" +
                "\"date\":\"01.02.2020\"},\n" +
                "{\"value\":\"88\",\n" +
                "\"date\":\"01.03.2020\"},\n" +
                "{\"value\":\"88\",\n" +
                "\"date\":\"01.04.2020\"},\n" +
                "{\"value\":\"90\",\n" +
                "\"date\":\"01.05.2020\"},\n" +
                "{\"value\":\"88\",\n" +
                "\"date\":\"01.06.2020\"},\n" +
                "{\"value\":\"88\",\n" +
                "\"date\":\"07.06.2020\"}\n" +
                "]\n" +
                "}\n]" +
                "}";
        return source;
    }

    private void processJS() {

        String headPart = this.getHeadJSPart();

        String columns = this.getColumnsJSPart();

        String data = getDataJSPart();

        String options = this.getOptionsJSPArt();

        String end = this.getEndJSPart();

        Clients.evalJavaScript(getJavaScript(headPart, columns, data, options, end));
    }

    private String getJavaScript(String headPart, String columns, String data, String options, String end) {
        return headPart + columns + data + options + end;
    }

    private String getDataJSPart() {
        String data = "data.addRows([";


        JsonElement render = getJSON();
        JsonArray main = (JsonArray) ((JsonObject) render).get(STUB_JSON_MAIN_FIELD);

        Date beginDate = resetHoursMinutesSeconds(this.chart1_db0);
        Date endDate = resetHoursMinutesSeconds(this.chart1_db1);

        Date date = null;
        for (Activity activity : getAllActivitiesForTheSameCategoryAndName(this.getActivityById(Integer.parseInt(this.activityId))))
        {
            if (isInTimePeriod(beginDate, endDate, activity.getDate()))
                data += "['" + new SimpleDateFormat("dd.MM.yyyy").format(activity.getDate()) + "', "
                        + activity.getValue()+ "],";
        }


        data += "]);";
        return data;
    }

    private List<Activity> getAllActivitiesForTheSameCategoryAndName(Activity selectedActivity) {
        List<Activity> result = new ArrayList<>();
        for (Activity activity : this.activities) {
            if (activity.getCategory().equals(selectedActivity.getCategory())
                    && activity.getName().equals(selectedActivity.getName()))
                result.add(activity);
        }
        return result;
    }

    private Date parseItemDate(JsonObject item) throws ParseException {
        return new SimpleDateFormat(STUB_JSON_DATE_FORMAT).parse(item.get(STUB_JSON_DATE_FIELD).getAsString());
    }

    private int getItemValue(JsonObject item) {
        return item.get(STUB_JSON_VALUE_FIELD).getAsInt();
    }

    private String getItemDate(JsonObject item) {
        return item.get(STUB_JSON_DATE_FIELD).getAsString().replaceAll("\"", "");
    }

    private boolean isInTimePeriod(Date beginDate, Date endDate, Date date) {
        return (beginDate == null && endDate == null) ||
                (beginDate == null && endDate != null && endDate.after(date)) ||
                (endDate == null && beginDate != null && beginDate.before(date)) ||
                (beginDate != null && beginDate.before(date) &&
                        endDate != null && endDate.after(date));
    }

    private boolean checkObjectActivityName(JsonObject obj) {
        return obj.get(STUB_JSON_ACTIVITY_FIELD).toString().equals("\"" + this.aktivitaet + "\"");
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

    private String getEndJSPart() {
        return getJavaScript("               var chart = new google.visualization.LineChart(", this.getTagToInsertChart(), ");\n", "                    chart.draw(data, options);\n", "                    }");
    }

    private String getOptionsJSPArt() {
        return "var options = {\n" +
                "                    title: '" +
                this.getActivityById(Integer.parseInt(this.activityId)).getCategory() + ": " +
                this.getActivityById(Integer.parseInt(this.activityId)).getName() + "',\n" +
                "                    hAxis: {title: " + this.getAxisXName() + ", titleTextStyle: {color: " + this.getTitleTextColor() + "}},\n" +
//                "                    width:" + this.getChartWidth() + ",\n" +
//                "                    height:" + this.getChartHeight() + "\n" +
                "                    width: "+CHART_WIDTH+",\n" +
                "                    height:"+CHART_HEIGHT+"\n" +

                "                    };\n";
    }

    private String getColumnsJSPart() {
        return "data.addColumn('string', " + this.getAxisXName() + ");\n" +
                "                    data.addColumn('number', " + this.getAxisYName() + ");";
    }

    private String getHeadJSPart() {
        return getJavaScript("   google.charts.load('current', {'packages':['corechart']});\n", "                    google.charts.setOnLoadCallback(drawChart);\n", "\n", "                    function drawChart() {\n", "                    var data = new google.visualization.DataTable();");
    }


    /*stub*/
    private void fillActivities() {

        this.activities.clear();
        try {
            this.activities.add(new Activity(11, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("14.05.2020"), null, null, 3,-1, -1, null));
            this.activities.add(new Activity(2, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("21.06.2020"), null, null, 2,-1, -1, null));
            this.activities.add(new Activity(3, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), null, null, 4,-1, -1, null));
            this.activities.add(new Activity(4, "Essen/Trinken", "Wieviele Mahlzeiten haben Sie zu sich genommen?", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2020"), null, null, 4,-1, -1, null));
            this.activities.add(new Activity(21, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("14.05.2020"), null, null, 4,-1, -1, null));
            this.activities.add(new Activity(6, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("21.06.2020"), null, null, 10,-1, -1, null));
            this.activities.add(new Activity(7, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), null, null, 12,-1, -1, null));
            this.activities.add(new Activity(8, "Wietere positive Aktivitäten", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2020"), null, null, 15,-1, -1, null));
            this.activities.add(new Activity(31, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("14.05.2020"), null, null, 8,-1, -1, null));
            this.activities.add(new Activity(10, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("21.06.2020"), null, null, 8,-1, -1, null));
            this.activities.add(new Activity(11, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), null, null, 9,-1, -1, null));
            this.activities.add(new Activity(12, "Schlaf", "Wielange waren Sie im Bett (in Stunden)?", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2020"), null, null, 9,-1, -1, null));
            this.activities.add(new Activity(41, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("14.05.2020"), null, null, 10,-1, -1, null));
            this.activities.add(new Activity(14, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("21.06.2020"), null, null, 8,-1, -1, null));
            this.activities.add(new Activity(15, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"), null, null, 9,-1, -1, null));
            this.activities.add(new Activity(16, "Entschpanungs- oder Atmenübungen", "Wie haben Sie sich dabei gefühlt?", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2020"), null, null, 9,-1, -1, null));


        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        return ChartVM.AXIS_Y_NAME;
    }

    private String getAxisXName() {
        return ChartVM.AXIS_X_NAME;
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

    public String getAktivitaet() {
        return aktivitaet;
    }

    public void setAktivitaet(String aktivitaet) {
        this.aktivitaet = aktivitaet;
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
