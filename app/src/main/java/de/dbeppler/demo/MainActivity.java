package de.dbeppler.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_target);
        LinearLayout ll = (LinearLayout)findViewById(R.id.choose_target);

        ArrayList<Button> buttons = new ArrayList<Button>();

        for (BluetoothDevice device : BluetoothAdapter.getDefaultAdapter().getBondedDevices()){
            buttons.add(new Button(this));
            buttons.get(buttons.size() - 1).setText(device.getName());
            buttons.get(buttons.size() - 1).setAllCaps(false);
            buttons.get(buttons.size() - 1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, KeyboardActivity.class);
                    intent.putExtra("name",device.getName());
                    startActivity(intent);
                }
            });
            ll.addView(buttons.get(buttons.size() - 1));
        }


    }
}