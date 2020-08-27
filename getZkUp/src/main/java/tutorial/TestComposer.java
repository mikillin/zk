package tutorial;

import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zk.ui.select.*;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.util.*;
import org.zkoss.zk.ui.ext.*;
import org.zkoss.zk.au.*;
import org.zkoss.zk.au.out.*;
import org.zkoss.zul.*;
import java.util.*;

public class TestComposer extends SelectorComposer<Component> {

//    @Wire("#listboxDocumentos2")
//    private Listbox listboxDocumentos2;
    private ListModelList<Locale> listModel;

    @Override
    public void doAfterCompose(final Component comp) throws Exception {
        super.doAfterCompose(comp);
        listModel = new ListModelList<Locale>(Locale.getAvailableLocales());
//        listboxDocumentos2.setModel(listModel);
    }

    @Listen("onDeleteItem = #listboxDocumentos")
    public void delete(final Event event) {
        listModel.remove(event.getData());
    }
}