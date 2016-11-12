package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

import objects.modelAssociationEvent;

/**
 * Created by romichandra on 11/11/16.
 */

public class AssociationEventsAdapter extends BaseAdapter {

    ArrayList<modelAssociationEvent> list;
    Context context;
    LayoutInflater inflater;
    public AssociationEventsAdapter(Context context, ArrayList<modelAssociationEvent> list) {
        this.list = list;
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textTitle, textDate, textTime, textLocation;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.adapter_association_upcoming_events, null);
        }

        textTitle = (TextView)convertView.findViewById(R.id.textAssociationEventTitle);
        textDate = (TextView)convertView.findViewById(R.id.textAssociationEventDate);
        textTime = (TextView)convertView.findViewById(R.id.textAssociationEventTime);
        textLocation = (TextView)convertView.findViewById(R.id.textAssociationEventLocation);

        textTitle.setText(list.get(position).getTitle());
        textDate.setText(list.get(position).getDate());
        textTime.setText(list.get(position).getTime());
        textLocation.setText(list.get(position).getLocation());
        return convertView;
    }
}
