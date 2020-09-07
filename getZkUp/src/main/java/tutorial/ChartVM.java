package tutorial;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ChartVM {


    public static final String AXIS_X_NAME = "'Date'";
    public static final String AXIS_Y_NAME = "'Auswertung'";
    public static final int CHART_WIDTH = 300;
    public static final int CHART_HEIGHT = 200;
    public static final String TAG_TO_INSERT_CHART = "document.getElementsByClassName('chartItem')[0]";
    public static final String TITLE_TEXT_COLOR = "'#333'";
    public static final String STUB_JSON_DATE_FORMAT = "dd.MM.yyyy";
    public static final String STUB_JSON_DATE_FIELD = "date";
    public static final String STUB_JSON_VALUE_FIELD = "value";
    public static final String STUB_JSON_DATA_FIELD = "data";
    public static final String STUB_JSON_ACTIVITY_FIELD = "activity";
    public static final String STUB_JSON_MAIN_FIELD = "fromDB";

    @Wire("#chart1_db0")
    private Date chart1_db0;

    @Wire("#chart1_db1")
    private Date chart1_db1;

    @Wire("#aktivitaet")
    private String aktivitaet;

    @Init
    public void init() {
    }

    @Command
    public void renderChart() {
        processJS();
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

        for (JsonElement obj : main) {
            if (checkObjectActivityName((JsonObject) obj)) {
                JsonArray items = (JsonArray) ((JsonObject) obj).get(STUB_JSON_DATA_FIELD);

                for (JsonElement item : items) {
                    Date date = null;
                    try {
                        date = parseItemDate((JsonObject) item);
                        if (isInTimePeriod(beginDate, endDate, date))
                            data += "['" + getItemDate((JsonObject) item) + "', "
                                    + getItemValue((JsonObject) item) + "],";
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        data += "]);";
        return data;
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
        return (beginDate == null && endDate != null && endDate.after(date)) ||
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
                "                    title: '" + this.getAktivitaet() + "',\n" +
                "                    hAxis: {title: " + this.getAxisXName() + ", titleTextStyle: {color: " + this.getTitleTextColor() + "}},\n" +
                "                    width:" + this.getChartWidth() + ",\n" +
                "                    height:" + this.getChartHeight() + "\n" +
                "                    };\n";
    }

    private String getColumnsJSPart() {
        return "data.addColumn('string', " + this.getAxisXName() + ");\n" +
                "                    data.addColumn('number', " + this.getAxisYName() + ");";
    }

    private String getHeadJSPart() {
        return getJavaScript("   google.charts.load('current', {'packages':['corechart']});\n", "                    google.charts.setOnLoadCallback(drawChart);\n", "\n", "                    function drawChart() {\n", "                    var data = new google.visualization.DataTable();");
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
}
