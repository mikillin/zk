package tutorial;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Window;

import java.util.Date;


public class Chart3VMC extends SelectorComposer<Window> {

    @Wire("#chart1_db0")
    private Datebox chart1_db0;

    @Wire("#chart1_db1")
    private Datebox chart1_db1;

    @Wire("#nearest_selected_date_begin")
    private Date nearestSelectedDateBegin;

    @Wire("#nearest_selected_date_end")
    private Date nearestSelectedDateEnd;


    @Listen("onClick=#woche")
    public void woche() {
        Date resultWocheAgo = resetHoursMinutesSeconds(new Date());

        chart1_db0.setValue(new Date(resultWocheAgo.getTime() - 7 * 24 * 60 * 60 * 1000));
        chart1_db1.setValue(resetHoursMinutesSeconds(new Date()));
        Events.postEvent("onChange", chart1_db0, null);

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
    }

    @Listen("onClick=#jahr")
    public void jahr() {
        Date resultYearAgo = resetHoursMinutesSeconds(new Date());
        resultYearAgo.setYear(resultYearAgo.getYear() - 1);
        chart1_db1.setValue(resetHoursMinutesSeconds(new Date()));
        chart1_db0.setValue(resultYearAgo);
        Events.postEvent("onChange", chart1_db0, null);

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


    private boolean isInTimePeriod(Date beginDate, Date endDate, Date date) {
        return (beginDate == null && endDate != null && endDate.after(date)) ||
                (endDate == null && beginDate != null && beginDate.before(date)) ||
                (beginDate != null && beginDate.before(date) &&
                        endDate != null && endDate.after(date));
    }
}
