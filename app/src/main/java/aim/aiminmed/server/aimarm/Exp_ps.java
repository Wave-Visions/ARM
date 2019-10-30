package aim.aiminmed.server.aimarm;

import android.content.Intent;
import android.database.Cursor;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import info.hoang8f.widget.FButton;

/**
 * Created by server on 08/09/2016.
 */
public class Exp_ps extends AppCompatActivity implements View.OnClickListener{
    private ArmDataBase ArmDataBaseObject = new ArmDataBase(this);
    LineGraphSeries<DataPoint> current_series, rest_series, squeeze_series, cough_series;
    TextView timer_text;
    TextView first;
    TextView pressure_text;
    CountDownTimer count;
    GraphView graph;
    double  x=1;
    char insert_clicked=0;
    LinearLayout ll;
    FButton insert,Squeeze,Cough,exit,Air;
    InputStream is;
    PrintWriter out;
    BufferedReader br;
    Socket socket = null;
    String result=null;
    String dada_point=null;
    double mmhg_int;
    int mmhg_avg,temp_data,air_avg,rst_avg,sqz_avg,cgf_avg,air_max,rst_max,sqz_max,cgf_max,max_data;
    int timer_sec=30;//Timer Second
    char pressure_mult=0;
    char count_data;
    //pass_status status_esp;
    View rootView;
    File temp_file;
    String patitent;
    private static final int PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exp_ps);
        setTitle(R.string.Graph);//Setting the title of this activity
        first = (TextView) findViewById(R.id.first);
        final TextView Doc = (TextView)findViewById(R.id.doctor_name);
        final TextView Pet = (TextView)findViewById(R.id.patient_name);

        rootView = getWindow().getDecorView().findViewById(android.R.id.content);

        ArmDataBaseObject.open();
        //Cursor cursorData = ArmDataBaseObject.H_getDocDetails(ArmDataBase.DNAME);
        Cursor cursorData = ArmDataBaseObject.H_DocDetails();
        while (cursorData.moveToNext()) {
            String Doctor = cursorData.getString(cursorData.getColumnIndex(ArmDataBase.DNAME));


            Doc.setText(Doctor);
        }
        ArmDataBaseObject.close();

       ArmDataBaseObject.open();
        Cursor cursorDataa = ArmDataBaseObject.H_PatDetails();
        while (cursorDataa.moveToNext()) {
            patitent = cursorDataa.getString(cursorDataa.getColumnIndex(ArmDataBase.PATITENT_NAME));
            Pet.setText(patitent);

        }
        ArmDataBaseObject.close();


       // socket= pass_status.getSocket();
       // out=pass_status.getOut();
       // read read_msg= new read();//Second AsyncTask to read received data from server
       // read_msg.execute();

        new ClientAsyncTask().execute("192.168.4.1", "80", ":Connect;");


        //Adding insert button for further process

        ll = (LinearLayout) findViewById(R.id.process_lay);
        insert = new FButton(this);
        insert.setText("Rest");
        insert.setId(R.id.insert_btn);//set ids for button
        insert.setButtonColor(getResources().getColor(R.color.fbutton_color_turquoise));
        insert.setVisibility(View.INVISIBLE);//do invisible

        //Adding insert button for further process

        //Air = new Button(this);
        Air = new FButton(this);
        Air.setText("Air");
        Air.setId(R.id.air_btn);//set ids for button
        Air.setButtonColor(getResources().getColor(R.color.fbutton_color_turquoise));
        ll.addView(Air,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));//to add button in layout

        Squeeze = new FButton(this);
        Squeeze.setText("Squeeze");
        Squeeze.setId(R.id.squeeze_btn);//set ids for button
        Squeeze.setButtonColor(getResources().getColor(R.color.fbutton_color_turquoise));
        Squeeze.setVisibility(View.INVISIBLE);//do invisible

        Cough = new FButton(this);
        Cough.setText("Cough");
        Cough.setId(R.id.cough_btn);//set ids for button
        Cough.setButtonColor(getResources().getColor(R.color.fbutton_color_turquoise));
        Cough.setVisibility(View.INVISIBLE);//do invisible

        exit = new FButton(this);
        exit.setText("Generate Report");
        exit.setId(R.id.exit_btn);//set ids for button
        exit.setButtonColor(getResources().getColor(R.color.fbutton_color_turquoise));
        exit.setVisibility(View.INVISIBLE);//do invisible

        graph = (GraphView) findViewById(R.id.graph1);
        current_series = new LineGraphSeries<DataPoint>();
        rest_series = new LineGraphSeries<DataPoint>(); //second data point for another graph
        squeeze_series = new LineGraphSeries<DataPoint>();
        cough_series = new LineGraphSeries<DataPoint>(); //second data point for another graph

        current_series.setThickness(10);
        current_series.setDrawBackground(true); // activate the background feature<br />

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(100);//Max value of y axis
        graph.getViewport().setMinX(0.0);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxX(5);

        graph.getViewport().setScalable(true); //manually user can change Scal of X axis
        graph.getViewport().setScrollable(true);////manually user can scroll of X axis

        //graph.setTitle("Chart Title");
        graph.getGridLabelRenderer().setVerticalAxisTitle("mm Hg");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Second");
        graph.getGridLabelRenderer().setVerticalLabelsAlign(Paint.Align.LEFT);


        //current_series.setDrawDataPoints(true);
        current_series.setDrawBackground(true); // activate the background feature<br />
        current_series.setColor(Color.CYAN);//set color of line of graph
        //current_series.setBackgroundColor(Color.GREEN);//
        //Change the colour for new data
        //rest_series.setDrawDataPoints(true);
        rest_series.setDrawBackground(true); // activate the background feature<br />
        rest_series.setColor(Color.GREEN);
        rest_series.setThickness(10);
        //rest_series.setBackgroundColor(Color.BLUE);

        //squeeze_series.setDrawDataPoints(true);
        squeeze_series.setDrawBackground(true); // activate the background feature<br />
        squeeze_series.setColor(Color.RED);
        squeeze_series.setThickness(10);
        //squeeze_series.setBackgroundColor(Color.RED);

        //cough_series.setDrawDataPoints(true);
        cough_series.setDrawBackground(true); // activate the background feature<br />
        cough_series.setColor(Color.BLUE);
        cough_series.setThickness(10);
        //cough_series.setBackgroundColor(Color.MAGENTA);

        timer_text = (TextView) findViewById(R.id.textimer);
        pressure_text = (TextView) findViewById(R.id.textupper);
        insert.setOnClickListener(this);
        Air.setOnClickListener(this);
        Squeeze.setOnClickListener(this);
        Cough.setOnClickListener(this);
        exit.setOnClickListener(this);

        pressure_text.setText("Air Pressure");

        count=new CountDownTimer((timer_sec*1000), 500) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                //mmhg_int=pressure_mult*Math.random();
                count_data++;
                temp_data=(int)mmhg_int;
                if(temp_data>max_data){
                    max_data=temp_data;
                }

                mmhg_avg+=mmhg_int;
                first.setText(temp_data +" "+ "mmhg ");
                switch(insert_clicked) {
                    case 0:

                        timer_text.setText("Wait for : " + millisUntilFinished / 1000);
                        current_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(current_series);
                        x=x+0.5;
                        break;

                    case 1:
                        timer_text.setText("Wait for : " + millisUntilFinished / 1000);
                        rest_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(rest_series);
                        x=x+0.5;;
                        break;
                    case 2:
                        timer_text.setText("Wait for : " + millisUntilFinished / 1000);
                        squeeze_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(squeeze_series);
                        x=x+0.5;
                        break;
                    case 3:
                        timer_text.setText("Wait for : " + millisUntilFinished / 1000);
                        cough_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(cough_series);
                        x=x+0.5;
                        break;
                }


            }


            public void onFinish() {
                count_data++;
                mmhg_avg+=mmhg_int;
                mmhg_avg=mmhg_avg/count_data;
                if(temp_data>max_data){
                    max_data=temp_data;
                }
                count_data=0;
                first.setText("Average" +" "+ Integer.toString(mmhg_avg) +" "+ "mmhg ");
                switch(insert_clicked) {
                    case 0:
                        timer_text.setText("");
                        //insert.setVisibility(View.VISIBLE);
                        current_series.appendData(new DataPoint(x, mmhg_int), true, 100);//Plotting graph here because last second ends in onFinish function
                        graph.addSeries(current_series);

                        rest_series.appendData(new DataPoint(x, mmhg_int), true, 100);//Adding this graph here to join two different graphs
                        graph.addSeries(rest_series);
                        pressure_mult=10;//Temp variable for demo app to generate proper pressure value
                        x=x+0.5;
                        air_avg=mmhg_avg;
                        air_max=max_data;
                        mmhg_avg=0;
                        max_data=0;
                        try{
                            out.println(":green_on;");
                            out.flush();
                        }catch (Exception e) {

                        }

                        break;

                    case 1:
                        timer_text.setText("");
                        //Squeeze.setVisibility(View.VISIBLE);
                        rest_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(rest_series);
                        squeeze_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(squeeze_series);
                        pressure_mult=50;
                        x=x+0.5;
                        rst_avg=mmhg_avg;
                        rst_max=max_data;
                        mmhg_avg=0;
                        max_data=0;
                        try{
                            out.println(":red_on;");
                            out.flush();
                        }catch (Exception e) {}
                        break;

                    case 2:
                        timer_text.setText("");
                        //Cough.setVisibility(View.VISIBLE);
                        squeeze_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(squeeze_series);
                        cough_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(cough_series);
                        pressure_mult=40;
                        x=x+0.5;
                        sqz_avg=mmhg_avg;
                        sqz_max=max_data;
                        mmhg_avg=0;
                        max_data=0;
                        try{
                            out.println(":blue_on;");
                            out.flush();
                        }catch (Exception e) {}
                        break;

                    case 3:
                        timer_text.setText("");
                        cough_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(cough_series);
                        x=x+0.5;
                        cgf_avg=mmhg_avg;
                        cgf_max=max_data;
                        mmhg_avg=0;
                        max_data=0;
                        //Put further button here
                        ll.addView(exit,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));
                        exit.setVisibility(View.VISIBLE);
                        Toast toast= Toast.makeText(getApplicationContext(),"Pinch to zoom/Slide to scroll", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        break;



                }

            }

        };
        rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        //View v1 = getWindow().getDecorView().getRootView();
    }
    @Override
    public void onClick(View view) {

        switch(view.getId()) {


            case R.id.air_btn :
                try{
                    //Intent it = new Intent(Exp_ps.this, report.class);
                    //startActivity(it);
                    //finish();

                    out.println(":start;");
                    out.flush();
                    count.cancel();
                    count.start();
                    ll.removeView(Air);
                }catch (Exception e) {

                }


                //ll.addView(insert,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150));//to add button in layout

                break;


            case R.id.exit_btn :
                insert_clicked++;
                int ary[]= new int[6];
                ary[0]=rst_avg;
                ary[1]=sqz_avg;
                ary[2]=cgf_avg;
                ary[3]=rst_max;
                ary[4]=sqz_max;
                ary[5]=cgf_max;

                //pass_status.set_data(ary);
                Intent it = new Intent(Exp_ps.this, report.class);
                startActivity(it);
                finish();

                //out.println("procedure_done");
                break;
        }

    }

    public class ClientAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            try {
                //Create a wifi socket and define internet address and the port of the server
                socket  = new Socket(params[0],
                        Integer.parseInt(params[1]));
                //Get the output stream of the wifi socket
                //Write data to the output stream of the wifi socket
                //status_esp.set_socket(socket);//Pass socket value to pass_status class so which can be used in another activity
                pass_status.set_socket(socket);
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                pass_status.set_out(out);
                out.println(params[2]);
                out.flush(); //............Added by me

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            read read_msg= new read();//Second AsyncTask to read received data from server
            read_msg.execute();
        }

    }

    /*  Second AsyncTask to read received data from server  */
    public class read extends AsyncTask<Void, Void, String> {

        byte[] buffer = new byte[6];
        Message serverMessage= Message.obtain();

        @Override
        protected String doInBackground(Void... params) {
            try{
                is = socket.getInputStream();
                int read_byte=is.read(buffer);

                if(read_byte==6) {
                    serverMessage.obj = new String(buffer);
                }


                buffer=null;


            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            mHandler.sendMessage(serverMessage);//Passing the received data from server to handler
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try{
                messageDisplay(msg.obj.toString());
            }catch (Exception e) {}
        }
    };

    public void messageDisplay(String servermessage)
    {
        //dada_point=servermessage;


        if(servermessage.contains(":Oxy;")){
            pressure_text.setText("Rest pressure");
            count.cancel();
            count.start();
            insert_clicked++; //increment to change the graph data
        }
        else if(servermessage.contains(":Rst;")){
            pressure_text.setText("Squeeze pressure");
            count.cancel();
            count.start();
            insert_clicked++; //increment to change the graph data
        }
        else if(servermessage.contains(":Sqz;")){
            pressure_text.setText("Cough pressure");
            count.cancel();
            count.start();
            insert_clicked++; //increment to change the graph data
        }

        read read_msg= new read();//Again call Second AsyncTask to read next data from server
        read_msg.execute();

        mmhg_int=Integer.parseInt(servermessage.replaceAll("[\\D]",""));



    }


    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler_exit = new Handler();

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mHandler_exit != null) { mHandler_exit.removeCallbacks(mRunnable); }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent it = new Intent(Exp_ps.this, Splash.class);
            startActivity(it);
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar snackbar = Snackbar
                .make(rootView, "Press again to go Back...", Snackbar.LENGTH_LONG);

        snackbar.show();
        mHandler_exit.postDelayed(mRunnable, 2000);
    }
}



