package activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

import adapters.ExecCouncilAdapter;
import objects.modelExecCouncil;

/**
 * Created by romichandra on 12/11/16.
 */
public class AssociationExecCouncilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exec_council_association);

        getSupportActionBar().setTitle("UPOA Executive Council");

        ListView listViewExecCouncil = (ListView)findViewById(R.id.listExecCouncil);
        ArrayList<modelExecCouncil> listExecCouncil = new ArrayList();

        for (int i = 0; i < 16; i++){
            modelExecCouncil model = new modelExecCouncil();
            model.setName("Dr Romi Chandra");
            model.setDesignation("President");
            model.setPhone("+91 9012345678");
            model.setEmail("romi.chandra@yahoo.com");

            listExecCouncil.add(model);
        }

        listViewExecCouncil.setAdapter(new ExecCouncilAdapter(this, listExecCouncil));

    }
}
