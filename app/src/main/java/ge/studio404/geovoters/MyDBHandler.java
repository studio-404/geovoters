package ge.studio404.geovoters;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

public class MyDBHandler extends SQLiteOpenHelper{


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String geovote_users = "CREATE TABLE IF NOT EXISTS geovote_users ( id INTEGER PRIMARY KEY AUTOINCREMENT, registered_date TEXT, registered_ip TEXT, username TEXT, namelname TEXT, email TEXT, mobile TEXT, gender TEXT, address TEXT );";
        db.execSQL(geovote_users);
        String geovote_cataloglist = "CREATE TABLE IF NOT EXISTS geovote_cataloglist ( id INTEGER PRIMARY KEY AUTOINCREMENT, catalogidx INTEGER, catalogtitle TEXT );";
        db.execSQL(geovote_cataloglist);
//        String geovote_itemlists = "CREATE TABLE IF NOT EXISTS geovote_itemlists ( id INTEGER PRIMARY KEY AUTOINCREMENT, idx INTEGET, catidx INTEGER, date INTEGER, question TEXT, usersin INTEGER );";
//        db.execSQL(geovote_itemlists);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS geovote_users");
        onCreate(db);
    }


    // add user if not exists
    public void addUser(String username){
        ContentValues values = new ContentValues();
        values.put("username", username);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("geovote_users", null, values);
        db.close();
    }

    public boolean checkuser(String username){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM geovote_users WHERE username='"+username+"'";
        Cursor c = db.rawQuery(query, null);
        int cnt = c.getCount();
        db.close();
        c.close();
        return cnt > 0;
    }

    public String[] selectUserData(){
        String[] outText = new String[4];
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM geovote_users";
        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()){
            outText[0] = c.getString(c.getColumnIndex("username"));
            outText[1] = c.getString(c.getColumnIndex("namelname"));
            outText[2] = c.getString(c.getColumnIndex("email"));
            outText[3] = c.getString(c.getColumnIndex("gender"));
        }
        db.close();
        c.close();
        return outText;
    }

    public Integer selectCataloglistIdx(Integer tabid){
        int out = 0;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT catalogidx FROM geovote_cataloglist ORDER BY id ASC LIMIT "+tabid+",1";
        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()){
            out = c.getInt(c.getColumnIndex("catalogidx"));
        }
        db.close();
        c.close();

        return out;
    }

    public void updateProfile(String namelname, String email, String gender){
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE geovote_users SET namelname='"+namelname+"', email='"+email+"', gender='"+gender+"' ";
        db.execSQL(query);

    }

    public String[] selectCatalogList(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT catalogtitle FROM geovote_cataloglist ORDER BY id ASC", null);
        int cnt = c.getCount();
        String[] column1 = new String[cnt];
        if(c.moveToFirst()){
            int o = 0;
            do{
                column1[o] = c.getString(0);
                o++;
            }while(c.moveToNext());
        }
        c.close();
        db.close();
        return column1;
    }

    public void insertCatalogList(int idx, String arrayl){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id FROM geovote_cataloglist WHERE catalogidx='" + idx + "'", null);
        int cnt = c.getCount();
        if(cnt<=0) {
            ContentValues values = new ContentValues();
            values.put("catalogidx", idx);
            values.put("catalogtitle", arrayl);
            db.insert("geovote_cataloglist", null, values);
            db.close();
        }
        c.close();
        db.close();
    }

    public void clearCatalogList(){
       SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM geovote_cataloglist WHERE 1";
        db.execSQL(query);
    }

}
