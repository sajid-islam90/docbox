package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sajid.myapplication.DatabaseHandler;
import com.example.sajid.myapplication.PhotoHelper;
import com.example.sajid.myapplication.R;
import com.example.sajid.myapplication.RoundImage;

import java.util.ArrayList;

import objects.personal_obj;

/**
 * Created by nevermore on 10/25/2015.
 */
public class adapter_on_calendar_date_patients extends BaseAdapter {
    Context context;
    LayoutInflater lf;
    ArrayList<String> names;

    public adapter_on_calendar_date_patients(Context ctx, ArrayList<String> names) {
        this.context = ctx;
        this.names = names;
        lf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView==null){
            view = lf.inflate(R.layout.adapter_on_calendar_date_patients, null);
            DatabaseHandler dbHandle = new DatabaseHandler(context);
            personal_obj personalObj =  dbHandle.getPersonalInfo();

            TextView textView = (TextView)view.findViewById(R.id.text_calendar_list_view);

            textView.setText(names.get(position));
            ImageView imageView = (ImageView)view.findViewById(R.id.image_calendar_list_view);
            Bitmap bmp = null;
            bmp = BitmapFactory.decodeFile(personalObj.get_photoPath());
            RoundImage roundedImage;

            if(bmp!=null)
            {bmp = PhotoHelper.getResizedBitmap(bmp, 100, 100);
                roundedImage = new RoundImage(bmp);
                // Bitmap bmpImage = BitmapFactory.decodeByteArray(image, 0, image.length);

            }
            else {

                bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.add_new_photo);
                roundedImage = new RoundImage(bmp);
                imageView.setBackgroundResource(R.drawable.add_new_photo);
            }
            imageView.setImageDrawable(roundedImage);

            return view;
        }

        return convertView;

    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public int getCount() {
        return names.size();
    }
}
