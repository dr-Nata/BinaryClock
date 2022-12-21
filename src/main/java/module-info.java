module com.example.binaryclock {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.binaryclock to javafx.fxml;
    exports com.example.binaryclock;
}