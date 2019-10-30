package aim.aiminmed.server.aimarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import info.hoang8f.widget.FButton;

public class time_select extends AppCompatActivity implements View.OnClickListener {

    private FButton biofeed,tonometry;
    private EditText graph_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_select);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        biofeed =(FButton)findViewById(R.id.nxt);
        tonometry =(FButton)findViewById(R.id.nxt2);
        //graph_time =(EditText) findViewById(R.id.graph_time);

        biofeed.setOnClickListener(this);
        tonometry.setOnClickListener(this);
}



    @Override
    public void onClick(View view) {

        switch(view.getId()) {

            case R.id.nxt:

                //pass_status.setsec(Integer.parseInt(graph_time.getText().toString()));
                Intent it = new Intent(time_select.this, biofeed_init.class);
                startActivity(it);
                finish();
                break;

            case R.id.nxt2:

                //pass_status.setsec(Integer.parseInt(graph_time.getText().toString()));
                Intent it2 = new Intent(time_select.this, mp_graph_init.class);
                startActivity(it2);
                finish();
                break;

        }
    }
}
