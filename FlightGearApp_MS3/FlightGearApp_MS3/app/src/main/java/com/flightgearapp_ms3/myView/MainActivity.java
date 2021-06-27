package com.flightgearapp_ms3.myView;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.flightgearapp_ms3.myViewModel.MyViewModel;
import com.flightgearapp_ms3.R;
import com.flightgearapp_ms3.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    MyViewModel vm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // creates viewModel and make binding.
        vm = new MyViewModel();
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(vm);

        // define joystick's "onChange" method.
        int joystickId = R.id.Joystick;
        Joystick joystick = findViewById(joystickId);
        joystick.onChange = (a, e) -> {
            vm.setAileron(a);
            // we want positive y to be up, default is down.
            vm.setElevator(-e);
        };
    }

    public void connect(View view) {
        EditText ipText = findViewById(R.id.ip_insert);
        EditText portText = findViewById(R.id.port_insert);
        String ip = ipText.getText().toString();
        String portStr = portText.getText().toString();
        // ip and port inserted
        if (!ip.isEmpty() && !portStr.isEmpty()) {
            int port = Integer.parseInt(portStr);
            // vm send connect command to server
            vm.connect(ip, port);
            Toast.makeText(this, "connecting...", Toast.LENGTH_SHORT).show();
            // running waiting for success\fail Toast in other thread.
            Thread t = new Thread(this::isSuccess);
            t.start();
        } else {
            Toast.makeText(this, "Please insert ip/port...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * show toast whether connection succeed.
     */
    public void isSuccess() {
        int count = 0;
        Looper.prepare();
        // try 10 seconds (20 counts) to wait if server success connecting.
        while (count < 20) {
            if (vm.isSuccess()) {
                Toast.makeText(this, "connection succeed", Toast.LENGTH_LONG).show();
                break;
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
            }
        }
        if (count >= 20)
            Toast.makeText(this, "connection failed", Toast.LENGTH_LONG).show();
        Looper.loop();
    }
}