package fhdo;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Window;

import java.util.Date;


public class Chart8Controller extends SelectorComposer<Window> {

    public static final int AMOUNT_OF_MS_IN_7_DAYS = 7 * 24 * 60 * 60 * 1000;
    public static final int AMOUNT_OF_MS_IN_1_DAY = 1 * 24 * 60 * 60 * 1000;

    @Wire("#chart1_db0")
    private Datebox chart1_db0;

    @Wire("#chart1_db1")
    private Datebox chart1_db1;


    @Listen("onClick=#woche")
    public void woche() {
        Date resultWocheAgo = resetHoursMinutesSeconds(new Date());

        chart1_db0.setValue(new Date(resultWocheAgo.getTime() - AMOUNT_OF_MS_IN_7_DAYS));
        chart1_db1.setValue(resetHoursMinutesSeconds(new Date()));
        Events.postEvent("onChange", chart1_db0, null);
        Events.postEvent("onChange", chart1_db1, null);
        BindUtils.postGlobalCommand(null, null, "fireRenderChart", null);
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
        BindUtils.postGlobalCommand(null, null, "fireRenderChart", null);

    }

    @Listen("onClick=#tag")
    public void jahr() {
        Date resultDayAgo = resetHoursMinutesSeconds(new Date());
        chart1_db0.setValue(new Date(resultDayAgo.getTime() - AMOUNT_OF_MS_IN_1_DAY));
        chart1_db1.setValue(resetHoursMinutesSeconds(new Date()));
        Events.postEvent("onChange", chart1_db0, null);
        Events.postEvent("onChange", chart1_db1, null);
        BindUtils.postGlobalCommand(null, null, "fireRenderChart", null);


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


}
