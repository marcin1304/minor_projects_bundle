package com.example.lab09_application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxmls/WindowView.fxml"));
        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("FXML Welcome");
        stage.setScene(scene);
        stage.show();
    }

}