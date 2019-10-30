package aim.aiminmed.server.aimarm;

import android.app.Application;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class pass_status extends Application{

    public static Socket socket_pass;
    public static PrintWriter out_pass;
    public static InputStream in_pass;
    public static int[] intArray = new int[14];
    public static int[] intArray2 = new int[6];
    public static char skip_t,sqz_c,cgh_c;
    public static int sec_val;
    public static String reading_s="";
    public static String reading_c="";
    public static String reading_r="";

    public static  char getskip() {

       return skip_t;
    }
    public static  void setskip(char temp) {

        skip_t=temp;
    }

    public static  char get_sqz() {

        return sqz_c;
    }
    public static  char get_cgh() {

        return cgh_c;
    }
    public static  void set_sqz_cgh(char temp,char temp1) {

        sqz_c=temp;
        cgh_c=temp1;
    }


    public static  void setsec(int temp) {

        sec_val=temp;
    }


    public static  int getsec() {

        return sec_val;
    }

    public static Socket getSocket() {

        return socket_pass;
    }

    public static PrintWriter getOut() {

        return out_pass;
    }

    public static void set_socket(Socket aSocket) {

        socket_pass = aSocket;

    }

    public static void set_out(PrintWriter aOut) {

        out_pass = aOut;

    }

    public static void set_stream(InputStream aStream) {

        in_pass = aStream;

    }

    public static InputStream get_stream() {

        return in_pass;
    }

    public static void set_data(int[]args) {
        for(char i=0;i<14;i++){
           intArray[i]=args[i];
        }
    }

    public static void get_data(int[]args) {
        for(char i=0;i<14;i++){
            args[i]=intArray[i];
        }
    }
    public static void set_data2(int[]args) {
        for(char i=0;i<6;i++){
            intArray2[i]=args[i];
        }
    }

    public static void get_data2(int[]args) {
        for(char i=0;i<6;i++){
            args[i]=intArray2[i];
        }
    }


    public static void set_reading(String s,String c,String r) {
        for(int i=0;i<s.length();i++)
        {
            reading_s+=s.charAt(i);
        }
        for(int i=0;i<c.length();i++)
        {
            reading_c+=c.charAt(i);
        }
        for(int i=0;i<r.length();i++)
        {
            reading_r+=r.charAt(i);
        }

    }

    public static void set_reading_s(String s) {
        for (int i = 0; i < s.length(); i++) {
            reading_s += s.charAt(i);
        }
    }
    public static void set_reading_r(String r) {
        for (int i = 0; i < r.length(); i++) {
            reading_r += r.charAt(i);
        }
    }
    public static void set_reading_c(String c) {
        for (int i = 0; i < c.length(); i++) {
            reading_c += c.charAt(i);
        }
    }


    public static void clear_reading() {
        reading_s=" ";
        reading_r=" ";
        reading_c=" ";
    }

    public static void set_reading_2(String s,String r) {
        for(int i=0;i<s.length();i++)
        {
            reading_s+=s.charAt(i);
        }

        for(int i=0;i<r.length();i++)
        {
            reading_r+=r.charAt(i);
        }

    }

    public static String get_reading_s() {

        return reading_s;

    }
    public static String get_reading_r() {

        return reading_r;

    }
    public static String get_reading_c() {

        return reading_c;

    }








    @Override
    public void onCreate() {
        super.onCreate();

    }

}