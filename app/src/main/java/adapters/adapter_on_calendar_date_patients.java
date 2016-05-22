package adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import utilityClasses.DatabaseHandler;
import utilityClasses.PhotoHelper;
import com.elune.sajid.myapplication.R;
import utilityClasses.RoundImage;
import utilityClasses.utility;
import com.loopj.android.http.RequestParams;

import org.json.simple.JSONValue;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import activity.PatientProfileActivity;
import objects.DataBaseEnums;
import objects.Patient;
import objects.personal_obj;

/**
 * Created by nevermore on 10/25/2015.
 */
public class adapter_on_calendar_date_patients extends BaseAdapter {
    Context context;
    boolean showCancelButton;
    LayoutInflater lf;
    ArrayList<String> names;
    String date;
    FragmentManager fragManager = null;

    public adapter_on_calendar_date_patients(Context ctx, ArrayList<String> names,String date,boolean showCancelButton) {
        this.context = ctx;
        this.showCancelButton = showCancelButton;
        this.names = names;
        this.date = date;
        lf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        Bitmap bmp = null;
        Resources res = context.getResources();
        final String address = res.getString(R.string.action_server_ip_address);
        final RequestParams params = new RequestParams();
        if (convertView==null){
            view = lf.inflate(R.layout.adapter_on_calendar_date_patients, null);
            convertView= view;
        }
        final DatabaseHandler dbHandle = new DatabaseHandler(context);
       // names = dbHandle.getAppointmentsForDate(date);
        personal_obj personalObj =  dbHandle.getPersonalInfo();
        final ImageButton imageButtonAddNewFirstAidPatient = (ImageButton)convertView.findViewById(R.id.addNewFirstAidPatient);
        final ImageButton imageButtonCancelAppointment = (ImageButton)convertView.findViewById(R.id.cancelAppointment);
        LinearLayout linearLayout = (LinearLayout)convertView.findViewById(R.id.rowLinearLayout);
        Patient patient = new Patient();
        if(Integer.parseInt(names.get(position))!=0)
        {
            patient = dbHandle.getPatient(Integer.parseInt(names.get(position)));
            if(patient == null)
            {
                patient =dbHandle.getPatientFromFirstAidId(Integer.parseInt(names.get(position)));
            }


        }
        if(showCancelButton)
        {
            imageButtonCancelAppointment.setVisibility(View.VISIBLE);
        }
        else
        {
            imageButtonCancelAppointment.setVisibility(View.GONE);
        }
        imageButtonCancelAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                doWork(position);
            }
        });
        if(patient == null)
        {
            patient =dbHandle.getFirstAidPatient(Integer.parseInt(names.get(position)));
            imageButtonAddNewFirstAidPatient.setVisibility(View.VISIBLE);
            final Patient finalPatient1 = patient;
            try {
                bmp = BitmapFactory.decodeResource(res,R.drawable.add_new_photo);
                finalPatient1.set_bmp(PhotoHelper.getBitmapAsByteArray(bmp));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            final Patient finalPatient2 = patient;



            imageButtonAddNewFirstAidPatient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long pid = dbHandle.addPatient(finalPatient1);
                    utility.addFieldsToPatient((int) pid, context);
                    dbHandle.updatePatient(DataBaseEnums.KEY_FIRST_AID_ID, names.get(position), String.valueOf(pid));
                    dbHandle.updateAppointments(DataBaseEnums.KEY_ID, String.valueOf(pid), names.get(position));
                    //imageButtonAddNewFirstAidPatient.setVisibility(View.GONE);
                    Toast.makeText(context, finalPatient1.get_name() + "added", Toast.LENGTH_SHORT).show();
                    String patientJson = dbHandle.composeJSONfromSQLitePatient(String.valueOf(pid), context);
                    params.put("usersJSON", patientJson);
                    utility.sync("http://" + address + "/insertPatient.php", params, context);
                }
            });
        }
        else
        {
            final Patient finalPatient = patient;
            imageButtonAddNewFirstAidPatient.setVisibility(View.GONE);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PatientProfileActivity.class);
                    intent.putExtra("id", finalPatient.get_id());
                    context.startActivity(intent);
                }
            });
        }
       //else


            TextView textView = (TextView)convertView.findViewById(R.id.text_calendar_list_view);

            textView.setText(patient.get_name());
            ImageView imageView = (ImageView)convertView.findViewById(R.id.image_calendar_list_view);

        if(patient.get_bmp()!=null)
            bmp = BitmapFactory.decodeByteArray(patient.get_bmp(),0,patient.get_bmp().length);
        else
        bmp = BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.add_new_photo);
            RoundImage roundedImage;

            if(bmp!=null)
            {bmp = PhotoHelper.getResizedBitmap(bmp, 100, 100);
                roundedImage = new RoundImage(bmp);
                // Bitmap bmpImage = BitmapFactory.decodeByteArray(image, 0, image.length);

            }
            else {

                bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.add_new_photo);
                roundedImage = new RoundImage(bmp);
                imageView.setBackgroundResource(R.drawable.add_new_photo);
            }
        imageView.setImageDrawable(roundedImage);




        return convertView;

    }



    public void  doWork(final int position)
    {
        {
            final DatabaseHandler dbHandle = new DatabaseHandler(context);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    final ArrayList<ArrayList<String>> notesList;
                    notesList = new ArrayList<ArrayList<String>>();
                    final RequestParams paramsDocuments = new RequestParams();
                    final RequestParams paramsCancelMessage = new RequestParams();
                    final ArrayList<String> list = new ArrayList<>();

                    SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    list.add(String.valueOf(dbHandle.getCustomerId()));
                    Patient patient = dbHandle.getPatient(Integer.parseInt(names.get(position)));
                    if(patient == null)
                    {
                        patient = dbHandle.getPatientFromFirstAidId(Integer.parseInt(names.get(position)));
                    }
                    list.add(String.valueOf(patient.get_first_aid_id()));
                    list.add(date);
                    String dayOfTheWeek ="";
                    Date date1 = null;
                    try {
                        date1 = sdf.parse(date);
                        dayOfTheWeek  = sdf1.format(date1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    ArrayList<String> settings = new ArrayList<>();


                    notesList.add(list);
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);


                    String hash = sharedPref.getString(context.getString(R.string.hash_code), "");
                    ArrayList<String> hex = new ArrayList<>();
                    hex.add(hash);
                    notesList.add(hex);
                    String s1 = null;

                    StringWriter out = new StringWriter();
                    try {
                        JSONValue.writeJSONString(notesList, out);
                        s1 = out.toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    paramsDocuments.put("cancelAppointment", s1);
                    String address ="docbox.co.in/sajid/"; //appointment_settings.this.getResources().getString(R.string.action_server_ip_address);
                    utility.sync("http://" + address + "/cancelAppointment.php", paramsDocuments, context);
                    //sendCancellationMessage(position);
                    names.remove(position);



notifyDataSetChanged();




                    // User clicked OK button
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            builder.setMessage("Are you sure you want to delete an appointment for ")
                    .setTitle("Book Appointment");
            AlertDialog dialogNew = builder.create();
            dialogNew.show();
        }
    }
    public void  sendCancellationMessage(final int position)
    {
        {
            final DatabaseHandler dbHandle = new DatabaseHandler(context);



                    Patient patient = dbHandle.getPatient(Integer.parseInt(names.get(position)));
                    if ((patient.get_contact_number() != null) && (!patient.get_contact_number().equals(" "))) {


                    final RequestParams paramsCancelMessage = new RequestParams();
                    final ArrayList<String> list = new ArrayList<>();

                    SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    list.add(dbHandle.getPersonalInfo().get_name());
                    list.add(patient.get_contact_number());
                    list.add(date);

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);


                    String hash = sharedPref.getString(context.getString(R.string.hash_code), "");
                    ArrayList<String> hex = new ArrayList<>();
                    hex.add(hash);

                    String s1 = null;

                    StringWriter out = new StringWriter();
                    try {
                        JSONValue.writeJSONString(list, out);
                        s1 = out.toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    paramsCancelMessage.put("data", s1);
                    String address = "docbox.co.in/sajid/"; //appointment_settings.this.getResources().getString(R.string.action_server_ip_address);
                    utility.sync("http://" + address + "/appointmentCancelPatientSMS.php", paramsCancelMessage, context);





                }
                    else
                    {
                        Toast.makeText(context,"No phone number found , no message sent",Toast.LENGTH_SHORT);
                    }
                // User clicked OK button
            }


        }


    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public int getCount() {
        return names.size();
    }
}
