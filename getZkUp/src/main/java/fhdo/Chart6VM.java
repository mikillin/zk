package fhdo;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Chart6VM {


    private Date chart1_db0;


    private Date chart1_db1;


    private String aktivitaet;
    private String chosenQuestion;

    @Init
    public void init() {
    }


    @Command
    public void renderChart() {
        this.executeJS();
    }


    @Command
    public void openModalQuestions() {
        Map args = new HashMap();
        Window win = (Window) Executions.createComponents(
                "/treeInOutput.zul", null, args);
        win.doModal();


    }

    private JsonElement getJSON() {
        return new JsonParser().parse(this.getStubJSONString());
    }


    private String getStubJSONString() {
        String source = "[\n" +
                "  {\n" +
                "    \"activity\": \"Schlafen\",\n" +
                "    \"date\": \"02.02.2020\",\n" +
                "    \"value\": 30\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Schlafen\",\n" +
                "    \"date\": \"02.03.2020\",\n" +
                "    \"value\": 95\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Schlafen\",\n" +
                "    \"date\": \"02.04.2020\",\n" +
                "    \"value\": 22\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Schlafen\",\n" +
                "    \"date\": \"02.05.2020\",\n" +
                "    \"value\": 14\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Schlafen\",\n" +
                "    \"date\": \"02.06.2020\",\n" +
                "    \"value\": 59\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Schlafen\",\n" +
                "    \"date\": \"02.07.2020\",\n" +
                "    \"value\": 52\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Schlafen\",\n" +
                "    \"date\": \"02.08.2020\",\n" +
                "    \"value\": 88\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Schlafen\",\n" +
                "    \"date\": \"02.09.2020\",\n" +
                "    \"value\": 20\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Schlafen\",\n" +
                "    \"date\": \"02.10.2020\",\n" +
                "    \"value\": 99\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Schlafen\",\n" +
                "    \"date\": \"02.02.2020\",\n" +
                "    \"value\": 66\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Essen/Trinken\",\n" +
                "    \"date\": \"02.02.2020\",\n" +
                "    \"value\": 37\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Essen/Trinken\",\n" +
                "    \"date\": \"02.03.2020\",\n" +
                "    \"value\": 50\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Essen/Trinken\",\n" +
                "    \"date\": \"02.04.2020\",\n" +
                "    \"value\": 81\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Essen/Trinken\",\n" +
                "    \"date\": \"02.05.2020\",\n" +
                "    \"value\": 79\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Essen/Trinken\",\n" +
                "    \"date\": \"02.06.2020\",\n" +
                "    \"value\": 84\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Essen/Trinken\",\n" +
                "    \"date\": \"02.07.2020\",\n" +
                "    \"value\": 91\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Essen/Trinken\",\n" +
                "    \"date\": \"02.08.2020\",\n" +
                "    \"value\": 82\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Essen/Trinken\",\n" +
                "    \"date\": \"02.09.2020\",\n" +
                "    \"value\": 89\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Essen/Trinken\",\n" +
                "    \"date\": \"02.10.2020\",\n" +
                "    \"value\": 6\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Essen/Trinken\",\n" +
                "    \"date\": \"02.02.20200\",\n" +
                "    \"value\": 67\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Laufen\",\n" +
                "    \"date\": \"02.02.2020\",\n" +
                "    \"value\": 96\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Laufen\",\n" +
                "    \"date\": \"02.03.2020\",\n" +
                "    \"value\": 13\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Laufen\",\n" +
                "    \"date\": \"02.04.2020\",\n" +
                "    \"value\": 98\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Laufen\",\n" +
                "    \"date\": \"02.05.2020\",\n" +
                "    \"value\": 10\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Laufen\",\n" +
                "    \"date\": \"02.06.2020\",\n" +
                "    \"value\": 86\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Laufen\",\n" +
                "    \"date\": \"02.07.2020\",\n" +
                "    \"value\": 23\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Laufen\",\n" +
                "    \"date\": \"02.08.2020\",\n" +
                "    \"value\": 74\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Laufen\",\n" +
                "    \"date\": \"02.09.2020\",\n" +
                "    \"value\": 47\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Laufen\",\n" +
                "    \"date\": \"02.10.2020\",\n" +
                "    \"value\": 73\n" +
                "  },\n" +
                "  {\n" +
                "    \"activity\": \"Laufen\",\n" +
                "    \"date\": \"02.02.20200\",\n" +
                "    \"value\": 40\n" +
                "  }\n" +
                "]";
        return source;
    }


    private void executeJS() {

        Clients.evalJavaScript(new ChartsUtil().compileChart("heatmap"));

        return;
        /*
        Clients.evalJavaScript("console.log('" + this.getJSON() + "')");
        String headPart = "var margin = {top: 80, right: 25, bottom: 30, left: 40},\n" +
                "  width = 650 - margin.left - margin.right,\n" +
                "  height = 650 - margin.top - margin.bottom;\n" +
                "\n" +
                "// append the svg object to the body of the page\n" +
                "var svg = d3.select(\".chart-class-type-6\")\n" +
                ".append(\"svg\")\n" +
                "  .attr(\"width\", width + margin.left + margin.right)\n" +
                "  .attr(\"height\", height + margin.top + margin.bottom)\n" +
                ".append(\"g\")\n" +
                "  .attr(\"transform\",\n" +
                "        \"translate(\" + margin.left + \",\" + margin.top + \")\");\n" +
                "\n" +
                "//Read the data\n" +
                //"d3.json("+this.getJSON()+", function(data) {\n" + //todo: set JSON as an object
                "\n" +
                "var data= " + this.getJSON() + ";" +
                "  // Labels of row and columns -> unique identifier of the column called 'group' and 'variable'\n" +
//                "  var datesDomain = d3.map(data, function(d){return d.activity;}).keys()\n" +
//                "  var activitiesDomain = d3.map(data, function(d){return d.date;}).keys()\n" +

                "  var datesDomain = d3.map(data, function(d){return d.date;}).keys()\n" +
                "  var activitiesDomain = d3.map(data, function(d){return d.activity;}).keys()\n" +
                "\n" +
                "  // Build X scales and axis:\n" +
                "  var x = d3.scaleBand()\n" +
                "    .range([ 0, width ])\n" +
                "    .domain(datesDomain)\n" +
                "    .padding(0.05);\n" +
                "  svg.append(\"g\")\n" +
                "    .style(\"font-size\", 15)\n" +
                "    .attr(\"transform\", \"translate(0,\" + height + \")\")\n" +
                "    .call(d3.axisBottom(x).tickSize(0))\n" +
                "    .call(g => g.select(\".domain\").remove())" +
                " .selectAll(\"text\")  \n" +
                "        .style(\"text-anchor\", \"end\")\n" +
                "        .attr(\"dx\", \"-.8em\")\n" +
                "        .attr(\"dy\", \".15em\")\n" +
                "        .attr(\"transform\", \"rotate(-65)\")\n" +
                "        .attr(\"font-size\",\"12px\")" +
                "    .select(\".domain\").remove()\n" +
                "\n" +
                "  // Build Y scales and axis:\n" +
                "  var y = d3.scaleBand()\n" +
                "    .range([ height, 0 ])\n" +
                "    .domain(activitiesDomain)\n" +
                "    .padding(0.05);\n" +
                "  svg.append(\"g\")\n" +
                "    .style(\"font-size\", 15)\n" +
                "    .call(d3.axisLeft(y).tickSize(0))\n" +
                "    .select(\".domain\").remove()\n" +
                "\n" +
                "  // Build color scale\n" +
                "  var myColor = d3.scaleSequential()\n" +
                "    .interpolator(d3.interpolateRdYlGn)\n" +
                "    .domain([1,100])\n" +
                "console.log(myColor)" +
                "\n" +
                "  // create a tooltip\n" +
                "  var tooltip = d3.select(\".chart-class-type-6\")\n" +
                "    .append(\"div\")\n" +
                "    .style(\"opacity\", 0)\n" +
                "    .attr(\"class\", \"tooltip\")\n" +
                "    .style(\"background-color\", \"white\")\n" +
                "    .style(\"border\", \"solid\")\n" +
                "    .style(\"border-width\", \"2px\")\n" +
                "    .style(\"border-radius\", \"5px\")\n" +
                "    .style(\"padding\", \"5px\")\n" +
                "\n" +
                "  // Three function that change the tooltip when user hover / move / leave a cell\n" +
                "  var mouseover = function(d) {\n" +
                "    tooltip\n" +
                "      .style(\"opacity\", 1)\n" +
                "    d3.select(this)\n" +
                "      .style(\"stroke\", \"black\")\n" +
                "      .style(\"opacity\", 1)\n" +
                "  }\n" +
                "  var mousemove = function(d) {\n" +
                "    tooltip\n" +
                "      .html(\"The exact value of<br>this cell is: \" + d.value)\n" +
                "      .style(\"left\", (d3.mouse(this)[0]+70) + \"px\")\n" +
                "      .style(\"top\", (d3.mouse(this)[1]) + \"px\")\n" +
                "  }\n" +
                "  var mouseleave = function(d) {\n" +
                "    tooltip\n" +
                "      .style(\"opacity\", 0)\n" +
                "    d3.select(this)\n" +
                "      .style(\"stroke\", \"none\")\n" +
                "      .style(\"opacity\", 0.8)\n" +
                "  }\n" +
                "\n" +
                "  // add the squares\n" +
                "  svg.selectAll()\n" +
                "    .data(data, function(d) {return d.activity+':'+d.date;})\n" +
                "    .enter()\n" +
                "    .append(\"rect\")\n" +
                "      .attr(\"x\", function(d) { return x(d.date) })\n" +
                "      .attr(\"y\", function(d) { return y(d.activity) })\n" +
                "      .attr(\"rx\", 4)\n" +
                "      .attr(\"ry\", 4)\n" +
                "      .attr(\"width\", x.bandwidth() )\n" +
                "      .attr(\"height\", y.bandwidth() )\n" +
                "      .style(\"fill\", function(d) {" +
                "console.log(myColor(d.value));" +
                " return myColor(d.value);} )\n" +
                "      .style(\"stroke-width\", 4)\n" +
                "      .style(\"stroke\", \"none\")\n" +
                "      .style(\"opacity\", 0.8)\n" +
                "    .on(\"mouseover\", mouseover)\n" +
                "    .on(\"mousemove\", mousemove)\n" +
                "    .on(\"mouseleave\", mouseleave)\n" +
//                "})\n" +
//                "}\n" +
                "\n" +
                "// Add title to graph\n" +
                "svg.append(\"text\")\n" +
                "        .attr(\"x\", 0)\n" +
                "        .attr(\"y\", -50)\n" +
                "        .attr(\"text-anchor\", \"left\")\n" +
                "        .style(\"font-size\", \"22px\")\n" +
                "        .text(\"A d3.js heatmap\");\n" +
                "\n" +
                "// Add subtitle to graph\n" +
                "svg.append(\"text\")\n" +
                "        .attr(\"x\", 0)\n" +
                "        .attr(\"y\", -20)\n" +
                "        .attr(\"text-anchor\", \"left\")\n" +
                "        .style(\"font-size\", \"14px\")\n" +
                "        .style(\"fill\", \"grey\")\n" +
                "        .style(\"max-width\", 400)\n" +
                "        .text(\"A short description of the take-away message of this chart.\");\n" +
                "\n";


        Clients.evalJavaScript(headPart);
*/

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

    public String getChosenQuestion() {
        return chosenQuestion;
    }

    public void setChosenQuestion(String chosenQuestion) {
        this.chosenQuestion = chosenQuestion;
    }
}
