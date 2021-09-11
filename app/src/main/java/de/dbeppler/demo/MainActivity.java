package de.dbeppler.demo;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import de.dbeppler.demo.bluetooth.HidDataSender;
import de.dbeppler.demo.input.KeyboardHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BluetoothHidDemo";
    private static final String TARGET_DEVICE_NAME = "IPC6308"; // insert target device here

    private HidDataSender hidDataSender;
    private KeyboardHelper keyboardHelper;

    private final HidDataSender.ProfileListener profileListener =
            new HidDataSender.ProfileListener() {
                @Override
                @MainThread
                public void onDeviceStateChanged(BluetoothDevice device, int state) {
                    // 0 = disconnected, 1 = connecting, 2 = connected
                    Log.d(TAG, "device state changed to " + state);
                }

                @Override
                @MainThread
                public void onAppUnregistered() {
                    Log.v(TAG, "app unregistered");
                }

                @Override
                @MainThread
                public void onServiceStateChanged(BluetoothProfile proxy) {
                    Log.v(TAG, "service state changed to" + proxy.toString());
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        hidDataSender = HidDataSender.getInstance();
        hidDataSender.register(getApplicationContext(), profileListener);

        keyboardHelper = new KeyboardHelper(hidDataSender);

    }

    public void sendMessage(View view) {
//        String message = "TestMessage";
        Button b = (Button) view;
        String message = b.getText().toString();

        if (hidDataSender.isConnected()) {
            Log.d(TAG, "Sending message: " + message);
            sendString(message);
        } else {
            for (BluetoothDevice device : BluetoothAdapter.getDefaultAdapter().getBondedDevices())
                if (TARGET_DEVICE_NAME.equals(device.getName())) {
                    Log.d(TAG, "Requesting connection to " + device.getName());
                    hidDataSender.requestConnect(device);
                }
        }
    }

    // send normal char like a,b,c
    public void sendChar(View view) {
        Button b = (Button) view;
        char c = b.getText().toString().charAt(0);
        if (hidDataSender.isConnected()) {
            Log.d(TAG, "Sending message: " + c);
            if (keyboardHelper.pressedModifier.isEmpty()){
                keyboardHelper.sendChar(c);
            }
            else {
                keyboardHelper.sendModifierKey(c);
            }
        } else {
            for (BluetoothDevice device : BluetoothAdapter.getDefaultAdapter().getBondedDevices())
                if (TARGET_DEVICE_NAME.equals(device.getName())) {
                    Log.d(TAG, "Requesting connection to " + device.getName());
                    hidDataSender.requestConnect(device);
                }
        }
    }

    // send special char like enter, esc
    public void sendSpecialChar(View view) {
        Button b = (Button) view;
        String string = b.getText().toString();
        if (hidDataSender.isConnected()) {
            Log.d(TAG, "Sending message: " + string);
            if (keyboardHelper != null){
                        if (keyboardHelper.pressedModifier.isEmpty()){
                            keyboardHelper.sendSpecialKey(string);
                        }
                        else {
                            keyboardHelper.sendModifierKey(string);
                        }
            }
        } else {
            for (BluetoothDevice device : BluetoothAdapter.getDefaultAdapter().getBondedDevices())
                if (TARGET_DEVICE_NAME.equals(device.getName())) {
                    Log.d(TAG, "Requesting connection to " + device.getName());
                    hidDataSender.requestConnect(device);
                }
        }
    }

    // send modifier like ctrl,shift and alt
    public void sendModifierChar(View view) {
        Button b = (Button) view;
        String string = b.getText().toString();
        if (hidDataSender.isConnected()) {
            Log.d(TAG, "Sending message: " + string);
            if (keyboardHelper != null)
            {
//                Log.d(TAG,"string is " + string);
                keyboardHelper.pressedModifier.add(string);
//                for (String c: keyboardHelper.pressedModifier)
//                    Log.d(TAG,"list is " + c);


//                if (string.equals("Shift")) keyboardHelper.pressedModifier.add("Shift");
//                if (string.equals("Shift")) keyboardHelper.pressedModifier.add(string);
//
//                if (string == "Ctrl") keyboardHelper.pressedModifier.add(string);
//                if (string == "Alt") keyboardHelper.pressedModifier.add(string);
//                if (string == "Win") keyboardHelper.pressedModifier.add(string);
//                for (String c: keyboardHelper.pressedModifier)
//                    Log.d(TAG,"list is " + c);
            }
//                switch (string){
//                    case "Ctrl":
//                        keyboardHelper.pressedModifier.add("Ctrl");
//                        break;
//                    case "Shift":
//                        keyboardHelper.pressedModifier.add("Shift");
//                        break;
//                    case "Alt":
//                        keyboardHelper.pressedModifier.add("Alt");
//                        break;
//                    case "Win":
//                        keyboardHelper.pressedModifier.add("Win");
//                        break;
//                }

        } else {
            for (BluetoothDevice device : BluetoothAdapter.getDefaultAdapter().getBondedDevices())
                if (TARGET_DEVICE_NAME.equals(device.getName())) {
                    Log.d(TAG, "Requesting connection to " + device.getName());
                    hidDataSender.requestConnect(device);
                }
        }
    }



    /*
     * helper method to send multiple characters
     */
    private void sendString(String string) {
        if (keyboardHelper != null){
                            for (char c : string.toCharArray())
                                keyboardHelper.sendChar(c);
        }
    }
}
