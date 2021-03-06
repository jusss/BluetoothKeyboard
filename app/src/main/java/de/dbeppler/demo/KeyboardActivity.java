package de.dbeppler.demo;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import de.dbeppler.demo.bluetooth.HidDataSender;
import de.dbeppler.demo.bluetooth.HidDeviceProfile;
import de.dbeppler.demo.input.KeyboardHelper;

public class KeyboardActivity extends AppCompatActivity {

    private static final String TAG = "BluetoothHidDemo";
//    private static final String TARGET_DEVICE_NAME = "IPC6308"; // insert target device here
    private static String TARGET_DEVICE_NAME = null;
    private Boolean repeat = false;
    Handler handler;
    Runnable runRepeat;

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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//           moveTaskToBack(false);
//           return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    // press back button return home
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
//        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent =getIntent();
        ArrayList<String> name = (ArrayList<String>) intent.getSerializableExtra("name");

        TARGET_DEVICE_NAME  = name.get(0);
        String screenSize = name.get(1);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

//        Configuration config = getResources().getConfiguration();
//        Log.d("TAG", "width is " + config.screenWidthDp);
//        Log.d("TAG", "height is " + config.screenHeightDp);
//        if (config.screenWidthDp >= 720) {
//            setContentView(R.layout.activity_keyboard_rn7);
//        } else {
//            setContentView(R.layout.activity_keyboard_r2);
//        }

        //redmi note 7, 2340 x 1080, 409 ppi   w860 h371
        //redmi 2, 1280 x 720, 312 ppi   w640 h336

//        SharedPreferences userInfo = getSharedPreferences("LatestConnectedBluetooth", MODE_PRIVATE);
//        SharedPreferences.Editor editor = userInfo.edit();

        if (screenSize.equals("1280x720"))
            setContentView(R.layout.activity_keyboard_1280x720);

        if (screenSize.equals("2340x1080"))
            setContentView(R.layout.activity_keyboard_2340x1080);

        if (screenSize.equals("1280x720_large"))
            setContentView(R.layout.activity_keyboard_1280x720_large);

        hidDataSender = HidDataSender.getInstance();

        HidDeviceProfile hidDeviceProfile = hidDataSender.register(getApplicationContext(), profileListener);

        keyboardHelper = new KeyboardHelper(hidDataSender);

//        hidDeviceProfile.getConnectedDevices();

//  repeat send backspace when hold Back key
        findViewById(R.id.back).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int event = motionEvent.getAction();
                if (event == MotionEvent.ACTION_DOWN){
                    handler = new Handler();
                    runRepeat = new Runnable(){
                        @Override
                        public void run() {
                                sendSpecialChar(view);
                                view.playSoundEffect(SoundEffectConstants.CLICK);
                                handler.postDelayed(this,100);
                        }
                    };
                    handler.post(runRepeat);
                }

                if (event == MotionEvent.ACTION_UP){
                    handler.removeCallbacks(runRepeat);
                }
                return true;
            }
        });

    }

    // this would lag in quick input
    public void longClickChar(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int event = motionEvent.getAction();
                if (event == MotionEvent.ACTION_DOWN){
                    handler = new Handler();
                    runRepeat = new Runnable(){
                        @Override
                        public void run() {
                            sendChar(view);
                            view.playSoundEffect(SoundEffectConstants.CLICK);
                            handler.postDelayed(this,200);
                        }
                    };
                    handler.post(runRepeat);
                }

                if (event == MotionEvent.ACTION_UP){
                    handler.removeCallbacks(runRepeat);
                }
                return true;
            }
        });
    }

    public void longClickSpecialChar(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int event = motionEvent.getAction();
                if (event == MotionEvent.ACTION_DOWN){
                    handler = new Handler();
                    runRepeat = new Runnable(){
                        @Override
                        public void run() {
                            sendSpecialChar(view);
                            view.playSoundEffect(SoundEffectConstants.CLICK);
                            handler.postDelayed(this,200);
                        }
                    };
                    handler.post(runRepeat);
                }

                if (event == MotionEvent.ACTION_UP){
                    handler.removeCallbacks(runRepeat);
                }
                return true;
            }
        });
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
//            {
//                hidDataSender.register(getApplicationContext(), profileListener);
//                hidDataSender.requestConnect(device);
//            }
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
//            {
//                hidDataSender.register(getApplicationContext(), profileListener);
//                hidDataSender.requestConnect(device);
//            }
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
//            {
//                hidDataSender.register(getApplicationContext(), profileListener);
//                hidDataSender.requestConnect(device);
//            }
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

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
