package adapters;

import android.app.Activity;
import android.content.Context;
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

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        final View rowView = inflater.inflate(R.layout.view_all_followup_version_row, parent, false);


        // 3. Get the two text view from the rowView
        TextView chiefComplaint = (TextView) rowView.findViewById(R.id.textViewComplaint);
        TextView date = (TextView) rowView.findViewById(R.id.textViewDate);
//
//        rowView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v){
//                final PopupMenu popup = new PopupMenu(context,rowView);
//                popup.getMenuInflater().inflate(R.menu.followup_pop_up
//                        , popup.getMenu());
//
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//
//
//
//                        if (item.getTitle().equals("Delete")) {
//
//                            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
//
//                            alert.setTitle("Alert!!");
//                            alert.setMessage("Are you sure to delete this follow up ? ");
//
//                            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
////                                    DatabaseHandler databaseHandler = new DatabaseHandler(_context);
////                                    databaseHandler.deleteDiagnosis(_listDataHeader.get(groupPosition));
////                                    _listDataHeader.remove(groupPosition);
////                                    updateReceiptsList();
//                                    dialog.dismiss();
//
//                                }
//                            });
//
//
//                            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    dialog.dismiss();
//                                }
//                            });
//                            alert.show();
//
//                        }
//                        if (item.getTitle().equals("Open")) {
//
//
//
//                        }
//
//
//                        return false;
//
//
//                    }
//                });
//
//                /** Showing the popup menu */
//                popup.show();
//                return false;
//            }
//        });

        // 4. Set the text for textView

        chiefComplaint.setText(itemsArrayList.get(position).getTitle());

       date.setText(itemsArrayList.get(position).getDiagnosis());


        // 5. retrn rowView
        return rowView;
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
