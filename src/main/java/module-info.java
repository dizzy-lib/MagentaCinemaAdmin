module org.example.magentacinema {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires io.github.cdimascio.dotenv.java;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;
    requires mysql.connector.j;

    opens org.example.magentacinema to javafx.fxml;
    exports org.example.magentacinema;
    // Abre el paquete de entidades para que JavaFX pueda acceder por reflexión
    opens org.example.magentacinema.domain.entities to javafx.base;
    exports org.example.magentacinema.presentation.controller;
    opens org.example.magentacinema.presentation.controller to javafx.fxml;
    exports org.example.magentacinema.presentation.view;
    opens org.example.magentacinema.presentation.view to javafx.fxml;
}