module com.example.smart_trilab {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.java;

    opens com.example.smart_trilab to javafx.fxml;
    exports com.example.smart_trilab;
}