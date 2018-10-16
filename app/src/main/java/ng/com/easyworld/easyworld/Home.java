package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import microsoft.aspnet.signalr.client.hubs.HubProxy;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , SensorEventListener {


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private MainActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private FloatingActionButton fab;

    static final String URL = "http://www.easyworld.com.ng/ewc.asmx/getbc";
    ArrayList<EasyUser> easyUsers;
    ListView listView;
    private static EasyAdapter adapter;
    private TextView txtName, txtPhone,txtEmail;
    private ImageButton imgPhoto;
    private  boolean fabFlag=false;
    private ImageButton fabFrag;
    NavigationView navigationView;
    private ViewStub viewStub;
    private String longi;
    private TextView longiText ;
    SharedPreferences userSession;
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    private DrawerLayout drawer ;


    //username
    private String username;

    //Connected users
    private String connectedUser;

    //Webrtc Connection
    private String yourConn;


    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_PROFFESSIONALS = "profffessionals";
    private static final String TAG_FRIENDS = "friends";
    private static final String TAG_FRIENDS_ALARM = "friendsalarm";
    private static final String TAG_EVENT = "event";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG =null;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    private Handler mHandler;
    private TextView errTxt;

    private TabLayout tabLayout;
    boolean  mBounded;
    EasyService mEasyService;
    ServiceConnection mConnection;
    private int[] tabIcons = {
            R.mipmap.connect,
            R.mipmap.newsfeed,
            R.mipmap.chat,
            R.mipmap.notification,
            R.mipmap.posters,
            R.mipmap.business_card
    };


    private TextView VeryFreeVal;
    private TextView MovingSlowlyVal;
    private TextView StandstillVal;
    private EditText locationNameTxt;
    private LinearLayout cotainerLocatnName;

    private PrivateMessage loginData;
    private static String Caller;
    private static String pw;

    private String host;
    private HubProxy hub;
    private TrafficPrivateMessage trafficPm;

    private DbaseHelper db ;

    //Sensor
    private SensorManager sensorManager;
    private boolean color = false;
    private View view;
    private long lastUpdate;
    private CountDownTimer myTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easyhome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


       // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);


        setSupportActionBar(toolbar);

        ActionBar acb = getSupportActionBar();
        // acb.setIcon(R.drawable.send);
        // acb.setHomeButtonEnabled(true);
        acb.setDisplayHomeAsUpEnabled(true);




        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new MainActivity.SectionsPagerAdapter(getSupportFragmentManager());



        tabLayout = (TabLayout) findViewById(R.id.tabs);



        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);



        tabLayout.setupWithViewPager(mViewPager);

        setupTabIcons();
        setToolbarTitle();



        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                long currentTab = mSectionsPagerAdapter.getItemId(mViewPager.getCurrentItem());
                if (currentTab>0){

                }
                return false;
            }
        });

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){


            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);

                loadNavHeader();

            }


        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // load nav menu header data


        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;


        }

        //SENSOR----------------------------------------------------
       // sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
       // lastUpdate = System.currentTimeMillis();
        //----------------------------------------------------------


        if(isOnline()){
            if(!isMyServiceRunning(EasyService.class)){

                try{
                    myTimer =  new CountDownTimer(10000, 10000) {

                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {



                            try{

                                startService(new Intent(Home.this, EasyService.class));
                                Log.d("Alarm","Finished");


                            }catch(Exception e){

                            }



                        }
                    }.start();
                }catch (Exception ee){
                    Log.d("Alarm","Failed");
                    ee.printStackTrace();
                }



            }

        }




    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
        tabLayout.getTabAt(5).setIcon(tabIcons[5]);


        //Set the titles to nothing
        tabLayout.getTabAt(0).setText("");
        tabLayout.getTabAt(1).setText("");
        tabLayout.getTabAt(2).setText("");
        tabLayout.getTabAt(3).setText("");
        tabLayout.getTabAt(4).setText("");
        tabLayout.getTabAt(5).setText("");

    }

    //START THE SERVICE IN THE ONSTART EVENT-------------------------------------------
    @Override
    protected void onStart() {
        super.onStart();
        //If service is running bind to it












    }

    protected void onResume() {
    super.onResume();


        if(isOnline()) {
            //If service is running bind to it
            if (isMyServiceRunning(EasyService.class)) {

                StartServiceBind();

            } else {//start the service

                startService(new Intent(Home.this, EasyService.class));

            }
        }

       // sensorManager.registerListener(this,
               // sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
               // SensorManager.SENSOR_DELAY_NORMAL);


    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
       // sensorManager.unregisterListener(this);
    }


    private void loadNavHeader() {

        //Find the controls

        String emailPref = null ;
        String pwPref = null ;
        String phonePref = null ;
        String usernamePref = null ;
        String photoPref = null ;
        String FirstnamePref = null ;
        String LastnamePref = null ;

        try {
            userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);

            Map<String, ?> allPreferences = userSession.getAll();

            if (!allPreferences.isEmpty()){
                emailPref = userSession.getString("Email", null);
                pwPref = userSession.getString("Pw", null);
                phonePref = userSession.getString("Phone", null);
                usernamePref = userSession.getString("Username", null);
                FirstnamePref = userSession.getString("Firstname", null);
                LastnamePref = userSession.getString("Lastname", null);
                photoPref = userSession.getString("Photo", null);

            }

        }
        catch(Exception e){
            String err = e.toString();

        }





        // name, website
        txtName = (TextView) findViewById(R.id.DrawTxtname);
        txtName.setText(FirstnamePref);

        txtPhone = (TextView) findViewById(R.id.DrawTxtPhone);
        txtPhone.setText(phonePref);

        txtEmail = (TextView) findViewById(R.id.DrawTextEmail);
        txtEmail.setText(emailPref);


        imgPhoto = (ImageButton) findViewById(R.id.DrawerProfileImage);

        String  uri =null;

            uri = "https://www.easyworld.com.ng/images/" + photoPref;

        ImageLoader imageLoader = new ImageLoader(getBaseContext());
        imageLoader.DisplayImage(uri, imgPhoto,"", null);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }




    private void setUpNavigationView() {



    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title


        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            toggleFV();
            return;
        }

        // update the main content by replacing fragments
        Fragment fragment = getHomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();







        //Closing drawer on item click
        drawer.closeDrawers();

        TabLayout tabb = (TabLayout) findViewById(R.id.tabs);


        if (navItemIndex == 0){

            tabb.setVisibility(View.VISIBLE);

        }
        else{
            tabb.setVisibility(View.INVISIBLE);
        }



        // refresh toolbar menu
        invalidateOptionsMenu();


    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:


            case 1:

            case 2:

            case 3:

            case 4:

            case 5:





            default:
                return null;
        }
    }

    private void setToolbarTitle() {

        // getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id==R.id.action_logout){


            userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = userSession.edit();
            editor.clear();
            editor.commit();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            //Replacing the main content with ContentFragment Which is our Inbox View;

            case R.id.nav_profile:
                // launch new intent instead of loading fragment
                startActivity(new Intent(Home.this, ProfileActivity.class));
                drawer.closeDrawers();
                return true;


            case R.id.nav_proffessionals:
                // launch new intent instead of loading fragment
                startActivity(new Intent(Home.this, NearByProfActivity.class));
                drawer.closeDrawers();
                return true;

            case R.id.nav_Friends:
                // launch new intent instead of loading fragment
                startActivity(new Intent(Home.this, FriendsActivity.class));
                drawer.closeDrawers();
                return true;

            case R.id.nav_Friends_Alarm:
                // launch new intent instead of loading fragment
                startActivity(new Intent(Home.this, FriendsAlarmActivity.class));
                drawer.closeDrawers();
                return true;

            case R.id.nav_Nearest_Event:
                // launch new intent instead of loading fragment
                startActivity(new Intent(Home.this, NearByEventActivity.class));
                drawer.closeDrawers();
                return true;



            case R.id.nav_Settings:
                // launch new intent instead of loading fragment
                startActivity(new Intent(Home.this, SettinActivity.class));
                drawer.closeDrawers();
                return true;

            case R.id.About_us:
                // launch new intent instead of loading fragment
                startActivity(new Intent(Home.this, AboutUs.class));
                drawer.closeDrawers();
                return true;
            case R.id.privacy_policy:
                // launch new intent instead of loading fragment
                startActivity(new Intent(Home.this, PrivacyPolicy.class));
                drawer.closeDrawers();
                return true;



            default:
                navItemIndex = 0;


        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);




        return true;
    }

    public void fabFragClicked (View v){

        viewStub = (ViewStub) findViewById(R.id.viewStub);

        Animation hidefabbtn = AnimationUtils.loadAnimation(  getBaseContext(), R.anim.hidefabbtn);

        fabFrag=(ImageButton) findViewById(R.id.fabbtn);


        viewStub.setVisibility(View.VISIBLE);


        fabFrag.startAnimation(hidefabbtn);
        fabFrag.setVisibility(View.INVISIBLE);



        GetObjectDataLst getObjDataList = new GetObjectDataLst();

        listView=(ListView)findViewById(R.id.ConnectList);


        GetFeedFromUrl getfeed = new GetFeedFromUrl();


        try {
            FeedObject FD = getfeed.GetFeedFromURL(URL);

            //CHECK IF THERE IS AN ERR FROM THE SERVER
            //IF THERE IS NO ERROR POPULATE THE LISTVIEW
            //ELSE DISPLAY A SERVER ERROR MESSAGE
            if (FD.getErr()){
                easyUsers = getObjDataList.GetObjectDataList(FD.getMyXml());

                adapter= new EasyAdapter(easyUsers,getApplicationContext());

                listView.setAdapter(adapter);

            }else{
                errTxt = (TextView) findViewById(R.id.errTxt);
                errTxt.setText("Server Error, Please try again later");
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }






    }

    public void ProfileImageBtnClicked(View v){

        startActivity(new Intent(Home.this, ProfileActivity.class));


    }

    public void ReplaceFrag() {
        // update the main content by replacing fragments
        Fragment fragment = getHomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.container, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    // show or hide the fab
    private void toggleFV() {

        if (navItemIndex == 0){
            fab.show();
            mViewPager.setVisibility(View.VISIBLE);
        }
        else{
            mViewPager.setVisibility(View.INVISIBLE);
            fab.hide();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void getAccelerometer(SensorEvent event) {
        NotificationFlag notificationFlag = new NotificationFlag();
        if(notificationFlag.getFlag()!=0) {

            try {
                float[] values = event.values;
                // Movement
                float x = values[0];
                float y = values[1];
                float z = values[2];

                float accelationSquareRoot = (x * x + y * y + z * z)
                        / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
                long actualTime = event.timestamp;
                if (accelationSquareRoot >= 2) //
                {
                    if (actualTime - lastUpdate < 200) {
                        return;
                    }
                    lastUpdate = actualTime;


                    try {


                        SharedPreferences userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);
                        Map<String, ?> allPreferences = userSession.getAll();

                        if (!allPreferences.isEmpty()) {
                            //Cancel the notification
                            // Get the field to update in the traffic table
                            GetFromSharedPrefs getFromSharedPrefs = new GetFromSharedPrefs(Home.this, "MPREFS");
                            String msgId = getFromSharedPrefs.GetVal("MSGID");

                            //Send Verfree back to the caller
                            Bundle extras = new Bundle();
                            Intent ServiceTask = new Intent(getBaseContext(), EasyService.class);
                            ServiceTask.setAction("SEND_TRAFFIC_ANSWER");
                            extras.putString("ANSWER", "Very Free");
                            extras.putString("MSGID", msgId);
                            ServiceTask.putExtras(extras);
                            getBaseContext().startService(ServiceTask);


                            int NOTIFICATION_ID = userSession.getInt("NOTIID", 0);
                            if (NOTIFICATION_ID != 0) {
                                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(NOTIFICATION_ID);

                                SharedPreferences.Editor editor = userSession.edit();
                                editor.putInt("NOTIID", 0);
                                editor.apply();
                                String o = "we";


                                notificationFlag.setFlag(0);
                            }
                        }

                    } catch (Exception eee) {
                        eee.printStackTrace();
                    }

                }
            } catch (Exception exx) {
                exx.printStackTrace();
            }
        }
    }


    //THIS IS THE ADAPTER FOR THE DIFFERENT SECTION OF THE TAB LAYOUT
    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    FConnect connectt = new FConnect();
                    return connectt;
                case 1:
                    FFeed feed = new FFeed();
                    return feed;
                case 2:
                    FChat chat = new FChat();
                    return chat;
                case 3:
                    FNotification notificatn = new FNotification();
                    return notificatn;


                case 4:
                    FPosters posterss = new FPosters();
                    return posterss;

                case 5:
                    FBusinesscard bcard = new FBusinesscard();
                    return bcard;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 6 total pages.
            return 6;
        }



    }

    public void ListviewClicked(View v){
        listView = (ListView) findViewById(R.id.ConnectList);

        //Respond to click event on the Listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedUserEmail = easyUsers.get(position).bcemailaddress.toString();
                String selectedUserPhone = easyUsers.get(position).bcphone1.toString();

                userSession =getSharedPreferences("MYCURRENTCHAT", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = userSession.edit();
                editor.putString("selUserEmail", selectedUserEmail);
                editor.putString("selUserPhone", selectedUserPhone);

                editor.apply();

                Intent chatPage = new Intent(Home.this,ChatControlActivity.class);

                startActivity(chatPage);

            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){

        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){

            Toast.makeText(this,"Landscape",Toast.LENGTH_LONG);
        }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){


            Toast.makeText(this,"Portrait",Toast.LENGTH_LONG);

        }
    }


    public void StartServiceBind(){
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {


                mBounded = true;
                EasyService.EasyBinder easyBinder = (EasyService.EasyBinder) service;
                mEasyService = easyBinder.getService();

                SaveToSharePref saveToSharePref = new SaveToSharePref(Home.this,"MYPREFS");
                Toast.makeText(getBaseContext(), "Service Connected to Activity!", Toast.LENGTH_LONG).show();
                saveToSharePref.SaveContent("SERVICEBINDHOME","HOMEBOUND");


            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

                mBounded = false;
                mEasyService = null;

                SaveToSharePref saveToSharePref = new SaveToSharePref(Home.this,"MYPREFS");
                Toast.makeText(getBaseContext(), "Service Disconnected from Activity!", Toast.LENGTH_LONG).show();
                saveToSharePref.SaveContent("SERVICEBINDHOME","HOMEUNBOUND");

            }
        };






    }

    public void PostMsg(View v){
        //START THE SERVICE
        Intent PostMsgPage = new Intent( this ,PostMsg.class);

        startActivity(PostMsgPage);
    }



    @Override
    protected void onStop() {
        super.onStop();

        if(mBounded){
            unbindService(mConnection);
        }

        mBounded = false;



    }

    public class handleTrafficQuestionAsyn extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {

            //Pop up Traffic Dialog


            return null;
        }

        @Override
        public void onPostExecute(String val) {

            dialogTrafficQuestion dialog = new dialogTrafficQuestion();
            Home home = new Home();
            dialog.showDialog(home);

        }

    }


    //Record the User chat login status
    public void LogIntoChat() {

        try {
            userSession = getSharedPreferences("MYCURRENTCHAT", Service.MODE_PRIVATE);

            SharedPreferences.Editor editor = userSession.edit();
            editor.putString("ChatStatus", "IN");

            editor.apply();
        } catch (Exception e) {
            String err = e.toString();

        }

    }








    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public void SendBtnClicked(View v){
        //START THE SERVICE AND SEND DATA


        try{
            //CHECK IF SERVICE IS RUNNING
             IsServiceRunning checker = new IsServiceRunning(Home.this);
             boolean serviceStarted = checker.isMyServiceRunning(EasyService.class);

             if(isOnline()){

                 startServiceTasks startServiceTasks = new startServiceTasks();
                 startServiceTasks.execute();

             }

        }
        catch(Exception e){
            Log.d("Error","Message Sending Error");
        }



    }


    class startServiceTasks extends   AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                    //GENERATE A MESSAGE ID TO IDENTIFY THE MESSAGE
                    SerialGen2 gen = new SerialGen2();
                    String msgId = gen.getNewSerialName();

                    //SAVE THE ID
                    SaveToSharePref saveData = new SaveToSharePref(Home.this,"MYPREFS");
                    saveData.SaveContent("MSGID",msgId);


                    try{

                        locationNameTxt = findViewById(R.id.LocationNameTxt);
                        //GET THE LOCATION NAME
                        GetFromSharedPrefs locatnName = new GetFromSharedPrefs(Home.this,"MYPREFS");
                        String location = locationNameTxt.getText().toString();
                        String VeryFast ="0";
                        String MovingSlowly ="0";
                        String StandStill ="0";

                        insertAsyn insertAsyn = new insertAsyn();
                        //SAVE THE REQUEST IN DATABASE
                        insertAsyn.execute(new TrafficQueryData(msgId, location,VeryFast,MovingSlowly,StandStill));

                    }catch(Exception err){

                        String d=err.getMessage();
                    }









            } catch (Exception e) {
                String err = e.toString();

            }


            return null;
        }


        @Override
        protected void onPostExecute(Void v){
            try{
                cotainerLocatnName = findViewById(R.id.CotainerLocatnName);
                cotainerLocatnName.setVisibility(View.GONE);
            }catch(Exception ee){

            }


        }
    }


    class insertAsyn extends AsyncTask<TrafficQueryData,Void,String>{

        @Override
        protected String doInBackground(TrafficQueryData... trafficQueryData) {

            String id = "";
            String msgId ="";
            try{
                db = new DbaseHelper(getBaseContext());
                // inserting note in db and getting
                // newly inserted note id

                 msgId = trafficQueryData[0].getMsgId().toString();
                String locatio = trafficQueryData[0].getLocation().toString();
                String veryfree = trafficQueryData[0].getVeryFree().toString();
                String movigslowly = trafficQueryData[0].getMovingSlowly().toString();
                String standstill = trafficQueryData[0].getStandStill().toString();

             Boolean res =     db.insertTrffic(msgId,locatio,veryfree,movigslowly,standstill);
             if(res){
                 id=msgId;
             }

            }
            catch(Exception ex){
                String exx = ex.toString();
            }

            return id;
        }

        @Override
        protected void onPostExecute(String msgId){


            try{
                SharedPreferences userSession;
                userSession =getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);

                Map<String, ?> allPreferences = userSession.getAll();

                if (!allPreferences.isEmpty()) {

                    if(msgId!=""){
                        double minlat = 0.0 ;
                        double maxlat = 0.0;
                        double minlongi = 0.0 ;
                        double maxlongi = 0.0;

                        minlat = userSession.getFloat("MinLat", 0);
                        maxlat = userSession.getFloat("MaxLat", 0);
                        minlongi = userSession.getFloat("MinLongi", 0);
                        maxlongi = userSession.getFloat("MaxLongi", 0);

                        //Broadcast the Location Question to the Service
                        Intent intent = new Intent("android.intent.action.SENDTRAFFICQUERRY");
                        intent.putExtra("MSGID", msgId);
                        intent.putExtra("MINLAT", minlat);
                        intent.putExtra("MAXLAT", maxlat);
                        intent.putExtra("MINLONGI", minlongi);
                        intent.putExtra("MAXLONGI", maxlongi);

                        //sendBroadcast(intent);

                        IsServiceRunning isServiceRunning = new IsServiceRunning(Home.this);
                      boolean serviceRunning =  isServiceRunning.isMyServiceRunning(EasyService.class);

                      if(serviceRunning){
                          sendBroadcast(intent);
                      }

                    }

                }
            }catch(Exception ee){

            }






        }
    }


    public boolean isOnline() {

        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = ( activeNetwork != null && activeNetwork.isConnectedOrConnecting());


            if (isConnected) {
                return true;    }
        }catch(Exception ee){

        }
        return false;
    }


}




