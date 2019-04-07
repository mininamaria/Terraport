package com.example.maria.terraport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class RobotActivity extends AppCompatActivity {

//    private TextView mTextViewAngleRight;
//    private TextView mTextViewStrengthRight;
//    private TextView mTextViewCoordinateRight;
    public TextView textViewLog;
    public static final String IP = "";
    public NetEx ex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot);

//        mTextViewAngleRight = findViewById(R.id.textView_angle_right);
//        mTextViewStrengthRight =  findViewById(R.id.textView_strength_right);
//        mTextViewCoordinateRight = findViewById(R.id.textView_coordinate_right);

        textViewLog = findViewById(R.id.text_log);

        final Robot robot = new Robot();

        Intent intent = getIntent();
        final String uriAddress = intent.getStringExtra(IP);
        final JoystickView joystickRight = findViewById(R.id.joystickView);

        ex = new NetEx(uriAddress, 100); // поменять timeout
        ex.toServer = new JReq(2);
        ex.fromServer = new JReq(2);
        ex.ready = false;   //этот флажок мы устанавливаем, колгда данные для контроллера готовы
        ex.done = true;   // этот флажок устанавливает фоновый процесс (процесс - субъект)
        ex.execute();

        joystickRight.setOnMoveListener(new JoystickView.OnMoveListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onMove(int angle, int strength) {
//                mTextViewAngleRight.setText(angle + "°");
//                mTextViewStrengthRight.setText(strength + "%");
////                mTextViewCoordinateRight.setText(
////                        String.format("x%03d:y%03d",
////                                joystickRight.getNormalizedX(),
////                                joystickRight.getNormalizedY())
////                );
//                mTextViewCoordinateRight.setText(ex.toServer.getJSON());

                    robot.setAngle(angle);
                    robot.setStrength(strength);
                    robot.calc();


                    if(ex.done){
                        ex.toServer.setPair(0, "set", "MotorA", String.valueOf(robot.getMotorA()));
                        ex.toServer.setPair(1, "set", "MotorB", String.valueOf(robot.getMotorB()));
                        ex.ready=true;
                    }
            }
        });


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ex.cancel(true);
    }
}
