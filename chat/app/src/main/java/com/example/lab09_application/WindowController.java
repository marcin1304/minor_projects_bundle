package com.example.lab09_application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import com.example.lab09_library.*;

import javax.crypto.IllegalBlockSizeException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class WindowController implements Initializable, MessageListener {
    @FXML
    private Button listenButton;
    @FXML
    private Button connectButton;
    @FXML
    private Button sendButton;

    @FXML
    private Label infoLabel;

    @FXML
    private TextField listenPortTextField;
    @FXML
    private TextField sendIpTextField;
    @FXML
    private TextField sendPortTextField;

    @FXML
    private TextArea messagesTextArea;
    @FXML
    private TextArea newMessageTextArea;

    private RsaEncryption rsaEncryption;
    private Sender sender;
    private Receiver receiver;

    private Integer listeningPort;
    private Integer sendPort;
    private String sendIp;

    private final DecimalFormat twoDigitsFormat = new DecimalFormat("00");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rsaEncryption = new RsaEncryption();
        sender = new Sender(rsaEncryption);
    }

    @FXML
    private void createListeningPortAction() {
        try {
            listeningPort = extractPort(listenPortTextField.getText());

            receiver = new Receiver(rsaEncryption, listeningPort);
            receiver.addMyListener(this);
            receiver.start();

            setGuiForConnectionEstablishment();
        } catch (IllegalArgumentException e) {
            showInformation(e.getMessage());
        }
    }

    private void setGuiForConnectionEstablishment() {
        listenButton.setDisable(true);
        listenPortTextField.setDisable(true);

        connectButton.setDisable(false);
    }

    @FXML
    private void establishConnectionAction() {
        try {
            sendPort = extractPort(sendPortTextField.getText());
            setIp(sendIpTextField.getText());
            sender.sendPublicKey(sendIp, sendPort);

            setGuiForMessageSending();
            showInformation("Connection established");
        } catch (IllegalArgumentException e) {
            showInformation(e.getMessage());
        }
    }

    private void setGuiForMessageSending() {
        connectButton.setDisable(true);
        sendIpTextField.setDisable(true);
        sendPortTextField.setDisable(true);

        sendButton.setDisable(false);
    }

    @FXML
    private void sendMessageAction() {
        try {
            String message = newMessageTextArea.getText();
            sender.sendMessage(message, sendIp, sendPort);
            addMessage("You ", message);

            newMessageTextArea.setText("");
        } catch (IllegalBlockSizeException e) {
            showInformation("Message is too long");
        }
    }

    private int extractPort(String rawPort) throws IllegalArgumentException {
        String sixDigitsRegex = "^\\d{1,6}$";
        if(!rawPort.matches(sixDigitsRegex))
            throw new IllegalArgumentException("Invalid port format");
        int port = Integer.parseInt(rawPort);
        int maxPortNumber = 65535;
        if(port > maxPortNumber)
            throw new IllegalArgumentException("Invalid port number");
        return port;
    }

    private void setIp(String rawIp) throws IllegalArgumentException {
        String ipRegex = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$";
        if(!rawIp.matches(ipRegex))
            throw new IllegalArgumentException("Invalid IP format");
        sendIp = rawIp;
    }

    private String getCurrentTime() {
        LocalTime now = LocalTime.now();
        return twoDigitsFormat.format(now.getHour()) + ":" + twoDigitsFormat.format(now.getMinute());
    }

    private void showInformation(String message) {
        infoLabel.setText(message);
    }

    private void addMessage(String nickNameOfSender, String rawMessage) {
        String message = getCurrentTime() + "\t" + nickNameOfSender + ":\t" + rawMessage + "\n";
        Platform.runLater(() -> showMessage(message));
    }

    private void showMessage(String message) {
        messagesTextArea.appendText(message);
        messagesTextArea.setScrollTop(Double.MAX_VALUE);
    }

    @Override
    public void messageReceived(String message) {
        addMessage("They", message);
    }
}
