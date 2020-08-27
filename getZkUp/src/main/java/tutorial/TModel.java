package tutorial;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;

public class TModel  {

    @Command
    public void doSomeAction(@BindingParam("query") Event query) {

        System.out.printf("doSomeAction:" + query);
    }

    @Listen("onDeleteItem = #listboxDocumentos")
    public void delete(final Event event) {
        System.out.print(event.getData());
        System.out.println("Hallo!");
    }
}
