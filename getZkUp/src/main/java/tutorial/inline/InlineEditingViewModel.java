package tutorial.inline;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.ListModelList;
import tutorial.Fragebogen;

import java.util.List;

public class InlineEditingViewModel {

    private List<Fragebogen> fragebogensList = new ListModelList<Fragebogen>();

    @Command
    public void doSomeAction(@BindingParam("query") String query) {

        System.out.printf("doSomeAction:" + query);
    }

    private FragebogenData data = new FragebogenData();


    public List<Fragebogen> getAllFrageboegen() {
        return data.getAllFrageboegen();
    }
}
