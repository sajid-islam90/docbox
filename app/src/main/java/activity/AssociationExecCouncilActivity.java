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

    String[] Names = new String [] {
            "Prof. S. P. Singh",
            "Prof. Vinita Singh",
            "Prof. R. C. Gupta",
            "Dr. Malay Chaturvedi",
            "Dr. Dharmendra Nath",
            "Dr. R. N. Kushwaha",
            "Dr. Shalini Mohan",
            "Dr. Abhishek Chandra",
            "Dr. B. N. Chaudhary",
            "Dr. Vonod Biala",
            "Dr. Alka Gupta",
            "Prof. Apjit Kaur",
            "Dr. S. Som",
            "Dr. Anil Srivastava",
            "Dr. Ram Kumar Jaiswal",
    };

    String[] Designations = new String [] {
            "President",
            "President Elect",
            "Vice President",
            "General Secretary",
            "Chairman Scientific Committee",
            "Joint Secretary",
            "Treasurer",
            "Editor UP Journal",
            "Editor Proceedings",
            "Chairman ARC ",
            "Member ARC",
            "Member ARC ",
            "Chairman Reception Committee",
            "Chairman Organising Committee",
            "Organising Secretary",
    };

    String[] Phones = new String [] {
            "",
            "09335513077",
            "09415050918",
            "09415125522",
            "09758010040",
            "09415898178",
            "09506740966",
            "09651726602",
            "09760541507",
            "09810213980",
            "09412201639",
            "09415197157",
            "",
            "09415210529",
            "09415250151",
    };

    String[] Emails = new String [] {
            "",
            "",
            "rameshch.gupta@gmail.com",
            "",
            "dr.dnath@yahoo.com",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "anil9988@gmail.com",
            "ramkumarjaiswal35@yahoo.com"
    };

    int [] Drawables = new int[] {
            R.drawable.default_pic,
            R.drawable.img_vineetasingh,
            R.drawable.img_rcgupta,
            R.drawable.img_malaychaturvedi,
            R.drawable.img_dharmendranath,
            R.drawable.default_pic,
            R.drawable.img_smohan,
            R.drawable.img_abhishekchandra,
            R.drawable.default_pic,
            R.drawable.default_pic,
            R.drawable.default_pic,
            R.drawable.default_pic,
            R.drawable.img_ssom,
            R.drawable.img_anil_srivastava,
            R.drawable.default_pic,


    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exec_council_association);

        getSupportActionBar().setTitle("UPSOS Executive Council");

        ListView listViewExecCouncil = (ListView)findViewById(R.id.listExecCouncil);
        ArrayList<modelExecCouncil> listExecCouncil = new ArrayList();

        for (int i = 0; i < Names.length; i++){
            modelExecCouncil model = new modelExecCouncil();
            model.setName(Names[i]);
            model.setDesignation(Designations[i]);
            model.setPhone(Phones[i]);
            model.setEmail(Emails[i]);
            model.setDrawableid(Drawables[i]);
            listExecCouncil.add(model);
        }

        listViewExecCouncil.setAdapter(new ExecCouncilAdapter(this, listExecCouncil));

    }
}
