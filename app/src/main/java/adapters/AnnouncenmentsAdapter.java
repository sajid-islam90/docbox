package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

import objects.modelAnnouncement;

/**
 * Created by romichandra on 14/11/16.
 */

public class AnnouncenmentsAdapter extends BaseAdapter {
    Context context;
    ArrayList<modelAnnouncement> list;
    LayoutInflater inflater;
    public AnnouncenmentsAdapter(Context context, ArrayList<modelAnnouncement> list) {
        this.context = context;
        this.list = list;
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
        TextView textDate, textMonth, textTitle, textSubtitle;

        if (convertView == null){
            convertView = inflater.inflate(R.layout.adapter_association_announcements, null);
        }

        textDate = (TextView)convertView.findViewById(R.id.textDateAnouncement);
        textMonth = (TextView)convertView.findViewById(R.id.textMonthAnouncement);
        textTitle = (TextView)convertView.findViewById(R.id.textTitleAnnouncement);
        textSubtitle = (TextView)convertView.findViewById(R.id.textSubTitleAnnouncement);

        textDate.setText(list.get(position).getDate());
        textMonth.setText(list.get(position).getMonth());
        textTitle.setText(list.get(position).getTitle());
        textSubtitle.setText(list.get(position).getSubtitle());

        return convertView;
    }
}
