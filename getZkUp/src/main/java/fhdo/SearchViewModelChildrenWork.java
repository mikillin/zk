package fhdo;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.ListModelList;

import java.util.List;

public class SearchViewModelChildrenWork extends SelectorComposer<Component> {

    private String keyword;
    private List<Car> carList = new ListModelList<Car>();
    private Car selectedCar;

    private CarService carService = new CarServiceImpl();
    private List<String> items = new ListModelList<String>();

    @Command
    public void search() {
        System.out.printf("search SearchViewModelChildrenWork!");
        carList.clear();
        carList.addAll(carService.search(keyword));
        items.add("1");
        items.add("2");
        items.add("3");
        items.add("4");

    }

    @Listen("onDeleteItem = #listboxDocumentos")
    public void delete(final Event event) {
        System.out.print(event.getData());
        System.out.println("Hallo!");
    }

    @Command
    public void doSomeAction() {

        System.out.printf("doSomeAction");
    }

    @GlobalCommand
    public void test(Event ev) {
        String st = ev.getTarget().getAttribute("idperson").toString();
//        alert(ev.getTarget().getClass().toString());
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

    public void setSelectedCar(Car selectedCar) {
        this.selectedCar = selectedCar;
    }

    public Car getSelectedCar() {
        return selectedCar;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
