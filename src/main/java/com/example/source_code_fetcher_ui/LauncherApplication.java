package com.example.source_code_fetcher_ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LauncherApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LauncherApplication.class.getResource("launcher-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 350);
        stage.setTitle("HTML Source Code Fetcher");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}