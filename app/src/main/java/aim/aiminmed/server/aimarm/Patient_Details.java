package aim.aiminmed.server.aimarm;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import info.hoang8f.widget.FButton;

public class Patient_Details extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    private ArmDataBase ArmDataBaseObject = new ArmDataBase(this);
    String[] gender = { "Male", "Female", "Other"};
    Spinner spin;
    String spin_value = "Male";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_detailsxml);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setTitle("Enter Patient's Detail");
        Intent ite = getIntent();
        final TextView Doc = (TextView) findViewById(R.id.doc_txt);
        final EditText pet = (EditText)findViewById(R.id.patient_txt);
        final EditText age = (EditText)findViewById(R.id.age_txt);
        final EditText history = (EditText)findViewById(R.id.history_txt);
        final EditText remark = (EditText)findViewById(R.id.rem_txt);
        final EditText ext1 = (EditText)findViewById(R.id.ext_txt1);
        final EditText ext2 = (EditText)findViewById(R.id.ext_txt2);
        //Doc.setText("Dr. ");
      // Doc.setSelection(Doc.getText().length());

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        spin = (Spinner) findViewById(R.id.spinner_gender);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,gender   );
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        ArmDataBaseObject.open();
        //Cursor cursorData = ArmDataBaseObject.H_getDocDetails(ArmDataBase.DNAME);
        Cursor cursorData = ArmDataBaseObject.H_DocDetails ();
        while (cursorData.moveToNext()) {
            String Doctor = cursorData.getString(cursorData.getColumnIndex(ArmDataBase.DNAME));
            String pat = cursorData.getString(cursorData.getColumnIndex(ArmDataBase.HNAME));

           Doc.setText(Doctor);
           // pet.setText(pat);
        }
        ArmDataBaseObject.close();
        FButton button_submit = (FButton)findViewById(R.id.button_submit);
        button_submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                Toast.makeText(getBaseContext(), "Data Saved!" , Toast.LENGTH_SHORT ).show();

                ArmDataBaseObject.open();
                ContentValues cv = new ContentValues();
                cv.put(ArmDataBase.DOCTOR_NAME,Doc.getText().toString());
                cv.put(ArmDataBase.PATITENT_NAME,pet.getText().toString());
                cv.put(ArmDataBase.GENDER,spin_value);
                cv.put(ArmDataBase.AGE,age.getText().toString());
                cv.put(ArmDataBase.HISTORY,history.getText().toString());
                cv.put(ArmDataBase.REMARKS,remark.getText().toString());
                cv.put(ArmDataBase.EXTRA_FIELD1,ext1.getText().toString());
                cv.put(ArmDataBase.EXTRA_FIELD2,ext2.getText().toString());
                ArmDataBaseObject.insert(ArmDataBase.P_DATABASE_PATITENT_D,cv);

                ArmDataBaseObject.close();
                Doc.setText("");
                Intent it = new Intent(Patient_Details.this, time_select.class);
                startActivity(it);
                //finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(Patient_Details.this, Splash.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(getApplicationContext(),gender[i] , Toast.LENGTH_LONG).show();
        spin_value = gender[i];

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}


