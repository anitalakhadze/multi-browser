module com.example.source_code_fetcher_ui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;
    requires selenium.api;
    requires selenium.firefox.driver;

    opens com.example.source_code_fetcher_ui to javafx.fxml;
    exports com.example.source_code_fetcher_ui;
}