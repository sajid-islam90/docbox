package activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import objects.*;

import utilityClasses.DatabaseHandler;
import redundant.MyAnalogClock;
import com.elune.sajid.myapplication.R;
import utilityClasses.utility;
import com.loopj.android.http.RequestParams;

import org.json.simple.JSONValue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

public class appointment_settings extends AppCompatActivity {
    static time startTime = new time();
    static time endTime = new time();
    static String startAmPm;
    static MyAnalogClock startAnalogClock;
    static String endAmPm;
    static MyAnalogClock endAnalogClock;
    static TextView startAmPmTextView;
    static TextView textView;
    static TextView textView2;
    static TextView endAmPmTextView;
   static NumberPicker numberPicker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_settings);
        DatabaseHandler databaseHandler = new DatabaseHandler(appointment_settings.this);
        ArrayList<String> settings = databaseHandler.getAppointmentSettings();

        //appointment settings per day patch start
        CheckBox sunday = (CheckBox)findViewById(R.id.checkBox);
        CheckBox monday = (CheckBox)findViewById(R.id.checkBox1);
        CheckBox tuesday = (CheckBox)findViewById(R.id.checkBox2);
        CheckBox wednesday = (CheckBox)findViewById(R.id.checkBox3);
        CheckBox thursday = (CheckBox)findViewById(R.id.checkBox4);
        CheckBox friday = (CheckBox)findViewById(R.id.checkBox5);
        CheckBox saturday = (CheckBox)findViewById(R.id.checkBox6);
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        //appointment settings per day patch end
         textView = (TextView)findViewById(R.id.startTime);
        Typeface font = Typeface.createFromAsset(appointment_settings.this.getAssets(), "digital.ttf");
        textView.setTypeface(font);
        textView2 = (TextView)findViewById(R.id.endTime);

        textView2.setTypeface(font);
        numberPicker = (NumberPicker)findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(true);
        if((settings.size()>0)&&(startTime.getHour()==0)&&(endTime.getHour()==0))

        {
            daySetter(settings.get(0));
            int strtTme[] = timeParser(settings.get(1));
            int endTme[] = timeParser(settings.get(2));
            startTime.setHour(strtTme[0]);
            startTime.setMinute(strtTme[1]);
            endTime.setHour(endTme[0]);
            endTime.setMinute(endTme[1]);
            numberPicker.setValue(Integer.parseInt(settings.get(3)));



        }



        if(startTime.getHour()>=12)
        {
            startAmPm = "PM";
            if(startTime.getMinute()>10)
            textView.setText((startTime.getHour()-12)+":"+startTime.getMinute());
            else
                textView.setText((startTime.getHour()-12)+":0"+startTime.getMinute());
        }
        else
        {
            startAmPm = "AM";

            if(startTime.getMinute()>10)
                textView.setText((startTime.getHour())+":"+startTime.getMinute());
            else
                textView.setText((startTime.getHour())+":0"+startTime.getMinute());
        }
        if(endTime.getHour()>=12)
        {
            endAmPm = "PM";
            if(endTime.getMinute()>10)
                textView2.setText((endTime.getHour()-12)+":"+endTime.getMinute());
            else
                textView2.setText((endTime.getHour()-12)+":0"+endTime.getMinute());
        }
        else
        {
            endAmPm = "AM";
            if(endTime.getMinute()>10)
                textView2.setText((endTime.getHour())+":"+endTime.getMinute());
            else
                textView2.setText((endTime.getHour())+":0"+endTime.getMinute());
        }
        startAmPmTextView = (TextView)findViewById(R.id.startAmPm);
        startAnalogClock = (MyAnalogClock) findViewById(R.id.startClock);
        startAnalogClock.setTime(startTime.getHour(), startTime.getMinute(), 0);

        startAmPmTextView.setText(startAmPm);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWork(startTime);

            }
        });

        endAmPmTextView = (TextView)findViewById(R.id.endAmPm);
        endAnalogClock = (MyAnalogClock)findViewById(R.id.endClock);
        endAnalogClock.setTime(endTime.getHour(), endTime.getMinute(), 0);
        endAmPmTextView.setText(endAmPm);

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWork(endTime);

            }
        });





    }
    public void doWork(final time time)
    {
        LayoutInflater li = LayoutInflater.from(appointment_settings.this);
        final View promptsView = li.inflate(R.layout.time_picker, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                appointment_settings.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                final TimePicker timePicker = (TimePicker) promptsView.findViewById(R.id.timePicker);

                                time.setHour(timePicker.getCurrentHour());
                                time.setMinute(timePicker.getCurrentMinute());
                                startAnalogClock.setTime(time.getHour(), time.getMinute(), 0);
                                utility.recreateActivityCompat(appointment_settings.this);



                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appointment_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            saveAppointmentPreferences();
            startTime = new time();
            endTime = new time();
            Intent intent = new Intent(this,Activity_main_2.class);
           // startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void saveAppointmentPreferences()
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(appointment_settings.this);
        personal_obj personalObj = databaseHandler.getPersonalInfo();
        String doc_id = String.valueOf(personalObj.get_customerId());

        ArrayList<String> data = new ArrayList<>();
        CheckBox sunday = (CheckBox)findViewById(R.id.checkBox);
        CheckBox monday = (CheckBox)findViewById(R.id.checkBox1);
        CheckBox tuesday = (CheckBox)findViewById(R.id.checkBox2);
        CheckBox wednesday = (CheckBox)findViewById(R.id.checkBox3);
        CheckBox thursday = (CheckBox)findViewById(R.id.checkBox4);
        CheckBox friday = (CheckBox)findViewById(R.id.checkBox5);
        CheckBox saturday = (CheckBox)findViewById(R.id.checkBox6);
        String onlineDays = "";
        if(sunday.isChecked())
            onlineDays = onlineDays+"sunday";
        if(monday.isChecked())
            onlineDays = onlineDays+"monday";
        if(tuesday.isChecked())
            onlineDays = onlineDays+"tuesday";
        if(wednesday.isChecked())
            onlineDays = onlineDays+"wednesday";
        if(thursday.isChecked())
            onlineDays = onlineDays+"thursday";
        if(friday.isChecked())
            onlineDays = onlineDays+"friday";
        if(saturday.isChecked())
            onlineDays = onlineDays+"saturday";

        String startingTime = String.valueOf(startTime.getHour())+":"+String.valueOf(startTime.getMinute());
        String endingTime = String.valueOf(endTime.getHour())+":"+String.valueOf(endTime.getMinute());
        String numberOfPatients = String.valueOf(numberPicker.getValue());
        data.add(doc_id);
        data.add(onlineDays);
        data.add(startingTime);
        data.add(endingTime);
        data.add(numberOfPatients);
        String s1 = null;

        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(data, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print(s1);
        RequestParams paramsDocuments = new RequestParams();
        paramsDocuments.put("appointmentPreferenceJSON", s1);
        String address ="docbox.co.in/sajid/"; //appointment_settings.this.getResources().getString(R.string.action_server_ip_address);
        utility. sync("http://" + address + "/saveAppointmentPreference.php", paramsDocuments, appointment_settings.this);
        databaseHandler.saveAppointmentSettings(onlineDays,startingTime,endingTime, Integer.parseInt(numberOfPatients));


    }
    public void daySetter(String days)
    {

        CheckBox sunday = (CheckBox)findViewById(R.id.checkBox);
        CheckBox monday = (CheckBox)findViewById(R.id.checkBox1);
        CheckBox tuesday = (CheckBox)findViewById(R.id.checkBox2);
        CheckBox wednesday = (CheckBox)findViewById(R.id.checkBox3);
        CheckBox thursday = (CheckBox)findViewById(R.id.checkBox4);
        CheckBox friday = (CheckBox)findViewById(R.id.checkBox5);
        CheckBox saturday = (CheckBox)findViewById(R.id.checkBox6);
        if(days.contains("sunday"))
        {
            sunday.setChecked(true);
        }
        if(days.contains("monday"))
        {
            monday.setChecked(true);
        }

        if(days.contains("tuesday"))
        {
            tuesday.setChecked(true);
        }

        if(days.contains("wednesday"))
        {
            wednesday.setChecked(true);
        }

        if(days.contains("thursday"))
        {
            thursday.setChecked(true);
        }

        if(days.contains("friday"))
        {
            friday.setChecked(true);
        }

        if(days.contains("saturday"))
        {
            saturday.setChecked(true);
        }





    }


    public int[] timeParser(String time)

    {
        int a[] = new int[2];

        int hourIndex = 0;
        int minuteIndex = time.indexOf(":");
        int hour = Integer.parseInt(time.substring(hourIndex , minuteIndex));
        int minute =  Integer.parseInt(time.substring(minuteIndex+1));
        a[0] = hour;
        a[1] = minute;

        return a;
    }
}
