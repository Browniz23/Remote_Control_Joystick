package com.flightgearapp_ms3.myModel;

import java.io.PrintWriter;

public class Send extends Thread {


    private String msg;
    private PrintWriter out = null;


    //CTOR
    Send(String msg, PrintWriter out) {
        this.msg = msg;
        this.out =out;
    }

    /**
     * send the msg got in CTOR
     */
    @Override
    public void run() {
        out.print(msg);
        out.flush();
    }
}