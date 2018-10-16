package ng.com.easyworld.easyworld;

/**
 * Created by Oisrael on 8/10/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

    public class DbHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "EasyWorlddb.db";
        public static final String TABLE_NAME = "TrafficTbl";

        public static final String MSG_ID = "msgid";
        public static final String LOCATION = "location";
        public static final String VERYFREE = "veryfree";
        public static final String MOVINGLOWLY = "movingslowly";
        public static final String STANDSTILL = "standstill";

        Context cx = null;

        private HashMap hp;

        public DbHelper(Context context) {
            super(context, DATABASE_NAME , null, 2);

            cx=context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(
                    "create table TrafficTbl " +
                            "( msgid text,location text,veryfree text, movingslowly text,standstill text)"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS TrafficTbl");
            onCreate(db);
        }

        public boolean insertTrffic (String msgid, String location, String veryfree, String movingslowly,String standstill) {

            try{
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("msgid", msgid);
                contentValues.put("location", location);
                contentValues.put("veryfree", veryfree);
                contentValues.put("movingslowly", movingslowly);
                contentValues.put("standstill", standstill);
                db.insert("TrafficTbl", null, contentValues);
                return true;
            }catch(Exception errr){

            }


           return false;
        }

        public Cursor getData(String location) {

            Cursor res=null;
            try{
                SQLiteDatabase db = this.getReadableDatabase();
                 res =  db.rawQuery( "select * from TrafficTbl where location="+location+"", null );
            }catch (Exception ee){
                ee.printStackTrace();
            }
            return res;
        }


        public int getVeryFree(String msgid) {
            int veryFree = 0;
            Cursor cursor=null;
            try{
                SQLiteDatabase db = this.getReadableDatabase();
                cursor =  db.rawQuery( "select veryfree from TrafficTbl where msgid="+msgid+"", null );
            }catch (Exception ee){
                ee.printStackTrace();
            }

            if(cursor!=null){
                if(cursor.moveToFirst()){

                    do{

                        veryFree = cursor.getInt(cursor.getColumnIndex("veryfree"));

                    }while(cursor.moveToNext());

                }

            }
            return veryFree;
        }

        public int getMovingSlowly(String msgid) {
            int movingslowly = 0;
            Cursor cursor=null;
            try{
                SQLiteDatabase db = this.getReadableDatabase();
                cursor =  db.rawQuery( "select movingslowly from TrafficTbl where msgid="+msgid+"", null );
            }catch (Exception ee){
                ee.printStackTrace();
            }

            if(cursor!=null){
                if(cursor.moveToFirst()){

                    do{

                        movingslowly = cursor.getInt(cursor.getColumnIndex("movingslowly"));

                    }while(cursor.moveToNext());

                }

            }
            return movingslowly;
        }


        public int getStandstill(String msgid) {
            int standstill = 0;
            Cursor cursor=null;
            try{
                SQLiteDatabase db = this.getReadableDatabase();
                cursor =  db.rawQuery( "select standstill from TrafficTbl where msgid="+msgid+"", null );
            }catch (Exception ee){
                ee.printStackTrace();
            }

            if(cursor!=null){
                if(cursor.moveToFirst()){

                    do{

                        standstill = cursor.getInt(cursor.getColumnIndex("standstill"));

                    }while(cursor.moveToNext());

                }

            }
            return standstill;
        }

        public ArrayList<TrafficQueryData> getAllData() {
            Cursor cursor=null;
           ArrayList<TrafficQueryData> list = new ArrayList<>();
            try{
                String location = null;
                SQLiteDatabase db = this.getReadableDatabase();
                // res =  db.rawQuery( "select location,veryfree,movingslowly,standstill from TrafficTb", null );

                cursor =  db.query( "TrafficTbl",new String[]{"msgid","location","veryfree","movingslowly","standstill"},null,null,null,null,null );


                if(cursor!=null){
                    if(cursor.moveToFirst()){

                        do{
                            String msgid = cursor.getString(cursor.getColumnIndex("msgid"));
                            String locationName = cursor.getString(cursor.getColumnIndex("location"));
                            String veryFast = cursor.getString(cursor.getColumnIndex("veryfree"));
                            String movingSlowly = cursor.getString(cursor.getColumnIndex("movingslowly"));
                            String standStill = cursor.getString(cursor.getColumnIndex("standstill"));

                            list.add(new TrafficQueryData(msgid,locationName,veryFast,movingSlowly,standStill));

                        }while(cursor.moveToNext());


                    }

                }


            }catch (Exception ee){
                ee.printStackTrace();
            }

            return list;
        }


        public boolean updateVeryFree (String msgid, String val) {

            try{
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("veryfree", val);

                db.updateWithOnConflict("TrafficTbl", contentValues, "msgid = " + msgid, null,1);
                return true;
            }catch (Exception ee){

                ee.printStackTrace();
            }


           return false;
        }

        public boolean updateMovingslowly (String msgid, String val) {

            try{
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("movingslowly", val);

                db.update("TrafficTbl", contentValues, "msgid = " + msgid, new String[] { msgid } );
                return true;
            }catch (Exception ee){
                ee.printStackTrace();
            }


            return false;
        }
        public boolean updateStandstill (String msgid, String val) {

            try{
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("standstill", val);

                db.update("TrafficTbl", contentValues, "msgid = " + msgid, new String[] { msgid } );
                return true;
            }catch (Exception ee){
                ee.printStackTrace();
            }


            return false;
        }


    }









