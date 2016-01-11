package adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sajid.myapplication.DatabaseHandler;
import com.example.sajid.myapplication.R;
import com.example.sajid.myapplication.util.calendar_descriptor;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by nevermore on 10/24/2015.
 */
public class adapter_view_visit_calendar extends BaseAdapter {

    String[] data;
    int pid;
    Context context;
    boolean SwitchState;

    public adapter_view_visit_calendar(Context cxt, String[] data, int id,boolean SwitchState) {
        this.data = data;
        this.pid = id;
        this.context = cxt;
        this.SwitchState = SwitchState;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(calendar_descriptor.startDate);
        //   cal.add(Calendar.MONTH, calendar_descriptor.months_added);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, position - calendar_descriptor.number_last_days_last_month);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        return df.format(cal.getTime());
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater lf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {
            gridView = new View(context);
            gridView = lf.inflate(R.layout.layout_adapter_view_inventory, null);
            RelativeLayout rl = (RelativeLayout) gridView.findViewById(R.id.calendar_relative_layout);
            LinearLayout ll = (LinearLayout) gridView.findViewById(R.id.calendar_cell_linear_layout);
            TextView tv_date = (TextView) gridView.findViewById(R.id.textview_dates);
            TextView tv_visits = (TextView) gridView.findViewById(R.id.textView_visits);

            tv_date.setText(data[position]);

            if(calendar_descriptor.dates_colors[position]) {
//                rl.setBackgroundColor(Color.WHITE);
                rl.setBackground(context.getResources().getDrawable(R.drawable.rounded_corners_gray));
                tv_date.setTextColor(Color.GRAY);

            }
            else{
                tv_date.setTextColor(Color.parseColor("#24b36b"));
                rl.setBackground(context.getResources().getDrawable(R.drawable.rounded_corners_white));
            }

            if(position>=calendar_descriptor.number_last_days_last_month && position<calendar_descriptor.number_of_days_from_last){
                try {
//                    ArrayList<String> visits = new ArrayList<String>(new DatabaseHandler(context).getAllNotesDates(pid));
//                    for (String visit: visits){
//
//                    }
                   //set imageview visibility
                    DatabaseHandler db = new DatabaseHandler(context);
                    ArrayList<String> list = new ArrayList<String>();
                    if(SwitchState)
                    list = db.getPatientsForDate(getItem(position).toString());


                    if (list.size() == 0){
                        tv_visits.setVisibility(View.GONE);
                    }else{
                        tv_visits.setVisibility(View.VISIBLE);
                        tv_date.setTextColor(Color.WHITE);
                        rl.setBackground(context.getResources().getDrawable(R.drawable.rounder_corners));
                        tv_visits.setText( Integer.toString( list.size()));
                    }
                }
                catch(Exception e){
                    tv_visits.setVisibility(View.GONE);
                    e.printStackTrace();
                }

            }




            int weight = context.getResources().getDisplayMetrics().widthPixels;
            int height = context.getResources().getDisplayMetrics().heightPixels;
            rl.setMinimumHeight(weight / 7);
//            Animation animation1 = AnimationUtils.loadAnimation(context,R.anim.to_middle);
//            Animation animation2 = AnimationUtils.loadAnimation(context,R.anim.from_middle);
//
//            AnimationSet animationSet = new AnimationSet(true);
//            animationSet.addAnimation(animation1);
//            animationSet.addAnimation(animation2);
//            LayoutAnimationController controller = new LayoutAnimationController(animationSet,0.5f);
//            ll.setLayoutAnimation(controller);

            // return gridView;
        } else
            gridView = (View) convertView;

        return gridView;

    }
}
