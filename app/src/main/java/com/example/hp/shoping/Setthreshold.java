package com.example.hp.shoping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Setthreshold extends AppCompatActivity {
    EditText e1;
    Button b1;
    String threshold="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setthreshold);
        e1=(EditText) findViewById(R.id.editText14);
        b1=(Button) findViewById(R.id.button5);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                threshold=e1.getText().toString();
                SharedPreferences sh= PreferenceManager .getDefaultSharedPreferences(Setthreshold.this);
                SharedPreferences.Editor editor=sh.edit();
                editor.putString("threshold",threshold);
                editor.commit();

                Toast.makeText(Setthreshold.this, "Threshold Successfully Set", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),Home.class);
                startActivity(i);

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(getApplicationContext(),Home.class);
        startActivity(intent);
    }
}
