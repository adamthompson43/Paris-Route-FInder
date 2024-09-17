module com.routefinder.routefinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.routefinder.routefinder to javafx.fxml;
    exports com.routefinder.routefinder;
}