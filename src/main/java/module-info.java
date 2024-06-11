module com.example.test_appliances {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.test_appliances to javafx.fxml;
    exports com.example.test_appliances;
}