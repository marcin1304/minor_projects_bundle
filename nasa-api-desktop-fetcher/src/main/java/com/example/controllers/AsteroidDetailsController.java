package com.example.controllers;

import com.example.networking.Asteroid;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.Format;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class AsteroidDetailsController implements Initializable {

    @FXML private Button goBackButton;

    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label nasaJplUrlLabel;
    @FXML private Label hazardousLabel;

    @FXML private Label closeApproachDateLabel;
    @FXML private Label velocityLabel;
    @FXML private Label missDistanceLabel;
    @FXML private Label orbitingBodyLabel;

    @FXML private Label minDiameterLabel;
    @FXML private Label maxDiameterLabel;

    @FXML private ResourceBundle resourceBundle;

    private Asteroid selectedAsteroid;

    public void initData(Asteroid asteroid){
        selectedAsteroid = asteroid;
        setTextFields();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;
    }

    public void goBackAction(ActionEvent event) throws IOException {
        ResourceBundle asteroidsResourceBundle = ResourceBundle.getBundle("AsteroidsProperties", resourceBundle.getLocale());
        Parent asteroidsViewParent = FXMLLoader.load(getClass().getResource("/fxmls/AsteroidsView.fxml"), asteroidsResourceBundle);
        Scene asteroidsViewScene = new Scene(asteroidsViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(asteroidsViewScene);
        window.show();
    }

    private void setTextFields() {
        MessageFormat messageFormat = prepareMessageFormat();

        messageFormat.applyPattern(resourceBundle.getString("id"));
        Object[] args = {selectedAsteroid.getId()};
        idLabel.setText(messageFormat.format(args));

        messageFormat.applyPattern(resourceBundle.getString("name"));
        args = new Object[]{selectedAsteroid.getName()};
        nameLabel.setText(messageFormat.format(args));

        messageFormat.applyPattern(resourceBundle.getString("nasaJplUrl"));
        args = new Object[]{selectedAsteroid.getId()};
        nasaJplUrlLabel.setText(messageFormat.format(args));

        messageFormat.applyPattern(resourceBundle.getString("hazardous"));
        args = new Object[]{selectedAsteroid.getId()};
        hazardousLabel.setText(messageFormat.format(args));

        messageFormat.applyPattern(resourceBundle.getString("date"));
        args = new Object[]{selectedAsteroid.getCloseApproachData().getCloseApproachDate()};
        closeApproachDateLabel.setText(messageFormat.format(args));

        messageFormat.applyPattern(resourceBundle.getString("velocity"));
        args = new Object[]{selectedAsteroid.getCloseApproachData().getRelativeVelocityInKmPerSecond()};
        velocityLabel.setText(messageFormat.format(args));

        messageFormat.applyPattern(resourceBundle.getString("missDistance"));
        args = new Object[]{selectedAsteroid.getCloseApproachData().getMissDistanceInAstronomicalUnits()};
        missDistanceLabel.setText(messageFormat.format(args));

        messageFormat.applyPattern(resourceBundle.getString("orbitingBody"));
        args = new Object[]{selectedAsteroid.getCloseApproachData().getOrbitingBody()};
        orbitingBodyLabel.setText(messageFormat.format(args));

        messageFormat.applyPattern(resourceBundle.getString("minDiameter"));
        args = new Object[]{selectedAsteroid.getEstimatedDiameter().getMinInKm()};
        minDiameterLabel.setText(messageFormat.format(args));

        messageFormat.applyPattern(resourceBundle.getString("maxDiameter"));
        args = new Object[]{selectedAsteroid.getEstimatedDiameter().getMaxInKm()};
        maxDiameterLabel.setText(messageFormat.format(args));

        goBackButton.setText(resourceBundle.getString("goBack"));
    }

    private MessageFormat prepareMessageFormat() {
        MessageFormat messageFormat = new MessageFormat("");
        messageFormat.setLocale(resourceBundle.getLocale());
        Format[] format = {null};
        messageFormat.setFormats(format);
        return messageFormat;
    }
}
