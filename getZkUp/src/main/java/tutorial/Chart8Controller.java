package tutorial;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.util.Date;


public class Chart8Controller extends SelectorComposer<Window> {

    public static final int AMOUNT_OF_MS_IN_7_DAYS = 7 * 24 * 60 * 60 * 1000;

    @Wire("#chart1_db0")
    private Datebox chart1_db0;

    @Wire("#chart1_db1")
    private Datebox chart1_db1;

    @Wire("#chosenQuestion")
    private Textbox chosenQuestion;

    @Wire("#fullSelectedQuestionPopup")
    private Popup popup;


    @Listen("onClick=#woche")
    public void woche() {
        Date resultWocheAgo = resetHoursMinutesSeconds(new Date());

        chart1_db0.setValue(new Date(resultWocheAgo.getTime() - AMOUNT_OF_MS_IN_7_DAYS));
        chart1_db1.setValue(resetHoursMinutesSeconds(new Date()));
        Events.postEvent("onChange", chart1_db0, null);
        Events.postEvent("onChange", chart1_db1, null);
    }


    @Listen("onClick=#monat")
    public void monat() {
        Date resultMonatAgo = resetHoursMinutesSeconds(new Date());
        chart1_db1.setValue(resetHoursMinutesSeconds(new Date()));

        if (resultMonatAgo.getMonth() == 0) {
            resultMonatAgo.setMonth(11);
            resultMonatAgo.setYear(resultMonatAgo.getYear() - 1);
        } else {
            resultMonatAgo.setMonth(resultMonatAgo.getMonth() - 1);
        }

        chart1_db0.setValue(resultMonatAgo);
        Events.postEvent("onChange", chart1_db0, null);
        Events.postEvent("onChange", chart1_db1, null);
    }

    @Listen("onClick=#jahr")
    public void jahr() {
        Date resultYearAgo = resetHoursMinutesSeconds(new Date());
        resultYearAgo.setYear(resultYearAgo.getYear() - 1);
        chart1_db1.setValue(resetHoursMinutesSeconds(new Date()));
        chart1_db0.setValue(resultYearAgo);
        Events.postEvent("onChange", chart1_db0, null);
        Events.postEvent("onChange", chart1_db1, null);

    }

    @Listen("onClick=#popupImageId")
    public void popup() {
        Events.postEvent("onChange", chosenQuestion, null);

        if (chosenQuestion != null && chosenQuestion.getValue() != null && chosenQuestion.getValue().length() > 0)
        {
            ((Label) (popup.getChildren().get(0))).setValue(chosenQuestion.getValue());
        } else {
            ((Label) (popup.getChildren().get(0))).setValue("Keine Frage wird ausgewählt.");
        }
    }


    private Date resetHoursMinutesSeconds(Date date) {
        Date result = null;
        if (date != null) {
            result = new Date(date.getTime());
            result.setHours(23);
            result.setMinutes(59);
            result.setSeconds(59);
        }
        return result;
    }


    public boolean checkAllInputFields() {

        System.out.printf("Hallo ! ");
//        Clients.showNotification(msg, type, component, position, duration);
        return true;
    }

    //todo: insert data for the activity
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
