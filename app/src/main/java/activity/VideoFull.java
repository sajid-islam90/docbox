package activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.VideoView;

import com.example.sajid.myapplication.R;

import java.io.File;


public class VideoFull extends ActionBarActivity {
    int pid;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_full);
        Intent intent = getIntent();
        pid = intent.getIntExtra("id",0);
        path = intent.getStringExtra("path");
       this.playVideo();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video_full, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void playVideo()
    {
        VideoView videoView = (VideoView)findViewById(R.id.videoView);


        videoView.setVideoPath(path);
        videoView.canPause();
        videoView.canSeekBackward();
        videoView.canSeekForward();
        videoView.start();

    }
}
