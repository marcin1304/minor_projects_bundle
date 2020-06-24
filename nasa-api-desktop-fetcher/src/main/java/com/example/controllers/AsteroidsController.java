package com.example.controllers;

import com.example.networking.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;



import java.io.IOException;
import java.net.URL;
import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.time.temporal.ChronoUnit.DAYS;

public class AsteroidsController implements Initializable {

    @FXML private Button goToStartButton;
    @FXML private Button goToDetailsButton;
    @FXML private Button loadAsteroidsButton;

    @FXML private Label titleLabel;
    @FXML private Label userInfoLabel;
    @FXML private Label allAsteroidsLabel;

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;

    @FXML private TableView<Asteroid> asteroidsTableView;

    @FXML private TableColumn<Asteroid, String> idColumn;
    @FXML private TableColumn<Asteroid, String> nameColumn;
    @FXML private TableColumn<Asteroid, String> hazardousColumn;

    @FXML private ResourceBundle resourceBundle;

    private AsteroidApiData asteroidApiData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;

        loadTextFields();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        hazardousColumn.setCellValueFactory(new PropertyValueFactory<>("potentiallyHazardous"));
    }

    private void loadTextFields() {
        goToStartButton.setText(resourceBundle.getString("goToStart"));
        goToDetailsButton.setText(resourceBundle.getString("goToDetails"));
        loadAsteroidsButton.setText(resourceBundle.getString("search"));

        titleLabel.setText(resourceBundle.getString("title"));
        userInfoLabel.setText("");
        allAsteroidsLabel.setText("");

        idColumn.setText(resourceBundle.getString("idColumn"));
        nameColumn.setText(resourceBundle.getString("nameColumn"));
        hazardousColumn.setText(resourceBundle.getString("hazardousColumn"));
    }

    private void informUser(String info) {
        userInfoLabel.setText(info);
    }

    public void moveToStart(ActionEvent event) throws IOException {
        ResourceBundle startResourceBundle = ResourceBundle.getBundle("StartProperties", resourceBundle.getLocale());
        Parent startViewParent = FXMLLoader.load(getClass().getResource("/fxmls/StartView.fxml"), startResourceBundle);
        Scene startViewScene = new Scene(startViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(startViewScene);
        window.show();
    }

    public void searchAction() {
        if(startDatePicker.getValue() == null || endDatePicker.getValue() == null)
            informUser(resourceBundle.getString("needToChooseDateInfo"));
        else {
            long daysBetween = DAYS.between(startDatePicker.getValue(), endDatePicker.getValue());
            if(daysBetween<0)
                informUser(resourceBundle.getString("wrongDatesInfo"));
            else {
                if(daysBetween >= 7)
                    informUser(resourceBundle.getString("tooManyDaysInfo"));
                else {
                    try {
                        getNewAsteroidApiData();
                        informUser(resourceBundle.getString("dataLoadedSuccessfully"));
                        asteroidsTableView.setItems(getAllAsteroids());
                    } catch (IOException e) {
                        informUser(resourceBundle.getString("connectionProblemInfo"));
                    }
                }
            }
        }
    }

    public void detailsAction(ActionEvent event) throws IOException {
        if (asteroidsTableView.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/AsteroidDetailsView.fxml"));
            loader.setResources(ResourceBundle.getBundle("AsteroidDetails", resourceBundle.getLocale()));
            Parent asteroidDetailsViewParent = loader.load();

            Scene asteroidDetailsViewScene = new Scene(asteroidDetailsViewParent);

            AsteroidDetailsController asteroidDetailsController = loader.getController();
            asteroidDetailsController.initData(asteroidsTableView.getSelectionModel().getSelectedItem());

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(asteroidDetailsViewScene);
            window.show();
        }
    }

    private void getNewAsteroidApiData() throws IOException {
        asteroidApiData = new AsteroidApiData(startDatePicker.getValue().toString(), endDatePicker.getValue().toString());
        allAsteroidsLabel.setText(getAsteroidNumberFormatted(resourceBundle, asteroidApiData.getElementCount()));
    }

    private ObservableList<Asteroid> getAllAsteroids() {
        List<Asteroid> asteroids = new ArrayList<>();

        asteroidApiData.getDateAsteroidsMap().forEach((k, v) -> asteroids.addAll(v));

        return FXCollections.observableArrayList(asteroids);
    }

    private String getAsteroidNumberFormatted(ResourceBundle resources, Integer numberOfAsteroids) {
        MessageFormat messageFormat = new MessageFormat("");
        messageFormat.setLocale(resources.getLocale());

        double[] fileLimits1 = {0, 1};
        String[] fileStrings1 = {
                resources.getString("noAsteroidsNumber"),
                resources.getString("pluralAsteroidNumber")
        };
        ChoiceFormat choiceFormat1 = new ChoiceFormat(fileLimits1, fileStrings1);

        double[] fileLimits2 = {0,1,2,5};
        String[] fileStrings2 = {
                resources.getString("pluralAsteroids"),
                resources.getString("oneAsteroid"),
                resources.getString("specialPluralAsteroids"),
                resources.getString("pluralAsteroids")};
        ChoiceFormat choiceFormat2 = new ChoiceFormat(fileLimits2, fileStrings2);

        String pattern = resources.getString("asteroidsNumberPattern");
        messageFormat.applyPattern(pattern);

        Format[] formats = {choiceFormat1, choiceFormat2, NumberFormat.getInstance()};
        messageFormat.setFormats(formats);

        Object[] args = {numberOfAsteroids, numberOfAsteroids, numberOfAsteroids};

        return messageFormat.format(args);
    }
}
