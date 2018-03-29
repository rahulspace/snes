package com.example.hp.shoping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Registration extends AppCompatActivity {
    EditText e1,e2,e3,e4,e5,e6,e7,e8,e9,e10,e11;
    Button b1;
    String url="",name="",age="",email="",mobile="",housename="",street="",place="",district="",pin="",pass="",confirm="";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        e1=(EditText) findViewById(R.id.editText);
        e2=(EditText) findViewById(R.id.editText2);
        e3=(EditText) findViewById(R.id.editText3);
        e4=(EditText) findViewById(R.id.editText4);
        e5=(EditText) findViewById(R.id.editText5);
        e6=(EditText) findViewById(R.id.editText6);
        e7=(EditText) findViewById(R.id.editText7);
        e8=(EditText) findViewById(R.id.editText8);
        e9=(EditText) findViewById(R.id.editText9);
        e10=(EditText) findViewById(R.id.editText11);
        e11=(EditText) findViewById(R.id.editText12);
        b1=(Button) findViewById(R.id.button);

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(Registration.this);
        url= "http://"+sharedPreferences.getString("ip","192.168.0.0")+"/shopping/android_connections/userregistration.php";
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=e1.getText().toString();
                age=e2.getText().toString();
                email=e3.getText().toString();
                mobile=e4.getText().toString();
                housename=e5.getText().toString();
                street=e6.getText().toString();
                place=e7.getText().toString();
                district=e8.getText().toString();
                pin=e9.getText().toString();
                pass=e10.getText().toString();
                confirm=e11.getText().toString();

                int flag=0;
                if(name.equals("")){
                    e1.setError("Enter name");
                    flag++;
                    e1.setFocusable(true);
                }
               try {
                   String num="0123456789";
                   int nameFlag=0;
                   for(int k=0;k<name.length();k++){
                       if(num.contains(name.substring(k,k+1))){
                           nameFlag++;
                       }

                   }
                   if(nameFlag>0){
                       e1.setError("Numbers are not allowed in name...");
                       flag++;
                       e1.setFocusable(true);
                   }

               }catch (Exception e){

                   Log.d("errr reg-----",e.toString());
               }

                if(age.equals("")){
                    e2.setError("Enter your age");
                    flag++;
                    e2.setFocusable(true);
                }


                if(email.equals("")){
                    e3.setError("Add your email address");
                    flag++;
                    e3.setFocusable(true);
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    e3.setError("Invalid email address");
                    flag++;
                    e3.setFocusable(true);
                }
                if(mobile.equals("")){
                    e4.setError("Enter your age");
                    flag++;
                    e4.setFocusable(true);
                }
                if(!Patterns.PHONE.matcher(mobile).matches()){
                    e4.setError("Invalid mobile number");
                    flag++;
                    e4.setFocusable(true);
                }
                if(housename.equals("")){
                    e5.setError("Enter Housename");
                    flag++;
                    e5.setFocusable(true);
                }
                if(street.equals("")){
                    e6.setError("Enter street");
                    flag++;
                    e6.setFocusable(true);
                }
                if(district.equals("")){
                    e7.setError("Enter your district");
                    flag++;
                    e7.setFocusable(true);
                }
                if(pin.equals("")){
                    e8.setError("Enter pin");
                    flag++;
                    e8.setFocusable(true);
                }
                if(pass.equals("")){
                    e9.setError("Enter password");
                    flag++;
                    e9.setFocusable(true);
                }
                if(pass.length()<6){
                    e9.setError("password minimum 6 digits");
                    flag++;
                    e9.setFocusable(true);
                }
                if(confirm.equals("")){
                    e10.setError("Enter Confirm password");
                    flag++;
                    e10.setFocusable(true);
                }
                if(!pass.equals(confirm)){
                    e10.setError("password and confirm are not equal");
                    flag++;
                    e10.setFocusable(true);
                }
                if(flag==0){
                    new DatauploadService().execute();
                }

            }
        });
    }
    class DatauploadService extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(Registration.this);
            pd.setMessage("Loading....");
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("0")) {
                Toast.makeText(Registration.this, "Error,Try again...!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Registration.this, "Registration completed..!", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
            pd.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "0";
            try {
                List<NameValuePair> para = new ArrayList<>();
                para.add(new BasicNameValuePair("name", name));
                para.add(new BasicNameValuePair("age", age));
                para.add(new BasicNameValuePair("email", email));
                para.add(new BasicNameValuePair("mobile", mobile));
                para.add(new BasicNameValuePair("housename", housename));
                para.add(new BasicNameValuePair("street", street));
                para.add(new BasicNameValuePair("place", place));
                para.add(new BasicNameValuePair("district", district));
                para.add(new BasicNameValuePair("pin", pin));
                para.add(new BasicNameValuePair("password", pass));
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = jsonParser.makeHttpRequest(url, "GET", para);
                result = jsonObject.getString("status");
            } catch (Exception ex) {
                Log.d("err====", ex.toString());
            }
            return result;
        }

        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
    }
    }


