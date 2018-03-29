package com.example.hp.shoping;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Cartnew extends AppCompatActivity {
    TextView t1,t2,t3;
    ImageView i1;


Button b1,b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartnew);
        t1=(TextView) findViewById(R.id.textView29);
        t2=(TextView) findViewById(R.id.textView30);
        t3=(TextView) findViewById(R.id.textView31);
        i1=(ImageView) findViewById(R.id.imageView3);

        String p=Viewingcart.product_id.get(Viewingcart.pos);
        t1.setText(p);
        String p1=Viewingcart.product_name.get(Viewingcart.pos);
        t2.setText(p1);
        String p3=Viewingcart.rate.get(Viewingcart.pos);
        t3.setText(p3);



    }
}
