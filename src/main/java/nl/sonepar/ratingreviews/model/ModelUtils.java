package nl.sonepar.ratingreviews.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilities for model classes
 * Created by Wilco on 08/01/2015.
 */
class ModelUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    /**
     * Safely convert a date to a String
     *
     * @param pDate
     *          The date (or null)
     * @return The string, "" when date is null
     */
    protected static String safeDateFormat(Date pDate)
    {
        return pDate == null ? "" : DATE_FORMAT.format(pDate);
    }

}
