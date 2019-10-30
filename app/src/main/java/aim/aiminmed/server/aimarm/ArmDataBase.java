package aim.aiminmed.server.aimarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by server on 06/09/2016.
 */
public class ArmDataBase {

    /**
     * Database Opening count is Maintained
     */
    public static int dbOpenCount = 0;

    /**
     * Database Closing count is Maintained
     */
    public static int dbCloseCount = 0;

    /**
     * Object used for creating, insertion into Database object
     */
    private static SQLiteDatabase sqlitedb;
    private final Context context;                    // Context for passing in current classes constructor
    private DatabaseHelper DBHelperObject;            // DatabaseHelper object for getting getting object

    /**
     * Database Name Declaration
     */
    public static final String DATABASE_NAME = "ArmDataBase.db";

    /**
     * Database Version Number
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Inner class object created for synchronizing when RLRDataStorage object is Created
     */
    private static DatabaseHelper iDbHelper;

    // Devices Table Fields


    public static final String DATABASE_MAIN_DATA = "MainData";

    public static final String DOCTOR_NAME = "Doc_name";

    public static final String DNAME = "Doctor_name";
    public static final String HNAME= "HOsp_name";
    public static final String EMAIL= "E_mail";
    public static final String CONTACT= "C_number";
    public static final String PIC_CODE = "Pincode";

    public static final String PATITENT_NAME = "patitent_name";
    public static final String GENDER = "gender";
    public static final String AGE = "age";

    public static final String P_DATABASE_PATITENT_D = "patient_Details";
    public static final String DATABASE_DOCTOR_D = "Doctor_Details";

    public static final String HISTORY = "history";
    public static final String REMARKS = "remark";
    public static final String EXTRA_FIELD1 = "extra_field1";
    public static final String EXTRA_FIELD2 = "extra_field2";
    public static final String EXTRA_FIELD3 = "extra_field3";


    /**
     * Database Object for Singleton
     */
    private static ArmDataBase instance;

    /**
     * Getting object of DatabaseHelper
     *
     * @param ctx
     */
    public ArmDataBase(Context ctx) {
        Log.i("RLRDataStorage", "Context");
        this.context = ctx;
        DBHelperObject = new DatabaseHelper(context);
        synchronized (DATABASE_NAME) {
            if (iDbHelper == null)
                iDbHelper = new DatabaseHelper(ctx);
        }
    }

    /**
     * Opens the database
     *
     * @return RLRDataStorage object
     * @throws SQLException
     */
    public ArmDataBase open() {
        try {
            dbOpenCount = dbOpenCount + 1;
            sqlitedb = DBHelperObject.getWritableDatabase();
            System.out.println("RLRDataStorage.open()" + dbOpenCount);
        } catch (SQLException ex) {
            sqlitedb = DBHelperObject.getReadableDatabase();
            Log.d("log_tag", "Exception is Thrown open get Readable");
            ex.printStackTrace();
        }
        return this;
    }

    /**
     * Closes the database
     *
     * @return RLRDataStorage object
     */
    public void close() {
        DBHelperObject.close();
        System.out.println("RLRDataStorage.close()" + dbCloseCount);
    }

    // Creating Devices table
    private static final String TABLE_DATA_COLLECTED = "CREATE TABLE "
            + DATABASE_DOCTOR_D + " ("
            + DNAME + " text,"
            + HNAME + " text,"
            + EMAIL + " text,"
            + CONTACT + " int);";



    private static final String P_TABLE_DATA_COLLECTED = "CREATE TABLE "
            + P_DATABASE_PATITENT_D + " ("
            + DOCTOR_NAME + " text,"
            + PATITENT_NAME + " text,"
            + GENDER + " text,"
            + AGE + " int,"
            + HISTORY + " text,"
            + REMARKS + " text,"
            + EXTRA_FIELD1 + " text,"
            + EXTRA_FIELD2 + " text,"
            + EXTRA_FIELD3 + " text);";

    // Creating Devices table

    /**
     * singleton BamDataStorage.
     *
     * @param context
     * @return BamDataStorage database instance.
     */
    public static ArmDataBase GetFor(Context context) {
        if (instance == null)
            instance = new ArmDataBase(context);
        // if (!instance.isOpen())
        // instance.open();
        return instance;
    }

    /**
     * Creation of Database Tables and Upgradation is done.
     *
     * @author Bhavin
     */
    public static class DatabaseHelper extends SQLiteOpenHelper {
        /**
         * Get records in a particular table according to sql query
         *
         * @param tablename
         * @return a cursor object pointed to the record(s) selected by sql query.
         */
        public synchronized static Cursor getRecordBySelection(String tablename) {
            return sqlitedb.query(tablename, null, null, null, null, null, null);
        }

        /**
         * Constructor created for DatabaseHelper
         *
         * @param context
         */
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //	Database Tables are created
        @Override
        public void onCreate(SQLiteDatabase db) {
            //db.execSQL(DATABASE_TEST);
            db.execSQL(TABLE_DATA_COLLECTED);
            db.execSQL(P_TABLE_DATA_COLLECTED);


        }

        // Database Upgradation is done
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("BAMDB", "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            //db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TEST);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_DOCTOR_D);
            db.execSQL("DROP TABLE IF EXISTS " + P_DATABASE_PATITENT_D);


            onCreate(db);
        }

    }
    public Cursor H_getDocDetails(String doc) {
        return sqlitedb.rawQuery("select * from "+DATABASE_DOCTOR_D+" where "+DNAME+" ='"+doc+"'", null);


    }
    public Cursor H_DocDetails (){
        return sqlitedb.rawQuery("select * from "+DATABASE_DOCTOR_D+"", null);


    }

    public Cursor H_PatDetails (){
        return sqlitedb.rawQuery("select * from "+P_DATABASE_PATITENT_D+"", null);


    }
    /**
     * Insert a record into particular table
     *
     * @param tablename
     * @param values
     * @returnthe row ID of the newly inserted row, or -1 if an error occurred
     */
    public synchronized long insert(String tablename, ContentValues values) {
        return sqlitedb.insert(tablename, null, values);
    }

    /**
     * Getting Profile Value from the Database
     *
     * @return cursor
     */
    public Cursor getSkillCodeQuestionId(String Value, int item) {
        return sqlitedb.rawQuery("select * from " + DATABASE_DOCTOR_D + " where option = '" + Value + "' and " + DNAME + " ='" + item + "'", null);

    }

    public void deleteAllData(String table) {
        sqlitedb.delete(table, null, null);
    }

    /**
     * Getting all the ProfileNames from the Profile Table
     *
     * @return cursor
     */
    public Cursor getTableData(String type) {
        return sqlitedb.rawQuery("select * from " + type + " ORDER BY Doctor_name ASC", null);
    }

    /**
     * Getting all the ProfileNames from the Profile Table
     *
     * @return cursor
     */
    public Cursor getTotalRows() {
        return sqlitedb.rawQuery("select * from " + DATABASE_MAIN_DATA, null);
    }

    /**
     * Setting Default Profile to set 0 from 1
     *
     * @return cursor
     */
    public int correctRadioButton(int skillCodeID, int score) {
        ContentValues args = new ContentValues();
        args.put(PIC_CODE, score);
        return sqlitedb.update(DATABASE_MAIN_DATA, args, DNAME + " ='" + skillCodeID + "'", null);
    }

    /**
     * Setting Default Profile to set 0 from 1
     *
     * @return cursor
     */
    public int savePatientDetails(ContentValues values, String doc) {
        return sqlitedb.update(DATABASE_DOCTOR_D, values, PATITENT_NAME + " ='" + doc + "'", null);
    }

    public int deletePatient(String doc) {
        return sqlitedb.delete(DATABASE_DOCTOR_D, PATITENT_NAME + " ='" + doc + "'", null);
    }

    public int updatePatient(ContentValues values, String doc) {
        return sqlitedb.update(DATABASE_DOCTOR_D, values, PATITENT_NAME + " ='" + doc + "'", null);
    }

    /**
     * Setting Default Profile to set 0 from 1
     *
     * @return cursor
     */
    public Cursor getPatientDetails(String doc) {
        return sqlitedb.rawQuery("select * from " + DATABASE_DOCTOR_D + " where " + PATITENT_NAME + " ='" + doc + "'", null);
    }


}
