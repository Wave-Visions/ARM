package aim.aiminmed.server.aimarm;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class pic_report extends AppCompatActivity implements View.OnClickListener {

    TextView air_t,rst_t,sqz_t,rlx_t,sqz2_t,rlx2_t,cgh_t,air_mt,rst_mt,sqz_mt,rlx_mt,sqz2_mt,rlx2_mt,cgh_mt;
    int ary[]= new int[14];
    LinearLayout lay1,lay2,lay3;
    TextView doc,pat,ag,date,time;
    String patient;
    String formattedTime,formattedDate;
    private ArmDataBase ArmDataBaseObject = new ArmDataBase(this);
    private static String FILE = "mnt/sdcard/invoice.pdf"; // add permission in your manifest...
    private Button print;
    private static int height , width;
    private static ScrollView sv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_report);

        doc= (TextView)findViewById(R.id.doc);
        pat= (TextView)findViewById(R.id.pat);
        ag= (TextView)findViewById(R.id.ag);
        date= (TextView)findViewById(R.id.date);
        time= (TextView)findViewById(R.id.time);
        print= (Button)findViewById(R.id.print);

        print.setOnClickListener(this);

        ArmDataBaseObject.open();
        //Cursor cursorData = ArmDataBaseObject.H_getDocDetails(ArmDataBase.DNAME);
        Cursor cursorData = ArmDataBaseObject.H_DocDetails ();
        while (cursorData.moveToNext()) {
            String Doctor = cursorData.getString(cursorData.getColumnIndex(ArmDataBase.DNAME));

            doc.setText(Doctor);

        }
        ArmDataBaseObject.close();

        ArmDataBaseObject.open();
        Cursor cursorData1 = ArmDataBaseObject.H_PatDetails ();
        while (cursorData1.moveToNext()) {
            patient = cursorData1.getString(cursorData1.getColumnIndex(ArmDataBase.PATITENT_NAME));
            String Age = cursorData1.getString(cursorData1.getColumnIndex(ArmDataBase.AGE));
            pat.setText(patient);
            ag.setText(Age);
        }
        ArmDataBaseObject.close();

        formattedTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        //View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        date.setText(formattedDate);
        time.setText(formattedTime);

        String path_air = Environment.getExternalStorageDirectory()+ "/AnoRectal Report/Air.png";
        String path_rst = Environment.getExternalStorageDirectory()+ "/AnoRectal Report/Rst.png";
        String path_sqz = Environment.getExternalStorageDirectory()+ "/AnoRectal Report/Sqz.png";
        String path_rlx = Environment.getExternalStorageDirectory()+ "/AnoRectal Report/Rlx.png";
        String path_sqz2 = Environment.getExternalStorageDirectory()+ "/AnoRectal Report/Sqz2.png";
        String path_rlx2 = Environment.getExternalStorageDirectory()+ "/AnoRectal Report/Rlx2.png";
        String path_cgh = Environment.getExternalStorageDirectory()+ "/AnoRectal Report/Cgh.png";

        File img_air = new File(path_air);
        File img_rst = new File(path_rst);
        File img_sqz = new File(path_sqz);
        File img_rlx = new File(path_rlx);
        File img_sqz2 = new File(path_sqz2);
        File img_rlx2 = new File(path_rlx2);
        File img_cgh = new File(path_cgh);

        air_t=(TextView)findViewById(R.id.a_t);
        rst_t=(TextView)findViewById(R.id.r_t);
        sqz_t=(TextView)findViewById(R.id.s_t);
        rlx_t=(TextView)findViewById(R.id.rx_t);
        sqz2_t=(TextView)findViewById(R.id.s2_t);
        rlx2_t=(TextView)findViewById(R.id.r2_t);
        cgh_t=(TextView)findViewById(R.id.c_t);
        air_mt=(TextView)findViewById(R.id.a_mt);
        rst_mt=(TextView)findViewById(R.id.r_mt);
        sqz_mt=(TextView)findViewById(R.id.s_mt);
        rlx_mt=(TextView)findViewById(R.id.rx_mt);
        sqz2_mt=(TextView)findViewById(R.id.s2_mt);
        rlx2_mt=(TextView)findViewById(R.id.r2_mt);
        cgh_mt=(TextView)findViewById(R.id.c_mt);

        lay1=(LinearLayout)findViewById(R.id.sqz2_lay);
        lay2=(LinearLayout)findViewById(R.id.rlx2_lay);
        lay3=(LinearLayout)findViewById(R.id.cgh_lay);


        if(img_air.exists())
        {
            Bitmap myBitmap1 = BitmapFactory.decodeFile(img_air.getAbsolutePath());
            ImageView imageView1=(ImageView)findViewById(R.id.air);
            imageView1.setImageBitmap(myBitmap1);
        }
        if(img_rst.exists())
        {
            Bitmap myBitmap2 = BitmapFactory.decodeFile(img_rst.getAbsolutePath());
            ImageView imageView2=(ImageView)findViewById(R.id.rst);
            imageView2.setImageBitmap(myBitmap2);
        }
        if(img_sqz.exists())
        {
            Bitmap myBitmap3 = BitmapFactory.decodeFile(img_sqz.getAbsolutePath());
            ImageView imageView3=(ImageView)findViewById(R.id.sqz);
            imageView3.setImageBitmap(myBitmap3);
        }
        if(img_rlx.exists())
        {
            Bitmap myBitmap4 = BitmapFactory.decodeFile(img_rlx.getAbsolutePath());
            ImageView imageView4=(ImageView)findViewById(R.id.rlx);
            imageView4.setImageBitmap(myBitmap4);
        }
        if(img_sqz2.exists())
        {
            Bitmap myBitmap5 = BitmapFactory.decodeFile(img_sqz2.getAbsolutePath());
            ImageView imageView5=(ImageView)findViewById(R.id.sqz2);
            imageView5.setImageBitmap(myBitmap5);
        }
        if(img_rlx2.exists())
        {
            Bitmap myBitmap6 = BitmapFactory.decodeFile(img_rlx2.getAbsolutePath());
            ImageView imageView6=(ImageView)findViewById(R.id.rlx2);
            imageView6.setImageBitmap(myBitmap6);
        }
        if(img_cgh.exists())
        {
            Bitmap myBitmap7 = BitmapFactory.decodeFile(img_cgh.getAbsolutePath());
            ImageView imageView7=(ImageView)findViewById(R.id.cgh);
            imageView7.setImageBitmap(myBitmap7);
        }


        air_t=(TextView)findViewById(R.id.a_t);
        rst_t=(TextView)findViewById(R.id.r_t);
        sqz_t=(TextView)findViewById(R.id.s_t);
        rlx_t=(TextView)findViewById(R.id.rx_t);
        sqz2_t=(TextView)findViewById(R.id.s2_t);
        rlx2_t=(TextView)findViewById(R.id.r2_t);
        cgh_t=(TextView)findViewById(R.id.c_t);
        air_mt=(TextView)findViewById(R.id.a_mt);
        rst_mt=(TextView)findViewById(R.id.r_mt);
        sqz_mt=(TextView)findViewById(R.id.s_mt);
        rlx_mt=(TextView)findViewById(R.id.rx_mt);
        sqz2_mt=(TextView)findViewById(R.id.s2_mt);
        rlx2_mt=(TextView)findViewById(R.id.r2_mt);
        cgh_mt=(TextView)findViewById(R.id.c_mt);

        pass_status.get_data(ary);
        air_t.setText(String.valueOf(ary[0])+" mmgh");
        rst_t.setText(String.valueOf(ary[1])+" mmgh");
        sqz_t.setText(String.valueOf(ary[2])+" mmgh");
        rlx_t.setText(String.valueOf(ary[3])+" mmgh");
        sqz2_t.setText(String.valueOf(ary[4])+" mmgh");
        rlx2_t.setText(String.valueOf(ary[5])+" mmgh");
        cgh_t.setText(String.valueOf(ary[6])+" mmgh");
        air_mt.setText(String.valueOf(ary[7])+" mmgh");
        rst_mt.setText(String.valueOf(ary[8])+" mmgh");
        sqz_mt.setText(String.valueOf(ary[9])+" mmgh");
        rlx_mt.setText(String.valueOf(ary[10])+" mmgh");
        sqz2_mt.setText(String.valueOf(ary[11])+" mmgh");
        rlx2_mt.setText(String.valueOf(ary[12])+" mmgh");
        cgh_mt.setText(String.valueOf(ary[13])+" mmgh");

        char skip=0;
        skip=pass_status.getskip();
        if(skip==1){
            lay1.setVisibility(View.INVISIBLE);
            lay2.setVisibility(View.INVISIBLE);
        }
        else if(skip==2){
            lay1.setVisibility(View.INVISIBLE);
            lay2.setVisibility(View.INVISIBLE);
            lay3.setVisibility(View.INVISIBLE);
        }


        sv = (ScrollView) findViewById(R.id.scrollView3);






    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {

            case R.id.print :
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
                LinearLayout root = (LinearLayout) inflater.inflate(R.layout.activity_pic_report, null); //RelativeLayout is root view of my UI(xml) file.
                root.setDrawingCacheEnabled(true);
                View child = sv.getRootView();
                height = sv.getChildCount();
                width = child.getWidth();
                Bitmap screen= getBitmapFromView(this.getWindow().findViewById(R.id.scrollView3)); // here give id of our root layout (here its my RelativeLayout's id)


                try
                {
                    //Rectangle pagesize = new Rectangle(500f, 2000f);
                    //Document document = new Document(pagesize, 36f, 72f, 108f, 180f);
                    //Document document = new Document();
                    Document document = new Document(PageSize.A4);
                    PdfWriter.getInstance(document, new FileOutputStream(FILE));
                    document.open();



                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap bt=Bitmap.createScaledBitmap(screen, (int)PageSize.A4.getWidth(),(int)PageSize.A4.getHeight(), true);
                    bt.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    addImage(document,byteArray);
                    //document.newPage();

                    document.close();
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }
            break;
        }
    }

    private static void addImage(Document document,byte[] byteArray)
    {
        Image image = null;
        try
        {
            image = Image.getInstance(byteArray);
        }
        catch (BadElementException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // image.scaleAbsolute(150f, 150f);
        try
        {
            document.add(image);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
       // int spec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
       // view.measure(spec, spec);
      //  view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
       // Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        int h = 0;
        //Bitmap bitmap = null;
        //get the actual height of scrollview
        for (int i = 0; i < height; i++) {
            h += sv.getChildAt(i).getHeight();
            sv.getChildAt(i).setBackgroundResource(R.color.white);
        }

        Bitmap returnedBitmap = Bitmap.createBitmap(width, h,Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
}
