package aim.aiminmed.server.aimarm;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class biofeed_init extends AppCompatActivity implements View.OnClickListener
{
             private LineChart lineChart;
             ArrayList<Entry> entries;
             LineData data;
             CountDownTimer count;
             LineData lineData;
             int time,time2=31;
             YAxis leftAxis;
             float var ,temp_var,sqz_max,rest_min,relax_min,relax2_min,sqz_max2;
             float air_avg,rst_avg,sqz_avg,sqz2_avg,rlx_avg,rlx2_avg,cgh_avg,var_avg;
             float air_max,rst_max,rlx_max,rlx2_max,cgh_max;
             float temp_max;
             TextView val,cnt;
             XAxis xl;
             ViewPortHandler handler;
             Button run,next;
             char count_mode,insert_clicked;
             int timer_sec=8;
             float x_labal,run_sec;
             float sec_time;
             int sqz_clicked,cgh_clicked;
    int interval_sec;
    long glob_sec;
    boolean check;
    PrintWriter out;
    BufferedReader br;
    Socket socket = null;
    InputStream is;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biofeed_init);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                // new ClientAsyncTask().execute("192.168.4.1", "80", ":start;");

                 val=(TextView)findViewById(R.id.val);

                 cnt=(TextView)findViewById(R.id.cnt);
                 run =(Button)findViewById(R.id.run);
                 next =(Button)findViewById(R.id.nxt);

        run.setOnClickListener(this);
        next.setOnClickListener(this);

        lineChart = (LineChart) findViewById(R.id.chart);
        handler = lineChart.getViewPortHandler();
        //lineChart.setOnChartValueSelectedListener(this);
        // enable touch gestures
        lineChart.setTouchEnabled(false);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        //lineChart.setPinchZoom(true);

        // set an alternative background color
        lineChart.setBackgroundColor(Color.WHITE);
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        lineChart.setData(data);
        lineChart.setDescription("");
        //lineChart.setDescriptionPosition(900,60);
        lineChart.setDrawBorders(true);
        lineChart.setBorderColor(Color.BLUE);
        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();


        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        //l.setTypeface(Typeface.DEFAULT);
        l.setTextColor(Color.WHITE);


        xl = lineChart.getXAxis();
        //xl.setTypeface(Typeface.DEFAULT);
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);
        xl.setSpaceBetweenLabels(5);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);

        leftAxis = lineChart.getAxisLeft();
        leftAxis.setTypeface(Typeface.DEFAULT);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaxValue(150f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawGridLines(true);
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setTypeface(Typeface.DEFAULT);
        rightAxis.setTextColor(Color.BLACK);
        rightAxis.setAxisMaxValue(150f);
        rightAxis.setAxisMinValue(0f);
        rightAxis.setDrawGridLines(true);
        rightAxis.setEnabled(true);

        rightAxis.setEnabled(true);


                 String path_air = Environment.getExternalStorageDirectory()+"mnt/sdcard";


                 File img_air = new File(path_air);



                 if(img_air.exists()){
                     img_air.delete();
                     if(img_air.exists())
                     getApplicationContext().deleteFile(img_air.getName());

                 }


                //timer_sec=pass_status.getsec();
                 timer_sec=180;

        for(char i=0;i<=(timer_sec*2);i++) {
            addEntry();
        }
        time=0;
        timer_sec++;
        cnt.setBackgroundResource(R.drawable.btn_gray);
        next.setBackgroundResource(R.drawable.btn_gray);
        lineChart.setDescriptionTextSize(30);
        lineChart.setVisibleXRangeMaximum(60);



                 count=new CountDownTimer(((timer_sec+1)*1000), 500) { // adjust the milli seconds here

                     public void onTick(long millisUntilFinished) {

                         addTimeEntry();

                         var_avg+=var;

                         glob_sec=millisUntilFinished;

                         if(insert_clicked==1){

                             run_sec=(((timer_sec+1)-(millisUntilFinished/1000))-sec_time);
                             cnt.setText(":"+Float.toString(10-run_sec)+" S");
                             if(run_sec>=10){
                                 insert_clicked=2;
                                 run.setEnabled(true);
                                 run.setText("Insert");
                                 run.setBackgroundResource(R.drawable.btn_dark);
                                 //lineChart.setDescription("Current Pressure");
                                 air_avg=var_avg/run_sec;
                                 cnt.setBackgroundResource(R.drawable.btn_gray);

                                 count.cancel();
                                 Toast.makeText(getBaseContext(), "Insert the Sensor probe and then click on the INSERT button",
                                         Toast.LENGTH_LONG).show();

                                 try {
                                     //out.println(":green_on;");
                                    // out.flush();
                                 } catch (Exception e) {
                                     e.printStackTrace();
                                 }

                                 //enable button
                                 //change the name from run to insert of button
                             }
                             run_sec=0;
                         }




                         if(insert_clicked==3){
                             //var=(float)(20*Math.random());
                             run_sec=(((timer_sec+1)-(millisUntilFinished/1000))-sec_time);
                             cnt.setText(":"+Float.toString(10-run_sec)+" S");
                             if(run_sec>=10){
                                 insert_clicked=4;
                                 run.setEnabled(false);
                                 run.setBackgroundResource(R.drawable.btn_gray);
                                 //run.setText("Rest");
                                 next.setEnabled(true);
                                 next.setBackgroundResource(R.drawable.btn_dark);
                                 //lineChart.setDescription("Current Pressure");
                                cnt.setBackgroundResource(R.drawable.btn_gray);
                                 //enable button and timer buttons too.
                                 //change the name from insert to rest of button
                                 //add start vertical limit line
                             }
                             run_sec=0;

                         }

                         if(insert_clicked==4){
                             //var=(float)(3*Math.random());
                         }



                     }


                     public void onFinish() {
                         //addTimeEntry();
                         //var_avg+=var;
                        // var_avg=var_avg/time;
                         getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                         time=0;

                         run.setEnabled(false);

                         run.setBackgroundResource(R.drawable.btn_gray);


                                 Intent it = new Intent(biofeed_init.this, biofeed_main.class);
                                 startActivity(it);
                                 finish();

                         }


                 };
     }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {

            case R.id.run:
                if(insert_clicked==0) {
                    count.cancel();
                    count.start();
                    sec_time=0;
                }
                else {
                    sec_time = ((timer_sec + 1) - (glob_sec / 1000));
                }

                if(insert_clicked==2){
                    //count.onTick(glob_sec);
                    //timer_sec=(int)glob_sec/1000;
                    count.start();
                    sec_time = 0;
                    //timer_sec=(int)glob_sec/1000;
                }
                insert_clicked++;

                var_avg=0;
                run.setEnabled(false);
                run.setBackgroundResource(R.drawable.btn_grn);
                cnt.setBackgroundResource(R.drawable.btn_dark);
                var_avg=0;
            break;



            case R.id.nxt:
                count.cancel();
                Intent it = new Intent(biofeed_init.this, biofeed_main.class);
                startActivity(it);
                finish();
                break;

        }
    }




    public void addTimeEntry() {

        //String entry_date_time = new SimpleDateFormat("MMM d - HH:mm:ss").format(new Date());
        String entry_date_time= Integer.toString(time);
        LineData lineData = lineChart.getData();

        int GRAPH_WIDTH = 31;

        if (lineData != null) {

            LineDataSet set = (LineDataSet) lineData.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                lineData.addDataSet(set);
            }

            // Make rolling window

            temp_max=lineChart.getData().getYMax();
                //lineData.getXVals().add(entry_date_time);
                //var=(float) (Math.random() * 40);
                lineData.addEntry(new Entry(var, time), 0);
            if(var >= 250){
                val.setText("MAX");
            }else {
                val.setText(String.format("%.2f", var));
            }

                if(insert_clicked==1 && time==4){
                    temp_var=var;
                }

            lineChart.setVisibleXRangeMaximum(60);

            if(time>=60){
                lineChart.moveViewToX(time-61);

            }
            // let the chart know it's data has changed
            time++;
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();





        }
    }

             public void addEntry() {

                 String entry_date_time= Float.toString(x_labal);
                 LineData lineData = lineChart.getData();

                 if (lineData != null) {

                     LineDataSet set = (LineDataSet) lineData.getDataSetByIndex(0);

                     if (set == null) {
                         set = createSet();
                         lineData.addDataSet(set);
                     }
                     lineData.getXVals().add(entry_date_time);
                     x_labal=x_labal+(float)0.5;
                     lineChart.notifyDataSetChanged();
                     lineChart.invalidate();
                 }
             }





    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Pressure Values");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2.5f);
        set.setCircleRadius(2f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        set.setDrawCubic(true);
        set.setCubicIntensity(0.05f);
        set.setDrawFilled(true);
        set.setDrawCircles(false);

        return set;
    }

    /*private Thread thread;

    private void feedMultiple() {

        if (thread != null)
            thread.interrupt();

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                //addEntry();
                addTimeEntry();
            }
        };

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {

                    // Don't generate garbage runnables inside the loop.
                    runOnUiThread(runnable);

                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }*/

    private void openAlert(final Button b1,final Button b2,String s,final String s1,final String s2) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(biofeed_init.this);

        alertDialogBuilder.setTitle(this.getTitle()+ " decision");
        alertDialogBuilder.setIcon(R.drawable.aim);
        //alertDialogBuilder.setMessage("Would you like to go through again from " + s + "mode");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Again Squeeze Mode",new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog,int id) {
                b1.setBackgroundResource(R.drawable.flow_g);
                b2.setBackgroundResource(R.drawable.flow_y);

                lineChart.setDescription(s1);
                try{
                    out.println(s2);
                    out.flush();
                }catch (Exception e) {}
            }

        });

        // set negative button: No message

        alertDialogBuilder.setNegativeButton("Skip to Cough Mode",new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog,int id) {

                // cancel the alert box and put a Toast to the user
                insert_clicked=5;
                //minus.setBackgroundResource(R.drawable.flow_g);
                //rlx.setBackgroundResource(R.drawable.flow_g);
                //cgh.setBackgroundResource(R.drawable.flow_y);
                lineChart.saveToPath("Rlx","/AnoRectal Report");
                lineChart.setDescription("Cough Pressure");
                char skip=1;
                pass_status.setskip(skip);
                try{
                    //out.println(":green_on;");
                    //out.flush();
                }catch (Exception e) {}
                dialog.cancel();


            }

        });

        // set neutral button: Exit the app message

        alertDialogBuilder.setNeutralButton("Generate Report",new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog,int id) {

                // exit the app and go to the HOME
                char skip=2;
                pass_status.setskip(skip);
                int ary[]= new int[14];
                ary[0]=(int)air_avg;
                ary[1]=(int)rst_avg;
                ary[2]=(int)sqz_avg;
                ary[3]=(int)rlx_avg;
                ary[4]=(int)sqz_avg;
                ary[5]=(int)rlx2_avg;
                ary[6]=(int)cgh_avg;
                ary[7]=(int)air_max;
                ary[8]=(int)rst_max;
                ary[9]=(int)sqz_max;
                ary[10]=(int)rlx_max;
                ary[11]=(int)sqz_max2;
                ary[12]=(int)rlx2_max;
                ary[13]=(int)cgh_max;

                pass_status.set_data(ary);
                Intent it = new Intent(biofeed_init.this, pic_report.class);
                startActivity(it);
                finish();

            }

        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

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
                //pass_status.set_socket(socket);
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                //pass_status.set_out(out);
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


            if(socket!= null){
                //count.cancel();
                //count.start();
            }

            //addTimeEntry();
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
        read read_msg= new read();//Second AsyncTask to read received data from server
        read_msg.execute();
        if(servermessage.contains(":IND;")){
            Toast.makeText(getBaseContext(), "Patient is ready for further procedure",
                    Toast.LENGTH_LONG).show();
        }
        var=Integer.parseInt(servermessage.replaceAll("[\\D]",""));
        if(var >= 250){
            var = 250;
        }

    }



    @Override
    public void onBackPressed() {
        count.cancel();
        super.onBackPressed();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent it = new Intent(biofeed_init.this, time_select.class);
        startActivity(it);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();


            // Release the Camera because we don't need it when paused
            // and other activities might need to use it.

        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        try {
            new ClientAsyncTask().execute("192.168.4.1", "80", ":start;");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}




