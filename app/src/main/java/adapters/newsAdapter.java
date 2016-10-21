package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elune.sajid.myapplication.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;

import objects.Item;
import utilityClasses.DatabaseHandler;

public class newsAdapter extends ArrayAdapter<Item> {

    private final Context context;
    public final ArrayList<Item> itemsArrayList;

    public newsAdapter(Context context, ArrayList<Item> itemsArrayList) {

        super(context, R.layout.news_preview, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    public void getLastVisitDate(int pid)
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);

    }
    static class ViewHolder {
        View rowView ;
        TextView titleView ;
        ImageView imageView ;
        TextView dateView ;
        TextView sourceView;
//RelativeLayout relativeLayout;
        LinearLayout linearLayout;

    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder ;
        // 1. Create inflater
        if(convertView==null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//Toast.makeText(context,position,Toast.LENGTH_LONG).show();
            // 2. Get rowView from inflater
            convertView  = inflater.inflate(R.layout.news_preview, parent, false);
            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.textViewTitle);
            holder.sourceView = (TextView) convertView.findViewById(R.id.textViewSource);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageViewPic);
            holder.dateView = (TextView) convertView.findViewById(R.id.textViewDate);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayoutNews);
//holder.relativeLayout = (RelativeLayout)convertView.findViewById(R.id.loadingPanel);

            holder.linearLayout.setTag(holder);

        }
        else
        {
            holder = (ViewHolder) convertView.getTag();

        }
        // 3. Get the two text view from the rowView


        // 4. Set the text for textView
        DatabaseHandler dbHandle = new DatabaseHandler(getContext());

String newsUrl = itemsArrayList.get(position).getDiagnosis();

        int i = newsUrl.indexOf("//");
        newsUrl = newsUrl.substring(i+2);
        i = newsUrl.indexOf("/");
        newsUrl = newsUrl.substring(0,i);
        holder.titleView.setText(itemsArrayList.get(position).getTitle());
        holder.sourceView.setText(newsUrl);
        holder.dateView.setText(itemsArrayList.get(position).getDate());
        String urldisplay = itemsArrayList.get(position).getExtra();
        Picasso.with(context).load(urldisplay)
                .placeholder(R.drawable.spin_animation)
                .resize(100, 100)
                .centerCrop().into(holder.imageView);
        Bitmap mIcon11 = null;
//        try {
//
//            //InputStream in = new java.net.URL(urldisplay).openStream();
//            //mIcon11 = BitmapFactory.decodeStream(in);
//            new DownloadImageTask(holder.imageView)
//                    .execute(urldisplay);
//
//        } catch (Exception e) {
//           // Log.e("Error", e.getMessage());
//            e.printStackTrace();
//        }
        //bmImage.setImageBitmap(result);
      //  holder.imageView.setImageBitmap(mIcon11);

        // 5. retrn rowView
        return convertView;
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        RelativeLayout relativeLayout;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            //.setVisibility(View.GONE);

        }
    }
    public static int getPosition()
    {
        return 1;
    }
    public void updateReceiptsList(ArrayList<Item> itemsArrayListNew) {
       //itemsArrayList.clear();
        if((itemsArrayListNew!=null)&&(itemsArrayList!=null))
        itemsArrayList.addAll(itemsArrayListNew);
        this.notifyDataSetChanged();
    }
    public void updateReceiptsList() {
        itemsArrayList.clear();
//        itemsArrayList.addAll(itemsArrayListNew);
        this.notifyDataSetChanged();
    }

    @Override
    public int getPosition(Item item) {
        return super.getPosition(item);
    }
}
