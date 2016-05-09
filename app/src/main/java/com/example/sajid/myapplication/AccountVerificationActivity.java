package com.example.sajid.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.simple.JSONValue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import activity.Activity_main_2;
import objects.personal_obj;

public class AccountVerificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_verification);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        final RelativeLayout regnRelativeLayout = (RelativeLayout)findViewById(R.id.regnRelativeLayout);
        final RelativeLayout laterRelativeLayout = (RelativeLayout)findViewById(R.id.laterRelativeLayout);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AccountVerificationActivity.this);
        EditText editText = (EditText)findViewById(R.id.graduationRegistrationNumberEditText);
        EditText editText1 = (EditText)findViewById(R.id.postGraduationRegistrationNumberEditText);
        EditText editText2 = (EditText)findViewById(R.id.grauationRegistrationYearEditText);
        EditText editText3 = (EditText)findViewById(R.id.postGraduationRegistrationYearEditText);
        String graduationRegistrationNumber = prefs.getString("graduationRegistrationNumber", "");
        String postGraduationRegistrationNumber = prefs.getString("postGraduationRegistrationNumber", "");
        String graduationRegistrationYear = prefs.getString("graduationRegistrationYear", "");
        String postGraduationRegistrationYear = prefs.getString("postGraduationRegistrationYear", "");
        editText.setText(graduationRegistrationNumber);
        editText1.setText(postGraduationRegistrationNumber);
        editText2.setText(graduationRegistrationYear);
        editText3.setText(postGraduationRegistrationYear);
        if((graduationRegistrationNumber.length()>0)||(postGraduationRegistrationNumber.length()>0))
        {
            regnRelativeLayout.setVisibility(View.GONE);
            laterRelativeLayout.setVisibility(View.VISIBLE);
        }
        else {
            regnRelativeLayout.setVisibility(View.VISIBLE);
            laterRelativeLayout.setVisibility(View.GONE);

        }
        TextView textView = (TextView)findViewById(R.id.enterAgainTextView);
        TextView textView1 = (TextView)findViewById(R.id.stillVerifyingTextView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regnRelativeLayout.setVisibility(View.VISIBLE);
                laterRelativeLayout.setVisibility(View.GONE);

            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regnRelativeLayout.setVisibility(View.VISIBLE);
                laterRelativeLayout.setVisibility(View.GONE);

            }
        });

        Button button = (Button)findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRegistrationNumber();

            }
        });
    }


    public void updateRegistrationNumber()
    {
        String s1 = "";
        final DatabaseHandler databaseHandler= new DatabaseHandler(AccountVerificationActivity.this);
        EditText editText = (EditText)findViewById(R.id.graduationRegistrationNumberEditText);
        EditText editText1 = (EditText)findViewById(R.id.postGraduationRegistrationNumberEditText);
        EditText editText2 = (EditText)findViewById(R.id.grauationRegistrationYearEditText);
        EditText editText3 = (EditText)findViewById(R.id.postGraduationRegistrationYearEditText);
        personal_obj personalObj = databaseHandler.getPersonalInfo();
        ArrayList<String> hash = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AccountVerificationActivity.this);
        String hashString = prefs.getString(AccountVerificationActivity.this.getString(R.string.hash_code), "");
        hash.add(hashString);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("graduationRegistrationNumber", editText.getText().toString());
        editor.commit();
        editor.putString("graduationRegistrationYear", editText2.getText().toString());
        editor.commit();
        editor.putString("postGraduationRegistrationNumber", editText1.getText().toString());
        editor.commit();
        editor.putString("postGraduationRegistrationYear", editText3.getText().toString());
        editor.commit();



        ArrayList<String> data = new ArrayList<>();
        ArrayList<String> data1 = new ArrayList<>();
        ArrayList<ArrayList<String>> personalData = new ArrayList<>();
        data.add(String.valueOf(personalObj.get_customerId()));
        data.add("1");//verification type 1: graduation 2: post graduation
        data.add(editText.getText().toString());
        data.add(editText2.getText().toString());
        data.add("0");// verification status 0 : not verified 1: verified

        data1.add(String.valueOf(personalObj.get_customerId()));
        data1.add("2");//verification type 1: graduation 2: post graduation
        data1.add(editText1.getText().toString());
        data1.add(editText3.getText().toString());
        data1.add("0");// verification status 0 : not verified 1: verified

        personalData.add(data);
        personalData.add(data1);
        personalData.add(hash);
        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(personalData, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final RequestParams requestParams = new RequestParams();
        requestParams.put("verificationJSON", s1);


        final String  address =  getResources().getString(R.string.action_server_ip_address);
        utility.sync("http://" + address + "/addVerificationDetails.php", requestParams,AccountVerificationActivity.this);
        Intent intent = new Intent(AccountVerificationActivity.this, AccountVerificationActivity.class);
        finish();
        startActivity(intent);




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_verification, menu);
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
}
