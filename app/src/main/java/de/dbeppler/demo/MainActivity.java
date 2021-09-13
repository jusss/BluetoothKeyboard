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
                    // unregister once app switch to background
                    hidDataSender.unregister(profileListener);
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
                    // register again when app switch back from background
                    hidDataSender.register(getApplicationContext(), profileListener);
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
                    // register again when app switch back from background
                    hidDataSender.register(getApplicationContext(), profileListener);
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
            if (keyboardHelper != null) keyboardHelper.pressedModifier.add(string);
        } else {
            for (BluetoothDevice device : BluetoothAdapter.getDefaultAdapter().getBondedDevices())
                if (TARGET_DEVICE_NAME.equals(device.getName())) {
                    Log.d(TAG, "Requesting connection to " + device.getName());
                    // register again when app switch back from background
                    hidDataSender.register(getApplicationContext(), profileListener);
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
