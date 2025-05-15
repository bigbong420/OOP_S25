package com.example.temp_converter;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ConverterController {

    @FXML private TextField celsiusInput;
    @FXML private TextField fahrenheitOutput;

    private CelsiusChangeListener celsiusListener;
    private FahrenheitChangeListener fahrenheitListener;

    @FXML
    public void initialize() {
        celsiusListener = new CelsiusChangeListener(fahrenheitOutput);
        fahrenheitListener = new FahrenheitChangeListener(celsiusInput);


        celsiusListener.setOtherListener(fahrenheitListener);
        fahrenheitListener.setOtherListener(celsiusListener);

        celsiusInput.textProperty().addListener(celsiusListener);
        fahrenheitOutput.textProperty().addListener(fahrenheitListener);
    }
}