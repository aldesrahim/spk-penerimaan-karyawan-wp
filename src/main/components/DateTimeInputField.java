package main.components;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 */
public class DateTimeInputField extends JXDatePicker {

    private DateFormat dateFormat = new SimpleDateFormat("EEEEE, dd MMMMM yyyy HH:mm", new Locale("id", "ID"));
    
    public DateTimeInputField() {
        super(new Date());

        initFormats();
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    private void initFormats() {
        setFormats(dateFormat);

        getMonthView().setLocale(new Locale("id", "ID"));
        getMonthView().setFirstDayOfWeek(Calendar.MONDAY);
    }

    public String getText() {
        Date date = getDateTime();

        if (date == null) {
            return null;
        }

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public Date getDateTime() {
        return (Date) getEditor().getValue();
    }

    public void setDateTime(Date date) {
        setDate(date);

        getEditor().setValue(date);

        initFormats();
    }

}
