package com.example.controllers;

import com.example.networking.DataNasaApi;
import com.example.networking.NasaPhoto;
import com.example.networking.NasaPhotoService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.*;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhotoOfDayController implements Initializable {

    @FXML private Button goToStartButton;
    @FXML private ImageView photoImageView;
    @FXML private Label titleLabel;

    private Locale selectedLocale;
    @FXML private ResourceBundle resourceBundle;

    public void initData(Locale locale) {
        selectedLocale = locale;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;
        loadLanguageFields();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DataNasaApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        NasaPhotoService service = retrofit.create(NasaPhotoService.class);
        Call<NasaPhoto> callSync = service.getPhotoUrl();

        try {

            Response<NasaPhoto> response = callSync.execute();
            NasaPhoto nasaPhoto = response.body();
            Image image = new Image(nasaPhoto.getUrl());
            photoImageView.setImage(image);

            titleLabel.setText(nasaPhoto.getTitle());

        } catch (Exception ex) {  }
    }

    private void loadLanguageFields() {
        goToStartButton.setText(resourceBundle.getString("goToStart"));
    }

    public void moveToStart(ActionEvent event) throws IOException {
        ResourceBundle startResourceBundle = ResourceBundle.getBundle("StartProperties", resourceBundle.getLocale());
        Parent startViewParent = FXMLLoader.load(getClass().getResource("/fxmls/StartView.fxml"), startResourceBundle);
        Scene startViewScene = new Scene(startViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(startViewScene);
        window.show();
    }
}