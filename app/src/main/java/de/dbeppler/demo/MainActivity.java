package de.dbeppler.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String screenSize = "1280x720";

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
                    ArrayList<String> name = new ArrayList<>();
                    name.add(device.getName());
                    name.add(screenSize);
                    Intent intent = new Intent(MainActivity.this, KeyboardActivity.class);
                    intent.putExtra("name",name);
                    startActivity(intent);
                }
            });
            ll.addView(buttons.get(buttons.size() - 1));
        }



        LinearLayout ls = (LinearLayout)findViewById(R.id.choose_size);
        CheckBox size1 = new CheckBox(this);
        size1.setText("1280x720");

        CheckBox size2 = new CheckBox(this);
        size2.setText("2340x1080");

        ls.addView(size1);
        ls.addView(size2);

        size1.setChecked(true);

        size1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size1.setChecked(true);
                size2.setChecked(false);
                screenSize = "1280x720";
            }
        });


        size2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size1.setChecked(false);
                size2.setChecked(true);
                screenSize = "2340x1080";
            }
        });

    }
}