package edu.wdaniels.lg;

import java.util.regex.Pattern;

/**
 *
 * @author rchirumamilla
 */
public class FieldValidator {

    /**
     * Validates a field to see if it's a decimal number.
     *
     * @param field
     * @return
     */
    public boolean decimalValidator(String field) {
        return !(Pattern.matches("[-]?[0-9]+[\\.]?[0-9]*", field)) || "".equals(field);

    }

    /**
     * Validates an integer field.
     *
     * @param field
     * @return
     */
    public boolean integerValidator(String field) {
        return !(Pattern.matches("[-]?[0-9]+", field)) || "".equals(field);

    }

}
