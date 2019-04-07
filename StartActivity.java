package com.example.maria.terraport;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {
    public static DBHelper TerraHelper;
    public static SQLiteDatabase db;
    public static Cursor cursor;
    public static CursorAdapter adapter;
    private Button buttonAdd;
    private Button buttonDelete;
    private Button buttonConnect;
    public TextView textViewLog;
  //  public JRequest zapros;
  //  public NetExchange ex;
    public Spinner spinner;
  //  private long timeout=5000;
   // private long lastUpdate;
    static final int ADDED_CONTROLLER_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        textViewLog = findViewById(R.id.text_log);
        buttonAdd = findViewById(R.id.button_add);
        buttonDelete = findViewById(R.id.button_delete);
        buttonConnect = findViewById(R.id.button_connect);
        spinner = findViewById(R.id.cont_spinner);

        //zapros = new JRequest("get", "type", "0");
        //ex = new NetExchange();

        tryToastedCursor();

        //ArrayAdapter <?> adapter = ArrayAdapter.createFromResource(this,R.array.arr_controllers,android.R.layout.simple_spinner_item );
        adapter  = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,cursor,new String []{"DESCRIPTION"},new int[]{android.R.id.text1},0 );

        spinner.setAdapter(adapter);


       buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addController();
            }
        });

       buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete("CONT_LIST", "_id = ?", new String[]{cursor.getString(0)});
                tryCursor();
            }
        });
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectController();
            }
        });
    }
    public void connectController() {
     //   String selectedCont = spinner.getSelectedItem().toString();
        NetEx ex = new NetEx(cursor.getString(1), 5000);
        ex.toServer = new JReq(1);
        ex.fromServer = new JReq(1);
        ex.toServer.setPair(0, "get", "type", "0");
        ex.ready = true;
        ex.done = false;
        while (!ex.done) {
            try {
                ex.execute();
                ex.done = true;
            }catch (Exception e){
                Toast.makeText(this, R.string.error_unavailable, Toast.LENGTH_SHORT).show();
            }
        }

        if (ex.getPair(0).PVal.equals("Error"))
            Toast.makeText(getApplicationContext(), R.string.error_PVal, Toast.LENGTH_SHORT).show();
        else {
            switch (Integer.parseInt(ex.getPair(0).PVal)) {
                case 20:
                    textViewLog.setText(R.string.led_title);
                    Intent intent_led = new Intent(this, LedActivity.class);
                    intent_led.putExtra(LedActivity.IP, cursor.getString(1));
                    ex.cancel(true);
                    ex = null;
                    textViewLog.setText(intent_led.toString());
                    startActivity(intent_led);
                    break;

                case 30:
                    textViewLog.setText(R.string.robot_title);
                    Intent intent_robot = new Intent(this, RobotActivity.class);
                    intent_robot.putExtra(RobotActivity.IP, cursor.getString(1));//Переделать
                    ex.cancel(true);
                    ex = null;
                    startActivity(intent_robot);
                    textViewLog.setText(intent_robot.toString());
                    break;

                default:
                    textViewLog.setText(R.string.error_unavailable);
            }
        }

    }
        public void addController(){
            Intent intent_add = new Intent(this, AddControllerActivity.class);
            startActivityForResult(intent_add, ADDED_CONTROLLER_REQUEST);

        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADDED_CONTROLLER_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                StartActivity.adapter.notifyDataSetChanged();
                // The user added a controller.
                //WRITE HERE THE FOLLOWING CODE!!!!!!!!!!!!!!!!!!!!!!!!!!
                // You can add a function to make the code easier to read and better organised
            }
        }
    }
        public void tryCursor () {
            try {
                TerraHelper = new DBHelper(getApplicationContext());
                db = TerraHelper.getWritableDatabase();
                cursor = db.query("CONT_LIST", new String[]{"_id", "URL", "DESCRIPTION"}, null, null, null, null, null);

            } catch (SQLException e) {  //Если не получилось - ничего не получилось.
            }
            adapter.changeCursor(cursor);

        }
        public void tryToastedCursor () {
            try {
                TerraHelper = new DBHelper(this);
                db = TerraHelper.getWritableDatabase();
                cursor = db.query("CONT_LIST", new String[]{"_id", "URL", "DESCRIPTION"}, null, null, null, null, null);

            } catch (SQLException e) {  //Если не получилось - выводим Toast об ошибке
                Toast toast_err = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
                toast_err.show();
            }

        }


    }
