package activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

import adapters.EDirectoryAdapter;
import objects.modelEDirectory;

/**
 * Created by romichandra on 13/11/16.
 */
public class AssociationEDirectoryActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_e_directory_association);

        getSupportActionBar().setTitle("E - Directory");

        ListView listViewEDirectory = (ListView)findViewById(R.id.listEDirectory);
        ArrayList<modelEDirectory> list = new ArrayList();

        for (int i = 0; i < 1600; i++){
            modelEDirectory model = new modelEDirectory();
            model.setName("Dr Romi Chandra");
            model.setDesignation("M.S. Orthopaedics");
            model.setAddress("Lucknow");
            model.setNumber1("9876543210");
            model.setNumber2("9012345678");
            model.setEmail("romi.chandra@yahoo.com");

            list.add(model);
        }
        listViewEDirectory.setAdapter(new EDirectoryAdapter(this, list));
    }
}
