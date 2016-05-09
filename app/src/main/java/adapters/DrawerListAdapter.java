package adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sajid.myapplication.DatabaseHandler;

import activity.activity_view_patient_visits;
import objects.Item;
import objects.Patient;
import activity.PatientProfileActivity;
import com.example.sajid.myapplication.R;
import com.example.sajid.myapplication.UserProfile;

public class DrawerListAdapter extends ArrayAdapter<Item> {

    private final Context context;
    private final ArrayList<Item> itemsArrayList;

    public DrawerListAdapter(Context context, ArrayList<Item> itemsArrayList) {

        super(context, R.layout.row, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    public void getLastVisitDate(int pid)
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);

    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;
        // 2. Get rowView from inflater

         rowView = inflater.inflate(R.layout.nav_drawer_list_item, parent, false);




       // rowView.setTag(position);

     /*   rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {




                int pos = (int)arg0.getTag();



                switch (pos)
                {
                    case 0:
                        context.startActivity(new Intent(context, UserProfile.class));
                }

                /*dbHandle.deletePatient(patient);

                MyAdapter.this.notifyDataSetChanged();*/






       //     }
     //   });
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






      //      }
       // });


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
        TextView textView = (TextView) rowView.findViewById(R.id.menuItemTextView);
       ImageView imageView = (ImageView)rowView.findViewById(R.id.menuItemImageView);
        String a = (itemsArrayList.get(position).getTitle());

       /* textView.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            if(position == 0)
                                            context.startActivity(new Intent(context, UserProfile.class));
                                            else
                                                context.startActivity(new Intent(context, activity_view_patient_visits.class));
                                            Toast.makeText(context,"position "+position,Toast.LENGTH_SHORT).show();

                                        }
                                    });*/

        // 4. Set the text for textView
        //String a = (itemsArrayList.get(position).getTitle());
//
     imageView.setImageBitmap(itemsArrayList.get(position).getBmp());
        textView.setText(a);
        // 5. retrn rowView
        return rowView;
    }
}