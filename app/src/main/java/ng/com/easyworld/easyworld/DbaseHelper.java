package ng.com.easyworld.easyworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Oisrael on 8/15/2018.
 */

public class DbaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "WorldEasy3.db";
    public static final String TABLE_NAME = "TrafficTb";

    public static final String MSG_ID = "msgid";
    public static final String LOCATION = "location";
    public static final String VERYFREE = "veryfree";
    public static final String MOVINGLOWLY = "movingslowly";
    public static final String STANDSTILL = "standstill";

    //WallPost Variable
    public static final String PID = "id";
    public static final String POSTID = "postid";
    public static final String POSTTITLE = "posttitle";
    public static final String IMAGEURL = "imageurl";
    public static final String VIDEOURL = "videourl";
    public static final String CONTENT = "content";
    public static final String EMAILADDRESS = "emailaddress";
    public static final String TIM = "tim";
    public static final String DAT = "dat";
    public static final String LONGI = "longi";
    public static final String LATI = "lati";
    public static final String PRIVACY = "privacy";
    public static final String LIKES = "likes";
    public static final String COMMENTS = "comment";
    public static final String DISLIKES = "dislikes";
    public static final String SENDERNAME = "sendername";
    public static final String PHOTO = "photo";

    Context cx = null;

    private HashMap hp;

    public DbaseHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);

        cx=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        //Traffic Management Table
        db.execSQL(
                "create table TrafficT " +
                        "(id integer primary key autoincrement,msgid text,location text,veryfree text, movingslowly text,standstill text)"
        );

        //WallPost Management Table
        db.execSQL(
                "create table WallPosttb " +
                        "(id integer primary key autoincrement,postid text,posttitle text,imageurl text, videourl text,content text,emailaddress text,tim text,dat text,longi text,lati text,privacy text,likes text,comment text,dislikes text,sendername text,photo text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS TrafficTbl");
        db.execSQL("DROP TABLE IF EXISTS WallPosttb");
        onCreate(db);
    }

    public boolean insertTrffic (String msgid, String location, String veryfree, String movingslowly,String standstill) {

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor  res =  db.rawQuery( "select * from TrafficT ", null );

            int count = res.getCount();

            if(count>=10){
                db.rawQuery( "delete from TrafficT where id>10", null );

            }

            ContentValues contentValues = new ContentValues();
            contentValues.put("msgid", msgid);
            contentValues.put("location", location);
            contentValues.put("veryfree", veryfree);
            contentValues.put("movingslowly", movingslowly);
            contentValues.put("standstill", standstill);
            db.insert("TrafficT", null, contentValues);
            return true;
        }catch(Exception errr){

            errr.printStackTrace();
        }


        return false;
    }

    public Cursor getData(String location) {

        Cursor res=null;
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            res =  db.rawQuery( "select * from TrafficT where location="+location+"", null );
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
            cursor =  db.rawQuery( "select veryfree from TrafficT where msgid="+msgid+"", null );
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
            cursor =  db.rawQuery( "select movingslowly from TrafficT where msgid="+msgid+"", null );
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
            cursor =  db.rawQuery( "select standstill from TrafficT where msgid="+msgid+"", null );
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

            cursor =  db.query( "TrafficT",new String[]{"msgid","location","veryfree","movingslowly","standstill"},null,null,null,null,"id DESC" );


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

            db.update("TrafficT", contentValues, "msgid = ?",new String[] { msgid });
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

            db.update("TrafficT", contentValues, "msgid = ?",new String[] { msgid });
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

            db.update("TrafficT", contentValues, "msgid = ?",new String[] { msgid });
            return true;
        }catch (Exception ee){
            ee.printStackTrace();
        }


        return false;
    }


    //**********************************WALLPOST METHODS******************************************
    public long insertWallPost (String postId, String postTitle, String imageUrl, String videoUrl,String content,
                                   String emailAddress, String tim, String dat, String longi,String lati,
                                   String privacy, String likes, String commemts, String dislikes,String senderName,String photo) {

        long id = 0;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("postId", postId);
            contentValues.put("postTitle", postTitle);
            contentValues.put("imageUrl", imageUrl);
            contentValues.put("videoUrl", videoUrl);
            contentValues.put("content", content);
            contentValues.put("emailAddress", emailAddress);
            contentValues.put("tim", tim);
            contentValues.put("dat", dat);
            contentValues.put("longi", longi);
            contentValues.put("lati", lati);
            contentValues.put("privacy", privacy);
            contentValues.put("likes", likes);
            contentValues.put("comment", commemts);
            contentValues.put("dislikes", dislikes);
            contentValues.put("senderName", senderName);
            contentValues.put("photo", photo);

          id =  db.insert("WallPosttb", null, contentValues);

                return id;
        }catch(Exception errr){

            errr.printStackTrace();
        }


        return id;
    }

    public ArrayList<EasyFeedMessage> getAllWallPost() {
        Cursor cursor=null;
        ArrayList<EasyFeedMessage> list = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();

            cursor =  db.query( "WallPosttb",new String[]{"postId","postTitle","imageurl","videourl","content","emailaddress","tim","dat","longi","lati","privacy","likes","comment","dislikes","sendername","photo"},null,null,null,null,"id DESC" );


            if(cursor!=null){
                if(cursor.moveToFirst()){

                    do{
                        String postId = cursor.getString(cursor.getColumnIndex("postid"));
                        String postTitle = cursor.getString(cursor.getColumnIndex("posttitle"));
                        String imageurl = cursor.getString(cursor.getColumnIndex("imageurl"));
                        String videourl = cursor.getString(cursor.getColumnIndex("videourl"));
                        String content = cursor.getString(cursor.getColumnIndex("content"));
                        String emailaddress = cursor.getString(cursor.getColumnIndex("emailaddress"));
                        String tim = cursor.getString(cursor.getColumnIndex("tim"));
                        String dat = cursor.getString(cursor.getColumnIndex("dat"));
                        String longi = cursor.getString(cursor.getColumnIndex("longi"));
                        String lati = cursor.getString(cursor.getColumnIndex("lati"));
                        String privacy = cursor.getString(cursor.getColumnIndex("privacy"));
                        String likes = cursor.getString(cursor.getColumnIndex("likes"));
                        String comments = cursor.getString(cursor.getColumnIndex("comment"));
                        String dislikes = cursor.getString(cursor.getColumnIndex("dislikes"));
                        String sendername = cursor.getString(cursor.getColumnIndex("sendername"));
                        String photo = cursor.getString(cursor.getColumnIndex("photo"));

                        list.add(new EasyFeedMessage (postId,postTitle,imageurl,videourl,content,emailaddress,tim,
                                dat,longi,lati,privacy,likes,comments,dislikes,sendername,photo));

                        String dommy = "";

                    }while(cursor.moveToNext());


                }

            }


        }catch (Exception ee){
            ee.printStackTrace();
        }

        return list;
    }


    public DateAndTime getLastItemTime(){

        String timee="";
        String datee="";
        //Return the time the last item was inserted
        Cursor cursor=null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            cursor =  db.rawQuery( "select tim,dat from WallPosttb order by id Desc ", null );
            if(cursor!=null){
                if(cursor.moveToFirst()){

                    do{

                        timee = cursor.getString(cursor.getColumnIndex("tim"));

                        datee = cursor.getString(cursor.getColumnIndex("dat"));
                    }while(cursor.moveToNext());


                }

            }
        }
        catch(Exception exx){

            exx.printStackTrace();
        }

        return new DateAndTime(datee,timee);
    }



    //*******************************8**WALLPOST METHODS END**************************************


}
