package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

import objects.modelExecCouncil;

/**
 * Created by romichandra on 12/11/16.
 */

public class ExecCouncilAdapter extends BaseAdapter {

    Context context;
    ArrayList<modelExecCouncil> list;
    LayoutInflater inflater;
    public ExecCouncilAdapter(Context context, ArrayList<modelExecCouncil> list) {
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
        TextView textName, textDesignation, textNumber, textEmail;

        if (convertView == null){
            convertView = inflater.inflate(R.layout.adapter_association_exec_council, null);
        }

        textName = (TextView)convertView.findViewById(R.id.textNameExecCouncil);
        textDesignation = (TextView)convertView.findViewById(R.id.textDesignationExecCouncil);
        textNumber = (TextView)convertView.findViewById(R.id.textNumberExecCouncil);
        textEmail = (TextView)convertView.findViewById(R.id.textEMailExecCouncil);

        textName.setText(list.get(position).getName());
        textDesignation.setText(list.get(position).getDesignation());
        textNumber.setText(list.get(position).getPhone());
        textEmail.setText(list.get(position).getEmail());

        return convertView;
    }
}
