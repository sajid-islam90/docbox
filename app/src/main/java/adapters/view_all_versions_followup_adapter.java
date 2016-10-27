package adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

import objects.Item;

/**
 * Created by romichandra on 21/10/16.
 */

public class view_all_versions_followup_adapter extends ArrayAdapter<Item> {

    private final Context context;
    private final ArrayList<Item> itemsArrayList;
    private final Activity activity_parent;

    public view_all_versions_followup_adapter(Activity activity_parent , Context context, ArrayList<Item> itemsArrayList) {

        super(context, R.layout.row, itemsArrayList);

        this.context = context;
        this.activity_parent = activity_parent;
        this.itemsArrayList = itemsArrayList;
    }
    static class ViewHolder {
        View rowView ;
        TextView chiefComplaint ;

        TextView date ;


    }

    @Override
    public View getView(int position,  View convertView, final ViewGroup parent) {
        ViewHolder holder ;
        if(convertView==null) {
            holder = new ViewHolder();
            // 1. Create inflater
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 2. Get rowView from inflater
            convertView= inflater.inflate(R.layout.view_all_followup_version_row, parent, false);


            // 3. Get the two text view from the rowView
//            TextView chiefComplaint = (TextView) rowView.findViewById(R.id.textViewComplaint);
//            TextView date = (TextView) rowView.findViewById(R.id.textViewDate);
             holder.chiefComplaint = (TextView) convertView.findViewById(R.id.textViewComplaint);
             holder.date = (TextView) convertView.findViewById(R.id.textViewDate);

        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
            Log.e("all followup adapter","view converted");
        }
        // 4. Set the text for textView
try {
    holder.chiefComplaint.setText(itemsArrayList.get(position).getTitle());

    holder.date.setText(itemsArrayList.get(position).getDiagnosis());
}
catch (Exception e)
{
   // e.printStackTrace();
}

        // 5. retrn rowView
        return convertView;
    }
    public void updateDataSet()
    {
        this.notifyDataSetChanged();
    }
    public void updateDataSet(ArrayList<Item> itemsArrayList)
    {
        this.itemsArrayList.clear();
        this.itemsArrayList.addAll(itemsArrayList);
        this.notifyDataSetChanged();
    }
}
