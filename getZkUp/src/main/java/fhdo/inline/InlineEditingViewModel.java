package fhdo.inline;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.ListModelList;
import fhdo.Survey;

import java.util.List;

public class InlineEditingViewModel {

    private List<Survey> fragebogensList = new ListModelList<Survey>();

    @Command
    public void doSomeAction(@BindingParam("query") String query) {

        System.out.printf("doSomeAction:" + query);
    }

    private FragebogenData data = new FragebogenData();


    public List<Survey> getAllFrageboegen() {
        return data.getAllFrageboegen();
    }
}
