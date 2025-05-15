module com.example.temp_converter {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.temp_converter to javafx.fxml;
    exports com.example.temp_converter;
}