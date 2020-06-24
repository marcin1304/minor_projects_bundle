package com.example.file_browser;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FileTransformer {
    public static Node getNode(String fileExtension, Object data) {
        if(fileExtension.equals("jpg") || fileExtension.equals("png"))
            return getImage(data);
        else
            return getText(data);
    }

    private static ImageView getImage(Object data) {
        Image image = (Image) data;
        return new ImageView(image);
    }

    private static TextArea getText(Object data) {
        TextArea textArea = new TextArea();
        textArea.setText((String) data);
        return textArea;
    }
}
