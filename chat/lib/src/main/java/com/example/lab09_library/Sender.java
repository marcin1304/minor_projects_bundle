package com.example.lab09_library;

import javax.crypto.IllegalBlockSizeException;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;

public class Sender{
    private RsaEncryption rsaEncryption;

    public Sender(RsaEncryption rsaEncryption) {
        this.rsaEncryption = rsaEncryption;
    }

    public void sendPublicKey(String host, int port) {
        send(rsaEncryption.getMyPublicKey().getEncoded(), host, port);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message, String host, int port) throws IllegalBlockSizeException{
        try {
            byte[] encodedMessage = rsaEncryption.encryptMessage(message.getBytes());
            send(encodedMessage, host, port);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    private void send(byte[] message, String host, int port){
        Socket s;
        try {
            s = new Socket(host,port);
            OutputStream out = s.getOutputStream();
            ByteBuffer bb = ByteBuffer.allocate(4);
            bb.putInt(message.length);
            out.write(bb.array());
            out.write(message);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
