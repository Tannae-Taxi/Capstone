package com.example.tannae.network;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Network {
    public static ServiceApi service;
    public static Socket socket;

    static {
        try {
            socket = IO.socket("http://15.164.225.233:3000/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    };
}
