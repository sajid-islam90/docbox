
package adapters;

        import java.util.ArrayList;

        import android.app.Activity;
        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import activity.PatientProfileActivity;
        import objects.Item;
        import com.example.sajid.myapplication.R;

public class TwoTextFieldsAdapter extends ArrayAdapter<Item> {

    private final Context context;
    private final ArrayList<Item> itemsArrayList;
    private final Activity activity_parent;

    public TwoTextFieldsAdapter(Activity activity_parent , Context context, ArrayList<Item> itemsArrayList) {

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
        View rowView = inflater.inflate(R.layout.two_text_row, parent, false);


        // 3. Get the two text view from the rowView
        TextView fieldName = (TextView) rowView.findViewById(R.id.textField1);
        TextView filedValue = (TextView) rowView.findViewById(R.id.textField2);
        ImageView imageView = (ImageView)rowView.findViewById(R.id.fieldIcon);
        if(PatientProfileActivity.class == activity_parent.getClass())
        {
fieldName.setVisibility(View.GONE);
            imageView.setImageBitmap(itemsArrayList.get(position).getBmp());
        }
        else
        {
            imageView.setVisibility(View.GONE);
        }

        // 4. Set the text for textView

        fieldName.setText(itemsArrayList.get(position).getTitle());

        if(!itemsArrayList.get(position).getDiagnosis().equals(""))
        filedValue.setText (itemsArrayList.get(position).getDiagnosis());
        else
        filedValue.setHint(itemsArrayList.get(position).getTitle());


        // 5. retrn rowView
        return rowView;
    }
}
