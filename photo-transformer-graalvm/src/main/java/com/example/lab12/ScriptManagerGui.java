package com.example.lab12;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScriptManagerGui extends JFrame {
    private ImageIcon originalImage;
    private ImageIcon transformedImage;

    private final JLabel originalImageLabel = new JLabel();
    private final JLabel transformedImageLabel = new JLabel();

    private final JPanel scriptPanel = new JPanel();
    private final JLabel loadedScriptNameLabel = new JLabel();
    private final JButton loadScriptButton = new JButton("Load script");
    private final JButton executeScriptButton = new JButton("Execute script");
    private final JButton unloadScriptButton = new JButton("Unload script");

    private List<ScriptListener> scriptListeners = new ArrayList<>();
    private JFileChooser jFileChooser = new JFileChooser();

    public ScriptManagerGui() {
        setSize(1000,750);
        setLayout(new FlowLayout());

        add(originalImageLabel);
        add(transformedImageLabel);

        setUpScriptPanel();
        add(scriptPanel);

        jFileChooser.setFileFilter(new FileNameExtensionFilter("JavaScript files", "js"));
        jFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void setUpScriptPanel() {
        scriptPanel.setLayout(new FlowLayout());

        loadedScriptNameLabel.setText("No loaded script");
        scriptPanel.add(loadedScriptNameLabel);

        loadScriptButton.addActionListener(e -> loadScript());
        scriptPanel.add(loadScriptButton);

        executeScriptButton.addActionListener(e -> executeScriptInformListeners());
        executeScriptButton.setEnabled(false);
        scriptPanel.add(executeScriptButton);

        unloadScriptButton.addActionListener(e -> unloadScriptInformListeners());
        scriptPanel.add(unloadScriptButton);
    }

    private void loadScript() {
        int returnVal = jFileChooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File scriptFile = jFileChooser.getSelectedFile();
            loadScriptInformListeners(scriptFile);
            executeScriptButton.setEnabled(true);
            loadedScriptNameLabel.setText(scriptFile.getName());
        }
    }

    public void registerScriptListener(ScriptListener scriptListener) {
        scriptListeners.add(scriptListener);
    }

    private void loadScriptInformListeners(File scriptFile) {
        for(ScriptListener scriptListener : scriptListeners) {
            try {
                scriptListener.loadScript(scriptFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void executeScriptInformListeners() {
        for(ScriptListener scriptListener : scriptListeners)
            scriptListener.executeLoadedScript();
    }

    private void unloadScriptInformListeners() {
        executeScriptButton.setEnabled(false);
        for(ScriptListener scriptListener : scriptListeners)
            scriptListener.unloadScript();
        loadedScriptNameLabel.setText("No loaded script");
    }

    public void setOriginalImage(BufferedImage image) {
        originalImage = new ImageIcon(getScaledImage(image));
        originalImageLabel.setIcon(originalImage);
    }

    public void setTransformedImage(BufferedImage image) {
        transformedImage = new ImageIcon(getScaledImage(image));
        transformedImageLabel.setIcon(transformedImage);
    }

    private Image getScaledImage(BufferedImage image) {
        return image.getScaledInstance(600,450, Image.SCALE_DEFAULT);
    }
}
