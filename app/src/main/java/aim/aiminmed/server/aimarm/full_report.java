package aim.aiminmed.server.aimarm;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class full_report extends AppCompatActivity implements View.OnClickListener {

    TextView rlx_t, con_txt;
    TextView rst_t, rst_mt, rst_dur;
    TextView sqz_t, sqz_mt;
    TextView cgh_t, cgh_mt;

    int ary[]= new int[14];
    LinearLayout lay1,lay2,lay3;
    TextView doc,pat, txt_gen,ag,date,time,hos,remark;
    String patient;
    String formattedTime,formattedDate;
    private ArmDataBase ArmDataBaseObject = new ArmDataBase(this);
    private static String FILEEE = "mnt/sdcard/Sphinctometer.pdf"; // add permission in your manifest...
    private Button print;
    private static int height , width;
    private static ScrollView sv;
    private File r_pdf;
    private static final int PERMISSION_REQUEST_CODE = 1;
    String path_air,path_temp;
    private String Doctor,hname, gender, Age,rm;
    private Image image,image_logo;
    String string_con = "";
    String interval = "";
    String path_pdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.full_report);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        hos= (TextView)findViewById(R.id.hos);
        doc= (TextView)findViewById(R.id.doc);
        pat= (TextView)findViewById(R.id.pat);
        txt_gen= (TextView)findViewById(R.id.txt_gender);
        ag= (TextView)findViewById(R.id.ag);
        date= (TextView)findViewById(R.id.date);
        time= (TextView)findViewById(R.id.time);
        print= (Button)findViewById(R.id.print);
        remark= (TextView)findViewById(R.id.rm);
        con_txt = (TextView)findViewById(R.id.con_text);
        print.setOnClickListener(this);

        ArmDataBaseObject.open();
        //Cursor cursorData = ArmDataBaseObject.H_getDocDetails(ArmDataBase.DNAME);
        Cursor cursorData = ArmDataBaseObject.H_DocDetails ();
        while (cursorData.moveToNext()) {
             Doctor = cursorData.getString(cursorData.getColumnIndex(ArmDataBase.DNAME));
             hname = cursorData.getString(cursorData.getColumnIndex(ArmDataBase.HNAME));
            doc.setText(Doctor);
            hos.setText(hname);
        }
        ArmDataBaseObject.close();

        ArmDataBaseObject.open();
        Cursor cursorData1 = ArmDataBaseObject.H_PatDetails ();
        while (cursorData1.moveToNext()) {
            patient = cursorData1.getString(cursorData1.getColumnIndex(ArmDataBase.PATITENT_NAME));
            gender = cursorData1.getString(cursorData1.getColumnIndex(ArmDataBase.GENDER));
             Age = cursorData1.getString(cursorData1.getColumnIndex(ArmDataBase.AGE));
             rm = cursorData1.getString(cursorData1.getColumnIndex(ArmDataBase.REMARKS));
            txt_gen.setText(gender);
            pat.setText(patient);
            ag.setText(Age);
            remark.setText(rm);
        }
        ArmDataBaseObject.close();

        formattedTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        //View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        date.setText(formattedDate);
        time.setText(formattedTime);

        //path_air ="mnt/sdcard/AnoRectal Report/Graph.jpg";
        path_air= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+ "/S_Graph.jpg";
       // path_air ="storage/emulated/0/DCIM/S_Graph.jpg";

        File img_air = new File(path_air);
        String todayAsString = new SimpleDateFormat("dd_MM_yyyy_hh_mm").format(new Date());

        path_pdf =  "mnt/sdcard/Sphinctometer"+todayAsString+".pdf";
        r_pdf = new File(path_pdf);

        //air_t=(TextView)findViewById(R.id.a_t);
        sqz_t=(TextView)findViewById(R.id.s_t);
        rlx_t=(TextView)findViewById(R.id.rx_t);

        cgh_t=(TextView)findViewById(R.id.c_t);


        lay1=(LinearLayout)findViewById(R.id.sqz2_lay);
        lay2=(LinearLayout)findViewById(R.id.rlx2_lay);
        lay3=(LinearLayout)findViewById(R.id.cgh_lay);


        if(img_air.exists())
        {
            Bitmap myBitmap1 = BitmapFactory.decodeFile(img_air.getAbsolutePath());
            ImageView imageView1=(ImageView)findViewById(R.id.rst);
            //imageView1.setImageBitmap(myBitmap1);
        }




        rst_t=(TextView)findViewById(R.id.r_t);
        rst_mt=(TextView)findViewById(R.id.r_mt);
        rst_dur=(TextView)findViewById(R.id.r_dur);

        sqz_t=(TextView)findViewById(R.id.s_t);
        sqz_mt=(TextView)findViewById(R.id.s_mt);

        cgh_t=(TextView)findViewById(R.id.c_t);
        cgh_mt=(TextView)findViewById(R.id.c_mt);



        pass_status.get_data(ary);

        rst_t.setText(String.valueOf(ary[0])+" mm Hg");
        //sqz_t.setText(String.valueOf(ary[2])+" mmhg");
        rlx_t.setText(String.valueOf(ary[3])+" mm Hg");

        //cgh_t.setText(String.valueOf(ary[6])+" mmhg");

        rst_mt.setText(String.valueOf(ary[1])+" mm Hg");
        rst_dur.setText(String.valueOf(ary[2])+" Sec");

        //cgh_mt.setText(String.valueOf(ary[13])+" mmhg");

        sqz_t.setText(pass_status.get_reading_s());
        cgh_t.setText(pass_status.get_reading_c());
        rlx_t.setText(pass_status.get_reading_r());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            con_txt.setText( extras.getString("comment"));
            string_con = extras.getString("comment");
            interval = extras.getString("interval");
        }

        if(pass_status.get_reading_s().equals("")){
            pass_status.set_reading_s("NO DATA FOUND");
            sqz_t.setText(pass_status.get_reading_s());
        }
        if(pass_status.get_reading_r().equals("")){
            pass_status.set_reading_r("NO DATA FOUND");
            rlx_t.setText(pass_status.get_reading_r());
        }

        if(pass_status.get_reading_c().equals("")){
            pass_status.set_reading_c("NO DATA FOUND");
            cgh_t.setText(pass_status.get_reading_c());
        }



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

                Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.medtech_dev);
                ByteArrayOutputStream stream_l = new ByteArrayOutputStream();
                logo.compress(Bitmap.CompressFormat.PNG, 100, stream_l);
                byte[] byteArray_l = stream_l.toByteArray();

                try
                {

                    image_logo = Image.getInstance(byteArray_l);
                    image = Image.getInstance(path_air);
                    //Rectangle pagesize = new Rectangle(500f, 2000f);
                    //Document document = new Document(pagesize, 36f, 72f, 108f, 180f);
                    //Document document = new Document();
                    Document document = new Document(PageSize.A4);
                    PdfWriter.getInstance(document, new FileOutputStream(path_pdf));
                    document.open();

                    addMetaData(document);
                    addTitlePage(document);

                    //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    //Bitmap bt=Bitmap.createScaledBitmap(screen, (int)PageSize.A4.getWidth(),(int)PageSize.A4.getHeight(), true);
                    //bt.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    //byte[] byteArray = stream.toByteArray();
                    //addImage(document,byteArray);
                    //document.newPage();

                    document.close();

                    try {
                        if (Build.VERSION.SDK_INT >= 23) {
                            if (checkPermission()) {
                                // Code for above or equal 23 API Oriented Device
                                // Your Permission granted already .Do next code

                                //Bitmap b = getScreenShot(rt);
                                //store(b, patient + "_Report_" + formattedDate + ".png");
                                shareImage(r_pdf);
                                //Intent sendIntent = new Intent();
                               // sendIntent.setAction(Intent.ACTION_SEND);
                                //sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                                // sendIntent.setType("text/plain");


                               // Uri uri = Uri.fromFile(r_pdf);
                               // sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                               // sendIntent.setType("application/pdf");
                                //sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sphinctometer Report");
                               // startActivity(Intent.createChooser(sendIntent, "Share Report"));
                                // startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.fromFile(reportFile), "application/pdf")));
                            } else {
                                requestPermission(); // Code for permission
                            }
                        } else {

                            //Bitmap b = getScreenShot(rt);
                            // store(b, patient + "_Report_" + formattedDate + ".png");
                            shareImage(r_pdf);
                            //Intent sendIntent = new Intent();
                            //sendIntent.setAction(Intent.ACTION_SEND);

                            // sendIntent.setType("text/plain");


                           // Uri uri = Uri.fromFile(r_pdf);
                            //sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            //sendIntent.setType("application/pdf");
                           // sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sphinctometer Report");
                           // startActivity(Intent.createChooser(sendIntent, "Share Report"));

                            // Code for Below 23 API Oriented Device
                            // Do next code
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }





                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }
            break;
        }
    }

    public void addMetaData(Document document)

    {
        document.addTitle("Report");
        document.addSubject("Person Info");
        //document.addKeywords("Personal,	Education, Skills");
        // document.addAuthor("TAG");
        // document.addCreator("TAG");
    }

    public void addTitlePage(Document document) throws DocumentException {
        // Font Style for Document
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD
                | Font.UNDERLINE, BaseColor.GRAY);
        Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        Font custom = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC);


        // Start New Paragraph
        Paragraph prHead = new Paragraph();
        // Set Font in this Paragraph
        prHead.setFont(titleFont);
        // Add item into Paragraph
        image_logo.scalePercent(20);
        image_logo.setAbsolutePosition(30,780);
        prHead.add(image_logo);
        prHead.add("SphinctoPress Report\n");

        // Create Table into Document with 1 Row
        PdfPTable myTable = new PdfPTable(1);
        // 100.0f mean width of table is same as Document size
        myTable.setWidthPercentage(100.0f);

        // Create New Cell into Table
        PdfPCell myCell = new PdfPCell(new Paragraph(""));
        myCell.setBorder(Rectangle.BOTTOM);

        // Add Cell into Table
        myTable.addCell(myCell);

        //prHead.setFont(normal);
        //prHead.add("\nDate: "+formattedDate+"\nTime: "+formattedTime+"\n");
        prHead.setAlignment(Element.ALIGN_CENTER);

        // Add all above details into Document
        document.add(prHead);


        Paragraph prHead1 = new Paragraph();
        prHead1.setFont(normal);
        prHead1.add("Date : "+formattedDate+"\nTime : "+formattedTime+"\n");
        prHead1.setAlignment(Element.ALIGN_RIGHT);
        document.add(prHead1);

        document.add(myTable);
        document.add(myTable);

        Phrase phrase1 = new Phrase();
        phrase1.add(
                new Chunk("Hospital Name",  new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD))
        );
        Phrase phrase2 = new Phrase();
        phrase2.add(
                new Chunk("Doctor Name",  new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD))
        );
        Phrase phrase3 = new Phrase();
        phrase3.add(
                new Chunk("Patient Name",  new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD))
        );
        Phrase phraseGen = new Phrase();
        phraseGen.add(
                new Chunk("Gender",  new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD))
        );
        Phrase phrase4 = new Phrase();
        phrase4.add(
                new Chunk("Age",  new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD))
        );
        Phrase phrase5 = new Phrase();
        phrase5.add(
                new Chunk("Remarks",  new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD))
        );

        Phrase colon = new Phrase();
        colon.add(
                new Chunk(":",  new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD))
        );

        Phrase hos_name = new Phrase();
        hos_name.add(
                new Chunk(hname,  new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL))
        );
        Phrase doc_name = new Phrase();
        doc_name.add(
                new Chunk(Doctor,  new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL))
        );
        Phrase pat_name = new Phrase();
        pat_name.add(
                new Chunk(patient,  new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL))
        );
        Phrase pat_gen = new Phrase();
        pat_gen.add(
                new Chunk(gender,  new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL))
        );
        Phrase pat_age = new Phrase();
        pat_age.add(
                new Chunk(Age,  new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL))
        );
        Phrase pat_rm = new Phrase();
        pat_rm.add(
                new Chunk(rm,  new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL))
        );


        PdfPTable main = new PdfPTable(3);
        main.setWidths(new float[] { 4, 1,3 });
        main.setWidthPercentage(50);
        main.setSpacingBefore(5);
        main.setSpacingAfter(5);
        main.setHorizontalAlignment(Element.ALIGN_LEFT);
        //----------Rest Mode Readings



        PdfPCell m1 = new PdfPCell(phrase1);
        m1.setHorizontalAlignment(Element.ALIGN_LEFT);
        m1.setBorder(Rectangle.NO_BORDER);
        main.addCell(m1);

        PdfPCell col = new PdfPCell(colon);
        col.setHorizontalAlignment(Element.ALIGN_LEFT);
        col.setBorder(Rectangle.NO_BORDER);
        main.addCell(col);

        PdfPCell m2 = new PdfPCell(hos_name);
        m2.setHorizontalAlignment(Element.ALIGN_LEFT);
        m2.setBorder(Rectangle.NO_BORDER);
        main.addCell(m2);

        PdfPCell m3 = new PdfPCell(phrase2);
        m3.setHorizontalAlignment(Element.ALIGN_LEFT);
        m3.setBorder(Rectangle.NO_BORDER);
        main.addCell(m3);

        main.addCell(col);

        PdfPCell m4 = new PdfPCell(doc_name);
        m4.setHorizontalAlignment(Element.ALIGN_LEFT);
        m4.setBorder(Rectangle.NO_BORDER);
        main.addCell(m4);

        PdfPCell m5 = new PdfPCell(phrase3);
        m5.setHorizontalAlignment(Element.ALIGN_LEFT);
        m5.setBorder(Rectangle.NO_BORDER);
        main.addCell(m5);

        main.addCell(col);

        PdfPCell m6 = new PdfPCell(pat_name);
        m6.setHorizontalAlignment(Element.ALIGN_LEFT);
        m6.setBorder(Rectangle.NO_BORDER);
        main.addCell(m6);

        //Gender
        PdfPCell mGen = new PdfPCell(phraseGen);
        mGen.setHorizontalAlignment(Element.ALIGN_LEFT);
        mGen.setBorder(Rectangle.NO_BORDER);
        main.addCell(mGen);

        main.addCell(col);

        PdfPCell mPatGen = new PdfPCell(pat_gen);
        mPatGen.setHorizontalAlignment(Element.ALIGN_LEFT);
        mPatGen.setBorder(Rectangle.NO_BORDER);
        main.addCell(mPatGen);
        //

        PdfPCell m7 = new PdfPCell(phrase4);
        m7.setHorizontalAlignment(Element.ALIGN_LEFT);
        m7.setBorder(Rectangle.NO_BORDER);
        main.addCell(m7);

        main.addCell(col);

        PdfPCell m8 = new PdfPCell(pat_age);
        m8.setHorizontalAlignment(Element.ALIGN_LEFT);
        m8.setBorder(Rectangle.NO_BORDER);
        main.addCell(m8);

        PdfPCell m9 = new PdfPCell(phrase5);
        m9.setHorizontalAlignment(Element.ALIGN_LEFT);
        m9.setBorder(Rectangle.NO_BORDER);
        main.addCell(m9);

        main.addCell(col);

        PdfPCell m10 = new PdfPCell(pat_rm);
        m10.setHorizontalAlignment(Element.ALIGN_LEFT);
        m10.setBorder(Rectangle.NO_BORDER);
        main.addCell(m10);

        document.add(main);


        document.add(myTable);

        document.add(myTable);

        /*Paragraph prProfile0 = new Paragraph();
        prProfile0.setFont(catFont);
        prProfile0.add("\nGraph : \n ");

        document.add(prProfile0);

        image.scalePercent(50);
        PdfPTable pic_table = new PdfPTable(1);
        pic_table.setWidthPercentage(100);
        pic_table.setSpacingBefore(30);
        pic_table.setSpacingAfter(50);
        pic_table.setHorizontalAlignment(Element.ALIGN_LEFT);

        pic_table.addCell(image);
        document.add(pic_table);  */

        Paragraph prProfile = new Paragraph();
        prProfile.setFont(catFont);
        prProfile.add("\n Rest Mode : \n ");

        document.add(prProfile);

        /*PdfPTable t = new PdfPTable(3);
        t.setWidthPercentage(100);
        t.setSpacingBefore(5);
        t.setSpacingAfter(10);
        t.setHorizontalAlignment(Element.ALIGN_LEFT);
        //----------Rest Mode Readings
        PdfPCell c1 = new PdfPCell(new Phrase("Average"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        t.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase("Maximum"));
        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        t.addCell(c2);

        PdfPCell c5 = new PdfPCell(new Phrase("Duration"));
        c5.setHorizontalAlignment(Element.ALIGN_CENTER);
        t.addCell(c5);


        PdfPCell c3 = new PdfPCell(new Phrase(String.valueOf(ary[0])+" mm Hg"));
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        t.addCell(c3);

        PdfPCell c4 = new PdfPCell(new Phrase(String.valueOf(ary[1])+" mm Hg"));
        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
        t.addCell(c4);

        PdfPCell c6 = new PdfPCell(new Phrase(String.valueOf(ary[2])+" Sec"));
        c6.setHorizontalAlignment(Element.ALIGN_CENTER);
        t.addCell(c6);*/

        Paragraph prProfile0 = new Paragraph();
        prProfile0.setFont(normal);
        String strr = " Avg : "+ (int)Math.round(ary[0]) + " mm Hg          " +"Max : "+(int)Math.round(ary[1])+ " mm Hg          "+"Duration : "+Integer.toString(ary[2])+" Sec\n\n";
        prProfile0.add("\n"+strr+"\n");
        //document.add(prProfile2);

        PdfPTable sqz_table0 = new PdfPTable(1);
        sqz_table0.setWidthPercentage(100);
        PdfPCell sqz_cell0 = new PdfPCell();
        //sqz_cell.setMinimumHeight(50);
        //sqz_cell.setVerticalAlignment(Element.ALIGN_CENTER);
        sqz_cell0.setBorderColor(BaseColor.BLACK);
        sqz_cell0.addElement(prProfile0);
        sqz_table0.addCell(sqz_cell0);
        document.add(sqz_table0);

        //----------Rest Mode Readings
        Paragraph prProfile1 = new Paragraph();
        prProfile1.setFont(catFont);
        prProfile1.add("\n Squeeze Mode : \n ");
        document.add(prProfile1);

        Paragraph prProfile2 = new Paragraph();
        prProfile2.setFont(normal);
        prProfile2.add("\n"+pass_status.get_reading_s()+"\n");
        //document.add(prProfile2);

        PdfPTable sqz_table = new PdfPTable(1);
        sqz_table.setWidthPercentage(100);
        PdfPCell sqz_cell = new PdfPCell();
        //sqz_cell.setMinimumHeight(50);
        //sqz_cell.setVerticalAlignment(Element.ALIGN_CENTER);
        sqz_cell.setBorderColor(BaseColor.BLACK);
        sqz_cell.addElement(prProfile2);
        sqz_table.addCell(sqz_cell);

        document.add(sqz_table);

        Paragraph Cough_mode = new Paragraph();
        Cough_mode.setFont(catFont);
        Cough_mode.add("\n Cough Reflex Mode : \n ");
        document.add(Cough_mode);

        Paragraph cough_data = new Paragraph();
        cough_data.setFont(normal);
        cough_data.add("\n"+pass_status.get_reading_c()+"\n");
        //document.add(prProfile4);

        //para.setLeading(0, 1);
        PdfPTable cgh_table = new PdfPTable(1);

        cgh_table.setWidthPercentage(100);
        PdfPCell cgh_cell = new PdfPCell();
        //cgh_cell.setMinimumHeight(50);
        //cgh_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cgh_cell.setBorderColor(BaseColor.BLACK);
        cgh_cell.addElement(cough_data);
        cgh_table.addCell(cgh_cell);

        document.add(cgh_table);

        Paragraph prConclusion = new Paragraph();
        prConclusion.setFont(catFont);
        prConclusion.add("\n Diagnosis : \n ");
        document.add(prConclusion);

        Paragraph prConclusionVal = new Paragraph();
        prConclusionVal.setFont(normal);
        prConclusionVal.add("\n"+string_con+"\n");
        document.add(prConclusionVal);

        document.newPage();

        Paragraph sqz_graph = new Paragraph();
        sqz_graph.setFont(catFont);
        sqz_graph.add("\n Squeeze Graph : \n ");
        document.add(sqz_graph);

        PdfPTable sqz_pic = new PdfPTable(1);
        sqz_pic.setHorizontalAlignment(Element.ALIGN_LEFT);
        sqz_pic.setSpacingBefore(5);
        sqz_pic.setSpacingAfter(10);
        sqz_pic.setWidthPercentage(100);


        for(int j=1;j<=pass_status.get_sqz();j++){
            path_temp= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/sqz_"+Integer.toString(j)+".jpg";
            try{
                Image image_temp = Image.getInstance(path_temp);
                image_temp.scalePercent(50);
                image_temp.setSpacingAfter(50);
                image_temp.setScaleToFitLineWhenOverflow(false);
                PdfPCell sqz_pic_cell = new PdfPCell();
                sqz_pic_cell.setBorder(Rectangle.NO_BORDER);
                //sqz_pic_cell.setMinimumHeight(50);
                //sqz_pic_cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Paragraph txt = new Paragraph("Squeeze "+Integer.toString(j));
                txt.setSpacingAfter(10);
                txt.setFont(custom);
                sqz_pic_cell.addElement(txt);
                //sqz_pic_cell.addElement(image_temp);
                sqz_pic.addCell(sqz_pic_cell);
                sqz_pic.addCell(image_temp);
            }catch (Exception e) {}
        }


        document.add(sqz_pic);

        /*Paragraph prProfile3 = new Paragraph();
        prProfile3.setFont(catFont);
        prProfile3.add("\n Relax Mode : \n ");
        document.add(prProfile3);

        Paragraph prProfile4 = new Paragraph();
        prProfile4.setFont(normal);
        prProfile4.add("\n"+pass_status.get_reading_r()+"\n");
        //document.add(prProfile4);

        //para.setLeading(0, 1);
        PdfPTable rlx_table = new PdfPTable(1);
        rlx_table.setWidthPercentage(100);
        PdfPCell rlx_cell = new PdfPCell();
        //rlx_cell.setMinimumHeight(50);
        rlx_cell.setBorderColor(BaseColor.GREEN);
        rlx_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        rlx_cell.addElement(prProfile4);
        rlx_table.addCell(rlx_cell);
        document.add(rlx_table);*/

        document.newPage();


        Paragraph cgh_graph = new Paragraph();
        cgh_graph.setFont(catFont);
        cgh_graph.add("\n Cough Reflex Graph : \n ");
        document.add(cgh_graph);



        PdfPTable cgh_pic = new PdfPTable(1);
        cgh_pic.setHorizontalAlignment(Element.ALIGN_LEFT);
        cgh_pic.setSpacingBefore(5);
        cgh_pic.setSpacingAfter(10);
        cgh_pic.setWidthPercentage(100);

        for(int k=1;k<=pass_status.get_cgh();k++){
            path_temp= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/cgh_"+Integer.toString(k)+".jpg";
            try{
                Image image_temp1 = Image.getInstance(path_temp);
                image_temp1.scalePercent(50);
                image_temp1.setSpacingAfter(50);
                image_temp1.setScaleToFitLineWhenOverflow(false);
                PdfPCell cgh_pic_cell = new PdfPCell();

                cgh_pic_cell.setBorder(Rectangle.NO_BORDER);
                //cgh_pic_cell.setMinimumHeight(250);
                Paragraph txt = new Paragraph("C Reflex "+Integer.toString(k));
                txt.setSpacingAfter(10);
                txt.setFont(custom);
                cgh_pic_cell.addElement(txt);
                //cgh_pic_cell.addElement(image_temp1);
                //cgh_pic_cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                cgh_pic.addCell(cgh_pic_cell);
                cgh_pic.addCell(image_temp1);
            }catch (Exception e) {}
        }
        //document.newPage();

        document.add(cgh_pic);

        //*para.setLeading(0, 1);
        /**PdfPTable rlx_table = new PdfPTable(1);
        rlx_table.setWidthPercentage(100);
        PdfPCell rlx_cell = new PdfPCell();
        //rlx_cell.setMinimumHeight(50);
        rlx_cell.setBorderColor(BaseColor.GREEN);
        rlx_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        rlx_cell.addElement(prProfile4);
        rlx_table.addCell(rlx_cell);
        document.add(rlx_table);*/
        ///
        document.newPage();
    }

    private void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("application/pdf");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "SphinctoPress Report");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Report"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getBaseContext(), "No App Available", Toast.LENGTH_SHORT).show();
        }
        /*if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                e.printStackTrace();
                Toast.makeText(full_report.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }*/


    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(full_report.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(full_report.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(full_report.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(full_report.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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



    @Override
    public void onBackPressed() {
        pass_status.clear_reading();
        super.onBackPressed();
        Intent it = new Intent(full_report.this, mp_graph.class);
        startActivity(it);
        finish();
    }
}
