package com.example.hp.shoping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class IPaddress extends AppCompatActivity implements View.OnClickListener {
    EditText e1;
    Button b1;
    String s;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipaddress);
        e1=(EditText) findViewById(R.id.editText21);
        b1=(Button) findViewById(R.id.button12);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(IPaddress.this);
        e1.setText(sharedPreferences.getString("ip","192.168.0.0"));
        b1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String ip=e1.getText().toString();
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("ip",ip);
        editor.commit();

        Intent i=new Intent(getApplicationContext(),Login.class);
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
    }
}
