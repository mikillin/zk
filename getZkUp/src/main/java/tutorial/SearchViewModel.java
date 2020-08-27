package tutorial;

import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
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
}
