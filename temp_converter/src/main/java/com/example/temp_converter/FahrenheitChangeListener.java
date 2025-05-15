package com.example.temp_converter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class FahrenheitChangeListener implements ChangeListener<String> {

    private final TextField celsiusOutputField;
    private CelsiusChangeListener otherListener;
    private boolean enabled = true;

    public FahrenheitChangeListener(TextField celsiusOutputField) {
        this.celsiusOutputField = celsiusOutputField;
    }

    public void setOtherListener(CelsiusChangeListener celsiusListener) {
        this.otherListener = celsiusListener;
    }

    public void setEnabled(boolean value) {
        enabled = value;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void changed(ObservableValue<? extends String> obs, String oldVal, String newVal) {
        if (!enabled) {
            // System.out.println("FahrenheitChangeListener disabled, returning.");
            return;
        }
        // System.out.println("FahrenheitChangeListener: processing '" + newVal + "'");


        if (newVal == null || newVal.trim().isEmpty()) {
            if (otherListener != null) {
                // System.out.println("Fahrenheit: Empty input, disabling Celsius listener before clear.");
                otherListener.setEnabled(false);
            }
            celsiusOutputField.clear();
            if (otherListener != null) {
                // System.out.println("Fahrenheit: Empty input, re-enabling Celsius listener after clear.");
                otherListener.setEnabled(true);
            }
            return;
        }

        try {
            double fahrenheit = Double.parseDouble(newVal);
            double celsius = (fahrenheit - 32.0) * 5.0 / 9.0;

            if (otherListener != null) {
                // System.out.println("Fahrenheit: Disabling Celsius listener before setting text: " + String.format("%.1f", celsius));
                otherListener.setEnabled(false);
            }
            celsiusOutputField.setText(String.format("%.1f", celsius));

        } catch (NumberFormatException e) {
            if (otherListener != null) {
                // System.out.println("Fahrenheit: Invalid input, disabling Celsius listener before setting 'Invalid'.");
                otherListener.setEnabled(false);
            }
            celsiusOutputField.setText("Invalid");
        } finally {
            if (otherListener != null) {
                // System.out.println("Fahrenheit: In finally, re-enabling Celsius listener.");
                otherListener.setEnabled(true);
            }
        }
    }
}