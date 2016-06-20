package activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import utilityClasses.DatabaseHandler;
import com.elune.sajid.myapplication.R;
import utilityClasses.utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import objects.DataBaseEnums;
import objects.time;


/**
 * Created by nevermore on 12/31/2015.
 */
public class AppointmentDaysFragment extends Fragment {

    private boolean isAvailable;
    private  String mStartTime, mEndTime, mAvailable, mOnlineDays;
    private  int position;
    String day = "";
    private static int[] availableDays = new int[] { 0, 0, 0, 0, 0, 0, 0};
    static String startAmPm;
    static String endAmPm;
      static time startTime = new time();
    static time endTime = new time();
    private  TextView textAvailable, textStartTime, textEndTime, textStartAmPm, textEndAmPm;
    private static NumberPicker numberPicker;
    private   CheckBox checkBox;

    public static AppointmentDaysFragment newInstance(boolean isAvailable, String fromtime, String endtime, int sectionNumber) {
        AppointmentDaysFragment fragment = new AppointmentDaysFragment();
        Bundle args = new Bundle();
        args.putInt("SelectedDayPosition", sectionNumber);
        args.putBoolean("Availability", isAvailable);
//        args.putString("StartTime", fromtime);
//        args.putString("EndTime", endtime);
        fragment.setArguments(args);
        return fragment;
    }

    public AppointmentDaysFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_appointment, container, false);

        Bundle b = getArguments();
        position = b.getInt("SelectedDayPosition", 0);
        switch (position)
        {
            case 1: day ="sunday";break;
            case 2: day ="monday";break;
            case 3: day ="tuesday";break;
            case 4: day ="wednesday";break;
            case 5: day ="thursday";break;
            case 6: day ="friday";break;
            case 7: day ="saturday";break;
        }
        final DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        ArrayList<String> appointmentSettings = databaseHandler.getAppointmentSettings(day);
       // mStartTime = b.getString("StartTime", "N/A");
       // mEndTime = b.getString("EndTime", "N/A");
        isAvailable = b.getBoolean("Availability", false);
        mAvailable = isAvailable ? "Available" : "Unavailable";
        initializeCheckBoxes();
        numberPicker = (NumberPicker) view.findViewById(R.id.numberPickerAppointments);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(200);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                databaseHandler.updateAppointmentSettings(DataBaseEnums.KEY_NUMBER_OF_PATIENTS, String.valueOf(newVal), day);

            }
        });
        CheckBox checkBox1 =(CheckBox)view.findViewById(R.id.checkBoxDay);

        if((appointmentSettings.size()>0)&&(startTime!= null)&&(endTime!=null))

        {
            int strtTme[] = timeParser(appointmentSettings.get(1));
            int endTme[] = timeParser(appointmentSettings.get(2));
            startTime.setHour(strtTme[0]);
            startTime.setMinute(strtTme[1]);
            endTime.setHour(endTme[0]);
            endTime.setMinute(endTme[1]);
            numberPicker.setValue(Integer.parseInt(appointmentSettings.get(3)));

            checkBox1.setChecked(true);
        }
        else
        {
            startTime.setHour(0);
            startTime.setMinute(0);
            endTime.setHour(0);
            endTime.setMinute(0);
            numberPicker.setValue(20);
            checkBox1.setChecked(false);
        }
        initViews(view);
//        initParseResponse();
       // textAvailable.setText(mAvailable);
//        textStartTime.setText(mStartTime);
//        textEndTime.setText(mEndTime);

        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        //textDate.setText(sdf.format(c.getTime()));


        return view;
    }

    public void doWork(final time time, final String flag)
    {
        LayoutInflater li = LayoutInflater.from(getActivity());
        final View promptsView = li.inflate(R.layout.time_picker, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
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

                                DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
                                if(checkBox.isChecked())
                                if(flag.equals("end"))
                                databaseHandler.saveAppointmentSettings(day, textStartTime.getText().toString(), endTime.getHour() + ":" + endTime.getMinute(), numberPicker.getValue());
                               // databaseHandler.updateAppointmentSettings(DataBaseEnums.KEY_END_TIME, endTime.getHour() + ":" + endTime.getMinute(), day);
                                else
                                    databaseHandler.saveAppointmentSettings(day, startTime.getHour() + ":" + startTime.getMinute(), textEndTime.getText().toString(), numberPicker.getValue());
                                utility.recreateActivityCompat(getActivity());


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

    private void initViews(View root){
       // textAvailable = (TextView)root.findViewById(R.id.textAppointmentAvailable);
        textStartTime = (TextView)root.findViewById(R.id.startTime);
        textEndTime = (TextView)root.findViewById(R.id.endTime);
        textStartAmPm = (TextView)root.findViewById(R.id.startAmPm);
        textEndAmPm = (TextView)root.findViewById(R.id.endAmPm);
        if(startTime.getHour()>=12)
        {
            startAmPm = "PM";
            if(startTime.getMinute()>10)
                textStartTime.setText((startTime.getHour()-12)+":"+startTime.getMinute());
            else
                textStartTime.setText((startTime.getHour()-12)+":0"+startTime.getMinute());
        }
        else
        {
            startAmPm = "AM";

            if(startTime.getMinute()>10)
                textStartTime.setText((startTime.getHour())+":"+startTime.getMinute());
            else
                textStartTime.setText((startTime.getHour())+":0"+startTime.getMinute());

        }
        if(endTime.getHour()>=12)
        {
            endAmPm = "PM";
            if(endTime.getMinute()>10)
                textEndTime.setText((endTime.getHour()-12)+":"+endTime.getMinute());
            else
                textEndTime.setText((endTime.getHour()-12)+":0"+endTime.getMinute());
        }
        else
        {
            endAmPm = "AM";
            if(endTime.getMinute()>10)
                textEndTime.setText((endTime.getHour())+":"+endTime.getMinute());
            else
                textEndTime.setText((endTime.getHour())+":0"+endTime.getMinute());

        }

        textStartAmPm.setText(startAmPm);
        textEndAmPm.setText(endAmPm);
        textEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWork(endTime,"end");

                //utility.recreateActivityCompat(getActivity());

            }
        });
        textStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWork(startTime,"start");

            }
        });

       // textDate = (TextView)root.findViewById(R.id.textAppointmentDate);
       CheckBox checkBoxDay = (CheckBox)root.findViewById(R.id.checkBoxDay);
        checkBoxDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    checkBox.setChecked(true);

                } else {
                    checkBox.setChecked(false);
                    DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
                    databaseHandler.deleteAppointmentSettings(day);
                }
            }
        });


        Typeface numericTypeFace = Typeface.createFromAsset(getActivity().getAssets(), "digital.ttf");
        textStartTime.setTypeface(numericTypeFace);
        textEndTime.setTypeface(numericTypeFace);
       // textEndTime.setText();
        //textStartTime.setText(mStartTime);
        //textEndTime.setText(mEndTime);
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
public void initializeCheckBoxes()
{
    if(position == 1)
    {
        checkBox = (CheckBox)  getActivity().findViewById(R.id.checkBoxSunday);

    }
    if(position == 2)
    {
        checkBox = (CheckBox)  getActivity().findViewById(R.id.checkBoxMonday);

    }
    if(position == 3)
    {
        checkBox = (CheckBox)  getActivity().findViewById(R.id.checkBoxTuesday);

    }
    if(position == 4)
    {
        checkBox = (CheckBox)  getActivity().findViewById(R.id.checkBoxWednesday);

    }
    if(position == 5)
    {
        checkBox = (CheckBox)  getActivity().findViewById(R.id.checkBoxThursday);

    }
    if(position == 6)
    {
        checkBox = (CheckBox)  getActivity().findViewById(R.id.checkBoxFriday);

    }
    if(position == 7)
    {
        checkBox = (CheckBox)  getActivity().findViewById(R.id.checkBoxSaturday);

    }
}

}
