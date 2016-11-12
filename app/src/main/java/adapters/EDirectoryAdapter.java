package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

import objects.modelEDirectory;

/**
 * Created by romichandra on 13/11/16.
 */

public class EDirectoryAdapter extends BaseAdapter {

    Context context;
    ArrayList<modelEDirectory> list;
    LayoutInflater inflater;
    public EDirectoryAdapter(Context context, ArrayList<modelEDirectory> list) {
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
        TextView textName, textDesignation, textNumber1, textNumber2, textEmail, textCity;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.adapter_association_e_directory, null);
        }

        textName = (TextView)convertView.findViewById(R.id.textNameEDirectory);
        textDesignation = (TextView)convertView.findViewById(R.id.textDesignationEDirectory);
        textNumber1 = (TextView)convertView.findViewById(R.id.textNumber1EDirectory);
        textNumber2 = (TextView)convertView.findViewById(R.id.textNumber2EDirectory);
        textEmail = (TextView)convertView.findViewById(R.id.textEmailEDirectory);
        textCity = (TextView)convertView.findViewById(R.id.textCityEDirectory);

        textName.setText(list.get(position).getName());
        textDesignation.setText(list.get(position).getDesignation());
        textNumber1.setText(list.get(position).getNumber1());
        textNumber2.setText(list.get(position).getNumber2());
        textEmail.setText(list.get(position).getEmail());
        textCity.setText(list.get(position).getAddress());

        return convertView;
    }
}
