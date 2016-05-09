package adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
    static class ViewHolder {
        View rowView ;
        TextView labelView ;
        ImageView imageView ;
        TextView diagnosisView ;
        TextView lastVisit ;
        TextView genderAge;
        LinearLayout linearLayout;

    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder ;
        // 1. Create inflater
        if(convertView==null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 2. Get rowView from inflater
            convertView  = inflater.inflate(R.layout.row, parent, false);
            holder = new ViewHolder();
            holder.labelView = (TextView) convertView.findViewById(R.id.label);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView2);
            holder.diagnosisView = (TextView) convertView.findViewById(R.id.row_diagnosis);
            holder.lastVisit = (TextView) convertView.findViewById(R.id.row_last_visit);
            holder.genderAge = (TextView) convertView.findViewById(R.id.genderAge);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.cardview_linearLayout);

            holder.linearLayout.setTag(holder);

        }
        else
        {
            holder = (ViewHolder) convertView.getTag();

        }
        // 3. Get the two text view from the rowView


        // 4. Set the text for textView
        DatabaseHandler dbHandle = new DatabaseHandler(getContext());
        Patient patient = new Patient();
if(itemsArrayList.get(position).getBmp()==null)
{
    itemsArrayList.get(position).setBmp(BitmapFactory.decodeResource(context.getResources(),R.drawable.default_photo));
}

        RoundImage roundedImage = new RoundImage( PhotoHelper.getResizedBitmap(itemsArrayList.get(position).getBmp(), 100, 100));

        patient = dbHandle.getPatient(itemsArrayList.get(position).getPatient_id());
        try {
            patient.set_bmp(PhotoHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(context.getResources(), R.drawable.default_photo)));

           // dbHandle.updatePatient(patient,0);
            String a = (itemsArrayList.get(position).getDiagnosis());
            holder.labelView.setText(itemsArrayList.get(position).getTitle());
            holder.lastVisit.setText(itemsArrayList.get(position).getDate());
            holder.imageView.setImageDrawable(roundedImage);
            holder. diagnosisView.setText((itemsArrayList.get(position).getDiagnosis()));
            holder. genderAge.setText(patient.get_gender() + "/" + patient.get_age());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // 5. retrn rowView
        return convertView;
    }

    public static int getPosition()
    {
        return 1;
    }
    public void updateReceiptsList(ArrayList<Item> itemsArrayListNew) {
//        itemsArrayList.clear();
//        itemsArrayList.addAll(itemsArrayListNew);
        this.notifyDataSetChanged();
    }
    public void updateReceiptsList() {
//        itemsArrayList.clear();
//        itemsArrayList.addAll(itemsArrayListNew);
        this.notifyDataSetChanged();
    }

    @Override
    public int getPosition(Item item) {
        return super.getPosition(item);
    }
}
