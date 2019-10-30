package aim.aiminmed.server.aimarm;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;


public class Video_view extends AppCompatActivity {

    VideoView videoView,videoView1;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view);
        videoView =
                (VideoView) findViewById(R.id.videoView);
        videoView1 =
                (VideoView) findViewById(R.id.videoView1);

        //videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro));
        //videoView1.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.demo));


        mediaController = new MediaController(this);

        mediaController.setPadding(32,0,32,0);

        videoView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if(videoView.isPlaying())
                {

                }
                else
                {
                    videoView1.pause();
                    videoView.requestFocus();
                    videoView.start();
                    mediaController.setAnchorView(videoView);
                    videoView.setMediaController(mediaController);



                }
                return false;
            }
        });


        videoView1.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if(videoView1.isPlaying())
                {


                }
                else
                {
                    videoView.pause();
                    videoView1.start();
                    videoView1.requestFocus();
                    mediaController.setAnchorView(videoView1);
                    videoView1.setMediaController(mediaController);

                }
                return false;
            }
        });
    }




}
