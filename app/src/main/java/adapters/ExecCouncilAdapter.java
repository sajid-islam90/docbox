package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elune.sajid.myapplication.R;
import com.squareup.picasso.Picasso;

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
        ImageView imageDoc;

        if (convertView == null){
            convertView = inflater.inflate(R.layout.adapter_association_exec_council, null);
        }
        imageDoc = (ImageView)convertView.findViewById(R.id.imgDoctorExecCouncil);

        textName = (TextView)convertView.findViewById(R.id.textNameExecCouncil);
        textDesignation = (TextView)convertView.findViewById(R.id.textDesignationExecCouncil);
        textNumber = (TextView)convertView.findViewById(R.id.textNumberExecCouncil);
        textEmail = (TextView)convertView.findViewById(R.id.textEMailExecCouncil);

        textName.setText(list.get(position).getName());
        textDesignation.setText(list.get(position).getDesignation());
        textNumber.setText(list.get(position).getPhone());
        textEmail.setText(list.get(position).getEmail());



        try{
            Picasso.with(context)
                            .load(list.get(position).getDrawableid())
                            .placeholder(R.drawable.default_pic)
                            .into(imageDoc);
//            switch (position){
//                case 1:{
//                    Picasso.with(context)
//                            .load(R.drawable.img_vineetasingh)
//                            .placeholder(R.drawable.default_pic)
//                            .into(imageDoc);
////                    imageDoc.setImageResource(R.drawable.img_vineetasingh);
//                    break;
//                }
//                case 2:{
//                    Picasso.with(context)
//                            .load(R.drawable.img_rcgupta)
//                            .placeholder(R.drawable.default_pic)
//                            .into(imageDoc);
////                    imageDoc.setImageResource(R.drawable.img_rcgupta);
//                    break;
//                }
//                case 3:{
//                    Picasso.with(context)
//                            .load(R.drawable.img_malaychaturvedi)
//                            .placeholder(R.drawable.default_pic)
//                            .into(imageDoc);
////                    imageDoc.setImageResource(R.drawable.img_malaychaturvedi);
//                    break;
//                }
//                case 4:{
//                    Picasso.with(context)
//                            .load(R.drawable.img_dharmendranath)
//                            .placeholder(R.drawable.default_pic)
//                            .into(imageDoc);
////                    imageDoc.setImageResource(R.drawable.img_dharmendranath);
//                    break;
//                }
//                case 6:{
//                    Picasso.with(context)
//                            .load(R.drawable.img_smohan)
//                            .placeholder(R.drawable.default_pic)
//                            .into(imageDoc);
////                    imageDoc.setImageResource(R.drawable.img_smohan);
//                    break;
//                }
//                case 7:{
//                    Picasso.with(context)
//                            .load(R.drawable.img_abhishekchandra)
//                            .placeholder(R.drawable.default_pic)
//                            .into(imageDoc);
////                    imageDoc.setImageResource(R.drawable.img_abhishekchandra);
//                    break;
//                }
//                case 12:{
//                    Picasso.with(context)
//                            .load(R.drawable.img_ssom)
//                            .placeholder(R.drawable.default_pic)
//                            .into(imageDoc);
////                    imageDoc.setImageResource(R.drawable.img_ssom);
//                    break;
//                }
//            }
        }catch (Exception e){

        }

        return convertView;
    }
}
