module com.example.smart_trilab {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.smart_trilab to javafx.fxml;
    exports com.example.smart_trilab;
}