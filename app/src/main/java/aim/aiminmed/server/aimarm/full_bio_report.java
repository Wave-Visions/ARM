package aim.aiminmed.server.aimarm;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class full_bio_report extends AppCompatActivity implements View.OnClickListener {

    TextView rst_t,sqz_t,rlx_t,rst_mt;
    int ary[]= new int[14];
    LinearLayout lay1,lay2,lay3;
    TextView doc,pat,ag,date,time,hos,remark;
    String patient;
    String formattedTime,formattedDate;
    private ArmDataBase ArmDataBaseObject = new ArmDataBase(this);
    private static String FILE = "mnt/sdcard/Bio Feedback.pdf"; // add permission in your manifest...
    private Button print;
    private static int height , width;
    private static ScrollView sv;
    File img_air,img_air2,r_pdf;
    private static final int PERMISSION_REQUEST_CODE = 1;
    String path_air,path_air2;
    File temp_file;
    ArrayList<Bitmap> chunkedImages;
    private String Doctor,hname,Age,rm;
    private Image image1,image2,image_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_bio_report);

        hos= (TextView)findViewById(R.id.hos);
        doc= (TextView)findViewById(R.id.doc);
        pat= (TextView)findViewById(R.id.pat);
        ag= (TextView)findViewById(R.id.ag);
        date= (TextView)findViewById(R.id.date);
        time= (TextView)findViewById(R.id.time);
        print= (Button)findViewById(R.id.print);
        remark= (TextView) findViewById(R.id.rm);

        print.setOnClickListener(this);

        ArmDataBaseObject.open();
        //Cursor cursorData = ArmDataBaseObject.H_getDocDetails(ArmDataBase.DNAME);
        Cursor cursorData = ArmDataBaseObject.H_DocDetails ();
        while (cursorData.moveToNext()) {
            Doctor = cursorData.getString(cursorData.getColumnIndex(ArmDataBase.DNAME));
            hname = cursorData.getString(cursorData.getColumnIndex(ArmDataBase.HNAME));
            doc.setText(Doctor);
            hos.setText(hname);
            doc.setText(Doctor);

        }
        ArmDataBaseObject.close();

        ArmDataBaseObject.open();
        Cursor cursorData1 = ArmDataBaseObject.H_PatDetails ();
        while (cursorData1.moveToNext()) {
            patient = cursorData1.getString(cursorData1.getColumnIndex(ArmDataBase.PATITENT_NAME));
            Age = cursorData1.getString(cursorData1.getColumnIndex(ArmDataBase.AGE));
            rm = cursorData1.getString(cursorData1.getColumnIndex(ArmDataBase.REMARKS));
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

        //path_air = "mnt/sdcard/AnoRectal Report/Graph.jpg";
        path_air= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+ "/B_Graph.jpg";
        path_air2= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+ "/B_Graph2.jpg";
       // path_air ="storage/emulated/0/DCIM/B_Graph.jpg";



        img_air = new File(path_air);
        img_air2 = new File(path_air2);
        String path_pdf ="mnt/sdcard/Bio Feedback.pdf";
        r_pdf = new File(path_pdf);

        //air_t=(TextView)findViewById(R.id.a_t);
        rst_t=(TextView)findViewById(R.id.r_t);
        sqz_t=(TextView)findViewById(R.id.s_t);
        rlx_t=(TextView)findViewById(R.id.rx_t);



        rst_mt=(TextView)findViewById(R.id.r_mt);

        lay1=(LinearLayout)findViewById(R.id.sqz2_lay);
        lay2=(LinearLayout)findViewById(R.id.rlx2_lay);
        lay3=(LinearLayout)findViewById(R.id.cgh_lay);


        if(img_air.exists())
        {
            Bitmap myBitmap1 = BitmapFactory.decodeFile(img_air.getAbsolutePath());
            ImageView imageView1=(ImageView)findViewById(R.id.trial1);
            imageView1.setImageBitmap(myBitmap1);
        }

        if(img_air2.exists())
        {
            Bitmap myBitmap2 = BitmapFactory.decodeFile(img_air2.getAbsolutePath());
            ImageView imageView2=(ImageView)findViewById(R.id.trial2);
            imageView2.setImageBitmap(myBitmap2);
        }




        rst_t=(TextView)findViewById(R.id.r_t);
        sqz_t=(TextView)findViewById(R.id.s_t);
        rlx_t=(TextView)findViewById(R.id.rx_t);



        rst_mt=(TextView)findViewById(R.id.r_mt);


        pass_status.get_data(ary);

        rst_t.setText(String.valueOf(ary[0])+" mmHg");
        //sqz_t.setText(String.valueOf(ary[2])+" mmHg");
        //rlx_t.setText(String.valueOf(ary[3])+" mmHg");

        //cgh_t.setText(String.valueOf(ary[6])+" mmHg");

        rst_mt.setText(String.valueOf(ary[1])+" mmHg");


        //cgh_mt.setText(String.valueOf(ary[13])+" mmHg");

        sqz_t.setText(pass_status.get_reading_s());

        rlx_t.setText(pass_status.get_reading_r());


        if(pass_status.get_reading_s().equals("")){
            pass_status.set_reading_s("NO DATA FOUND");
            sqz_t.setText(pass_status.get_reading_s());
        }
        if(pass_status.get_reading_r().equals("")){
            pass_status.set_reading_r("NO DATA FOUND");
            rlx_t.setText(pass_status.get_reading_r());
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

                Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.aim);
                ByteArrayOutputStream stream_l = new ByteArrayOutputStream();
                logo.compress(Bitmap.CompressFormat.PNG, 100, stream_l);
                byte[] byteArray_l = stream_l.toByteArray();



                /*Bitmap trial1= getBitmapFromView(this.getWindow().findViewById(R.id.trial1)); // here give id of our root layout (here its my RelativeLayout's id)
                Bitmap trial2= getBitmapFromView(this.getWindow().findViewById(R.id.trial2)); // here give id of our root layout (here its my RelativeLayout's id)


                Bitmap bt1=Bitmap.createScaledBitmap(trial1, trial1.getWidth()/2,trial1.getHeight()/2, true);

                ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                bt1.compress(Bitmap.CompressFormat.PNG,100,stream1);
                byte[] byteArray1 = stream1.toByteArray();

                Bitmap bt2=Bitmap.createScaledBitmap(trial2, trial2.getWidth()/2,trial2.getHeight()/2, true);
                ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                bt2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                byte[] byteArray2 = stream2.toByteArray();*/



                try
                {
                    image_logo = Image.getInstance(byteArray_l);
                    image1 = Image.getInstance(path_air);
                    image2 = Image.getInstance(path_air2);
                    //Rectangle pagesize = new Rectangle(500f, 2000f);
                    //Document document = new Document(pagesize, 36f, 72f, 108f, 180f);
                    Document document = new Document(PageSize.A4);
                    //Document document = new Document(PageSize.A3);
                    PdfWriter.getInstance(document, new FileOutputStream(FILE));
                    document.open();


                    addMetaData(document);
                    addTitlePage(document);
                 /*   ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    //Bitmap bt=Bitmap.createScaledBitmap(screen, (int)PageSize.A4.getWidth(),(int)PageSize.A3.getHeight(), true);
                    //bt.compress(Bitmap.CompressFormat.PNG, 100, stream);
                   // byte[] byteArray = stream.toByteArray();
                   // Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                   // store(bitmap, patient + "_Report_" + formattedDate + ".png");

                    int image_height=screen.getHeight();
                    splitImage(screen,(image_height/900));


                    for(int i=0;i<(image_height/900);i++){
                        Bitmap b1=chunkedImages.get(i);
                        Bitmap bt1=Bitmap.createScaledBitmap(b1, (int)PageSize.A4.getWidth(),(int)PageSize.A4.getHeight(), true);
                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                        bt1.compress(Bitmap.CompressFormat.PNG, 100, stream1);

                        byte[] byteArray1 = stream1.toByteArray();
                        addImage(document,byteArray1);
                    }
                    */

                    document.close();

                    try {
                        if (Build.VERSION.SDK_INT >= 23) {
                            if (checkPermission()) {
                                // Code for above or equal 23 API Oriented Device
                                // Your Permission granted already .Do next code

                                //Bitmap b = getScreenShot(rt);
                                //store(b, patient + "_Report_" + formattedDate + ".png");
                                //shareImage(r_pdf);
                               // Intent sendIntent = new Intent();
                               // sendIntent.setAction(Intent.ACTION_SEND);
                                //sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                                // sendIntent.setType("text/plain");
                               // shareImage(temp_file);

                               // Uri uri = Uri.fromFile(r_pdf);
                               // sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                               // sendIntent.setDataAndType(uri, "application/pdf");
                                shareImage(r_pdf);
                                //sendIntent.setType("application/pdf");
                                //sendIntent.setData(uri);

                              //  startActivity(Intent.createChooser(sendIntent, "Share Report"));
                               // startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.fromFile(reportFile), "application/pdf")));
                            } else {
                                requestPermission(); // Code for permission
                            }
                        } else {
                            shareImage(temp_file);
                            //Bitmap b = getScreenShot(rt);
                           // store(b, patient + "_Report_" + formattedDate + ".png");
                            //shareImage(r_pdf);
                         //   Intent sendIntent = new Intent();
                           // sendIntent.setAction(Intent.ACTION_SEND);

                            // sendIntent.setType("text/plain");


                           // Uri uri = Uri.fromFile(r_pdf);
                           // sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                           // sendIntent.setType("application/pdf");
                          //  startActivity(Intent.createChooser(sendIntent, "Share Report"));
                            shareImage(r_pdf);
                            // Code for Below 23 API Oriented Device
                            // Do next code
                        }

                    }catch (Exception e) {}



                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }
            break;
        }
    }

    // Set PDF document Properties
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


        // Start New Paragraph
        Paragraph prHead = new Paragraph();
        // Set Font in this Paragraph
        prHead.setFont(titleFont);
        // Add item into Paragraph
        image_logo.scalePercent(25);
        image_logo.setAbsolutePosition(140,782);
        prHead.add(image_logo);
        prHead.add("Bio Feedback Report\n");

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

        // Now Start another New Paragraph
       /* Paragraph prPersinalInfo = new Paragraph();
        prPersinalInfo.setFont(smallBold);
        prPersinalInfo.setSpacingBefore(10);



        prPersinalInfo.add("Hospital Name :"+hname+"\n");
        prPersinalInfo.add("Doctor Name :"+Doctor+"\n");
        prPersinalInfo.add("Patient Name :"+patient+"\n");
        prPersinalInfo.add("Age :"+Age+"\n");
        prPersinalInfo.add("Remarks :"+rm+"\n");


        prPersinalInfo.setAlignment(Element.ALIGN_LEFT);

        document.add(prPersinalInfo);*/
        document.add(myTable);

        document.add(myTable);

        Paragraph prProfile = new Paragraph();
        prProfile.setFont(catFont);
        prProfile.add("\n Rest Mode : \n ");

        document.add(prProfile);

        PdfPTable t = new PdfPTable(2);
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

        PdfPCell c3 = new PdfPCell(new Phrase(String.valueOf(ary[0])+" mmHg"));
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        t.addCell(c3);

        PdfPCell c4 = new PdfPCell(new Phrase(String.valueOf(ary[1])+" mmHg"));
        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
        t.addCell(c4);

        document.add(t);
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
        sqz_cell.setMinimumHeight(50);
        sqz_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        sqz_cell.addElement(prProfile2);
        sqz_table.addCell(sqz_cell);
        document.add(sqz_table);


        Paragraph prProfile3 = new Paragraph();
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
        rlx_cell.setMinimumHeight(50);
        rlx_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        rlx_cell.addElement(prProfile4);
        rlx_table.addCell(rlx_cell);
        document.add(rlx_table);

        //document.newPage();

        PdfPTable pic_table = new PdfPTable(1);
        pic_table.setWidthPercentage(100);
        pic_table.setSpacingBefore(5);
        pic_table.setSpacingAfter(10);
        pic_table.setHorizontalAlignment(Element.ALIGN_LEFT);


        Paragraph prProfile5 = new Paragraph();
        prProfile5.setFont(catFont);
        prProfile5.add("\n Best Trials : \n");
        //document.add(prProfile5);
        PdfPCell p1= new PdfPCell(prProfile5);
        p1.setBorder(Rectangle.NO_BORDER);
        p1.setMinimumHeight(50);
        pic_table.addCell(p1);

        Paragraph prProfile6 = new Paragraph();
        prProfile6.setAlignment(Element.ALIGN_CENTER);
        prProfile6.setFont(smallBold);


        image1.scalePercent(50);
        //prProfile6.add(image1);
        prProfile6.add("Best Mean Time");
        //prProfile6.setSpacingAfter(30);
        //document.add(prProfile6);
        pic_table.addCell(image1);
        PdfPCell p2= new PdfPCell(prProfile6);
        p2.setBorder(Rectangle.NO_BORDER);
        p2.setHorizontalAlignment(Element.ALIGN_CENTER);
        p2.setMinimumHeight(50);
        pic_table.addCell(p2);

        Paragraph prProfile7 = new Paragraph();
        prProfile7.setAlignment(Element.ALIGN_CENTER);
        prProfile7.setFont(smallBold);


        image2.scalePercent(50);


        //prProfile7.add(image2);
        prProfile7.add("Best Maximum Pressure");
        //prProfile7.setSpacingAfter(30);
        //document.add(prProfile7);
        pic_table.addCell(image2);
        PdfPCell p3= new PdfPCell(prProfile7);
        p3.setBorder(Rectangle.NO_BORDER);
        p3.setHorizontalAlignment(Element.ALIGN_CENTER);
        p3.setMinimumHeight(50);
        pic_table.addCell(p3);

        document.add(pic_table);
        document.newPage();
    }


    private void splitImage(Bitmap bitmap, int chunkNumbers) {

        //For the number of rows and columns of the grid to be displayed
        int rows,cols;

        //For height and width of the small image chunks
        int chunkHeight,chunkWidth;

        //To store all the small image chunks in bitmap format in this list
        chunkedImages = new ArrayList<Bitmap>(chunkNumbers);

        //Getting the scaled bitmap of the source image
        //BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        //Bitmap bitmap = drawable.getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        //rows = cols = (int) Math.sqrt(chunkNumbers);
        cols=1;
        rows=chunkNumbers;
        chunkHeight = bitmap.getHeight()/rows;
        chunkWidth = bitmap.getWidth()/cols;

        //xCoord and yCoord are the pixel positions of the image chunks
        int yCoord = 0;
        for(int x=0; x<rows; x++){
            int xCoord = 0;
            for(int y=0; y<cols; y++){
                chunkedImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
                xCoord += chunkWidth;
            }
            yCoord += chunkHeight;
        }
        yCoord = 0;
        /* Now the chunkedImages has all the small image chunks in the form of Bitmap class.
         * You can do what ever you want with this chunkedImages as per your requirement.
         * I pass it to a new Activity to show all small chunks in a grid for demo.
         * You can get the source code of this activity from my Google Drive Account.
         */

        //Start a new activity to show these chunks into a grid

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

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Bio Feedback Report");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Report"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getBaseContext(), "No App Available", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(full_bio_report.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(full_bio_report.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(full_bio_report.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(full_bio_report.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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
            document.setPageSize(image);
            document.newPage();
            image.setAbsolutePosition(5, 0);


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
        Intent it = new Intent(full_bio_report.this, Splash.class);
        startActivity(it);
        finish();
    }
}
