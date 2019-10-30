package aim.aiminmed.server.aimarm;

import android.app.Application;
import android.content.ContentValues;


public class pass_database extends Application{

    public static  ArmDataBase ArmDataBaseObject;
    public static   ContentValues cv;


    public static ArmDataBase get_database() {

        return ArmDataBaseObject;
    }



    public static void set_database(ArmDataBase abase) {

        ArmDataBaseObject = abase;

    }

    public static ContentValues get_cv() {

        return cv;
    }



    public static void set_cv(ContentValues aCv) {

        cv = aCv;

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

}