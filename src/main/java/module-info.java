module aaGame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires xstream;
    requires javafx.graphics;
    requires javafx.swing;
    requires javafx.media;
    
    exports view;
    exports model;
    opens view to javafx.fxml;
    opens model to xstream;
}