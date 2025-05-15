package com.example.temp_converter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class CelsiusChangeListener implements ChangeListener<String> {

    private final TextField fahrenheitOutputField;
    private FahrenheitChangeListener otherListener;
    private boolean enabled = true;

    public CelsiusChangeListener(TextField fahrenheitOutputField) {
        this.fahrenheitOutputField = fahrenheitOutputField;
    }

    public void setOtherListener(FahrenheitChangeListener fahrenheitListener) {
        this.otherListener = fahrenheitListener;
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
            // System.out.println("CelsiusChangeListener disabled, returning.");
            return;
        }
        // System.out.println("CelsiusChangeListener: processing '" + newVal + "'");

        if (newVal == null || newVal.trim().isEmpty()) {
            if (otherListener != null) {
                // System.out.println("Celsius: Empty input, disabling Fahrenheit listener before clear.");
                otherListener.setEnabled(false);
            }
            fahrenheitOutputField.clear();
            if (otherListener != null) {
                // System.out.println("Celsius: Empty input, re-enabling Fahrenheit listener after clear.");
                otherListener.setEnabled(true);
            }
            return;
        }

        try {
            double celsius = Double.parseDouble(newVal);
            double fahrenheit = (celsius * 9.0 / 5.0) + 32.0;

            if (otherListener != null) {
                // System.out.println("Celsius: Disabling Fahrenheit listener before setting text: " + String.format("%.1f", fahrenheit));
                otherListener.setEnabled(false);
            }
            fahrenheitOutputField.setText(String.format("%.1f", fahrenheit));

        } catch (NumberFormatException e) {
            if (otherListener != null) {
                // System.out.println("Celsius: Invalid input, disabling Fahrenheit listener before setting 'Invalid'.");
                otherListener.setEnabled(false);
            }
            fahrenheitOutputField.setText("Invalid");
        } finally {
            if (otherListener != null) {
                // System.out.println("Celsius: In finally, re-enabling Fahrenheit listener.");
                otherListener.setEnabled(true);
            }
        }
    }
}