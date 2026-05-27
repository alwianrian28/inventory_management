package com.inventory.util;

import javax.swing.JPasswordField;

public final class PasswordFieldUtils {

    private PasswordFieldUtils() {
    }

    public static String getPassword(JPasswordField field) {
        return new String(field.getPassword());
    }

    public static boolean isEmpty(JPasswordField field) {
        return field.getPassword().length == 0;
    }

    public static boolean isBlank(JPasswordField field) {
        return getPassword(field).trim().isEmpty();
    }
}
