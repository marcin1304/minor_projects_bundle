package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Locale defaultLanguage = new Locale("en", "EN");
        ResourceBundle resourceBundle = ResourceBundle.getBundle("StartProperties", defaultLanguage);
        Parent root = FXMLLoader.load(getClass().getResource("/fxmls/StartView.fxml"), resourceBundle);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
