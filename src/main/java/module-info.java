module com.example.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.j;
    requires fontawesomefx;


    opens com.example.cs262 to javafx.fxml;
    exports com.example.cs262;
}