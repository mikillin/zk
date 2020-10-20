package fhdo;

import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

public class EmployeeDialogController extends SelectorComposer<Component> {
    private static final long serialVersionUID = 1L;


    @Wire
    Window modalDialog;
    private String value1;
    private String value2;

    @Listen("onClick = #closeBtn")
    public void showModal(Event e) {
        modalDialog.detach();
    }

    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view,
                     @ExecutionArgParam("value1") String v1,
                     @ExecutionArgParam("value2") String v2) {
        Selectors.wireComponents(view, this, false);
        this.value1 = v1;
        this.value2 = v2;

    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }
}
