package com.inventory.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JTextField;
import javax.swing.text.*;

public class NumericDocumentFilter extends DocumentFilter {

    private final DecimalFormat formatter;

    public NumericDocumentFilter() {
        formatter = (DecimalFormat) NumberFormat.getInstance(new Locale("id", "ID"));
        DecimalFormatSymbols syms = formatter.getDecimalFormatSymbols();
        syms.setGroupingSeparator('.'); // pemisah ribuan
        syms.setDecimalSeparator(',');  // desimal (tidak dipakai untuk integer)
        formatter.setDecimalFormatSymbols(syms);
        formatter.setGroupingUsed(true);
        formatter.setMaximumFractionDigits(0); // hanya integer
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        replace(fb, offset, 0, string, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        Document doc = fb.getDocument();
        String oldText = doc.getText(0, doc.getLength());
        StringBuilder newText = new StringBuilder(oldText);
        newText.replace(offset, offset + length, text == null ? "" : text);

        // hanya angka & tanda minus
        String cleaned = newText.toString().replaceAll("[^0-9-]", "");

        // hanya boleh satu minus, dan harus di depan
        if (cleaned.indexOf('-') > 0) {
            cleaned = cleaned.replace("-", ""); 
        }

        // jika kosong atau hanya "-", biarkan saja
        if (cleaned.isEmpty() || "-".equals(cleaned)) {
            fb.replace(0, doc.getLength(), cleaned, attrs);
            return;
        }

        try {
            long value = Long.parseLong(cleaned);
            String formatted = formatter.format(value); // pakai formatter supaya ada ribuan
            fb.replace(0, doc.getLength(), formatted, attrs);
        } catch (NumberFormatException ex) {
            // input tidak valid → abaikan
        }
    }

    public static Long getNumericValue(JTextField textField) {
        String text = textField.getText().trim();
        if (text.isEmpty() || "-".equals(text)) {
            return null;
        }
        try {
            String clean = text.replaceAll("[^0-9-]", "");
            return Long.parseLong(clean);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer getNumericValueAsInt(JTextField textField) {
        Long value = getNumericValue(textField);
        return value != null ? value.intValue() : null;
    }
}
