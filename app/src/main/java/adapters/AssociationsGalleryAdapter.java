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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import objects.modelGallery;

/**
 * Created by romichandra on 15/11/16.
 */

public class AssociationsGalleryAdapter extends BaseAdapter {

    ArrayList<modelGallery> list;
    Context context;
    LayoutInflater inflater;
    public AssociationsGalleryAdapter(Context context, ArrayList<modelGallery> list) {
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
        TextView textTitle;
        ImageView imageThumb;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.adapter_association_gallery, null);
        }

        textTitle = (TextView)convertView.findViewById(R.id.textTitleGallery);
        imageThumb = (ImageView)convertView.findViewById(R.id.imgVideoThumbnail);


        textTitle.setText(list.get(position).getTitle());

        try{
            String img_url="http://img.youtube.com/vi/"+extractYoutubeId("https://www.youtube.com/watch?v=IsjnCXcqMNE")+"/0.jpg"; // this is link which will give u thumnail image of that video

            Picasso.with(context)
                    .load(img_url)
                    .placeholder(R.drawable.playyoutube)
                    .into(imageThumb);

        }catch (Exception e){}

        return convertView;
    }

    public String extractYoutubeId(String url) throws MalformedURLException {
        String query = new URL(url).getQuery();
        String[] param = query.split("&");
        String id = null;
        for (String row : param) {
            String[] param1 = row.split("=");
            if (param1[0].equals("v")) {
                id = param1[1];
            }
        }
        return id;
    }
}
