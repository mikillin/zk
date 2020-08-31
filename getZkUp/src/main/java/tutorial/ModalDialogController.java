package tutorial;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Window;

import java.util.HashMap;

public class ModalDialogController extends SelectorComposer<Component> {
    private static final long serialVersionUID = 1L;

    @Listen("onClick = #orderBtn")
    public void showModal(Event e) {
        //create a window programmatically and use it as a modal dialog.


        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("value1", "this.value1" );
        map.put("value2", "this.value2");


        Window window = (Window)Executions.createComponents(
                "/modal.zul", null, map);
        window.doModal();
    }
}