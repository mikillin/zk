package tutorial;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;

public class SearchViewModel {

    private String keyword;
    private List<Car> carList = new ListModelList<Car>();
    private List<Fragebogen> fragebogensList = new ListModelList<Fragebogen>();

    private CarService carService = new CarServiceImpl();
    private List<Fragebogen> items = new ListModelList<Fragebogen>();

    private String message = "message from java";

    @Command
    public void search() {
//        System.out.println("search! " + new Date());

        //keyword

        fragebogensList.clear();
        fragebogensList.addAll(carService.searchFragebogen(keyword));


        this.schowChart("linie", null);

        items.addAll(fragebogensList);
    }

    @Command
    public void report(@BindingParam("reportId") String reportId) {
        System.out.println("reportId! " + reportId);
    }


    @Listen("onDeleteItem = #listboxDocumentos")
    public void delete(final Event event) {
        System.out.print(event.getData());
        System.out.println("Hallo!");
    }


    private void schowChart(String type, HashMap<String, Object[]> params){

        Clients.evalJavaScript(this.getChartCode());
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public List<Car> getCarList() {
        return carList;
    }

//    public void setSelectedCar(Car selectedCar) {
//        this.selectedCar = selectedCar;
//    }
//
//    public Car getSelectedCar() {
//        return selectedCar;
//    }

    public List<Fragebogen> getItems() {
        return items;
    }

    public void setItems(List<Fragebogen> items) {
        this.items = items;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String getChartCode(){
        return "google.charts.load('current', {'packages':['corechart']});\n" +
                "      google.charts.setOnLoadCallback(drawChart);\n" +
                "\n" +
                "      function drawChart() {\n" +
                "        var data = google.visualization.arrayToDataTable([\n" +
                "          ['Year', 'Sales', 'Expenses'],\n" +
                "          ['2013',  1000,      400],\n" +
                "          ['2014',  1170,      460],\n" +
                "          ['2015',  660,       1120],\n" +
                "          ['2016',  1030,      540]\n" +
                "        ]);\n" +
                "\n" +
                "        var options = {\n" +
                "          title: 'Company Performance',\n" +
                "          hAxis: {title: 'Year',  titleTextStyle: {color: '#333'}},\n" +
                "          vAxis: {minValue: 0}\n" +
                "        };\n" +
                "\n" +
                "        var chart = new google.visualization.AreaChart(document.getElementsByTagName('body')[0].children[0].children[0]);\n" +
                "        chart.draw(data, options);\n" +
                "      }";
    }
}
