package com.flightgearapp_ms3.myModel;

import java.io.IOException;



public class Model {
    // connection contains Socket and PrintWriter.
    private Connect connection;

    /**
     * connect to the simulator.
     * @param ip
     * @param port
     * returns true if connection succeed.
     */
    public void connect(String ip, int port) {
        connection = new Connect(ip, port);
        connection.start();
    }

    /**
     * close the connection with FG.
     */
    public void stopConnect(){
        try {
            connection.getOut().close();
            connection.getFg().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * send to flight simulator the value of the aileron.
     * @param val
     */
    public void setAileron(double val){
        if (connection.getOut() == null)
            return;
        String msg = "set /controls/flight/aileron "+val+"\r\n";
        Send send = new Send(msg, connection.getOut());
        send.start();
    }

    /***
     * send to flight simulator the value of the elevator.
     * @param val
     */
    public void setElevator(double val){
        if (connection.getOut() == null)
            return;
        String msg = "set /controls/flight/elevator "+val+"\r\n";
        Send send = new Send(msg, connection.getOut());
        send.start();
    }

    /***
     * send to flight simulator the value of the rudder.
     * @param val
     */
    public void setRudder(double val){
        if (connection.getOut() == null)
            return;
        String msg = "set /controls/flight/rudder "+val+"\r\n";
        Send send = new Send(msg, connection.getOut());
        send.start();
    }

    /***
     * send to flight simulator the value of the throttle.
     * @param val
     */
    public void setThrottle(double val){
        if (connection.getOut() == null)
            return;
        String msg = "set /controls/engines/current-engine/throttle "+val+"\r\n";
        Send send = new Send(msg, connection.getOut());
        send.start();
    }

    public boolean getRunning() { return connection.getRunning(); }
}