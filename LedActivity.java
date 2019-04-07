package com.example.maria.terraport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.maria.terraport.StartActivity.cursor;

public class LedActivity extends AppCompatActivity {

    private SeekBar bright_bar;
    private TextView bright_txt;
    public TextView textViewLog;
    public static final String IP = "";
    public String uriAddress;
    public NetEx ex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);
        Intent intent = getIntent();
        uriAddress = intent.getStringExtra(IP);
        textViewLog = findViewById(R.id.text_log);


        bright_bar = findViewById(R.id.bright_bar);
        bright_txt = findViewById(R.id.bar_txt);
//
        ex = new NetEx(uriAddress, 100); // поменять timeout
       // Toast.makeText(getApplicationContext(), ex.URL, Toast.LENGTH_SHORT).show();
        ex.toServer = new JReq(1);
        ex.fromServer = new JReq(1);
        ex.ready = false;
        ex.done = true;
        ex.execute();
        bright_bar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(ex.done && fromUser) {
                            bright_txt.setText(progress + " / " + bright_bar.getMax());
                            ex.toServer.setPair(0, "set", "Brightness", String.valueOf(bright_bar.getProgress()));
                            ex.ready = true;
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
//                        ex.cancel(false);
                    }
                }
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ex.cancel(true);
    }
}
