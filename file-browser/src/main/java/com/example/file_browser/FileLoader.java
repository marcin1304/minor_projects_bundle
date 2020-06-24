package com.example.file_browser;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Stream;

public class FileLoader {
    public static Image getImage(File file) {
        return new Image(file.toURI().toString());
    }

    public static String getTextFile(File file) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
}
