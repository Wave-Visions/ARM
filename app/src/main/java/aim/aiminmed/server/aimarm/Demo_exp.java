package aim.aiminmed.server.aimarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by server on 21/11/2016.
 */
public class Demo_exp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_exp);
        setTitle("Experiment or Demo");
        Button button_next = (Button)findViewById(R.id.exp);
        Button button_demo = (Button)findViewById(R.id.dem);


        button_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent ite = new Intent(Demo_exp.this,MainActivity_1.class);
                startActivity(ite);
            }
        });
        button_demo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent ite = new Intent(Demo_exp.this,Demo_intro.class);
                startActivity(ite);
            }
        });
        Intent ite = getIntent();

    }
}
