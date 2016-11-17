package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

import objects.modelSubject;


/**
 * Created by romichandra on 15/11/16.
 */

public class AssociationConferenceSubjectsAdapter extends BaseAdapter{

    ArrayList<modelSubject> list;
    Context context;
    LayoutInflater inflater;
    public AssociationConferenceSubjectsAdapter(Context context, ArrayList<modelSubject> list) {
        this.context = context;
        this.list = list;
        this.inflater = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
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
        TextView textTitle, textSubtitle;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.adapter_association_conference_subject, null);
        }

        textTitle = (TextView)convertView.findViewById(R.id.textTitleSubject);
        textSubtitle = (TextView)convertView.findViewById(R.id.textSubtitleSubject);

        textTitle.setText(list.get(position).getTitle());
        textSubtitle.setText(list.get(position).getSubtitle());

        return convertView;
    }

}
