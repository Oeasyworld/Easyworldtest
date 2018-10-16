package ng.com.easyworld.easyworld;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FFeed extends Fragment {

    private  EasyMsgFeedAdapter adapterr;
    private  ArrayList<EasyFeedMessage> list_of_Messages;
   private String url;
   private EasyFeedMessage feedMessages;
   private ListView wallPostFeed = null;
   private SharedPreferences userSession;
   private String email;
    View  rootView;

    private DbaseHelper db;

    public FFeed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
       rootView = inflater.inflate(R.layout.fragment_ffeed, container, false);

        //GET EMAIL FROM SHAREDPREFERENCE
        userSession = getContext().getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);
        Map<String, ?> allPreferences = userSession.getAll();
        if (!allPreferences.isEmpty()){
            email = userSession.getString("Email", null);
        }



        //RUN THE LOADWALLPOST ASYNCTASK-------
        if(isOnline()) {

           // db = new DbaseHelper(getContext());
           //// list_of_Messages = db.getAllWallPost();

           // LoadListView loadWallPost = new LoadListView(list_of_Messages, rootView);
            //loadWallPost.LoadList();
        }



        return rootView;
    }

    public void onResume() {

        super.onResume();
        if(isOnline()) {
            db = new DbaseHelper(getContext());
            list_of_Messages = db.getAllWallPost();

            LoadListView loadWallPost = new LoadListView(list_of_Messages, rootView);
            loadWallPost.LoadList();
        }
    }

    public class LoadListView{
        ArrayList<EasyFeedMessage> list;
        View rootView;
        LoadListView( ArrayList<EasyFeedMessage> list,View rootView){
            this.list = list;
            this.rootView=rootView;
        }
        //SET THE DATAADAPTER

        public void LoadList(){

            if(isOnline()) {
                try {
                    //SET THE LISTVIEW
                    wallPostFeed = rootView.findViewById(R.id.WallPostFeedd);



                    adapterr = new EasyMsgFeedAdapter(list, getActivity());
                   // adapterr.clear();
                    adapterr.notifyDataSetChanged();

                    wallPostFeed.setAdapter(adapterr);
                //    wallPostFeed.invalidateViews();
                //    wallPostFeed.refreshDrawableState();



                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }


        public void BitmapDisplayer(Bitmap bitmap, ImageView imageView)

        {
            if(bitmap!=null)
                imageView.setImageBitmap(bitmap);
            else
                imageView.setImageResource(R.drawable.pen);
        }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = ( activeNetwork != null && activeNetwork.isConnectedOrConnecting());


        if (isConnected) {
            return true;    }    return false;
    }
}
