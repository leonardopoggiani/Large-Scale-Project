module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    opens org.openjfx.View to javafx.fxml;
    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
}