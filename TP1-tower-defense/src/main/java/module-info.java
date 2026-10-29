module org.towerdefense {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.xml;

    opens org.towerdefense.vista to javafx.graphics, javafx.fxml;
    opens org.towerdefense.vista.controlador to javafx.fxml;
}
