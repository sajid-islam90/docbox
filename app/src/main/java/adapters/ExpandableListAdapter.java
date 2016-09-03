package adapters;

/**
 * Created by sajid on 3/23/2016.
 */
import java.io.File;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.elune.sajid.myapplication.R;

import objects.DataBaseEnums;
import utilityClasses.DatabaseHandler;
import utilityClasses.utility;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    private List<String> _listDataHeaderDates;
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private HashMap<String, List<String>> _listDataChildDates;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData,List<String> listDataHeaderDates, HashMap<String, List<String>> listChildDataDates) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataHeaderDates = listDataHeaderDates;
        this._listDataChild = listChildData;
        this._listDataChildDates = listChildDataDates;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        String childTextDates = "";
        final String childText = (String) getChild(groupPosition, childPosition);
        if(!isLastChild)
         childTextDates = _listDataChildDates.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        TextView txtListChildDate = (TextView) convertView
                .findViewById(R.id.textViewDate);

        txtListChildDate.setText(childTextDates);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        String headerDate = (String)_listDataHeaderDates.get(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        final View finalConvertView = convertView;
        lblListHeader.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final PopupMenu popup = new PopupMenu(_context, finalConvertView);
                popup.getMenuInflater().inflate(R.menu.diagnosis_popup
                        , popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {



                        if (item.getTitle().equals("delete")) {

                            final AlertDialog.Builder alert = new AlertDialog.Builder(_context);

                            alert.setTitle("Alert!!");
                            alert.setMessage("Are you sure to delete "+ _listDataHeader.get(groupPosition));

                            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    DatabaseHandler databaseHandler = new DatabaseHandler(_context);
                                    databaseHandler.deleteDiagnosis(_listDataHeader.get(groupPosition));
                                    _listDataHeader.remove(groupPosition);
                                    updateReceiptsList();
                                    dialog.dismiss();

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

                return false;
            }
        });
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        TextView lblListHeaderDate = (TextView) convertView
                .findViewById(R.id.textViewHeaderDate);
        lblListHeaderDate.setTypeface(null, Typeface.ITALIC);
        lblListHeaderDate.setText(headerDate);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
    public void updateReceiptsList(List<String> listDataHeader,
                                   HashMap<String, List<String>> listChildData,List<String> listDataHeaderDates, HashMap<String, List<String>> listChildDataDates) {
        _listDataChild.clear();
       _listDataChild.putAll(listChildData);
        _listDataChildDates.clear();
        _listDataChildDates.putAll(listChildDataDates);
_listDataHeader.clear();
        _listDataHeader.addAll(listDataHeader);
        _listDataHeaderDates.clear();
        _listDataHeaderDates.addAll(listDataHeaderDates);
        this.notifyDataSetChanged();
    }
    public void updateReceiptsList() {

        this.notifyDataSetChanged();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
