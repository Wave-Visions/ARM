package aim.aiminmed.server.aimarm;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * Created by server on 03/09/2016.
 */
public class Splash extends Activity  implements Animation.AnimationListener, View.OnClickListener {
    String Doctor;
    private ArmDataBase ArmDataBaseObject = new ArmDataBase(this);
    Button reg;
    private VideoView mVideoView;
    private int splashTime = 3;
    private Handler handler = new Handler();
    private Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_back);

        //final ImageView iv = (ImageView)findViewById(R.id.logo);
        reg=(Button) findViewById(R.id.reg);

        final Animation an = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_in);
        //iv.startAnimation(an);
        an.setAnimationListener(this);
        reg.setOnClickListener(this);


        mVideoView = (VideoView) findViewById(R.id.videoView);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.intro_2);

        mVideoView.setVideoURI(uri);
        mVideoView.start();

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(Splash.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(Splash.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                    finish();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onAnimationStart(Animation animation){

    }
    @Override
    public void onAnimationEnd(Animation animation){

    }
    @Override
    public void onAnimationRepeat(Animation animation){

    }


    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.reg:

                ArmDataBaseObject.open();
                //Cursor cursorData = ArmDataBaseObject.H_getDocDetails(ArmDataBase.DNAME);
                Cursor cursorData = ArmDataBaseObject.H_DocDetails ();
                while (cursorData.moveToNext()) {
                    Doctor = cursorData.getString(cursorData.getColumnIndex(ArmDataBase.DNAME));
                }
                ArmDataBaseObject.close();
                if (null!=Doctor)
                {
                    Intent i = new Intent(Splash.this, Patient_Details.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent i = new Intent(Splash.this, register.class);
                    startActivity(i);
                    finish();
                }
                break;


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }
}
