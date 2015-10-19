
package adapters;

        import java.util.ArrayList;

        import android.app.Activity;
        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

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

       // TextView fieldName = (TextView) rowView.findViewById(R.id.textField1);
        //textView.setTag(position);
      //  textView.setOnClickListener(new View.OnClickListener() {

         /*   @Override
            public void onClick(View arg0) {

                int pos = (int)arg0.getTag();
                DatabaseHandler dbHandle = new DatabaseHandler(getContext());

                document_obj doc_obj;
                doc_obj = dbHandle.getSearchDocument(pos,itemsArrayList);
                Intent intent = new Intent(context,FullImage.class);
                intent.putExtra("id",doc_obj.get_id());
                intent.putExtra("path",doc_obj.get_doc_path());
                activity_parent.finish();
                context.startActivity(intent);
                /*dbHandle.deletePatient(patient);

                MyAdapter.this.notifyDataSetChanged();*/






           // }
      //  });
      /*  ImageView imageView1 = (ImageView) rowView.findViewById(R.id.imageView2);
        imageView1.setTag(position);
        imageView1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                int pos = (int)arg0.getTag();
                DatabaseHandler dbHandle = new DatabaseHandler(getContext());
                document_obj doc_obj;
                doc_obj = dbHandle.getSearchDocument(pos,itemsArrayList);
                Intent intent = new Intent(context,FullImage.class);
                intent.putExtra("id",doc_obj.get_id());
                intent.putExtra("path",doc_obj.get_doc_path());

                context.startActivity(intent);

                /*dbHandle.deletePatient(patient);

                MyAdapter.this.notifyDataSetChanged();






            }
        });*/


        /*
        Button b2 = (Button) rowView.findViewById(R.id.button2);
        b2.setTag(position);
        b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                int pos = (int)arg0.getTag();
                DatabaseHandler dbHandle = new DatabaseHandler(getContext());
                Patient patient = new Patient();
                patient = dbHandle.getPatient(pos);
                dbHandle.deletePatient(patient);

                MyAdapter.this.notifyDataSetChanged();


                Toast.makeText(context,"Deleted "+patient.get_name(),Toast.LENGTH_SHORT)
                        .show();



            }
        });
        */
        // 3. Get the two text view from the rowView
        TextView fieldName = (TextView) rowView.findViewById(R.id.textField1);
        TextView filedValue = (TextView) rowView.findViewById(R.id.textField2);
        // TextView diagnosisView = (TextView) rowView.findViewById(R.id.row_diagnosis);

        // 4. Set the text for textView

        fieldName.setText(itemsArrayList.get(position).getTitle());
        filedValue.setText (itemsArrayList.get(position).getDiagnosis());
        //diagnosisView.setText((itemsArrayList.get(position).getDiagnosis()));

        // 5. retrn rowView
        return rowView;
    }
}
