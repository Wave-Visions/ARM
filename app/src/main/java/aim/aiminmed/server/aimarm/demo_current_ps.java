package aim.aiminmed.server.aimarm;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import java.io.PrintWriter;
import java.net.Socket;

import info.hoang8f.widget.FButton;

/**
 * Created by server on 08/09/2016.
 */
public class demo_current_ps extends AppCompatActivity implements View.OnClickListener{

    LineGraphSeries<DataPoint> current_series, rest_series, squeeze_series, cough_series;
    TextView timer_text;
    TextView first;
    TextView pressure_text;
    CountDownTimer count;
    GraphView graph;
    double  x=1;
    char insert_clicked=0;
    LinearLayout ll;
    FButton insert,Squeeze,Cough,exit;

    PrintWriter out;
    BufferedReader br;
    Socket socket = null;
    String result=null;
    String dada_point=null;
    double mmhg_int;
    int mmhg_avg,temp_data;
    int timer_sec=10;//Timer Second
    char pressure_mult=0;
    //pass_status status_esp;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_pressure);
        setTitle(R.string.Graph_activity);//Setting the title of this activity
        first = (TextView) findViewById(R.id.first);


        //socket= pass_status.getSocket();
        //out=pass_status.getOut();
        //read read_msg= new read();//Second AsyncTask to read received data from server
        //read_msg.execute();




        //Adding insert button for further process

        ll = (LinearLayout) findViewById(R.id.process_lay);
        insert = new FButton(this);
        insert.setText("Insert");
        insert.setId(R.id.insert_btn);//set ids for button
        //insert.setBackgroundResource(R.drawable.btn);//Set background image
        insert.setButtonColor(getResources().getColor(R.color.fbutton_color_turquoise));
        ll.addView(insert,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));//to add button in layout
        insert.setVisibility(View.INVISIBLE);//do invisible

        //Adding insert button for further process

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
        exit.setText("Exit");
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



       // graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(100);//Max value of y axis
        //graph.getViewport().setMinX(0.0);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxX(30);

        graph.getViewport().setScalable(true); //manually user can change Scal of X axis
        graph.getViewport().setScrollable(true);////manually user can scroll of X axis

        //graph.setTitle("Chart Title");
        graph.getGridLabelRenderer().setVerticalAxisTitle("mm Hg");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Second");
        graph.getGridLabelRenderer().setVerticalLabelsAlign(Paint.Align.LEFT);


        //current_series.setDrawDataPoints(true);
        current_series.setDrawBackground(true); // activate the background feature<br />
        current_series.setColor(Color.GREEN);//set color of line of graph
        //current_series.setBackgroundColor(Color.GREEN);//
        //Change the colour for new data
       // rest_series.setDrawDataPoints(true);
        rest_series.setDrawBackground(true); // activate the background feature<br />
        rest_series.setColor(Color.BLUE);
        rest_series.setThickness(10);
        //rest_series.setBackgroundColor(Color.BLUE);

        //squeeze_series.setDrawDataPoints(true);
        squeeze_series.setDrawBackground(true); // activate the background feature<br />
        squeeze_series.setColor(Color.RED);
        squeeze_series.setThickness(10);
        //squeeze_series.setBackgroundColor(Color.RED);

        //cough_series.setDrawDataPoints(true);
        cough_series.setDrawBackground(true); // activate the background feature<br />
        cough_series.setColor(Color.MAGENTA);
        cough_series.setThickness(10);
        //cough_series.setBackgroundColor(Color.MAGENTA);

        timer_text = (TextView) findViewById(R.id.textimer);
        pressure_text = (TextView) findViewById(R.id.textupper);

        insert.setOnClickListener(this);
        Squeeze.setOnClickListener(this);
        Cough.setOnClickListener(this);
        exit.setOnClickListener(this);

        pressure_text.setText("Air Pressure");

        count=new CountDownTimer((timer_sec*1000), 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                mmhg_int=pressure_mult*Math.random();
                temp_data=(int)mmhg_int;
                mmhg_avg+=mmhg_int;
                first.setText(temp_data +" "+ "mmhg ");
                switch(insert_clicked) {
                    case 0:

                        timer_text.setText("Wait for : " + millisUntilFinished / 1000);
                        current_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(current_series);
                        x++;
                        break;

                    case 1:
                        timer_text.setText("Wait for : " + millisUntilFinished / 1000);
                        rest_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(rest_series);
                        x++;
                        break;
                    case 2:
                        timer_text.setText("Wait for : " + millisUntilFinished / 1000);
                        squeeze_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(squeeze_series);
                        x++;
                        break;
                    case 3:
                        timer_text.setText("Wait for : " + millisUntilFinished / 1000);
                        cough_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(cough_series);
                        x++;
                        break;
                }


            }


            public void onFinish() {
                mmhg_avg=mmhg_avg/timer_sec;
                first.setText("Average" +" "+ Integer.toString(mmhg_avg) +" "+ "mmhg ");
                mmhg_avg=0;
                switch(insert_clicked) {
                    case 0:
                        timer_text.setText("");
                        insert.setVisibility(View.VISIBLE);
                        current_series.appendData(new DataPoint(x, mmhg_int), true, 100);//Plotting graph here because last second ends in onFinish function
                        graph.addSeries(current_series);

                        rest_series.appendData(new DataPoint(x, mmhg_int), true, 100);//Adding this graph here to join two different graphs
                        graph.addSeries(rest_series);
                        pressure_mult=10;//Temp variable for demo app to generate proper pressure value
                        x++;
                        break;

                    case 1:
                        timer_text.setText("");
                        Squeeze.setVisibility(View.VISIBLE);
                        rest_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(rest_series);
                        squeeze_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(squeeze_series);
                        pressure_mult=50;
                        x++;
                        break;

                    case 2:
                        timer_text.setText("");
                        Cough.setVisibility(View.VISIBLE);
                        squeeze_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(squeeze_series);
                        cough_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(cough_series);
                        pressure_mult=40;
                        x++;
                        break;

                    case 3:
                        timer_text.setText("");
                        cough_series.appendData(new DataPoint(x, mmhg_int), true, 100);
                        graph.addSeries(cough_series);
                        x++;
                        //Put further button here
                        exit.setVisibility(View.VISIBLE);
                        Toast toast= Toast.makeText(getApplicationContext(),"Pinch to zoom/Slide to scroll", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        break;



                }

            }

        }.start();

    }
    @Override
    public void onClick(View view) {

        switch(view.getId()) {

            case R.id.insert_btn :

                //out.println("E");//Sending msg to hardware
                pressure_text.setText("Rest pressure");
               // if (dada_point.contains("READY") );
                count.cancel();
                count.start();
                insert_clicked++; //increment to change the graph data
                ll.removeView(insert);
                ll.addView(Squeeze,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));//to add button in layout

                break;

            case R.id.squeeze_btn :
                insert_clicked++;
                pressure_text.setText("Squeeze pressure");
                ll.removeView(Squeeze);
                ll.addView(Cough,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));//to add button in layout
                count.cancel();
                count.start();

                break;

            case R.id.cough_btn :
                insert_clicked++;
                pressure_text.setText("Cough pressure");
                ll.removeView(Cough);
                ll.addView(exit,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));
                count.cancel();
                count.start();

                break;
            case R.id.exit_btn :
                insert_clicked++;
                Intent i = new Intent(demo_current_ps.this,Splash.class);
                startActivity(i);
                finish();

                break;
        }

    }






}

