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
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Changepassword extends AppCompatActivity {
    EditText t1,t2,t3;
    Button b1;
    String password="",newpass="",confirm="",login_id="",url="";
    ProgressDialog pd;
    JSONParser jp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        t1=(EditText) findViewById(R.id.editText15);
        t2=(EditText) findViewById(R.id.editText16);
        t3=(EditText) findViewById(R.id.editText17);
        b1=(Button) findViewById(R.id.button6);

        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(Changepassword.this);
        login_id=sh.getString("login_id","");
        url= "http://"+sh.getString("ip","192.168.0.0")+"/shopping/android_connections/change_password.php";
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password=t1.getText().toString();
                newpass=t2.getText().toString();
                confirm=t3.getText().toString();

                int flag=0;
                if(password.equals("")){
                    t1.setError("Enter password");
                    flag++;
                    t1.setFocusable(true);
                }
                if(newpass.equals("")){
                    t2.setError("Enter new password");
                    flag++;
                    t2.setFocusable(true);
                }
                if(confirm.equals("")){
                    t3.setError("Enter confirm password");
                    flag++;
                    t3.setFocusable(true);
                }
                if(!newpass.equals(confirm)){
                    t3.setError("password and confirm are not equal");
                    flag++;
                    t3.setFocusable(true);
                }

                if(flag==0) {
                    new DataUploadService().execute();
                }

            }
        });

    }

    class DataUploadService extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(Changepassword.this);
            pd.setMessage("Loading...");
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("0"))
            {
                Toast.makeText(Changepassword.this, "Errorr...", Toast.LENGTH_SHORT).show();
            }
            else if (s.equals("password_mismatch"))
            {
                Toast.makeText(Changepassword.this, "Invalid Password", Toast.LENGTH_SHORT).show();
            }
            else if (s.equals("updated"))
            {
                Toast.makeText(Changepassword.this, "Updated Sucessfully", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(Changepassword.this,Home.class);
                startActivity(i);
            }
            pd.dismiss();

        }

        @Override
        protected String doInBackground(String... params) {
            String result="0";
            try {
                List<NameValuePair> param = new ArrayList<>();
                param.add(new BasicNameValuePair("login_id", login_id));
                param.add(new BasicNameValuePair("password", password));
                param.add(new BasicNameValuePair("confirmpassword", confirm));
                jp = new JSONParser();
                JSONObject jsonObject = jp.makeHttpRequest(url, "GET", param);
                result = jsonObject.getString("status");
                Log.d("res======", result);
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

        Intent intent=new Intent(getApplicationContext(),Home.class);
        startActivity(intent);
    }
}
