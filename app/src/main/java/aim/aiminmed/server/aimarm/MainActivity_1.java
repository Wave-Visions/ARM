package aim.aiminmed.server.aimarm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity_1 extends AppCompatActivity {
    TextView text;
    Button btn;
    InputStream is;
    PrintWriter out;
    BufferedReader br;
    Socket socket = null;
    String result=null;
    // pass_status status_esp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_1);
        setTitle(R.string.titile1);

        //status_esp = (pass_status) getApplicationContext();//Creating variable of paa_status class
        text = (TextView) findViewById(R.id.textView3);
        btn = (Button) findViewById(R.id.refresh);
        text.setText("Disconnected");
        new ClientAsyncTask().execute("192.168.4.1", "80", ":Connect;");
        btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View arg0) {


            }

        });


        Button button_next = (Button) findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent ite = new Intent(MainActivity_1.this, current_ps.class);
                startActivity(ite);
            }
        });


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
                pass_status.set_socket(socket);
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                pass_status.set_out(out);
                out.println(params[2]);
                out.flush(); //............Added by me

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            text.setText("Connected");
            read read_msg= new read();//Second AsyncTask to read received data from server
            read_msg.execute();
        }

    }

    /*  Second AsyncTask to read received data from server  */
    public class read extends AsyncTask<Void, Void, String> {
        byte[] buffer = new byte[32];
        Message serverMessage= Message.obtain();
        @Override
        protected String doInBackground(Void... params) {
            try{
                is = socket.getInputStream();
                is.read(buffer);
                result=new String(buffer);
                serverMessage.obj=result;
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
        //text.setText(servermessage);
        read read_msg= new read();//Again call Second AsyncTask to read next data from server
        read_msg.execute();
    }

}
