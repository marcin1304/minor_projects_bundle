package com.example.lab09_library;

import javax.crypto.IllegalBlockSizeException;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.*;
import java.util.*;

public class Receiver {
    private final List<MessageListener> messageListeners = new ArrayList<>();
    private Thread t = null;
    private int port = 0;
    private ServerSocket serverSocket;
    private final RsaEncryption rsaEncryption;

    public Receiver(RsaEncryption rsaEncryption, int port) {
        this.rsaEncryption = rsaEncryption;
        this.port = port;
    }

    public void stop() {
        t.interrupt();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        t = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);

                while(true) {
                    Socket socket = serverSocket.accept();
                    byte[] lenb = new byte[4];
                    socket.getInputStream().read(lenb,0,4);
                    ByteBuffer bb = ByteBuffer.wrap(lenb);
                    int len = bb.getInt();
                    byte[] message = new byte[len];
                    socket.getInputStream().read(message);

                    if(rsaEncryption.getEncryptionKey() == null) {
                        rsaEncryption.setEncryptionKey(generateRsaKey(message));
                    }
                    else {
                        String decryptedMessage = rsaEncryption.decryptMessage(message);
                        messageListeners.forEach(l -> l.messageReceived(decryptedMessage));
                    }
                    socket.close();
                }
            } catch(IOException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | IllegalBlockSizeException e){
                e.printStackTrace();
            }
        });
        t.start();
    }

    private PublicKey generateRsaKey(byte[] rawKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(rawKey));
    }

    private void setEncryptionKey(PublicKey encryptionKey) {
        rsaEncryption.setEncryptionKey(encryptionKey);
    }

    public void addMyListener(MessageListener m) {
        messageListeners.add(m);
    }

    public void removeMyListener(MessageListener m) {
        messageListeners.remove(m);
    }
}
