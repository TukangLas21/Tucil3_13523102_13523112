module com.tuciltiga {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;

    opens com.tuciltiga to javafx.fxml;
    opens com.tuciltiga.controller to javafx.fxml;
    
    exports com.tuciltiga;
    exports com.tuciltiga.controller;
}