package com.example.hp.shoping;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Viewingcart extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String login_id = "";
    String availQty="";
    String[] cart_id;
    public static ArrayList<String> product_id,product_name,rate,discount,date,image,quantity;
    ListView l1;
    //ProgressDialog pd;
    //JSONObject JD;
    Button b1;
    public static int pos;
    TextView tv_total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewingcart);
        l1=(ListView) findViewById(R.id.list2);
        b1=(Button) findViewById(R.id.button10);
        tv_total=(TextView)findViewById(R.id.textView47);
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(Viewingcart.this);
        login_id = sh.getString("login_id", "");

        try {
            if(Build.VERSION.SDK_INT>9){
                StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        }catch (Exception e){

        }
        l1.setOnItemClickListener(this);
       // new DataUploadService().execute();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent res=new Intent(getApplicationContext(),Codegeneration.class);
                startActivity(res);

            }


        });



try {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String curDate = simpleDateFormat.format(new Date());

    SQLiteDatabase sqd = openOrCreateDatabase("Shopping", SQLiteDatabase.CREATE_IF_NECESSARY, null);
    String qry = "create table if not exists cart(cart_id integer primary key autoincrement,login_id text,product_id text, product_name text,rate text,discount text, image text,quantity text,date text)";
    sqd.execSQL(qry);

    String qry1 = "select * from cart where login_id='" + login_id + "'";
    Cursor c = sqd.rawQuery(qry1, null);
    float total=0;
    if (c.getCount() > 0) {
        Log.d("count==cart==",c.getCount()+"");
        cart_id = new String[c.getCount()];
        product_id = new ArrayList<String>();
        product_name = new ArrayList<String>();
        rate = new ArrayList<String>();
        discount = new ArrayList<String>();
        image = new ArrayList<String>();
        date = new ArrayList<String>();
        quantity = new ArrayList<String>();
        int i = 0;
        c.moveToFirst();
        do {
            Log.d(i+"",c.getString(2));
            cart_id[i] = c.getString(0);
            product_id.add( c.getString(2));
            product_name.add( c.getString(3));
            rate.add(c.getString(4));
            discount.add(c.getString(5));
            image.add( c.getString(6));
            date.add( c.getString(8));
            quantity.add(c.getString(7));
            Log.d("v=======",c.getString(4)+":"+c.getString(7)+":"+c.getString(5));
            try{
                float rate=Float.parseFloat(c.getString(4));
                float qty=Float.parseFloat(c.getString(7));
                float dis=Float.parseFloat(c.getString(5));

                total+=(rate-((rate*dis)/100))*qty;
            }catch (Exception e){
                Log.d("errrr111111",e.toString());
            }
            i++;
        }
        while (c.moveToNext());

       tv_total.setText("Total:"+total);
        l1.setAdapter(new CustomCart(getApplicationContext(),product_name,rate,quantity,discount,image));

        }
        else{
            tv_total.setVisibility(View.INVISIBLE);
        }
        sqd.close();
    }catch(Exception ex){
        Log.d("ex==cart==",ex.toString());
        Toast.makeText(this, "errrrorrr...."+ex, Toast.LENGTH_SHORT).show();
    }



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        pos=position;

        //read available quantity
        try{

            String url="";
            SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(Viewingcart.this);
            url= "http://"+sharedPreferences.getString("ip","192.168.0.0")+"/shopping/android_connections/available_quantity.php";

            List<NameValuePair> para=new ArrayList<>();
            para.add(new BasicNameValuePair("product_id",product_id.get(pos)));
            JSONParser jsonParser=new JSONParser();
            JSONObject jsonObject=null;
            jsonObject=jsonParser.makeHttpRequest(url,"GET",para);
            availQty=jsonObject.getString("qty");

        }catch (Exception e){}


        AlertDialog.Builder alert=new AlertDialog.Builder(Viewingcart.this);
        final EditText ed_quntity=new EditText(Viewingcart.this);
        ed_quntity.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setTitle("Edit or Delete ");
        alert.setMessage("Enter Quantity...");
        alert.setView(ed_quntity);

        alert.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Viewingcart.this, ed_quntity.getText().toString(), Toast.LENGTH_SHORT).show();

                try {

                    float userQty=Float.parseFloat(ed_quntity.getText().toString());
                    float avQty=Float.parseFloat(availQty);
                    if(avQty<userQty){
                        Toast.makeText(Viewingcart.this, "Quantity not available...!!", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        editCartItem(ed_quntity.getText().toString());
                        Toast.makeText(Viewingcart.this, "Quantity Changed..", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){}
            }
        });

        alert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Viewingcart.this, "Quantity Deleted from your cart..", Toast.LENGTH_SHORT).show();
                deleteCartItem();
            }
        });
        AlertDialog alrt = alert.create();
        alrt.show();


    }

    public  void deleteCartItem ()
    {
        String selectedCartID=cart_id[pos];
        try {
            SQLiteDatabase sqd = openOrCreateDatabase("Shopping", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            String qry = "create table if not exists cart(cart_id integer primary key autoincrement,login_id text,product_id text, product_name text,rate text,discount text, image text,date text)";
            sqd.execSQL(qry);
            String delete="delete from cart where cart_id='"+selectedCartID+"'";
            sqd.execSQL(delete);
            sqd.close();
            Intent I=new Intent(Viewingcart.this,Viewingcart.class);
            startActivity(I);
        }catch (Exception e){}
    }

    public  void editCartItem (String qty) {
        String selectedCartID=cart_id[pos];
        try {
            SQLiteDatabase sqd = openOrCreateDatabase("Shopping", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            String qry = "create table if not exists cart(cart_id integer primary key autoincrement,login_id text,product_id text, product_name text,rate text,discount text, image text,date text)";
            sqd.execSQL(qry);
            String update="update cart set quantity='"+qty+"' where cart_id='"+selectedCartID+"'";
            sqd.execSQL(update);
            sqd.close();
            Intent I=new Intent(Viewingcart.this,Viewingcart.class);
            startActivity(I);
        }catch (Exception e){}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(getApplicationContext(),Home.class);
        startActivity(intent);
    }
}
