package org.milaifontanals.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Gerard Casas
 */
public class DateValidatorSimpleDateFormat{
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    public static boolean isValid(final String dateStr) {
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}


