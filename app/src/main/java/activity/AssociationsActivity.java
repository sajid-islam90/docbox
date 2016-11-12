package activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

/**
 * Created by romichandra on 08/11/16.
 */

public class AssociationsActivity extends AppCompatActivity {

    Spinner spinner;
    String mAssociation = "", mRegistrationID = "";
    TextInputEditText editTextRegistrationID;

    Button btnJoin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associations);
        getSupportActionBar().hide();

        editTextRegistrationID = (TextInputEditText)findViewById(R.id.etLoginInputText);
        spinner = (Spinner)findViewById(R.id.spinnerSelectAssociation);
        btnJoin = (Button)findViewById(R.id.btnLoginAssociation);

        final ArrayList<String> listAssociations = new ArrayList<String>();
        listAssociations.add("Choose your Medical Association...");
        listAssociations.add("Uttar Pradesh Orthopaedic Association");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.row_spinner_select_association, listAssociations);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    mAssociation = listAssociations.get(position);
                }
                else{
                    mAssociation = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAssociation = "";
            }
        });

        editTextRegistrationID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0){
                    mRegistrationID = "";
                }
                else{
                    mRegistrationID = s.toString();
                }
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAssociation.equals("")){
                    Toast.makeText(AssociationsActivity.this, "Please select an association to proceed", Toast.LENGTH_SHORT).show();
                }
                else if (mRegistrationID.equals("")){
                    Toast.makeText(AssociationsActivity.this, "Please enter your medical registration ID", Toast.LENGTH_SHORT).show();
                }
                else{
                    final ProgressDialog progressDialog = new ProgressDialog(AssociationsActivity.this);
                    progressDialog.setTitle("");
                    progressDialog.setMessage("Logging in. Please wait!");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            startActivity(new Intent(AssociationsActivity.this ,UPOA_Activity.class));
                        }
                    },1000);
                }
            }
        });

    }
}
