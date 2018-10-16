package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private FloatingActionButton fab;

    static final String URL = "http://www.easyworld.com.ng/ewc.asmx/getbc";
    ArrayList<EasyUser> easyUsers;
    ListView listView;
    private static EasyAdapter adapter;

    private  boolean fabFlag=false;
    private ImageButton fabFrag;

    private ViewStub viewStub;
    private String longi;
    private TextView longiText ;
    private TextView errText ;


    SharedPreferences userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       // StrictMode.setThreadPolicy(policy);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());



        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

      final  TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);









        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                long currentTab = mSectionsPagerAdapter.getItemId(mViewPager.getCurrentItem());
                if (currentTab>0){

                }
                return false;
            }
        });

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

    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
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

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "CONNECT";
                case 1:
                    return "FEED";
                case 2:
                    return "CHAT";
                case 3:
                    return "NOTIFICATION";
                case 4:
                    return "POSTERS";
                case 5:
                    return "BUSINESS CARD";
            }
            return null;
        }

    }


    public void fabFragClicked (View v){

        errText = (TextView) findViewById(R.id.errTxt);
        viewStub = (ViewStub) findViewById(R.id.viewStub);

        Animation hidefabbtn = AnimationUtils.loadAnimation(  getBaseContext(), R.anim.hidefabbtn);

        fabFrag=(ImageButton) findViewById(R.id.fabbtn);


        viewStub.setVisibility(View.VISIBLE);


        fabFrag.startAnimation(hidefabbtn);
        fabFrag.setVisibility(View.INVISIBLE);


        listView=(ListView)findViewById(R.id.ConnectList);


        GetFeedFromUrl getfeed = new GetFeedFromUrl();


        GetObjectDataLst getObjDataList = new GetObjectDataLst();


        try {
            FeedObject FD = getfeed.GetFeedFromURL(URL);

            //CHECK IF THERE IS AN ERR FROM THE SERVER
            //IF THERE IS NO ERROR POPULATE THE LISTVIEW
            //ELSE DISPLAY A SERVER ERROR MESSAGE
            if (FD.getErr()){
                easyUsers = getObjDataList.GetObjectDataList(FD.getMyXml());



            }else{

            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        adapter= new EasyAdapter(easyUsers,getApplicationContext());

        listView.setAdapter(adapter);




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
}
