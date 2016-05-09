package adapters;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sajid.myapplication.DatabaseHandler;
import com.example.sajid.myapplication.PhotoHelper;
import com.example.sajid.myapplication.R;
import com.example.sajid.myapplication.RoundImage;

import java.util.ArrayList;
import java.util.List;

import objects.Item;
import objects.Patient;

/**
 * Created by sajid on 12/2/2015.
 */
public class InputAgainstAFieldAdapter extends ArrayAdapter<Item> {
    private final ArrayList<Item> itemsArrayList;
    private int currentlyFocusedRow;
    public InputAgainstAFieldAdapter(Context context, ArrayList<Item> itemsArrayList) {
        super(context,R.layout.text_field_edit_value,itemsArrayList);
        this.itemsArrayList = itemsArrayList;
    }


    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.text_field_edit_value, parent, false);
        TextInputLayout textInputLayout = (TextInputLayout)rowView.findViewById(R.id.textInputLayoutField);
       // final int actual_position = itemsArrayList.getPositionForView((View) rowView.getParent());
        TextView Fieldname = (TextView) rowView.findViewById(R.id.fieldName);
       final EditText editText = (EditText)rowView.findViewById(R.id.fieldValue);
        textInputLayout.setHint(itemsArrayList.get(position).getTitle());
        if(!itemsArrayList.get(position).getDiagnosis().equals(""))
        editText.setText(itemsArrayList.get(position).getDiagnosis());




     /*  if(itemsArrayList.get(position).getDiagnosis()!="")
        {
            editText.setText(itemsArrayList.get(position).getDiagnosis());
        }*/
/*editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            Toast.makeText(getContext(),editText.getText().toString(),Toast.LENGTH_SHORT).show();
        }
        return false;
    }
});*/
       editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
              /// Toast.makeText(getContext(),editText.getText().toString(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // Toast.makeText(getContext(),editText.getText().toString(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void afterTextChanged(Editable s) {
                itemsArrayList.get(position).setDiagnosis(editText.getText().toString());

            }
        });
        Fieldname.setText(itemsArrayList.get(position).getTitle());


        // 3. Get the two text view from the rowView


        // 4. Set the text for textView




        // 5. retrn rowView
        return rowView;
    }

    public static int getPosition()
    {
        return 1;
    }

    @Override
    public int getPosition(Item item) {
        return super.getPosition(item);
    }
}
