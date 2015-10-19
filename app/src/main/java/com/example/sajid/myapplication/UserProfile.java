package com.example.sajid.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import objects.personal_obj;

public class UserProfile extends AppCompatActivity {
    RoundImage roundedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setUserProfileData();
       // this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

}

    public void makeEditable(View view)
    {
       this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        EditText editText = (EditText)findViewById(R.id.userProfileName);
        editText.setVisibility(View.VISIBLE);
        TextView textView = (TextView)findViewById(R.id.userProfileNameSolid);
        textView.setVisibility(View.GONE);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);


        return true;
    }



    public void setUserProfileData()
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        personal_obj personalObj = databaseHandler.getPersonalInfo();

        ImageView imageView = (ImageView)findViewById(R.id.userProfilePicture);
        TextView textView = (TextView)findViewById(R.id.userProfile_email);

        textView.setText(personalObj.get_email());
        TextView textView1 = (TextView)findViewById(R.id.userProfileName);
        TextView textView2 = (TextView)findViewById(R.id.userProfilePhoneNumber);
        TextView textView3 = (TextView)findViewById(R.id.userProfileNameSolid);

        textView1.setText(personalObj.get_name());
        textView2.setText(personalObj.get_password());
        textView3.setText(personalObj.get_name());
        Bitmap bmp = null;
        bmp = BitmapFactory.decodeFile(personalObj.get_photoPath());

        if(bmp!=null)
        {bmp = PhotoHelper.getResizedBitmap(bmp, 200, 200);
            roundedImage = new RoundImage(bmp);
            // Bitmap bmpImage = BitmapFactory.decodeByteArray(image, 0, image.length);

        }
        else {

            bmp = BitmapFactory.decodeResource(getResources(),R.drawable.add_new_photo);
            roundedImage = new RoundImage(bmp);
            imageView.setBackgroundResource(R.drawable.add_new_photo);
        }
        imageView.setImageDrawable(roundedImage);


    }

    public void savePersonalInfo()
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(UserProfile.this);

        EditText editText = (EditText)findViewById(R.id.userProfileName);

        personal_obj personalObj = new personal_obj();

        databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_NAME,editText.getText().toString());





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {

            savePersonalInfo();
            finish();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
