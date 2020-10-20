package fhdo;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;

public class TComposerController extends SelectorComposer<Component> {


    @Listen("onDeleteItem = #listboxDocumentos")
    public void delete(final Event event) {
        System.out.print(event.getData());
        System.out.println("Hallo!");
    }

    @Listen("onDeleteItem2")
    public void delete2(final Event event) {
        System.out.print(event.getData());
        System.out.println("Hallo!");
    }

    @Command
    public void doSomeAction2() {

        System.out.printf("doSomeAction");
    }

}
