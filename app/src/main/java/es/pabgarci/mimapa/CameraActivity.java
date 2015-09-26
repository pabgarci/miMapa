package es.pabgarci.mimapa;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraActivity extends AppCompatActivity {

    private Camera mCamera = null;
    Button buttonTakePicture;

    public String getName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yymmdd_hhmmss");
        String date = dateFormat.format(new Date());
        return "miMapa_" + date;
    }

    public void takePicture(View view){
        Intent intent = getIntent();
        setResult(1, intent);
        finish();
    }

   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        try {
            mCamera = Camera.open();//you can use open(int) to use different cameras
        } catch (Exception e) {
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }


        if (mCamera != null) {
            CameraView mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
            FrameLayout camera_view = (FrameLayout) findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout
        }
    }

        public void goBack(View view){
            Intent intent = getIntent();
            setResult(0, intent);
            finish();
    }


}
