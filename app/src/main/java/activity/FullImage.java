package activity;
//HANDLES THE FULL SCREEN IMAGE FUNCTIONALITY
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.example.sajid.myapplication.DatabaseHandler;
import objects.Patient;
import com.example.sajid.myapplication.PhotoHelper;
import com.example.sajid.myapplication.R;
import com.example.sajid.myapplication.ScaleImageView;
import objects.document_obj;


public class FullImage extends ActionBarActivity {

    private int id;
    private String path;
    private String className;
    private String section;
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_full_image);
        Intent intent = getIntent();
         id = intent.getIntExtra("id",0);
        path = intent.getStringExtra("path");
        className = intent.getStringExtra("className");
        section = intent.getStringExtra("section");
        setImageView(id,path);

    }

    public void setImageView(int id,String path)
    {
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        Patient patient = dbHandler.getPatient(id);

       // document_obj doc_obj = dbHandler.getSingleDocument(id,path);
        setTitle(path);
        ImageView imageView = (ImageView)findViewById(R.id.fullImage);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        bitmap = PhotoHelper.getResizedBitmap(bitmap, height, width);
       // imageView.setImageBitmap(bitmap);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        ScaleImageView image = (ScaleImageView) findViewById(R.id.fullImage);

        image.setImageBitmap(bitmap);
    }



    public void deleteDocument(View view)
    {
        final DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());



        final Intent documentIntent = new Intent(this, documents.class);
        final Intent MediaIntent = new Intent(this, View_Media_notes_grid.class);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Alert!!");
        alert.setMessage("Are you sure to delete");
        alert.setPositiveButton("YES",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(className.contains("documents") ){

                    documentIntent.putExtra("id",id);

                    document_obj doc_obj = dbHandler.getSingleDocument(id, path);
                    dbHandler.deleteDocument(doc_obj);

                    finish();
                    startActivity(documentIntent);
                }
                else if (className.contains("View_Media_notes_grid")  )
                {
                    MediaIntent.putExtra("id", id);
                    MediaIntent.putExtra("section",Integer.parseInt(section));
                    dbHandler.deleteMedia(path);
                    startActivity(MediaIntent);
                    finish();
                   // utility.recreateActivityCompat(documents.this);

                }
                dialog.dismiss();

            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alert.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_patient_data, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
               /* Intent intent1 = getIntent();
                int Activity_id = intent1.getIntExtra("id",0);
                Intent intent = new Intent(this,documents.class);
                intent.putExtra("id",Activity_id);
                startActivity(intent);
                return true;*/
                finish();
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);

        //noinspection SimplifiableIfStatement



    }

}
