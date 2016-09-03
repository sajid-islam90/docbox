package adapters;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;

import objects.personal_obj;
import utilityClasses.DatabaseHandler;

import activity.TabbedActivityCheck;
import objects.DataBaseEnums;
import objects.Item;

import com.elune.sajid.myapplication.R;
import objects.document_obj;

import utilityClasses.utility;

import activity.History_Activity;
import activity.Other_Notes_Activity;
import activity.Treatment_Activity;
import activity.View_Media_notes_grid;
import activity.documents;

public class DegreeListAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> itemsArrayList;
    private  Activity activity_parent ;
    private MediaScannerConnection conn;
    File[] allFiles ;

    public DegreeListAdapter(Activity activity_parent , Context context, ArrayList<String> itemsArrayList) {

        super(context, R.layout.row, itemsArrayList);

        this.context = context;
        this.activity_parent = activity_parent;
        this.itemsArrayList = itemsArrayList;
    }
    public void updateReceiptsList(ArrayList<String> itemsArrayListNew) {
        itemsArrayList.clear();
        itemsArrayList.addAll(itemsArrayListNew);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        final View rowView = inflater.inflate(R.layout.degree_list_row, parent, false);

        TextView labelView = (TextView) rowView.findViewById(R.id.textViewDegreeRow);

                rowView.setTag(position);

        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                int pos = (int)arg0.getTag();
                final DatabaseHandler dbHandle = new DatabaseHandler(getContext());


                    final PopupMenu popup = new PopupMenu(activity_parent, arg0);
                    popup.getMenuInflater().inflate(R.menu.degree_pop_up
                            , popup.getMenu());

                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(activity_parent, "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();


                           if (item.getTitle().equals("delete")) {


                                final AlertDialog.Builder alert = new AlertDialog.Builder(activity_parent);

                                alert.setTitle("Alert!!");
                                alert.setMessage("Are you sure to delete "+itemsArrayList.get(position));

                                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

itemsArrayList.remove(position);
                                        String degrees ="";
                                        for(int i = 0;i<itemsArrayList.size();i++)

                                        {
                                            degrees = degrees + itemsArrayList.get(i);
                                           if(itemsArrayList.size()!=1) {

                                               if(i!=itemsArrayList.size()-1)
                                               degrees = degrees+"/";

                                           }
                                        }
                                        if(!degrees.equals(""))
                                        if(degrees.charAt(0)=='/')
                                        {
                                            degrees = degrees.substring(1);
                                        }
                                        DatabaseHandler databaseHandler = new DatabaseHandler(context);

                                        databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_DESIGNATION,degrees);
                                        notifyDataSetChanged();
                                    }
                                });


                                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                    }
                                });
                                alert.show();

                            }
                            return true;


                        }
                    });

                    /** Showing the popup menu */
                    popup.show();
                }

                /*dbHandle.deletePatient(patient);

                MyAdapter.this.notifyDataSetChanged();*/







        });



        // 3. Get the two text view from the rowView

        // TextView diagnosisView = (TextView) rowView.findViewById(R.id.row_diagnosis);

        // 4. Set the text for textView
        String a = (itemsArrayList.get(position));
        labelView.setText(a);

        //diagnosisView.setText((itemsArrayList.get(position).getDiagnosis()));

        // 5. retrn rowView
        return rowView;
    }





}