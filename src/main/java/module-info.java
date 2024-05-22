module ws23.Two {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires javafx.media;

    opens ws23.Version_Two to javafx.fxml;
    exports ws23.Version_Two;
    exports ws23.Version_Two.Model;
    opens ws23.Version_Two.Model to javafx.fxml;
}