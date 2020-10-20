package fhdo;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.event.InputEvent;


public class Xmodel {

    private String message;


    @Command
    public void showTyping(@BindingParam("v") String value, @ContextParam(ContextType.TRIGGER_EVENT) InputEvent event) {

        System.out.println("Hallo model!: showTyping");
        message = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
