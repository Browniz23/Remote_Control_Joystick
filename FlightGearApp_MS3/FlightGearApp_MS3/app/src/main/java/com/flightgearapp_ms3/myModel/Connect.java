package com.flightgearapp_ms3.myModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Connect extends Thread {

    private boolean running = false;
    private String ip;
    private int port;
    private Socket fg = null;
    private PrintWriter out = null;

    public Connect(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    @Override
    public void run() {
        try {
            //create new socket, and use the socket to create printWriter obj,
            // use to send data to FG.
            System.out.println("try connecting to server..");
            fg = new Socket(ip, port);      //todo crush here on wrong ip\port when fg already have something
            out = new PrintWriter(fg.getOutputStream(), true);
            out.print("Hello Server!");
            out.flush();
            System.out.println("connecting to server succeed.");
            running = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrintWriter getOut() {
        return out;
    }

    public Socket getFg() {
        return fg;
    }

    public boolean getRunning() {
        return running;
    }
}