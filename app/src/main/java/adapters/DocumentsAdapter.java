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

public class DocumentsAdapter extends ArrayAdapter<Item> {

    private Context context;
    private ArrayList<Item> itemsArrayList;
    private  Activity activity_parent ;
    private MediaScannerConnection conn;
    File[] allFiles ;

    public DocumentsAdapter(Activity activity_parent , Context context, ArrayList<Item> itemsArrayList) {

        super(context, R.layout.row, itemsArrayList);

        this.context = context;
        this.activity_parent = activity_parent;
        this.itemsArrayList = itemsArrayList;
    }
    public void updateReceiptsList(ArrayList<Item> itemsArrayListNew) {
        itemsArrayList.clear();
        itemsArrayList.addAll(itemsArrayListNew);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        final View rowView = inflater.inflate(R.layout.doc_row, parent, false);
        final ImageView videoPlayIcon = (ImageView)rowView.findViewById(R.id.video_play_icon);
        TextView labelView = (TextView) rowView.findViewById(R.id.label);
        final ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView2);
        final document_obj[] doc_obj = new document_obj[1];



        rowView.setTag(position);
        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                int pos = (int)arg0.getTag();
                final DatabaseHandler dbHandle = new DatabaseHandler(getContext());
                if(activity_parent.getClass()== documents.class) {

                doc_obj[0] = dbHandle.getSearchDocument(pos,itemsArrayList);
                    final int pid = itemsArrayList.get(0).getPatient_id();
                    final String name = dbHandle.getPatient(pid).get_name();
                    File storageDir =
                            new File(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES), "Patient Manager/" + pid + "/Documents");
                   /* String path = new File(doc_obj.get_doc_path()).getParent();
                    File folder = new File(path);
                    allFiles = folder.listFiles();*/
                    final String filePath;
                    final File file;
                    if(new File(doc_obj[0].get_doc_path()).exists())
                    {
                        filePath = doc_obj[0].get_doc_path();
                        file = new File(filePath);
                    }
                    else
                    {
                        filePath =  storageDir.getPath()+"/"+doc_obj[0].get_doc_path();
                        file = new File(filePath);
                    }

                    Log.i("documents adapter", filePath);





                    final PopupMenu popup = new PopupMenu(activity_parent, arg0);
                    popup.getMenuInflater().inflate(R.menu.popup_image
                            , popup.getMenu());

                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(activity_parent, "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                            Uri intentUri;
                            if (item.getTitle().equals("open")) {

                               if((filePath.contains(".jpg"))||(filePath.contains(".png"))||(filePath.contains(".PNG"))) {
                                   File storageDir =
                                           new File(Environment.getExternalStoragePublicDirectory(
                                                   Environment.DIRECTORY_PICTURES), "Patient Manager/" + name + "/Documents");
                                   intentUri = Uri.fromFile(new File(filePath));
                                   new SingleMediaScanner(context, file);
                               }
                                else if (filePath.contains(".txt")){
                                   Intent intent = new Intent(Intent.ACTION_EDIT);
                                   Uri uri = Uri.fromFile(new File(filePath));
                                   intent.setDataAndType(uri, "text/plain");
                                  context.startActivity(intent);

                               }
                               else if (filePath.contains(".pdf")){
                                   Intent intent = new Intent(Intent.ACTION_VIEW);
                                   Uri uri = Uri.fromFile(new File(filePath));
                                   intent.setDataAndType(uri, "application/pdf");
                                   Intent intent1 = Intent.createChooser(intent, "Open File");
                                   context.startActivity(intent);

                               }
                               else if (filePath.contains(".doc")){
                                   Intent intent = new Intent(Intent.ACTION_EDIT);
                                   Uri uri = Uri.fromFile(new File(filePath));
                                   intent.setDataAndType(uri, "application/msword");
                                   context.startActivity(intent);

                               }

                            }

                            else if (item.getTitle().equals("rename"))
                            {
                                LayoutInflater li = LayoutInflater.from(context);


                                View promptsView = li.inflate(R.layout.sms_text, null);
                                final EditText editText = (EditText)promptsView.findViewById(R.id.sms_Edit_Text);
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        context);
                                alertDialogBuilder.setView(promptsView);
                                alertDialogBuilder
                                        .setCancelable(false)
                                        .setTitle("Rename Report")
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        dbHandle.updateDocument(DataBaseEnums.KEY_DOC_NAME,editText.getText().toString(),filePath);
                                                        dbHandle.updatePatient(DataBaseEnums.KEY_SYNC_STATUS,"0", String.valueOf(pid));
                                                        utility.recreateActivityCompat(activity_parent);
                                                    }
                                                })
                                        .setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                // create alert dialog
                                AlertDialog alertDialog = alertDialogBuilder.create();

                                // show it
                                alertDialog.show();

                                // FTPHelper.Dowork(filePath,name);

                                // Upload sdcard file
                               // uploadFile(f);





                            }


                            else if (item.getTitle().equals("delete")) {


                                final AlertDialog.Builder alert = new AlertDialog.Builder(activity_parent);

                                alert.setTitle("Alert!!");
                                alert.setMessage("Are you sure to delete");

                                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dbHandle.removeDocument(doc_obj[0]);
                                        dialog.dismiss();
                                        utility.recreateActivityCompat(activity_parent);
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
                else if (( activity_parent.getClass() == View_Media_notes_grid.class)||( activity_parent.getClass() == TabbedActivityCheck.class)
                        ||( activity_parent.getClass() == Treatment_Activity.class)
                        ||( activity_parent.getClass() == Other_Notes_Activity.class))
                {




                    final String filePath = itemsArrayList.get(pos).getDiagnosis();
                    String path = new File(filePath).getParent();
                    File folder = new File(path);
                    allFiles = folder.listFiles();

                    String section = itemsArrayList.get(pos).getTitle();

                    int pid =  itemsArrayList.get(pos).getPatient_id();
                    if(filePath.contains(".jpg"))
                    {


                        PopupMenu popup = new PopupMenu(activity_parent, arg0);
                        popup.getMenuInflater().inflate(R.menu.popup_image


                                , popup.getMenu());

                        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Toast.makeText(activity_parent, "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();


                                if(item.getTitle().equals("open"))
                                {

                                    new SingleMediaScanner(context, allFiles[0]);


                                }
                                else if(item.getTitle().equals("delete"))
                                {


                                    final AlertDialog.Builder alert = new AlertDialog.Builder(activity_parent);

                                    alert.setTitle("Alert!!");
                                    alert.setMessage("Are you sure to delete");

                                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dbHandle.deleteMedia(filePath);
                                            dialog.dismiss();
                                            utility.recreateActivityCompat(activity_parent);
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


                        popup.show();

                    }
                    else
                    {
                        PopupMenu popup = new PopupMenu(activity_parent, arg0);
                        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

                        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Toast.makeText(activity_parent, "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                                Uri intentUri;
                                if(item.getTitle().equals("play"))
                                {
                                     intentUri = Uri.fromFile(new File(filePath));

                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.setDataAndType(intentUri, "video/mp4");
                                    context.startActivity(intent);
                                }
                                else if(item.getTitle().equals("delete"))
                                {


                                    final AlertDialog.Builder alert = new AlertDialog.Builder(activity_parent);

                                    alert.setTitle("Alert!!");
                                    alert.setMessage("Are you sure to delete");

                                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dbHandle.deleteMedia(filePath);
                                            dialog.dismiss();
                                            utility.recreateActivityCompat(activity_parent);
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



                } else if ( activity_parent.getClass() == History_Activity.class)
                {

                }






            }
        });



        // 3. Get the two text view from the rowView

       // TextView diagnosisView = (TextView) rowView.findViewById(R.id.row_diagnosis);

        // 4. Set the text for textView
        String a = (itemsArrayList.get(position).getDiagnosis());
        labelView.setText(itemsArrayList.get(position).getTitle());
        imageView.setImageBitmap (itemsArrayList.get(position).getBmp());
        if((itemsArrayList.get(position).getDiagnosis().contains(".jpg"))||(itemsArrayList.get(position).getDiagnosis().contains(".txt"))||(itemsArrayList.get(position).getDiagnosis().contains(".pdf"))||(itemsArrayList.get(position).getDiagnosis().contains(".doc")))
        {
            videoPlayIcon.setVisibility(View.GONE);
        }
        //diagnosisView.setText((itemsArrayList.get(position).getDiagnosis()));

        // 5. retrn rowView
        return rowView;
    }


    public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

        private MediaScannerConnection mMs;
        private File mFile;

        public  SingleMediaScanner(Context context, File f) {
            mFile = f;
            mMs = new MediaScannerConnection(context, this);
            mMs.connect();
        }

        public void onMediaScannerConnected() {
            mMs.scanFile(mFile.getAbsolutePath(), null);
        }

        public void onScanCompleted(String path, Uri uri) {
            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setDataAndType(uri, "image/*");
            context.startActivity(intent);
            mMs.disconnect();
        }

    }


}