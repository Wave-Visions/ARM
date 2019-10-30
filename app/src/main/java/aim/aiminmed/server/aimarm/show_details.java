package aim.aiminmed.server.aimarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class show_details extends AppCompatActivity {
    private ArmDataBase ArmDataBaseObject = new ArmDataBase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detailsxml);
        final EditText Doc1 = (EditText)findViewById(R.id.doc_txt1);
        final EditText pet1 = (EditText)findViewById(R.id.patient_txt1);
        final EditText age1 = (EditText)findViewById(R.id.age_txt1);
        final EditText history1 = (EditText)findViewById(R.id.history_txt1);
        final EditText ext11 = (EditText)findViewById(R.id.ext_txt11);
        final EditText ext21 = (EditText)findViewById(R.id.ext_txt21);
        final EditText ext31 = (EditText)findViewById(R.id.ext_txt31);
        Button button_back = (Button)findViewById(R.id.button_back);

        button_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent ite = new Intent(show_details.this,current_ps.class);
                startActivity(ite);

            }
        });


    }
}