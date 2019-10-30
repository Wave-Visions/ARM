package aim.aiminmed.server.aimarm;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by server on 23/09/2016.
 */

public class register extends AppCompatActivity {
    private ArmDataBase ArmDataBaseObject = new ArmDataBase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final EditText Docc = (EditText) findViewById(R.id.dName);
        final EditText hosp = (EditText) findViewById(R.id.hName);
        final EditText email = (EditText) findViewById(R.id.eEmail);
        final EditText number = (EditText) findViewById(R.id.cNumber);
        Docc.setText("Dr. ");
        Docc.setSelection(Docc.getText().length());
        //Docc.setSingleLine(true);
       hosp.setSingleLine(true);
       email.setSingleLine(true);
       number.setSingleLine(true);


        Button button_submit = (Button) findViewById(R.id.bRegister);
        button_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ArmDataBaseObject.open();
                ContentValues cv = new ContentValues();
                cv.put(ArmDataBase.DNAME, Docc.getText().toString());
                cv.put(ArmDataBase.HNAME, hosp.getText().toString());
                cv.put(ArmDataBase.EMAIL, email.getText().toString());
                cv.put(ArmDataBase.CONTACT, number.getText().toString());
                ArmDataBaseObject.insert(ArmDataBase.DATABASE_DOCTOR_D,cv);
                ArmDataBaseObject.close();
                //Doc.setText("");
                //Doc.setSingleLine(true);
                Intent it = new Intent(register.this, Patient_Details.class);
                startActivity(it);
                finish();
            }
        });
    }
}
