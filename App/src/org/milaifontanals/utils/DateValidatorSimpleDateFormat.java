package org.milaifontanals.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Gerard Casas
 */
public class DateValidatorSimpleDateFormat {

    private String format;
    
    public DateValidatorSimpleDateFormat(String format) {
        this.format = format;
    }

    public boolean isValid(final String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
