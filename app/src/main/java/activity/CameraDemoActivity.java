package activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.elune.sajid.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import objects.document_obj;
import objects.media_obj;
import utilityClasses.DatabaseHandler;
import utilityClasses.PhotoHelper;
import utilityClasses.utility;

public class CameraDemoActivity extends Activity implements SurfaceHolder.Callback,
        View.OnClickListener {
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Button flipCamera;

    private Button flashCameraButton;
    private Button captureImage;
    private int cameraId;
    private boolean flashmode = false;
    private int rotation;
    private int pid;
    private String filePath;
    private String parentActivity = "";//1.0.9
    File photoFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameradema_activity);
        DatabaseHandler databaseHandler = new DatabaseHandler(CameraDemoActivity.this);
        // camera surface view created


        cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        flipCamera = (Button) findViewById(R.id.flipCamera);
        flashCameraButton = (Button) findViewById(R.id.flash);
        captureImage = (Button) findViewById(R.id.captureImage);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        Intent intent = getIntent();
        pid = intent.getIntExtra("pid", 0);
        filePath = intent.getStringExtra("filePath");
        parentActivity = intent.getStringExtra("parentActivity");
        if(parentActivity!=null)
        try {
            if(parentActivity.equals("documents")) {
                photoFile = PhotoHelper.createImageFileForDocument(pid, CameraDemoActivity.this);

                filePath = photoFile.getPath();
//            doc_obj.set_doc_name(photoFile.getName());
//            doc_obj.set_doc_path(filePath);
//            doc_obj.set_id(pid);
//            databaseHandler.addDocument(doc_obj);
            }
//            if(parentActivity.equals("notes"))
//            {
//
//                photoFile = PhotoHelper.createImageFileForNotes(pid, CameraDemoActivity.this);
//                filePath = photoFile.getPath();
//
//            }
            // databaseHandler.addDocument(doc_obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        flipCamera.setOnClickListener(this);
        captureImage.setOnClickListener(this);
        flashCameraButton.setOnClickListener(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Camera.getNumberOfCameras() > 1) {
            flipCamera.setVisibility(View.VISIBLE);
        }
        if (!getBaseContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH)) {
            flashCameraButton.setVisibility(View.GONE);
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!openCamera(Camera.CameraInfo.CAMERA_FACING_BACK)) {
            alertCameraDialog ();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flash:
                flashOnButton();
                break;
            case R.id.flipCamera:
                flipCamera();
                break;
            case R.id.captureImage:
                takeImage();
                break;

            default:
                break;
        }
    }

    private void alertCameraDialog() {
        AlertDialog.Builder dialog =  new AlertDialog.Builder(this);
        dialog.setMessage("error to open camera\nTry restarting your phone");
        dialog.setTitle("Camera info");


        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();

            }
        });

        dialog.show();
    }

    private boolean openCamera(int id) {
        boolean result = false;
        cameraId = id;
        releaseCamera();
        try {

            camera =
                    Camera.open(cameraId);
            Camera.Parameters param = camera.getParameters();
            //camera.setParameters();
            List<Camera.Size> pictureSizes = param.getSupportedPictureSizes();
           /* int height = pictureSizes.get(pictureSizes.size()-2).height;
            int width = pictureSizes.get(pictureSizes.size()-2).width;*/
            param.setPictureSize(612,816);
            param.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            camera.setParameters(param);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (camera != null) {
            try {
                setUpCamera(camera);
camera.setAutoFocusMoveCallback(new Camera.AutoFocusMoveCallback() {
    @Override
    public void onAutoFocusMoving(boolean start, Camera camera) {
        Toast.makeText(CameraDemoActivity.this,"Autofocus",Toast.LENGTH_LONG).show();
    }
});
                camera.setErrorCallback(new Camera.ErrorCallback() {

                    @Override
                    public void onError(int error, Camera camera) {
//to show the error message.
                    }
                });
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
                releaseCamera();
            }
        }
        return result;
    }
    private void setAutoFocusMoveCallback(Camera camera, Camera.AutoFocusMoveCallback cb)
    {
        System.out.println("setAutoFocusMoveCallback : Check  " + cb.toString());
        //Camera.cancelAutoFocus();
        camera.setAutoFocusMoveCallback(cb);
    }
    private void releaseCamera() {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.setErrorCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.toString());
            camera = null;
        }
    }


    private void setUpCamera(Camera c) {
        Camera.CameraInfo info = new Camera.CameraInfo();

        Camera.getCameraInfo(cameraId, info);
        rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;

            default:
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // frontFacing
            rotation = (info.orientation + degree) % 330;
            rotation = (360 - rotation) % 360;
        } else {
            // Back-facing
            rotation = (info.orientation - degree + 360) % 360;
        }
        c.setDisplayOrientation(rotation);
        Camera.Parameters params = c.getParameters();

        showFlashButton(params);

        List<String> focusModes = params.getSupportedFlashModes();
        if (focusModes != null) {
            if (focusModes
                    .contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            }
        }

        params.setRotation(rotation);
    }
    private void showFlashButton(Camera.Parameters params) {
        boolean showFlash = (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH) && params.getFlashMode() != null)
                && params.getSupportedFlashModes() != null
                && params.getSupportedFocusModes().size() > 1;

        flashCameraButton.setVisibility(showFlash ? View.VISIBLE
                : View.INVISIBLE);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void flashOnButton() {
        if (camera != null) {
            try {
                Camera.Parameters param = camera.getParameters();
                param.setFlashMode(!flashmode ? Camera.Parameters.FLASH_MODE_AUTO
                        : Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(param);
                if(!flashmode)
                flashCameraButton.setBackground(getResources().getDrawable(R.drawable.ic_action_flash_on));
                else
                    flashCameraButton.setBackground(getResources().getDrawable(R.drawable.ic_action_flash_off));
                flashmode = !flashmode;
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
    }
    private void flipCamera() {
        int id = (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK ? Camera.CameraInfo.CAMERA_FACING_FRONT
                : Camera.CameraInfo.CAMERA_FACING_BACK);
        if (!openCamera(id)) {
            alertCameraDialog();
        }
    }

    @Override
    public void onBackPressed() {
        if(photoFile.exists())
        photoFile.delete();
        finish();
        super.onBackPressed();
    }

    private void takeImage() {

//        if (camera.getParameters().getFocusMode().equals(C)){
//            camera.autoFocus(new Camera.AutoFocusCallback() {
//                @Override
//                public void onAutoFocus(boolean success, Camera camera) {
//                    camera.takePicture(shutterCallback, rawCallback, jpegCallback);
//                }
//            });
//        }else{
//            camera.takePicture(shutterCallback, rawCallback, jpegCallback);
//        }
camera.autoFocus(new Camera.AutoFocusCallback() {
    @Override
    public void onAutoFocus(boolean success, Camera camera) {


        camera.takePicture(new Camera.ShutterCallback() {
            @Override
            public void onShutter() {
                AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
            }
        }, null, new Camera.PictureCallback() {

            private File imageFile;

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    // convert byte array into bitmap
                    DatabaseHandler databaseHandler = new DatabaseHandler(CameraDemoActivity.this);
                    Bitmap loadedImage = BitmapFactory.decodeByteArray(data, 0,
                            data.length);
                    android.graphics.Matrix rotateMatrix = new android.graphics.Matrix();
                    // rotate Image
                   /* Matrix rotateMatrix = new Matrix();*/

                    rotateMatrix.postRotate(rotation);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(loadedImage, 0,
                            0, loadedImage.getWidth(), loadedImage.getHeight(),
                            rotateMatrix, false);
                    String state = Environment.getExternalStorageState();

                    imageFile = new File(filePath);
                    //= PhotoHelper.createImageFileForNotes(pid, getApplicationContext());


                    ByteArrayOutputStream ostream = new ByteArrayOutputStream();

                    // save image into gallery
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, ostream);


                    FileOutputStream fout = new FileOutputStream(imageFile);
                    fout.write(ostream.toByteArray());
                    fout.close();
                    Camera.Parameters param = camera.getParameters();
                    param.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                    param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                    camera.setParameters(param);
                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                   // final MediaPlayer mp = MediaPlayer.create(this, alarmSound);
                    // ContentValues values = new ContentValues();

                   /* values.put(MediaStore.Images.Media.DATE_TAKEN,
                            System.currentTimeMillis());
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.MediaColumns.DATA,
                            imageFile.getAbsolutePath());

                  /*  CameraDemoActivity.this.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);*/
                    if (parentActivity != null)
                        try {
                            if (parentActivity.equals("documents")) {  //photoFile = PhotoHelper.createImageFileForDocument(pid,CameraDemoActivity.this);
                                document_obj doc_obj = new document_obj();
                                // filePath = photoFile.getPath();
                                doc_obj.set_doc_name(photoFile.getName());
                                doc_obj.set_doc_path(filePath);
                                doc_obj.set_id(pid);
                                databaseHandler.addDocument(doc_obj);
                                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(CameraDemoActivity.this);

                                alert.setTitle("Alert!!");
                                alert.setMessage("Do you want to add another Report?");
                                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(CameraDemoActivity.this, "photo again?", Toast.LENGTH_SHORT).show();
                                        utility.recreateActivityCompat(CameraDemoActivity.this);
                                        dialog.dismiss();

                                    }
                                });
                                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        dialog.dismiss();
                                    }
                                });

                                alert.show();


                            } else {
//                            media_obj mediaObj = new media_obj() ;
//                            mediaObj.set_pid(pid);
//                            mediaObj.set_media_path(filePath);
//                            mediaObj.set_media_name(photoFile.getName());
//                            mediaObj = PhotoHelper.addMissingBmp(mediaObj,1);
                                finish();

                            }

                            // databaseHandler.addDocument(doc_obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }
});
    }
}