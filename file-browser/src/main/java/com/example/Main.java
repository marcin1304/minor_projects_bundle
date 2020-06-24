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
    public void start(Stage primaryStage) throws Exception {
        Locale locale = Locale.forLanguageTag("en");
        Parent root = FXMLLoader.load(getClass().getResource("/fxmls/StartView.fxml"),
                ResourceBundle.getBundle("startResourceBundle", locale));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("File Browser");
        primaryStage.setHeight(600);
        primaryStage.setWidth(800);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
