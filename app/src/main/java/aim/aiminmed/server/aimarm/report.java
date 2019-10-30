package aim.aiminmed.server.aimarm;

import android.content.ActivityNotFoundException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by server on 06/09/2016.
 */
public class report extends AppCompatActivity implements View.OnClickListener{

    Button share;
    File temp_file;
    TextView d,p,date,time,r_avg,s_avg,c_avg,r_max,s_max,c_max,ag;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private ArmDataBase ArmDataBaseObject = new ArmDataBase(this);
    String patient;
    LinearLayout rt;
    String formattedTime,formattedDate;
    int rst_avg,sqz_avg,cgf_avg,rst_max,sqz_max,cgf_max;
    View rootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_report);
        //View layout= (LinearLayout)findViewById(R.id.main_lay);
        rootView = getWindow().getDecorView().findViewById(R.id.main_lay);
        share= (Button)findViewById(R.id.share_button);
        share.setOnClickListener(this);

        d= (TextView)findViewById(R.id.doc_name);
        p= (TextView)findViewById(R.id.pat_name);
        ag= (TextView)findViewById(R.id.age);
        date= (TextView)findViewById(R.id.date);
        time= (TextView)findViewById(R.id.time);
        r_avg=(TextView)findViewById(R.id.r_avg);
        s_avg=(TextView)findViewById(R.id.s_avg);
        c_avg=(TextView)findViewById(R.id.c_avg);
        r_max=(TextView)findViewById(R.id.r_max);
        s_max=(TextView)findViewById(R.id.s_max);
        c_max=(TextView)findViewById(R.id.c_max);




        rt = (LinearLayout) findViewById(R.id.report);
        ArmDataBaseObject.open();
        //Cursor cursorData = ArmDataBaseObject.H_getDocDetails(ArmDataBase.DNAME);
        Cursor cursorData = ArmDataBaseObject.H_DocDetails ();
        while (cursorData.moveToNext()) {
            String Doctor = cursorData.getString(cursorData.getColumnIndex(ArmDataBase.DNAME));

            d.setText(Doctor);

        }
        ArmDataBaseObject.close();

        ArmDataBaseObject.open();
        Cursor cursorData1 = ArmDataBaseObject.H_PatDetails ();
        while (cursorData1.moveToNext()) {
            patient = cursorData1.getString(cursorData1.getColumnIndex(ArmDataBase.PATITENT_NAME));
            String Age = cursorData1.getString(cursorData1.getColumnIndex(ArmDataBase.AGE));
            p.setText(patient);
            ag.setText(Age);
        }
        ArmDataBaseObject.close();

        formattedTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        //View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        date.setText(formattedDate);
        time.setText(formattedTime);
        int ary[]= new int[6];
        //pass_status.get_data(ary);
        rst_avg=ary[0];
        sqz_avg=ary[1];
        cgf_avg=ary[2];
        rst_max=ary[3];
        sqz_max=ary[4];
        cgf_max=ary[5];
        r_avg.setText(String.valueOf(rst_avg));
        s_avg.setText(String.valueOf(sqz_avg));
        c_avg.setText(String.valueOf(cgf_avg));
        r_max.setText(String.valueOf(rst_max));
        s_max.setText(String.valueOf(sqz_max));
        c_max.setText(String.valueOf(cgf_max));

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {


            case R.id.share_button:
                try {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkPermission()) {
                            // Code for above or equal 23 API Oriented Device
                            // Your Permission granted already .Do next code

                            Bitmap b = getScreenShot(rt);
                            store(b, patient + "_Report_" + formattedDate + ".png");
                            shareImage(temp_file);
                        } else {
                            requestPermission(); // Code for permission
                        }
                    } else {

                        Bitmap b = getScreenShot(rt);
                        store(b, patient + "_Report_" + formattedDate + ".png");
                        shareImage(temp_file);
                        // Code for Below 23 API Oriented Device
                        // Do next code
                    }

                }catch (Exception e) {}

        break;
        }
    }



    public static Bitmap getScreenShot(View view) {
        View screenView = view;
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public  void store(Bitmap bm, String fileName){
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/AnoRectal Report";
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        temp_file=file;
        try {
            FileOutputStream fOut = new FileOutputStream(temp_file);
            bm.compress(Bitmap.CompressFormat.PNG, 0, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getBaseContext(), "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(report.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(report.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(report.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(report.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
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
            Intent it = new Intent(report.this, Splash.class);
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
