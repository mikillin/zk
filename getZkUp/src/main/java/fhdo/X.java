package fhdo;


import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.*;

public class X extends SelectorComposer<Window> {

    @Wire
    Textbox input;
    @Wire
    Label output;

    @Wire("#chart1_db1")
    private Datebox chart1_db1;

    @Listen("onClick=#ok")
    public void submit() {
//        System.out.println("X submit");
        this.chart1_db1.setValue(new java.util.Date());
        output.setValue(input.getValue());

        //check Event fire doesn't fire
        Events.postEvent("onChange", input, null);
    }

    @Listen("onClick=#cancel")
    public void cancel() {
       // System.out.println("X cancel");
        output.setValue("");



    }



}
