package nl.sonepar.ratingreviews.rest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilities for providing common utility functions for the handlers
 * Created by Wilco on 08/01/2015.
 */
public class ServiceUtils {

    public static Map<String, List<String>> parseMultiValueParam(String pParamValue) {
        Map<String, List<String>> result = new HashMap<>();

        if (pParamValue != null) {
            String[] nameAndValue = pParamValue.split(":");
            if (nameAndValue.length == 2) {
                result.put(nameAndValue[0], Arrays.asList(nameAndValue[1].split(",")));
            }
        }
        return result;
    }
}
