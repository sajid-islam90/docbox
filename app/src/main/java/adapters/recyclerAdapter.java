
package adapters;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import objects.*;

import utilityClasses.DatabaseHandler;
import com.elune.sajid.myapplication.R;
import utilityClasses.utility;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.ViewHolder>{

    private final Context context;
    private final ArrayList<Item> itemsArrayList;
    private final Activity activity_parent;
    private final int pid;
    private final int version;
    private final int photoFlag;// = 1 for two text fields: =2 for an image
    TextView textView1;
    TextView textView2;
    ImageView imageView;
    ImageView videoPlayIcon;
    LinearLayout linearLayout;
    View viewL;

    public recyclerAdapter(Activity activity_parent , Context context, ArrayList<Item> itemsArrayList,int photoFlag,int pid,int version) {

       // super(context, R.layout.row, itemsArrayList);

        this.context = context;
        this.photoFlag = photoFlag;
        this.activity_parent = activity_parent;
        this.itemsArrayList = itemsArrayList;
       this.pid = pid;
        this.version = version;
    }

    public void updateReceiptsList(ArrayList<Item> itemsArrayListNew) {

        //itemsArrayList.add(itemsArrayListNew);
        this.notifyDataSetChanged();
    }
    public void updateReceiptsListOtherNotes(ArrayList<Item> itemsArrayListNew) {
        itemsArrayList.clear();
       itemsArrayList.addAll(itemsArrayListNew);
        this.notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        viewL = inflater.inflate(R.layout.recycler_two_text_field_horizontal, parent, false);


        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(viewL);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        textView1 = holder.Field;
        textView2=holder.Value;
        imageView = holder.Photo;
        videoPlayIcon = holder.VideoPlayIcon;
        holder.position = position;
        linearLayout = holder.linearLayout1;
        textView1.setText(itemsArrayList.get(position).getTitle());
        textView2.setText(itemsArrayList.get(position).getDiagnosis());
        imageView.setImageBitmap(itemsArrayList.get(position).getBmp());
        if(photoFlag == 1)
        {
            imageView.setVisibility(View.GONE);

             videoPlayIcon.setVisibility(View.GONE);

        }
        else
        {
            textView1.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
            if(itemsArrayList.get(position).getDiagnosis().contains(".jpg"))
            videoPlayIcon.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
TextView Field;
        TextView Value;
        ImageView Photo;
        ImageView VideoPlayIcon;
        LinearLayout linearLayout1;
        int position;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            Field = (TextView)itemView.findViewById(R.id.textField1);
            Value = (TextView)itemView.findViewById(R.id.textField2);
            Photo = (ImageView)itemView.findViewById(R.id.imageView3);
            VideoPlayIcon = (ImageView)itemView.findViewById(R.id.video_play_icon);
            linearLayout1 = (LinearLayout)itemView.findViewById(R.id.horizontal_cardview_liniear_layout);



        }

        @Override
        public void onClick(View v) {
            final DatabaseHandler dbHandle = new DatabaseHandler(context);
            final File storageDir =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/" + pid + "/Notes");
if(photoFlag == 2)
{
    if((itemsArrayList.get(position).getDiagnosis().contains(".jpg"))||(itemsArrayList.get(position).getDiagnosis().contains(".png"))
        ||(itemsArrayList.get(position).getDiagnosis().contains(".PNG")))
    {


        PopupMenu popup = new PopupMenu(activity_parent, v);
        popup.getMenuInflater().inflate(R.menu.popup_image


                , popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(activity_parent, "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                Uri intentUri;
                if(item.getTitle().equals("open"))
                {

                    if(new File(itemsArrayList.get(position).getDiagnosis()).exists())

                    intentUri = Uri.fromFile(new File(itemsArrayList.get(position).getDiagnosis()));
                    else
                    intentUri = Uri.fromFile(new File(storageDir+"/"+itemsArrayList.get(position).getDiagnosis()));

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(intentUri, "image/*");
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

                            dbHandle.deleteMedia(itemsArrayList.get(position).getDiagnosis());
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




                       /* Intent intent = new Intent(context, VideoFull.class);
                        intent.putExtra("id", pid);
                        intent.putExtra("path", filePath);*/


        PopupMenu popup = new PopupMenu(activity_parent, v);
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(activity_parent, "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                Uri intentUri;
                if(item.getTitle().equals("play"))
                {
                    intentUri = Uri.fromFile(new File(itemsArrayList.get(position).getDiagnosis()));

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

                            dbHandle.deleteMedia(itemsArrayList.get(position).getDiagnosis());
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

}
    else {
    final int p = getAdapterPosition();
    String s = itemsArrayList.get(p).getTitle();
    String s1 = itemsArrayList.get(p).getDiagnosis();
    final Dialog dialog = new Dialog(context);
    dialog.setContentView(R.layout.two_field_alert_box);

    dialog.setTitle(s)
          ;
final other_obj otherObj = new other_obj();
    otherObj.set_field_name(s);
    otherObj.set_field_value(s1);
    TextView textView3 = (TextView) dialog.findViewById(R.id.value);
    ImageButton imageButton = (ImageButton)dialog.findViewById(R.id.imageButton2);
    imageButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dbHandle.deleteOtherNote(otherObj,pid,version);

        }
    });

    textView3.setText(s1);
    dialog.show();
}

        }
    }
}
