package com.example.hp.shoping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    EditText e1,e2,e3,e4,e5,e6,e7,e8;

    String login_id="";
    ProgressDialog pd;
    JSONParser jp;
    JSONObject jsonObject;
    Button b1;
    String name="",email="",mobile="",housename="",street="",place="",district="",pin="",url1="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        e1=(EditText) findViewById(R.id.editText22);
        e2=(EditText) findViewById(R.id.editText23);
        e3=(EditText) findViewById(R.id.editText24);
        e4=(EditText) findViewById(R.id.editText25);
        e5=(EditText) findViewById(R.id.editText26);
        e6=(EditText) findViewById(R.id.editText27);
        e7=(EditText) findViewById(R.id.editText28);
        e8=(EditText) findViewById(R.id.editText29);
        b1=(Button) findViewById(R.id.button9);
        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(Home.this);
        login_id=sh.getString("login_id","");
        url1= "http://"+sh.getString("ip","192.168.0.0")+"/shopping/android_connections/";
        new DataUploadService().execute();

        e1.setEnabled(false);
        e2.setEnabled(false);
        e3.setEnabled(false);
        e4.setEnabled(false);
        e5.setEnabled(false);
        e6.setEnabled(false);
        e7.setEnabled(false);
        e8.setEnabled(false);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(b1.getText().toString().equals("update")){
                    e1.setEnabled(true);
                    e2.setEnabled(true);
                    e3.setEnabled(true);
                    e4.setEnabled(true);
                    e5.setEnabled(true);
                    e6.setEnabled(true);
                    e7.setEnabled(true);
                    e8.setEnabled(true);
                    b1.setText("Save changes");
                }
                else if(b1.getText().equals("Save changes")){
                    b1.setText("update");
                    name=e1.getText().toString();
                    email=e2.getText().toString();
                    mobile=e3.getText().toString();
                    housename=e4.getText().toString();
                    street=e5.getText().toString();
                    place=e6.getText().toString();
                    district=e7.getText().toString();
                    pin=e8.getText().toString();

                    int flag=0;
                    if(name.equals("")){
                        e1.setError("Enter name");
                        flag++;
                        e1.setFocusable(true);
                    }

                    if(email.equals("")){
                        e2.setError("Add your email address");
                        flag++;
                        e2.setFocusable(true);
                    }
                    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        e2.setError("Invalid email address");
                        flag++;
                        e2.setFocusable(true);
                    }
                    if(mobile.equals("")){
                        e3.setError("Enter your age");
                        flag++;
                        e3.setFocusable(true);
                    }
                    if(!Patterns.PHONE.matcher(mobile).matches()){
                        e3.setError("Invalid mobile number");
                        flag++;
                        e3.setFocusable(true);
                    }
                    if(housename.equals("")){
                        e4.setError("Enter Housename");
                        flag++;
                        e4.setFocusable(true);
                    }
                    if(street.equals("")){
                        e5.setError("Enter street");
                        flag++;
                        e5.setFocusable(true);
                    }
                    if(place.equals("")){
                        e6.setError("Enter your age");
                        flag++;
                        e6.setFocusable(true);
                    }
                    if(district.equals("")){
                        e7.setError("Enter your district");
                        flag++;
                        e7.setFocusable(true);
                    }
                    if(pin.equals("")){
                        e8.setError("Enter your age");
                        flag++;
                        e8.setFocusable(true);
                    }
                    if(flag==0) {
                        new DatauploadService2().execute();
                    }
                }


            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Scanproduct.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.scan_product) {
            Intent res=new Intent(getApplicationContext(),Scanproduct.class);
            startActivity(res);
            // Handle the camera action
        } else if (id == R.id.set_threshold) {
            Intent res=new Intent(getApplicationContext(),Setthreshold.class);
            startActivity(res);

        } else if (id == R.id.view_cart) {
            Intent res=new Intent(getApplicationContext(),Viewingcart.class);
            startActivity(res);

        } else if (id == R.id.view_history) {
            Intent res= new Intent(getApplicationContext(),History.class);
            startActivity(res);

        } else if (id == R.id.view_offer) {
            Intent res= new Intent(getApplicationContext(),Viewingpurchase.class);
            startActivity(res);

        }else if (id == R.id.change_password) {
            Intent res=new Intent(getApplicationContext(),Changepassword.class);
            startActivity(res);

        }else if (id == R.id.logout) {
            Intent res=new Intent(getApplicationContext(),Login.class);
            startActivity(res);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class DataUploadService extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(Home.this);
            pd.setMessage("Profile");
            pd.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("ok")){
               try {
                   e1.setText(jsonObject.getString("name"));
                   e2.setText(jsonObject.getString("email"));
                   e3.setText(jsonObject.getString("mobile"));
                   e4.setText(jsonObject.getString("housename"));
                   e5.setText(jsonObject.getString("street"));
                   e6.setText(jsonObject.getString("place"));
                   e7.setText(jsonObject.getString("district"));
                   e8.setText(jsonObject.getString("pin"));
               }catch (Exception e){}
            }
            else{
                Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            String url=url1+"user_profile.php";
            String result="0";
            try{
                List<NameValuePair> para=new ArrayList<>();
                para.add(new BasicNameValuePair("login_id",login_id));
                jp=new JSONParser();
                jsonObject=jp.makeHttpRequest(url,"GET",para);
                result=jsonObject.getString("status");
                Log.d("res======",jsonObject.toString());

            }catch(Exception ex)
            {
                Log.d("err====",ex.toString());
            }

            return result;
        }
    }

    class DatauploadService2 extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(Home.this);
            pd.setMessage("updating");
            pd.show();

        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            if (s.equals("0")) {
                Toast.makeText(Home.this, "Error,Try again...!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Home.this, "Updation completed..!", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
            pd.dismiss();
        }

        @Override
        protected String doInBackground(String... params)
        {
            String url=url1+"user_update.php";
            String result="0";
            try{
                List<NameValuePair> para=new ArrayList<>();
                para.add(new BasicNameValuePair("login_id",login_id));
                para.add(new BasicNameValuePair("name",name));
                para.add(new BasicNameValuePair("email",email));
                para.add(new BasicNameValuePair("mobile",mobile));
                para.add(new BasicNameValuePair("housename",housename));
                para.add(new BasicNameValuePair("street",street));
                para.add(new BasicNameValuePair("place",place));
                para.add(new BasicNameValuePair("district",district));
                para.add(new BasicNameValuePair("pin",pin));

                jp=new JSONParser();
                jsonObject=jp.makeHttpRequest(url,"GET",para);
                result=jsonObject.getString("status");
                Log.d("res======",jsonObject.toString());

            }catch(Exception ex)
            {
                Log.d("err====",ex.toString());
            }


            return result;
        }
    }

}
