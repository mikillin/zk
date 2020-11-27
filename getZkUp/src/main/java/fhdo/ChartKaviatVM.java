package fhdo;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;

import java.util.HashMap;
import java.util.List;


public class ChartKaviatVM {
    private List<Object[]> surveyslist = new ListModelList<Object[]>();
    private List<String> surveyDates = new ListModelList<String>();
    private List<SurveyInfo> surveyInfos = new ListModelList<SurveyInfo>();


    private String surveyType;
    private String surveyDate;


    private List<String> surveys = new ListModelList<String>();


    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireEventListeners(view, this);

    }


    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        String whereClause = "";
//        List<Survey> surveys = new AssessmentService().getSurveysByDates(this.chart1_db0, this.chart1_db1);

        this.surveys.add("PHQ-D 22.10.2020");
        this.surveys.add("IES-R 19.10.2020");


        /// Von DB


        KaviatAxis axis;
        SurveyInfo surveyInfo = new SurveyInfo();

        HashMap firstAxisDisplayValues = new HashMap();
        firstAxisDisplayValues.put("1", "Ja");
        firstAxisDisplayValues.put("0", "Nein");
        surveyInfo.setName("PHQ-D");
        surveyInfo.setDateString("22.10.2020");
        axis = new KaviatAxis(0, "Die Antwort ist Ja", "1", firstAxisDisplayValues, 0, 1, 1);
        surveyInfo.getAxisData().add(axis);
        axis = new KaviatAxis(1, "Wie war Ihre Schlafqualität?", "2", null, 1, 6, 1);
        surveyInfo.getAxisData().add(axis);
        axis = new KaviatAxis(2, "Wie lang haben Sie davon tatsächlich geschlafen?", "3", null, 1, 12, 2);
        surveyInfo.getAxisData().add(axis);
        axis = new KaviatAxis(3, "Wie lang waren Sie im Bett?", "4", null, 1, 8, 1);
        surveyInfo.getAxisData().add(axis);
        axis = new KaviatAxis(4, "Achse mit Auswertungen ab 4 bis 9", "5", null, 4, 9, 1);
        surveyInfo.getAxisData().add(axis);

        surveyInfos.add(surveyInfo);


        //2

        surveyInfo = new SurveyInfo();

        surveyInfo.setName("PHQ-D");
        surveyInfo.setDateString("20.09.2020");
        axis = new KaviatAxis(0, "Die Antwort ist Ja", "2", null, 2, 6, 2);
        surveyInfo.getAxisData().add(axis);
        axis = new KaviatAxis(1, "Wie war Ihre Schlafqualität?", "2", null, 1, 6, 1);
        surveyInfo.getAxisData().add(axis);
        axis = new KaviatAxis(2, "Wie lang haben Sie davon tatsächlich geschlafen?", "3", null, 1, 12, 2);
        surveyInfo.getAxisData().add(axis);
        axis = new KaviatAxis(3, "Wie lang waren Sie im Bett?", "4", null, 1, 8, 1);
        surveyInfo.getAxisData().add(axis);
        axis = new KaviatAxis(4, "Achse mit Auswertungen ab 4 bis 9", "5", null, 4, 9, 1);
        surveyInfo.getAxisData().add(axis);

        surveyInfos.add(surveyInfo);


        //3

        surveyInfo = new SurveyInfo();

        surveyInfo.setName("IES-R");
        surveyInfo.setDateString("20.09.2020");
        axis = new KaviatAxis(0, "IES-R: Die Antwort ist Ja", "2", null, 2, 6, 2);
        surveyInfo.getAxisData().add(axis);
        axis = new KaviatAxis(1, "IES-R: Wie war Ihre Schlafqualität?", "2", null, 1, 6, 1);
        surveyInfo.getAxisData().add(axis);
        axis = new KaviatAxis(2, "IES-R: Wie lang haben Sie davon tatsächlich geschlafen?", "3", null, 1, 12, 2);
        surveyInfo.getAxisData().add(axis);
        axis = new KaviatAxis(3, "IES-R: Wie lang waren Sie im Bett?", "4", null, 1, 8, 1);
        surveyInfo.getAxisData().add(axis);
        axis = new KaviatAxis(4, "IES-R: Achse mit Auswertungen ab 4 bis 9", "5", null, 4, 9, 1);
        surveyInfo.getAxisData().add(axis);

        surveyInfos.add(surveyInfo);


        //  surveys
    }


    @Command
    public void renderDates(@BindingParam("ev") InputEvent event) {

        surveyDates.clear();
        for (SurveyInfo surveyInfo : surveyInfos)
            if (surveyInfo.getName().equals(surveyType))
                surveyDates.add(surveyInfo.getDateString());
    }


    @Command
    @NotifyChange("*")
    public void showKaviatChart(@BindingParam("ev") InputEvent event) {

        String params = "";

        this.surveyDates.clear();
        this.surveyDates.add("1");
        this.surveyDates.add("2");
        this.surveyDates.add("3");
        System.out.printf("show kaviat chart");

        if (surveyType.equals("IES-R 19.10.2020")) {
            params = "{\n" +
                    "    \"chartData\": {\n" +
                    "        \"chartHeight\": 270,\n" +
                    "        \"chartWidth\": 470,\n" +
                    "        \"centerOffset\": 10\n" +
                    "    },\n" +
                    "    \"axisData\": [\n" +
                    "        {\n" +
                    "            \"axeId\": 0,\n" +
                    "            \"name\": \"Die Antwort ist Ja\",\n" +
                    "            \"value\": 1,\n" +
                    "            \"displayValues\": {\"1\": \"Ja\", \"0\": \"Nein\"},\n" +
                    "            \"info\": {\"min\": 0, \"max\": 1, \"step\": 1}\n" +
                    "        },\n" +
                    "        {\"axeId\": 1, \"name\": \"Wie war Ihre Schlafqualität?\", \"value\": 2, \"info\": {\"min\": 1, \"max\": 6, \"step\": 1}},\n" +
                    "        {\n" +
                    "            \"axeId\": 2,\n" +
                    "            \"name\": \"Wie lang haben Sie davon tatsächlich geschlafen?\",\n" +
                    "            \"value\": 3,\n" +
                    "            \"info\": {\"min\": 1, \"max\": 12, \"step\": 2}\n" +
                    "        },\n" +
                    "        {\"axeId\": 3, \"name\": \"Wie lang waren Sie im Bett?\", \"value\": 4, \"info\": {\"min\": 1, \"max\": 8, \"step\": 1}},\n" +
                    "        {\"axeId\": 4, \"name\": \"Achse mit Auswertungen ab 4 bis 9\", \"value\": 5, \"info\": {\"min\": 4, \"max\": 9, \"step\": 1}}\n" +
                    "    ]\n" +
                    "}";
        } else {

            params = "{\n" +
                    "    \"chartData\": {\n" +
                    "        \"chartHeight\": 270,\n" +
                    "        \"chartWidth\": 470,\n" +
                    "        \"centerOffset\": 10\n" +
                    "    },\n" +
                    "    \"axisData\": [\n" +
                    "        {\n" +
                    "            \"axeId\": 0,\n" +
                    "            \"name\": \"Die Antwort ist Nein\",\n" +
                    "            \"value\": 0,\n" +
                    "            \"displayValues\": {\"1\": \"Ja\", \"0\": \"Nein\"},\n" +
                    "            \"info\": {\"min\": 0, \"max\": 1, \"step\": 1}\n" +
                    "        },\n" +
                    "        {\"axeId\": 1, \"name\": \"Wie war Ihre Schlafqualität?\", \"value\": 1, \"info\": {\"min\": 1, \"max\": 6, \"step\": 1}},\n" +
                    "        {\n" +
                    "            \"axeId\": 2,\n" +
                    "            \"name\": \"Wie lang haben Sie davon tatsächlich geschlafen?\",\n" +
                    "            \"value\": 5,\n" +
                    "            \"info\": {\"min\": 1, \"max\": 12, \"step\": 2}\n" +
                    "        },\n" +
                    "        {\"axeId\": 3, \"name\": \"Wie lang waren Sie im Bett?\", \"value\": 5, \"info\": {\"min\": 1, \"max\": 8, \"step\": 1}},\n" +
                    "        {\"axeId\": 4, \"name\": \"Achse mit Auswertungen ab 4 bis 9\", \"value\": 6, \"info\": {\"min\": 4, \"max\": 9, \"step\": 1}}\n" +
                    "    ]\n" +
                    "}";

        }

        Clients.evalJavaScript(new ChartsUtil().compileChart("kaviat", "render", params));

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

        Clients.evalJavaScript(new ChartsUtil().compileChart("kaviat"));
    }


    public String getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
    }


    public List<Object[]> getSurveyslist() {
        return surveyslist;
    }

    public void setSurveyslist(List<Object[]> surveyslist) {
        this.surveyslist = surveyslist;
    }

    public List<String> getSurveyDates() {
        return surveyDates;
    }

    public void setSurveyDates(List<String> surveyDates) {
        this.surveyDates = surveyDates;
    }

    public String getSurveyDate() {
        return surveyDate;
    }

    public void setSurveyDate(String surveyDate) {
        this.surveyDate = surveyDate;
    }

    public List<SurveyInfo> getSurveyInfos() {
        return surveyInfos;
    }

    public void setSurveyInfos(List<SurveyInfo> surveyInfos) {
        this.surveyInfos = surveyInfos;
    }

    public List<String> getSurveys() {
        return surveys;
    }

    public void setSurveys(List<String> surveys) {
        this.surveys = surveys;
    }
}
