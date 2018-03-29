package com.example.hp.shoping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    EditText e1,e2;
    Button b1;
    TextView t1,t2;
    String name="";
    String pass="";
    String url= "http://192.168.43.18/shopping/android_connections/user_login.php";
    ProgressDialog pd;
    JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        e1=(EditText) findViewById(R.id.editText10);
        e2=(EditText) findViewById(R.id.editText13);
        b1=(Button) findViewById(R.id.button2);
        t1=(TextView) findViewById(R.id.textView);
        t2=(TextView) findViewById(R.id.textView40);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=e1.getText().toString();
                pass=e2.getText().toString();
                new DatauploadService().execute();

            }


        });
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent res=new Intent(getApplicationContext(),Registration.class);
                startActivity(res);
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent res=new Intent(getApplicationContext(),IPaddress.class);
                startActivity(res);
            }
        });

        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(Login.this);
        url= "http://"+sharedPreferences.getString("ip","192.168.0.0")+"/shopping/android_connections/user_login.php";

    }

    class DatauploadService extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(Login.this);
            pd.setMessage("Loading....");
            pd.show();;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("error"))
            {
                Toast.makeText(Login.this, "Error..", Toast.LENGTH_SHORT).show();
            }
            else if (s.equals("invalid"))
            {
                Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
            else if (s.equals("valid"))
            {
                try {
                    String login_id=jsonObject.getString("login_id");
                    SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(Login.this);
                    SharedPreferences.Editor editor=sh.edit();
                    editor.putString("login_id",login_id);
                    editor.commit();
                }catch (Exception e){}
                Intent i=new Intent(Login.this,Home.class);
                startActivity(i);
            }
            pd.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {

            String result="0";
            try
            {
                List<NameValuePair> para=new ArrayList<>();
                para.add(new BasicNameValuePair("username",name));
                para.add(new BasicNameValuePair("password",pass));
                JSONParser jsonParser=new JSONParser();
                jsonObject=jsonParser.makeHttpRequest(url,"GET",para);
                result=jsonObject.getString("status");

            }
            catch (Exception ex)
            {
                Log.d("err====",ex.toString());

            }
            return result;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
