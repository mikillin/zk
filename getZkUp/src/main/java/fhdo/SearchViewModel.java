package fhdo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.gson.*;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;

public class SearchViewModel {


    @Wire("#chart1_db0")
    private Date chart1_db0;

    @Wire("#chart1_db1")
    private Date chart1_db1;

    @Wire("#aktivitaet")
    private String aktivitaet;


    @Wire("#aktivitaetZeitraum")
    private String aktivitaetZeitraum;

    private String keyword;
    private List<Survey> fragebogensList = new ListModelList<Survey>();

    private FragebogenService fragebogenService = new FragebogenServiceImpl();
    private List<Survey> items = new ListModelList<Survey>();

    private List<ActivityMessage> activityMessages = new ListModelList<ActivityMessage>();
    private List<ActivityMessage> activeActivityMessages = new ListModelList<ActivityMessage>();
    private List<Activity> activities = new ListModelList<Activity>();


    private String message = "message from java";


    @Init
    public void init() {
//        Clients.showNotification("Loading...");

        this.search();
        this.fillInitActivityMessages();
        this.fillActivities();
    }


    @Wire
    private Radiogroup transmissionRadiogroup;

    @Listen("onCheck = #transmissionRadiogroup")
    public void changeTransmission() {
        Radio selectedItem = transmissionRadiogroup.getSelectedItem();
    }


    private void fillActivities() {
//        try {
//            this.activities.add(new Activity(1, "Biegen", new Date(), 20));
//            this.activities.add(new Activity(2, "Biegen", new SimpleDateFormat("dd.MM.yyyy").parse("21.08.2020"), 30));
//            this.activities.add(new Activity(3, "Biegen", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"),40));
//            this.activities.add(new Activity(4, "Biegen", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2020"), 50));
//            this.activities.add(new Activity(5, "Laufen", new Date(), 20));
//            this.activities.add(new Activity(6, "Laufen", new SimpleDateFormat("dd.MM.yyyy").parse("21.08.2020"), 10));
//            this.activities.add(new Activity(7, "Laufen", new SimpleDateFormat("dd.MM.yyyy").parse("20.07.2020"),5));
//            this.activities.add(new Activity(8, "Laufen", new SimpleDateFormat("dd.MM.yyyy").parse("02.09.2020"), 1));

//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    private void fillInitActivityMessages() {

        this.activityMessages.clear();
//        try {
//            this.activityMessages.add(new ActivityMessage(1, "PHQ-D zugewiesen", new Date()));
//            this.activityMessages.add(new ActivityMessage(2, "PHQ-D ergänzt",
//                    new SimpleDateFormat("dd.MM.yyyy").parse("20.08.2020")));
//            this.activityMessages.add(new ActivityMessage(3, "IES-R zugewiesen",
//                    new SimpleDateFormat("dd.MM.yyyy").parse("01.08.2020")));
//            this.activityMessages.add(new ActivityMessage(4, "PHQ-D ist fertig",
//                    new SimpleDateFormat("dd.MM.yyyy").parse("31.07.2019")));


            this.activeActivityMessages.addAll(activityMessages);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        ;

    }


    @Command
    public void search() {
        fragebogensList.clear();
        fragebogensList.addAll(fragebogenService.searchFragebogen(keyword));

        items.clear();
        items.addAll(fragebogensList);

    }


    @Command
    public void renderActivityMessages() {

        this.activeActivityMessages.clear();

        Date currentTag = new Date();
        currentTag.setSeconds(0);
        currentTag.setMinutes(0);
        currentTag.setHours(0);

        for (ActivityMessage activityMessage : this.activityMessages)
            if (aktivitaetZeitraum.equals("t")) {
                if (activityMessage.getDate().getTime() >= new Date().getTime() - 1 * 24 * 60 * 60 * 1000)
                    this.activeActivityMessages.add(activityMessage);
            } else if (aktivitaetZeitraum.equals("w")) {
                if (activityMessage.getDate().getTime() >= new Date().getTime() - 7 * 24 * 60 * 60 * 1000)
                    this.activeActivityMessages.add(activityMessage);
            } else if (aktivitaetZeitraum.equals("m")) {
                Date compareDate = new Date();
                int month = compareDate.getMonth();
                if (month == 0) {
                    compareDate.setMonth(11);
                    compareDate.setYear(compareDate.getYear() - 1);
                } else {
                    compareDate.setMonth(compareDate.getMonth() - 1);
                }


                if (activityMessage.getDate().getTime() >= compareDate.getTime())
                    this.activeActivityMessages.add(activityMessage);
            }


    }

    @Command
    public void renderChart() {

        JsonElement render = getJSON("activity");
        evaluateJS();
        //render here charts
        System.out.println("hallo");
    }

    private void evaluateJS() {

        String head = "   google.charts.load('current', {'packages':['corechart']});\n" +
                "                    google.charts.setOnLoadCallback(drawChart);\n" +
                "\n" +
                "                    function drawChart() {\n" +
                "                    var data = new google.visualization.DataTable();";

        String columns = " data.addColumn('string', 'Date');\n" +
                "                    data.addColumn('number', 'Grades');";

        String data = "data.addRows([";


        JsonElement render = getJSON("activity");
        JsonArray main = (JsonArray) ((JsonObject) render).get("fromDB");

        Date beginDate = null;
        if (this.chart1_db0 != null) {
            beginDate = new Date(this.chart1_db0.getTime());
            beginDate.setHours(0);
            beginDate.setMinutes(0);
            beginDate.setSeconds(0);
        }
        Date endDate = null;
        if (this.chart1_db1 != null) {
            endDate = new Date(this.chart1_db1.getTime());
            endDate.setHours(0);
            endDate.setMinutes(0);
            endDate.setSeconds(0);
        }


        for (JsonElement obj : main) {

            if (((JsonObject) obj).get("activity").toString().equals("\"" + this.aktivitaet + "\"")) {
                JsonArray items = (JsonArray) ((JsonObject) obj).get("data");

                for (JsonElement item : items) {
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("dd.MM.yyyy").parse(((JsonObject) item).get("date").getAsString());
                        if (
                                (beginDate == null && endDate != null && endDate.after(date)) ||
                                        (endDate == null && beginDate != null && beginDate.before(date)) ||
                                        (beginDate != null && beginDate.before(date) &&
                                                endDate != null && endDate.after(date)))
                            data += "['" + ((JsonObject) item).get("date").getAsString().replaceAll("\"", "") + "', "
                                    + ((JsonObject) item).get("value").getAsInt() + "],";
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }

        }
        data += "]);";

        String end = "var options = {\n" +
                "                    title: '" + this.aktivitaet + "',\n" +
                "                    hAxis: {title: 'Datum', titleTextStyle: {color: '#333'}},\n" +
//                "                    vAxis: {minValue: 0},\n" +
                "                    width:350,\n" +
                "                    height:200\n" +
                "                    };\n" +
                "                    var chart = new google.visualization.LineChart(document.getElementsByClassName('chartItem')[0]);\n" +
                "                    chart.draw(data, options);\n" +
                "                    }";
        String result = head + columns + data + end;

        Clients.evalJavaScript(result);


        //*chart.js*/


        head = "let chart = new Chart(ctx, {\n" +
                "    type: 'line',\n" +
                "    data: {\n" +
                "        datasets: [{\n" +
                "            label: '" + this.aktivitaet + "',\n";

        data = "data: [";

        for (JsonElement obj : main) {

            if (((JsonObject) obj).get("activity").toString().equals("\"" + this.aktivitaet + "\"")) {
                JsonArray items = (JsonArray) ((JsonObject) obj).get("data");

                for (JsonElement item : items) {
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("dd.MM.yyyy").parse(((JsonObject) item).get("date").getAsString());
                        if (
                                (beginDate == null && endDate != null && endDate.after(date)) ||
                                        (endDate == null && beginDate != null && beginDate.before(date)) ||
                                        (beginDate != null && beginDate.before(date) &&
                                                endDate != null && endDate.after(date)))
                            data += "'" + ((JsonObject) item).get("value").getAsInt() + ",";
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }

        }


//        <canvas id="myChart"></canvas> //todo: add canvas


        String str = "let chart = new Chart(ctx, {\n" +
                "    type: 'line',\n" +
                "    data: {\n" +
                "        datasets: [{\n" +
                "            label: 'First dataset',\n" +
                "            data: [0, 20, 40, 50]\n" +
                "        }],\n" +
                "        labels: ['January', 'February', 'March', 'April']\n" +
                "    },\n" +
                "    options: {\n" +
                "        scales: {\n" +
                "            yAxes: [{\n" +
                "                ticks: {\n" +
                "                    suggestedMin: 50,\n" +
                "                    suggestedMax: 100\n" +
                "                }\n" +
                "            }]\n" +
                "        }\n" +
                "    }\n" +
                "});";

    }


    @Command
    public void doSomeAction(@BindingParam("query") String query) {

        System.out.printf("doSomeAction with parameters:" + query);
    }


    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }


    public List<Survey> getItems() {
        return items;
    }

    public void setItems(List<Survey> items) {
        this.items = items;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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


    //todo: delete
    private JsonElement getJSON(String type) {

        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(type.equals("activity") ? this.getJSONString() : this.getActivities());
        return jsonTree;
    }

    //todo: delete
    // Beispiel
    private String getJSONString() {
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

    private String getActivities() {
        return "[{\"id\":1,\n" +
                "\"description\":\"PHQ-D ist zugewiesen\",\n" +
                "\"date\":\"29.08.2020\"},\n" +
                "{\"id\":2,\n" +
                "\"description\":\"PHQ-D von 10.08.2020 ist ergänzt\",\n" +
                "\"date\":\"01.08.2020\"},\n" +
                "{\"id\":3,\n" +
                "\"description\":\"IES-R von 07.08.2020 ist fertig\",\n" +
                "\"date\":\"01.07.2020\"},\n" +
                "{\"id\":4,\n" +
                "\"description\":\"Letzter Besuch\",\n" +
                "\"date\":\"01.06.2020\"},\n" +
                "{\"id\":5,\n" +
                "\"description\":\"Text 1\",\n" +
                "\"date\":\"01.05.2020\"},\n" +
                "{\"id\":6,\n" +
                "\"description\":\"Text 2\",\n" +
                "\"date\":\"01.04.2020\"},\n" +
                "{\"id\":7,\n" +
                "\"description\":\"Text 3\",\n" +
                "\"date\":\"05.05.2020\"},\n" +
                "{\"id\":8,\n" +
                "\"description\":\"Text 4\",\n" +
                "\"date\":\"01.04.2020\"},\n" +
                "{\"id\":9,\n" +
                "\"description\":\"Text 5\",\n" +
                "\"date\":\"01.02.2020\"},\n" +
                "{\"id\":10,\n" +
                "\"description\":\"Text 6\",\n" +
                "\"date\":\"01.01.2020\"}]";
    }

    public String getAktivitaetZeitraum() {
        return aktivitaetZeitraum;
    }

    public void setAktivitaetZeitraum(String aktivitaetZeitraum) {
        this.aktivitaetZeitraum = aktivitaetZeitraum;
    }

    public List<Survey> getFragebogensList() {
        return fragebogensList;
    }

    public void setFragebogensList(List<Survey> fragebogensList) {
        this.fragebogensList = fragebogensList;
    }

    public FragebogenService getFragebogenService() {
        return fragebogenService;
    }

    public void setFragebogenService(FragebogenService fragebogenService) {
        this.fragebogenService = fragebogenService;
    }

    public List<ActivityMessage> getActivityMessages() {
        return activityMessages;
    }

    public void setActivityMessages(List<ActivityMessage> activityMessages) {
        this.activityMessages = activityMessages;
    }

    public List<ActivityMessage> getActiveActivityMessages() {
        return activeActivityMessages;
    }

    public void setActiveActivityMessages(List<ActivityMessage> activeActivityMessages) {
        this.activeActivityMessages = activeActivityMessages;
    }
}
