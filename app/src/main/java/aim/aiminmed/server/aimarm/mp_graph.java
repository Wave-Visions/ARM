package aim.aiminmed.server.aimarm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultYAxisValueFormatter;
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

public class mp_graph extends AppCompatActivity implements View.OnClickListener
{
             private LineChart lineChart;
             ArrayList<Entry> entries;
             LineData data;
             CountDownTimer count,count2,count3;
             LineData lineData;
             int time,time2=31;
             YAxis leftAxis;
             float var ,temp_var,sqz_max,rest_min,relax_min,relax2_min,sqz_max2;
             float air_avg,rst_avg,sqz_avg,sqz2_avg,rlx_avg,rlx2_avg,cgh_avg, value_avg;
             float air_max,rst_max,rlx_max,rlx2_max,cgh_max;
             float temp_max;
             TextView val,sc,cnt;
             XAxis xl;
             ViewPortHandler handler;
             Button run,ict,dct,sqz,cgh;
             char count_mode,insert_clicked;
             int timer_sec=8;
             float x_labal,run_sec;
             float sec_time,rx_start,rx_start_init;
             char count_rx_time,report_gen,sqz_counter,cgh_counter;
             int sqz_clicked,cgh_clicked;
    char target_achieve,target_achieve_c,target_achieve_r;
    int interval_sec;
    long glob_sec;
    boolean check;
    PrintWriter out;
    BufferedReader br;
    Socket socket = null;
    InputStream is;
    StringBuilder s = new StringBuilder(100);
    StringBuilder c = new StringBuilder(100);
    StringBuilder r = new StringBuilder(100);
    String path_air;
    NetworkInfo wifi;
    ConnectivityManager connManager;
    static char comm_led_toggle = 0;
    int second_rest = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);





        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp_graph);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                 //new ClientAsyncTask().execute("192.168.4.1", "80", ":start;");

                 val=(TextView)findViewById(R.id.val);
                 sc=(TextView)findViewById(R.id.sc);
                 cnt=(TextView)findViewById(R.id.cnt);
                 run =(Button)findViewById(R.id.run);
                 sqz=(Button)findViewById(R.id.sqz);
                 cgh=(Button)findViewById(R.id.cgh);
                ict=(Button)findViewById(R.id.ict);
                dct=(Button)findViewById(R.id.dct);

        run.setOnClickListener(this);
        ict.setOnClickListener(this);
        dct.setOnClickListener(this);
        sqz.setOnClickListener(this);
        cgh.setOnClickListener(this);

        lineChart = (LineChart) findViewById(R.id.chart);
        handler = lineChart.getViewPortHandler();
        //lineChart.setOnChartValueSelectedListener(this);
        // enable touch gestures
        lineChart.setTouchEnabled(true);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        //lineChart.setPinchZoom(true);

        // set an alternative background color
        lineChart.setBackgroundColor(Color.WHITE);
        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

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
        l.setTextColor(Color.BLACK);


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
        leftAxis.setAxisMaxValue(250f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawGridLines(true);
        final YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);



        path_air ="mnt/sdcard/AnoRectal Report";


        //path_air ="storage/emulated/0/DCIM";
                 File img_air = new File(path_air);



                 if(img_air.exists()){
                     img_air.delete();
                     if(img_air.exists())
                     getApplicationContext().deleteFile(img_air.getName());

                 }


                 //timer_sec=pass_status.getsec();
                 timer_sec=60*60;

        for(char i=0;i<=(timer_sec*2);i++) {
            addEntry();
        }
        time=0;
        timer_sec++;
        sc.setText("10");

        sqz.setBackgroundResource(R.drawable.btn_gray);
        cgh.setBackgroundResource(R.drawable.btn_gray);

        ict.setEnabled(true);
        dct.setEnabled(true);
        ict.setBackgroundResource(R.drawable.play);
        dct.setBackgroundResource(R.drawable.play2);
        cnt.setBackgroundResource(R.drawable.btn_gray);
        lineChart.setDescriptionTextSize(30);
        lineChart.setVisibleXRangeMaximum(80);
                 count=new CountDownTimer(((timer_sec+1)*1000), 500) { // adjust the milli seconds here

                     public void onTick(long millisUntilFinished) {

                         if (wifi.isConnected()){
                             try {
                                 if(comm_led_toggle == 0){
                                     //out.println(":blue_on;");
                                     //out.flush();
                                     comm_led_toggle = 1;
                                 }else{
                                     //out.println(":blue_off;");
                                     //out.flush();
                                     comm_led_toggle = 0;
                                 }

                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                         }
                         addTimeEntry();

                         //value_avg+=var;


                         glob_sec=millisUntilFinished;

                         if(((timer_sec+1)-(millisUntilFinished/1000))>=59970){

                             sqz.setEnabled(false);
                             //run.setEnabled(false);
                             ict.setEnabled(false);
                             dct.setEnabled(false);
                             cgh.setEnabled(false);

                             //run.setBackgroundResource(R.drawable.btn_gray);
                             ict.setBackgroundResource(R.drawable.play_gray);
                             dct.setBackgroundResource(R.drawable.play2_gray);
                             sqz.setBackgroundResource(R.drawable.btn_gray);
                             cgh.setBackgroundResource(R.drawable.btn_gray);
                             cnt.setBackgroundResource(R.drawable.btn_gray);
                         }


                         if(insert_clicked==5){
                             //var=(float)(3*Math.random());
                             run_sec=(((timer_sec+1)-(millisUntilFinished/1000))-sec_time);
                             interval_sec=Integer.parseInt(sc.getText().toString());
                             cnt.setText(Float.toString(interval_sec-run_sec)+" S");




                             if(run_sec>=interval_sec){
                                 insert_clicked=6;
                                 //run.setEnabled(true);
                                 run.setBackgroundResource(R.drawable.btn_gray);
                                 sqz.setEnabled(true);
                                 cgh.setEnabled(true);
                                 ict.setEnabled(true);
                                 dct.setEnabled(true);
                                 ict.setBackgroundResource(R.drawable.play);
                                 dct.setBackgroundResource(R.drawable.play2);
                                 sqz.setBackgroundResource(R.drawable.btn_dark);
                                 cgh.setBackgroundResource(R.drawable.btn_dark);
                                 //lineChart.setDescription("Current Pressure");
                                 cnt.setBackgroundResource(R.drawable.btn_gray);
                                 rst_avg= value_avg/(run_sec*2);
                                 Math.ceil(rst_avg);

                                 rst_max=temp_max;
                                 Math.ceil(rst_max);
                                 value_avg=0;
                                 LimitLine upper_limit = new LimitLine((time-1), String.format("%.2f", rst_avg )+"mm Hg");
                                 upper_limit.setLineWidth(4f);
                                 //upper_limit.enableDashedLine(10f, 10f, 0f);
                                 upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
                                 upper_limit.setTextSize(10f);
                                 int color = getResources().getColor(R.color.yellow);
                                 upper_limit.setLineColor(color);
                                 xl.addLimitLine(upper_limit);
                                 Toast.makeText(getBaseContext(), "Ask patient to start squeezing the sphincter and press the SQUEEZE key or\n ask patient to cough and press the COUGH REFLEX key.",Toast.LENGTH_LONG).show();
                                 //enable button and timer buttons too.
                                 //change the name from insert to rest of button
                                 //add start vertical limit line
                             }
                             run_sec=0;

                         }


                         if(sqz_clicked==1){


                             run_sec=(((timer_sec+1)-(millisUntilFinished/1000))-sec_time);
                             interval_sec=Integer.parseInt(sc.getText().toString());
                             cnt.setText(Float.toString(interval_sec-run_sec)+" S");
                             if(run_sec>=interval_sec){
                                 sqz_clicked=0;
                                 sqz_avg= value_avg /(run_sec*2);
                                 value_avg=0;

                                 target_achieve_r++;

                                 s.append(Integer.toString(target_achieve_r) + ")");
                                 //s.append(" Avg : "+ String.format("%.2f", sqz_avg) + " mm Hg " +"Max : "+String.format("%.2f", temp_max)+ " mm Hg\n"+"Duration: "+Integer.toString(interval_sec)+" Sec\n\n");
                                 s.append(" Avg : "+ (int)Math.round(sqz_avg) + " mm Hg          " +"Max : "+(int)Math.round(temp_max)+ " mm Hg          "+"Duration : "+Integer.toString(interval_sec)+" Sec\n\n");


                                 run.setEnabled(true);
                                 run.setBackgroundResource(R.drawable.btn_dark);
                                 run.setText("Report");
                                 report_gen = 1;
                                 sqz.setEnabled(true);
                                 cgh.setEnabled(true);
                                 ict.setEnabled(true);
                                 dct.setEnabled(true);
                                 ict.setBackgroundResource(R.drawable.play);
                                 dct.setBackgroundResource(R.drawable.play2);
                                 sqz.setBackgroundResource(R.drawable.btn_dark);
                                 cgh.setBackgroundResource(R.drawable.btn_dark);
                                 cnt.setBackgroundResource(R.drawable.btn_gray);
                                 //lineChart.setDescription("Current Pressure");
                                 insert_clicked=8;

                                 LimitLine upper_limit = new LimitLine((time-1), String.format("%.2f", sqz_avg)+"mm Hg");
                                 upper_limit.setLineWidth(4f);
                                 //upper_limit.enableDashedLine(10f, 10f, 0f);
                                 upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
                                 upper_limit.setTextSize(10f);
                                 int color = getResources().getColor(R.color.red);
                                 upper_limit.setLineColor(color);
                                 xl.addLimitLine(upper_limit);
                                 count_rx_time=1;
                                 rx_start_init = ((timer_sec + 1) - (millisUntilFinished / (float)1000));
                                 temp_max=0;
                                 count2.start();

                                 //and disable rest button
                                 //enable button squeeze and cough
                                 //add end vertical limit line
                                 try {
                                     out.println(":red_off;");
                                     out.flush();
                                 } catch (Exception e) {
                                     e.printStackTrace();
                                 }

                             }



                             if(var<45){
                                 //var=var+2;
                             }

                             if(var>45){
                                 //var=44;
                             }
                             run_sec=0;
                         }



                         if(var<=sqz_avg && count_rx_time==1) {
                             rx_start = ((float)(timer_sec + 1) - (millisUntilFinished / (float)1000));
                             count_rx_time=2;
                         }

                         /*if(var<=rst_avg && count_rx_time==2){
                             rlx_avg=value_avg/((((float)(timer_sec + 1) - (millisUntilFinished / (float)1000)) - rx_start_init)*2);


                             rlx_max=temp_max;
                             Math.ceil(rlx_avg);
                             Math.ceil(rlx_max);
                             value_avg=0;
                             temp_max=0;
                             target_achieve_r++;
                             r.append(Integer.toString(target_achieve_r) + ")");
                             r.append(" Avg : "+ String.format("%.2f", rlx_avg) + " mm Hg " +"Max : "+String.format("%.2f", rlx_max)+ " mm Hg\n"+"Relaxed Time :"+ String.format("%.2f", (((float)(timer_sec + 1) - (millisUntilFinished / (float)1000)) - rx_start))+" Sec\n\n");


                             count_rx_time=0;
                             sec_time = ((timer_sec + 1) - (glob_sec / 1000));
                             LimitLine upper_limit = new LimitLine((time-1), "Relaxed in "+String.format("%.2f",(((float)(timer_sec + 1) - (millisUntilFinished / (float)1000)) - rx_start))+" Sec");
                             upper_limit.setLineWidth(4f);
                             //upper_limit.enableDashedLine(10f, 10f, 0f);
                             upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
                             upper_limit.setTextSize(10f);
                             int color = getResources().getColor(R.color.fbutton_color_sun_flower);
                             upper_limit.setLineColor(color);
                             xl.addLimitLine(upper_limit);

                             //TODO Sqz issue
                             //count2.start();

                         }*/


                         if(cgh_clicked==1){

                             run_sec=(((timer_sec+1)-(millisUntilFinished/1000))-sec_time);
                             interval_sec=Integer.parseInt(sc.getText().toString());
                             cnt.setText(Float.toString(interval_sec-run_sec)+" S");
                             if(run_sec>=interval_sec){
                                 //insert_clicked=7;
                                 cgh_clicked=0;
                                 cgh_avg= value_avg /(run_sec*2);
                                 value_avg=0;

                                 target_achieve_c++;

                                 c.append(Integer.toString(target_achieve_c) + ")");
                                 //c.append(" Avg : "+ String.format("%.2f", cgh_avg) + " mm Hg " +"Max : "+String.format("%.2f", temp_max)+ " mm Hg\n"+"Duration: "+Integer.toString(interval_sec)+" Sec\n\n");
                                 c.append(" Avg : "+ (int)Math.round(cgh_avg) + " mm Hg          " +"Max : "+(int)Math.round(temp_max)+ " mm Hg          "+"Duration : "+Integer.toString(interval_sec)+" Sec\n\n");


                                 run.setEnabled(true);
                                 run.setBackgroundResource(R.drawable.btn_dark);
                                 run.setText("Report");
                                 report_gen = 1;
                                 sqz.setEnabled(true);
                                 cgh.setEnabled(true);
                                 ict.setEnabled(true);
                                 dct.setEnabled(true);
                                 ict.setBackgroundResource(R.drawable.play);
                                 dct.setBackgroundResource(R.drawable.play2);
                                 sqz.setBackgroundResource(R.drawable.btn_dark);
                                 cgh.setBackgroundResource(R.drawable.btn_dark);
                                 cnt.setBackgroundResource(R.drawable.btn_gray);
                                 insert_clicked=8;

                                 LimitLine upper_limit = new LimitLine((time-1), String.format("%.2f", cgh_avg)+"mm Hg");
                                 upper_limit.setLineWidth(4f);
                                 //upper_limit.enableDashedLine(10f, 10f, 0f);
                                 upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
                                 upper_limit.setTextSize(10f);
                                 int color = getResources().getColor(R.color.LiteGreen);
                                 upper_limit.setLineColor(color);
                                 xl.addLimitLine(upper_limit);
                                 //and disable rest button
                                 //enable button squeeze and cough
                                 //add end vertical limit line
                                 temp_max=0;
                                count3.start();
                                 try {
                                     out.println(":green_off;");
                                     out.flush();
                                 } catch (Exception e) {
                                     e.printStackTrace();
                                 }

                             }
                             //var=(float)(50*Math.random());
                             run_sec=0;
                         }


                         if(insert_clicked==6){
                             //var=(float)(2*Math.random());

                         }

                         if(insert_clicked==8){
                             //var=var-5;
                             if(var<10){
                                 //var=1;
                             }

                         }




                     }


                     public void onFinish() {
                         //addTimeEntry();
                         //value_avg +=var;
                         //value_avg = value_avg /time;
                         getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                         time = 0;
                         report_gen = 1;
                         run.setEnabled(true);
                         sqz.setEnabled(false);
                         cgh.setEnabled(false);
                         ict.setEnabled(false);
                         dct.setEnabled(false);
                         run.setText("Report");
                         run.setBackgroundResource(R.drawable.btn_dark);
                         cgh.setBackgroundResource(R.drawable.btn_gray);
                         sqz.setBackgroundResource(R.drawable.btn_gray);
                         ict.setBackgroundResource(R.drawable.play_gray);
                         dct.setBackgroundResource(R.drawable.play2_gray);


                     }


                 };


        count3=new CountDownTimer(500, 100) {
            public void onFinish() {
                lineChart.saveToGallery("cgh_"+Integer.toString(target_achieve_c),100);
            }

            public void onTick(long millisUntilFinished) {

            }
        };

        count2=new CountDownTimer(500, 100) {
            public void onFinish() {
                lineChart.saveToGallery("sqz_"+Integer.toString(target_achieve_r),100);
            }

            public void onTick(long millisUntilFinished) {

            }
        };
     }



    @Override
    public void onClick(View view) {

        switch(view.getId()) {

            case R.id.run:

                if(report_gen==1){
                    report_gen=0;
                    //xl.removeAllLimitLines();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    //lineChart.fitScreen();
                    lineChart.saveToGallery("S_Graph",100);

                    int ary[]= new int[14];

                    ary[0]=(int)rst_avg;
                    ary[1]=(int)rst_max;
                    ary[2]= second_rest;

                    pass_status.set_reading(s.toString(),c.toString(),r.toString());

                    pass_status.set_data(ary);
                    pass_status.set_sqz_cgh(target_achieve_r,target_achieve_c);
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    final EditText edittext = new EditText(getApplicationContext());
                    //alert.setMessage("Almost Done");
                    alert.setTitle("Diagnosis for the Patient?");
                    alert.setView(edittext);
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent it = new Intent(mp_graph.this, full_report.class);
                            it.putExtra("comment", edittext.getText().toString());
                            startActivity(it);
                            finish();
                        }
                    });

                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent it = new Intent(mp_graph.this, full_report.class);
                            startActivity(it);
                            finish();
                        }
                    });

                    alert.show();

                    break;
                }

                if(insert_clicked==0) {
                    count.cancel();
                    count.start();
                    sec_time=0;
                    second_rest = Integer.parseInt(sc.getText().toString());
                }
                else {
                    sec_time = ((timer_sec + 1) - (glob_sec / 1000));
                }
                insert_clicked++;


                run.setEnabled(false);
                run.setBackgroundResource(R.drawable.btn_grn);
                insert_clicked=5;
                    //lineChart.setDescription("Rest Pressure");
                    LimitLine upper_limit = new LimitLine(sec_time*2, "Rest");
                    upper_limit.setLineWidth(4f);
                    //upper_limit.enableDashedLine(10f, 10f, 0f);
                    upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                    upper_limit.setTextSize(10f);
                    //int color = getResources().getColor(R.color.LiteGreen);
                    int color = getResources().getColor(R.color.yellow);
                    upper_limit.setLineColor(color);
                    xl.addLimitLine(upper_limit);
                    ict.setEnabled(false);
                    dct.setEnabled(false);
                    ict.setBackgroundResource(R.drawable.play_gray);
                    dct.setBackgroundResource(R.drawable.play2_gray);
                cnt.setBackgroundResource(R.drawable.btn_dark);
                temp_max=0;
                value_avg =0;
            break;

            case R.id.ict:
                interval_sec=Integer.parseInt(sc.getText().toString());
                interval_sec++;
                sc.setText(Integer.toString(interval_sec));
                  if(interval_sec==31){
                      interval_sec=5;
                      sc.setText(Integer.toString(interval_sec));
                  }

            break;

            case R.id.dct:
                interval_sec=Integer.parseInt(sc.getText().toString());
                interval_sec--;
                sc.setText(Integer.toString(interval_sec));
                if(interval_sec==4){
                    interval_sec=30;
                    sc.setText(Integer.toString(interval_sec));
                }
            break;

            case R.id.sqz:
                sqz_clicked=1;
                value_avg =0;
                temp_max=0;
                sqz_counter++;
                insert_clicked=7;
                sec_time=((timer_sec+1)-(glob_sec/1000));
                sqz.setEnabled(false);
                run.setEnabled(false);
                cgh.setEnabled(false);
                ict.setEnabled(false);
                dct.setEnabled(false);
                ict.setBackgroundResource(R.drawable.play_gray);
                dct.setBackgroundResource(R.drawable.play2_gray);
                sqz.setBackgroundResource(R.drawable.btn_grn);
                cgh.setBackgroundResource(R.drawable.btn_gray);
                cnt.setBackgroundResource(R.drawable.btn_dark);
                run.setBackgroundResource(R.drawable.btn_gray);
                //lineChart.setDescription("Squeeze Pressure");

                LimitLine upper_limit3 = new LimitLine((time-1), "Squeeze "+Integer.toString(sqz_counter));
                upper_limit3.setLineWidth(4f);
                //upper_limit.enableDashedLine(10f, 10f, 0f);
                upper_limit3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                upper_limit3.setTextSize(10f);
                int color3 = getResources().getColor(R.color.red);
                upper_limit3.setLineColor(color3);
                xl.addLimitLine(upper_limit3);
                try {
                    out.println(":red_on;");
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            break;

            case R.id.cgh:
                cgh_clicked=1;
                value_avg =0;
                temp_max=0;
                cgh_counter++;
                insert_clicked=9;
                sqz.setEnabled(false);
                run.setEnabled(false);
                cgh.setEnabled(false);
                ict.setEnabled(false);
                dct.setEnabled(false);
                ict.setBackgroundResource(R.drawable.play_gray);
                dct.setBackgroundResource(R.drawable.play2_gray);
                sqz.setBackgroundResource(R.drawable.btn_gray);
                cgh.setBackgroundResource(R.drawable.btn_grn);
                cnt.setBackgroundResource(R.drawable.btn_dark);
                run.setBackgroundResource(R.drawable.btn_gray);
                //lineChart.setDescription("Cough Pressure");
                sec_time=((timer_sec+1)-(glob_sec/1000));

                LimitLine upper_limit2 = new LimitLine((time-1), "C Reflex"+Integer.toString(cgh_counter));
                upper_limit2.setLineWidth(4f);
                //upper_limit2.enableDashedLine(10f, 10f, 0f);
                upper_limit2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                upper_limit2.setTextSize(10f);
                int color2 = getResources().getColor(R.color.LiteGreen);
                upper_limit2.setLineColor(color2);
                xl.addLimitLine(upper_limit2);
                try {
                    out.println(":green_on;");
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }


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

            //temp_max=lineChart.getData().getYMax();
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

            if(var>temp_max){
                temp_max=var;
            }

            lineChart.setVisibleXRangeMaximum(80);
            if(time>=80){
                lineChart.moveViewToX(time-81);

            }

            value_avg+=var;
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



    private void openAlert(final Button b1,final Button b2,String s,final String s1,final String s2) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mp_graph.this);

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
                    out.println(":green_on;");
                    out.flush();
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
                Intent it = new Intent(mp_graph.this, pic_report.class);
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
        //val.setText(Float.toString(var));


        if(servermessage.contains(":Oxy;")){
            //pressure_text.setText("Rest pressure");
            lineChart.fitScreen();//Reset graph to its original position

            lineChart.clearValues();//Clear data points
            xl.setSpaceBetweenLabels(1);
            count.cancel();
            count.start();
            insert_clicked++; //increment to change the graph data
        }
        else if(servermessage.contains(":Rst;")){
            //pressure_text.setText("Squeeze pressure");
            lineChart.fitScreen();//Reset graph to its original position
            lineChart.clearValues();//Clear data points
            xl.setSpaceBetweenLabels(1);
            count.cancel();
            count.start();
            insert_clicked++; //increment to change the graph data
        }
        else if(servermessage.contains(":Sqz;")){
            //pressure_text.setText("Cough pressure");
            lineChart.fitScreen();//Reset graph to its original position
            lineChart.clearValues();//Clear data points
            xl.setSpaceBetweenLabels(1);
            count.cancel();
            count.start();
            insert_clicked++; //increment to change the graph data
        }
        else if(servermessage.contains(":Rlx;")){
            //pressure_text.setText("Cough pressure");
            lineChart.fitScreen();//Reset graph to its original position
            lineChart.clearValues();//Clear data points
            xl.setSpaceBetweenLabels(1);
            count.cancel();
            count.start();
            insert_clicked++; //increment to change the graph data
        }
        else if(servermessage.contains(":Sqz_sec;")){
            //pressure_text.setText("Cough pressure");
            count.cancel();
            count.start();
            insert_clicked++; //increment to change the graph data
        }
        else if(servermessage.contains(":Rlx_sec;")){
            //pressure_text.setText("Cough pressure");
            count.cancel();
            count.start();
            insert_clicked++; //increment to change the graph data
        }
        else if(servermessage.contains(":Cgh;")){
            //pressure_text.setText("Cough pressure");
            lineChart.fitScreen();//Reset graph to its original position
            lineChart.clearValues();//Clear data points
            xl.setSpaceBetweenLabels(1);
            count.cancel();
            count.start();
            insert_clicked++; //increment to change the graph data
        }
        read read_msg= new read();//Again call Second AsyncTask to read next data from server
        read_msg.execute();
        var=Integer.parseInt(servermessage.replaceAll("[\\D]",""));
        if(var >= 250){
            var = 250;
        }
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


    @Override
    public void onBackPressed() {
            count.cancel();
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
            super.onBackPressed();
            Intent it = new Intent(mp_graph.this, time_select.class);
            startActivity(it);
            finish();
            return;


    }

    @Override
    public void onDestroy() {
        count.cancel();
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onDestroy();

    }
}


