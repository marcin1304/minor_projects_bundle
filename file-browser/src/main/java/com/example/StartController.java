package com.example;

import com.example.file_browser.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.WeakHashMap;

public class StartController implements Initializable {

    @FXML private ListView<String> filesListView;
    @FXML private Pane filePane;
    @FXML private Label currentDirectoryLabel;
    @FXML private Label loadSourceLabel;

    @FXML private ResourceBundle resourceBundle;

    private File currentDirectory;
    private final String parentRepresentation = "...";
    private final String startDirectoryArgument = "user.dir";
    private WeakHashMap<File, Object> dataWeakHashMap;

    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;

        String startDirectory = System.getProperty(startDirectoryArgument);
        setCurrentDirectory(startDirectory);

        filesListView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2)
                userAction();
        });

        filesListView.setOnKeyPressed(event -> {
            if(event.getCode().getName().equals("Enter"))
                userAction();
            else if(event.getCode().getName().equals("Backspace"))
                setParentDirectoryAsCurrent();
        });

        dataWeakHashMap = new WeakHashMap<>();
    }

    private void setCurrentDirectory(String directory) {
        File newCurrentDirectory = new File(directory);
        if(newCurrentDirectory.isDirectory()) {
            currentDirectory = newCurrentDirectory;
            loadCurrentDirectoryFiles();
            showCurrentDirectory();
        }
    }

    private void setParentDirectoryAsCurrent() {
        if(currentDirectory.getParent() != null)
            setCurrentDirectory(currentDirectory.getParent());
    }

    private void showCurrentDirectory() {
        String directoryPrefix = resourceBundle.getString("currentDirectory");
        currentDirectoryLabel.setText(directoryPrefix + currentDirectory.toString());
    }

    private void showFileLoadSource(String fileName, String source) {
        loadSourceLabel.setText(fileName + resourceBundle.getString("loadedFrom") + source);
    }

    private void loadCurrentDirectoryFiles() {
        ObservableList<String> files = FXCollections.observableArrayList();
        files.add(parentRepresentation);
        files.addAll(currentDirectory.list());

        filesListView.setItems(files);
    }

    private Boolean isParentRepresentation(String string) {
        return string.equals(parentRepresentation);
    }

    private void userAction() {
        String selectedItem = filesListView.getSelectionModel().getSelectedItem();

        if(selectedItem != null) {
            File selectedFile = new File(currentDirectory.toString() + "\\" + selectedItem);

            if(isParentRepresentation(selectedItem))
                setParentDirectoryAsCurrent();

            else if(selectedFile.exists()) {
                if(selectedFile.isDirectory())
                    setCurrentDirectory(selectedFile.toString());
                else if(selectedFile.isFile()) {
                    getFile(selectedFile);
                }
            }
        }
    }

    private void getFile(File file) {
        Object fileData = dataWeakHashMap.get(file);
        String sourceHashMap = resourceBundle.getString("sourceHashMap");
        String sourceOriginal = resourceBundle.getString("sourceOriginal");

        if(fileData == null) {
            fileData = loadFileFromSource(file);
            dataWeakHashMap.put(file, fileData);
            showFileLoadSource(file.getName(), sourceOriginal);
        }
        else
            showFileLoadSource(file.getName(), sourceHashMap);

        showNode(FileTransformer.getNode(FilenameUtils.getExtension(file.getName()), fileData));
    }

    private Object loadFileFromSource(File file) {
        if(file.canRead()) {
            String fileExtension = FilenameUtils.getExtension(file.toString());
            if(fileExtension.equals("jpg") || fileExtension.equals("png"))
                return FileLoader.getImage(file);
            else
                return FileLoader.getTextFile(file);
        }
        else
            showNode(getFileInformationLabel(resourceBundle.getString("noReadPermission")));
        return null;
    }

    private Label getFileInformationLabel(String information) {
        Label label = new Label();
        label.setText(information);
        return label;
    }

    private void showNode(Node node) {
        filePane.getChildren().clear();
        if(node instanceof ImageView) {
            ((ImageView) node).setFitHeight(filePane.getHeight());
            ((ImageView) node).setFitWidth(filePane.getWidth());
        }
        else if(node instanceof TextArea) {
            ((TextArea) node).setWrapText(true);
            ((TextArea) node).setPrefWidth(filePane.getWidth());
            ((TextArea) node).setPrefHeight(filePane.getHeight());
        }
        filePane.getChildren().add(node);
    }
}
