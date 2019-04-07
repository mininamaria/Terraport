package com.example.maria.terraport;

import android.content.ContentValues;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maria.terraport.R;
import com.example.maria.terraport.StartActivity;

import static com.example.maria.terraport.StartActivity.TerraHelper;
import static com.example.maria.terraport.StartActivity.adapter;
import static com.example.maria.terraport.StartActivity.cursor;
import static com.example.maria.terraport.StartActivity.db;

public class AddControllerActivity extends AppCompatActivity {
    private Button buttonOK;
    private Button buttonCancel;
    public EditText contrURL;
    public EditText contrName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_controller);

        contrURL = findViewById(R.id.url_edit);
        contrName = findViewById(R.id.name_edit);
        buttonOK = findViewById(R.id.button_ok1);
        buttonCancel = findViewById(R.id.button_cancel1);


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
            }
        });
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues add_values = new ContentValues();
                add_values.put("DESCRIPTION", String.valueOf(contrName.getText()));
                add_values.put("URL", String.valueOf(contrURL.getText()));
                add_values.put("DELETED", "0");
                long newRowId =db.insert("CONT_LIST",null,add_values);
                // Если ID  -1, значит произошла ошибка
                if (newRowId == -1) {
                    Toast sh_err= Toast.makeText(getApplicationContext(), "Error",Toast.LENGTH_SHORT);
                    sh_err.show();

                } else {
                    Toast.makeText(getApplicationContext(), "ok" + newRowId, Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                setResult(RESULT_OK);
                tryCursor();
                finish();
            }
        });
    }
    public void tryCursor(){
        try{
            TerraHelper=new DBHelper(getApplicationContext());
            db=TerraHelper.getWritableDatabase();
            cursor= db.query("CONT_LIST",new String[]{"_id","URL","DESCRIPTION"},null,null,null,null,null);

        } catch (SQLException e){  //Если не получилось - ничего не получилось.
        }
        adapter.changeCursor(cursor);

    }
}
