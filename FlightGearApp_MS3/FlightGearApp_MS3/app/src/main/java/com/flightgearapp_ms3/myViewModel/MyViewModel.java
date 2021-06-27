package com.flightgearapp_ms3.myViewModel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.flightgearapp_ms3.myModel.Model;

import java.io.IOException;

public class MyViewModel extends BaseObservable {

    private final Model model;      // made final
    private int xSeekBar;
    private int ySeekBar;
    private boolean running;

    public MyViewModel() {
        model = new Model();
        running = false;
    }

    @Bindable
    public int getXSeekBar() {
        return xSeekBar;
    }

    @Bindable
    public int getYSeekBar() {
        return ySeekBar;
    }

    public void setXSeekBar(int val) {
        if (running) {
            xSeekBar = val;
            // 1056964608 - floats between 0 and 1. divide pos nums for pos x.
            model.setRudder((float)xSeekBar / 1056964608);
        }
    }

    public void setYSeekBar(int val) {
        if (running) {
            ySeekBar = val;
            // 1056964608 - floats between 0 and 1. divide pos nums for pos y
            model.setThrottle((float)ySeekBar / 1056964608);
        }
    }

    public void setAileron(float val) {
        if (running)
            model.setAileron(val);
    }

    public void setElevator(float val) {
        if (running)
            model.setElevator(val);
    }

    public void connect(String ip, int port) {
        if (running) {
            model.stopConnect();
            running = false;
        }
        model.connect(ip, port);
    }

    public boolean isSuccess() {
        running = model.getRunning();
        return running;
    }
}
