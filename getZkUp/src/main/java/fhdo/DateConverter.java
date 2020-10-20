package fhdo;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements Converter {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public Object coerceToUi(Object val, Component comp, BindContext ctx) {
        final Date date = (Date) val;
        return date == null ? null : sdf.format(date);
    }

    public Object coerceToBean(Object val, Component comp, BindContext ctx) {
        final String date = (String) val;
        sdf.setLenient(false);
        try {
            return date == null ? null : sdf.parse(date);
        } catch (ParseException e) {
            comp.invalidate();
            return null;

        }
    }


}
