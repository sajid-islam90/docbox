package adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sajid.myapplication.DatabaseHandler;
import objects.Item;
import objects.Patient;
import activity.PatientProfileActivity;

import com.example.sajid.myapplication.PhotoHelper;
import com.example.sajid.myapplication.R;
import com.example.sajid.myapplication.RoundImage;

public class MyAdapter extends ArrayAdapter<Item> {

    private final Context context;
    private final ArrayList<Item> itemsArrayList;

    public MyAdapter(Context context, ArrayList<Item> itemsArrayList) {

        super(context, R.layout.row, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    public void getLastVisitDate(int pid)
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);

    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row, parent, false);
        TextView labelView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView2);
        TextView diagnosisView = (TextView) rowView.findViewById(R.id.row_diagnosis);
        TextView lastVisit = (TextView)rowView.findViewById(R.id.row_last_visit);
        TextView genderAge = (TextView)rowView.findViewById(R.id.genderAge);
        LinearLayout linearLayout = (LinearLayout)rowView.findViewById(R.id.cardview_linearLayout);

        linearLayout.setTag(position);

       /* linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                int pos = (int)arg0.getTag();
                DatabaseHandler dbHandle = new DatabaseHandler(getContext());
                Patient patient = new Patient();
                patient = dbHandle.getSearchPatient(pos,itemsArrayList);
                Intent intent = new Intent(context,PatientProfileActivity.class);
                intent.putExtra("id",patient.get_id());
                context.startActivity(intent);
                /*dbHandle.deletePatient(patient);

                MyAdapter.this.notifyDataSetChanged();*/






        //    }
       // });
       /* ImageView imageView1 = (ImageView) rowView.findViewById(R.id.imageView2);
        imageView1.setTag(position);
        imageView1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                int pos = (int)arg0.getTag();
                DatabaseHandler dbHandle = new DatabaseHandler(getContext());
                Patient patient = new Patient();
                patient = dbHandle.getSearchPatient(pos,itemsArrayList);
                Intent intent = new Intent(context,PatientProfileActivity.class);
                intent.putExtra("id",patient.get_id());
                context.startActivity(intent);
                /*dbHandle.deletePatient(patient);

                MyAdapter.this.notifyDataSetChanged();*/






        //   }
      //  });


        /*
        Button b2 = (Button) rowView.findViewById(R.id.button2);
        b2.setTag(position);
        b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                int pos = (int)arg0.getTag();
                DatabaseHandler dbHandle = new DatabaseHandler(getContext());
                Patient patient = new Patient();
                patient = dbHandle.getPatient(pos);
                dbHandle.deletePatient(patient);

                MyAdapter.this.notifyDataSetChanged();


                Toast.makeText(context,"Deleted "+patient.get_name(),Toast.LENGTH_SHORT)
                        .show();



            }
        });
        */
        // 3. Get the two text view from the rowView


        // 4. Set the text for textView
        DatabaseHandler dbHandle = new DatabaseHandler(getContext());
        Patient patient = new Patient();


        RoundImage roundedImage = new RoundImage( PhotoHelper.getResizedBitmap(itemsArrayList.get(position).getBmp(), 100, 100));
        patient = dbHandle.getPatient(itemsArrayList.get(position).getPatient_id());
        String a = (itemsArrayList.get(position).getDiagnosis());
        labelView.setText(itemsArrayList.get(position).getTitle());
        lastVisit.setText(itemsArrayList.get(position).getDate());
        imageView.setImageDrawable(roundedImage);
        diagnosisView.setText((itemsArrayList.get(position).getDiagnosis()));
        genderAge.setText(patient.get_gender()+"/"+patient.get_age());

        // 5. retrn rowView
        return rowView;
    }

    public static int getPosition()
    {
        return 1;
    }

    @Override
    public int getPosition(Item item) {
        return super.getPosition(item);
    }
}
