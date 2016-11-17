package activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

import adapters.AssociationsGalleryAdapter;
import objects.modelGallery;

/**
 * Created by romichandra on 13/11/16.
 */
public class AssociationGalleryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associations_gallery);
        getSupportActionBar().setTitle("UPSOS Gallery");

        ListView listViewGallery = (ListView)findViewById(R.id.listAssociationGallery);
        ArrayList<modelGallery> listGallery = new ArrayList();

        for (int i = 0; i < 2; i++){
            modelGallery model = new modelGallery();
            model.setPath("https://www.youtube.com/watch?v=E9QXO5m_iS0");
            model.setTitle("Aajtak Live TV Stream");

            listGallery.add(model);
        }

        listViewGallery.setAdapter(new AssociationsGalleryAdapter(this, listGallery));
        listViewGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=E9QXO5m_iS0"));
                intent.putExtra("force_fullscreen",true);
                startActivity(intent);
            }
        });
    }
}
