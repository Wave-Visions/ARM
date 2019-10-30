package aim.aiminmed.server.aimarm;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ir.noghteh.JustifiedTextView;


public class test_gui extends AppCompatActivity implements Animation.AnimationListener,View.OnClickListener{
    private JustifiedTextView summary,step,intro;
    private ImageView Device,probe;
    private char process=0;
    Animation an_fade_in,an_zoom_out, an_zoom_in,an_fade_out;
    LinearLayout ll_main,ll,ll_down;
    Button s1,s2,s3,s4,s5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);


        ll_main = (LinearLayout) findViewById(R.id.lay_0);//Main Screen layout
        ll = (LinearLayout) findViewById(R.id.lay_1);//Upper layout for images
        ll_down = (LinearLayout) findViewById(R.id.lay_2);//Lower layout for text

        s1=(Button)findViewById(R.id.step1);
        s2=(Button)findViewById(R.id.step2);
        s3=(Button)findViewById(R.id.step3);
        s4=(Button)findViewById(R.id.step4);
        s5=(Button)findViewById(R.id.step5);

        s1.setOnClickListener(this);
        s2.setOnClickListener(this);
        s3.setOnClickListener(this);
        s4.setOnClickListener(this);
        s5.setOnClickListener(this);

        Device = new ImageView(this);
        Device.setId(R.id.id_dev);
        Device.setBackgroundResource(R.drawable.dev);
        ll.addView(Device,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));


        probe = new ImageView(this);
        probe.setId(R.id.id_prob);
        probe.setBackgroundResource(R.drawable.probe);


        summary = new JustifiedTextView(this);
        summary.setId(R.id.id_summary);
        summary.setText("In anorectal manometry, a thin tube, called a manometry probe, is inserted into the anal canal and slowly withdrawn. The probe is attached to a pressure transducer that measures the pressures exerted by the rectal and anal sphincter muscles, which relax and contract to control bowel movements.");
        summary.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
        summary.setAlignment(Paint.Align.LEFT);
        summary.setLineSpacing(10);
        summary.setPadding(0,0,16,0);
        ll_down.addView(summary,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

        step = new JustifiedTextView(this);
        //step.setTextColor(getResources().getColor(R.color.Violate));
        step.setId(R.id.id_step1_text);
        step.setText(" ");
        step.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
        step.setAlignment(Paint.Align.LEFT);
        step.setPadding(0,0,16,0);

        intro = new JustifiedTextView(this);
        intro.setId(R.id.id_intro);
        intro.setText(" ");
        intro.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
        intro.setLineSpacing(10);
        intro.setPadding(0,0,16,0);//Padding to right only to avoid text cutting from right side.
        intro.setAlignment(Paint.Align.LEFT);


        an_fade_in = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_in);
        an_fade_out = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_out);
        an_zoom_out = AnimationUtils.loadAnimation(getBaseContext(),R.anim.zoom_out);
        an_zoom_in = AnimationUtils.loadAnimation(getBaseContext(),R.anim.zoom_in);


        an_fade_in.setAnimationListener(this);
        an_fade_out.setAnimationListener(this);
        an_zoom_out.setAnimationListener(this);
        an_zoom_in.setAnimationListener(this);

        ll_main.startAnimation(an_fade_in);//Animation on whole layout
        //Device.startAnimation(an_fade_in);




    }


    @Override
    public void onAnimationStart(Animation animation){

    }
    @Override
    public void onAnimationEnd(Animation animation){


        /*if(animation == an_fade_in){

            ll.startAnimation(an_fade_out);

        }*/
        if(animation == an_fade_out){

            ll.removeAllViews();//Remove upper layout
            ll_down.removeAllViews();//Remove lower layout

            switch(process){

                case 1:
                    //ll.addView(step,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                    ll.addView(probe,new ViewGroup.LayoutParams(250,ViewGroup.LayoutParams.WRAP_CONTENT));
                    intro.setText("Prepare the probe by filling the water in tube and then connect it to handheld device.");
                    ll_down.addView(intro);
                    ll_main.startAnimation(an_fade_in);

                    setTitle("Step 1");

                    break;
                case 2:

                    setTitle("Step 2");
                    Device.setBackgroundResource(R.drawable.mobile);
                    ll.addView(Device,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,400));
                    intro.setText("Turn on the wifi in mobile then search for the device and connect to it and open the AIM android application.");
                    ll_down.addView(intro);
                    ll_main.startAnimation(an_fade_in);

                    break;
                case 3:
                    setTitle("Step 3");
                    Device.setBackgroundResource(R.drawable.press);
                    ll.addView(Device,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                    intro.setText("Hold the probe in AIR for 30 seconds to get the initial pressure then, insert the probe in anal/rectum then press the ready key to indicate the doctor that patient is ready for further process.");
                    ll_down.addView(intro);
                    ll_main.startAnimation(an_fade_in);


                    break;
                case 4:
                    setTitle("Step 4");
                    Device.setBackgroundResource(R.drawable.details);
                    intro.setText("Different LED indicators are given to alter the patient to generate pressure by appropriate modes, Android application will show live graph of pressure and display average pressure in mmhg of every mode.");
                    //step.setText("");


                    ll.addView(Device,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                    ll_down.addView(intro);

                    //ll.addView(step);
                    ll_main.startAnimation(an_fade_in);

                    break;
                case 5:
                    Intent i = new Intent(test_gui.this,current_ps.class);
                    startActivity(i);
                    finish();
                    break;
            }

        }

    }
    @Override
    public void onAnimationRepeat(Animation animation){

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.step1:
                ll_main.startAnimation(an_fade_out);//Animation on whole layout
                process=1;
                break;
            case R.id.step2:
                ll_main.startAnimation(an_fade_out);//Animation on whole layout
                process=2;
                break;
            case R.id.step3:
                ll_main.startAnimation(an_fade_out);//Animation on whole layout
                process=3;
                break;
            case R.id.step4:
                ll_main.startAnimation(an_fade_out);//Animation on whole layout
                process=4;
                break;
            case R.id.step5:
                ll_main.startAnimation(an_fade_out);//Animation on whole layout
                process=5;
                break;

        }
    }

}
