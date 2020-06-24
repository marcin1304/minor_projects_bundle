package com.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    @FXML private Button goToPhotoOfDayButton;
    @FXML private Button goToAsteroidsButton;

    @FXML private ComboBox<Locale> chooseLanguageComboBox;

    @FXML private Label startInformationLabel;

    @FXML private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        resourceBundle = resources;

        completeChooseLanguageComboBox(resources.getLocale());

        reloadLanguageFields();
    }

    private void setResourceLanguage(Locale locale) {
        resourceBundle = ResourceBundle.getBundle(resourceBundle.getBaseBundleName(), locale);
    }

    private void completeChooseLanguageComboBox(Locale defaultLanguage) {
        ObservableList<Locale> availableLanguagesList = FXCollections.observableArrayList();
        availableLanguagesList.add(new Locale("en", "EN"));
        availableLanguagesList.add(new Locale("pl", "PL"));

        chooseLanguageComboBox.setItems(availableLanguagesList);
        chooseLanguageComboBox.getSelectionModel().select(defaultLanguage);
    }

    private void reloadLanguageFields() {
        startInformationLabel.setText(resourceBundle.getString("information"));
        goToPhotoOfDayButton.setText(resourceBundle.getString("goToPhotoOfDay"));
        goToAsteroidsButton.setText(resourceBundle.getString("goToAsteroids"));
    }

    public void changeLanguage() {
        Locale chosenLanguage = chooseLanguageComboBox.getValue();
        setResourceLanguage(chosenLanguage);

        reloadLanguageFields();
    }

    public void moveToPhotoOfDay(ActionEvent event) throws IOException {
        ResourceBundle photoOfDayResourceBundle = ResourceBundle.getBundle("PhotoOfDayProperties", resourceBundle.getLocale());
        Parent photoOfDayViewParent = FXMLLoader.load(getClass().getResource("/fxmls/PhotoOfDayView.fxml"), photoOfDayResourceBundle);
        Scene photoOfDayViewScene = new Scene(photoOfDayViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(photoOfDayViewScene);
        window.show();
    }

    public void moveToAsteroids(ActionEvent event) throws IOException {
        ResourceBundle asteroidsResourceBundle = ResourceBundle.getBundle("AsteroidsProperties", resourceBundle.getLocale());
        Parent asteroidsViewParent = FXMLLoader.load(getClass().getResource("/fxmls/AsteroidsView.fxml"), asteroidsResourceBundle);
        Scene asteroidsViewScene = new Scene(asteroidsViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(asteroidsViewScene);
        window.show();
    }
}
