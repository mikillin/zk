package fhdo;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;


public class ChartHeatMapController extends SelectorComposer<Window> {


    @Wire("#chosenQuestion")
    private Textbox chosenQuestion;

    @Wire("#fullSelectedQuestionPopup")
    private Popup popup;


    @Listen("onClick=#popupImageId")
    public void popup() {
        Events.postEvent("onChange", chosenQuestion, null);

        if (chosenQuestion != null && chosenQuestion.getValue() != null && chosenQuestion.getValue().length() > 0) {
            ((Label) (popup.getChildren().get(0))).setValue(chosenQuestion.getValue());
        } else {
            ((Label) (popup.getChildren().get(0))).setValue("Keine Frage wird ausgewählt.");
        }
    }


    public Textbox getChosenQuestion() {
        return chosenQuestion;
    }

    public void setChosenQuestion(Textbox chosenQuestion) {
        this.chosenQuestion = chosenQuestion;
    }

    public void setChosenQuestion(String chosenQuestion) {
//        this.getChosenQuestion().setValue(chosenQuestion);
    }
}
