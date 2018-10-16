package ng.com.easyworld.easyworld;


import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class FConnect extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;

    ArrayList<EasyUserProfile> easyUsers2;
    double lat;
    double lng;
    final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    String mLastKnownLocation;
    Button myLocationText ;
    Location location;
    private  String  Useremail = null;
    private String Userpass = null;
    SharedPreferences userSession;
    private String ServEmail;
    private String Servpass;
    private ViewStub viewStub;
    boolean mLocationPermissionGranted = false;
    ListView listView;
   private EasyProfileAdapter adapter;
   private double newLat ;
   private double newLon ;
    private Handler handler;
    private  ImageButton HideLstView;
    private ImageButton FabBtn;
    private boolean HideLstViewFlag;
    boolean isOnline;
    private  TextView NetworkConnTxt;
    private ImageButton HideLstViewBtn;
    private TextView errTxt;
    private LinearLayout GeneralActionBtnContainer;

    boolean  mBounded;
    EasyService mEasyService;
    ServiceConnection mConnection;
   private AlertDialog mGPSDialog;

   private int GPS_ENABLE_REQUEST = 1;

    public FConnect() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_fconnect, container, false);

        NetworkConnTxt = (TextView) rootView.findViewById(R.id.NetworkConnTxt);
        myLocationText = (Button) rootView.findViewById(R.id.Locatn);
        mMapView = (MapView) rootView.findViewById(R.id.emap);
        mMapView.onCreate(savedInstanceState);


        HideLstViewBtn =  rootView.findViewById(R.id.HideLstViewBtn);
        //GeneralActionBtnContainer =  rootView.findViewById(R.id.GeneralActionBtnContainer);



        //CHECK IF THE USER IS ONLINE
        isOnline = isOnline();
        if (isOnline){

            mMapView.onResume(); // needed to get the map to display immediately

        }else{

            NetworkConnTxt.setVisibility(View.VISIBLE);

        }


        userSession = getContext().getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);

        Map<String, ?> allPreferences = userSession.getAll();
        if (!allPreferences.isEmpty()){
            Useremail= userSession.getString("Email","");
            Userpass= userSession.getString("Pw", "");


        }

//////////////////////////////////////////////////////////////////////////////////////////////////

        if (isOnline){////////////////////////////////////////bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap mMap) {
                    googleMap = mMap;

                    // For showing a move to my location button

                    viewStub = (ViewStub) getActivity().findViewById(R.id.viewStub);
                    HideLstViewFlag = false;

                    HideLstView = (ImageButton) getActivity().findViewById(R.id.HideLstViewBtn);
                    HideLstView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            if(HideLstViewFlag==false){

                                Animation hideList = AnimationUtils.loadAnimation(getContext(), R.anim.hidelistview);

                                listView.startAnimation(hideList);

                                listView.setVisibility(View.INVISIBLE);

                                HideLstViewFlag = true;
                            }
                            else{

                                Animation showList = AnimationUtils.loadAnimation(getContext(), R.anim.showlistview);

                                listView.startAnimation(showList);

                                listView.setVisibility(View.VISIBLE);

                                HideLstViewFlag = false;
                            }


                        }
                    });

                    //GET THE SESSION INFORMATION OF THE USER
                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                        @Override
                        public void onMapClick(LatLng arg0) {
                            // TODO Auto-generated method stub

                             newLat = arg0.latitude;
                            newLon = arg0.longitude;

                            LatLng curentLocatn = new LatLng(newLat, newLon);

                            FabBtn = (ImageButton) getActivity().findViewById(R.id.fabbtn);

                            viewStub.setVisibility(View.VISIBLE);

                            /////////////////////////////////////////////////////////////////////////////////////////

                            getUserByLocation UsersAtLocation = new getUserByLocation();

                            UsersAtLocation.execute(newLat,newLon);

                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);


                            googleMap.addMarker(new MarkerOptions().position(curentLocatn).title("Marker Title").snippet("Marker Description").icon(bitmapDescriptor));


                            // HideLstView.setVisibility(View.VISIBLE);
                            updateWithNewLocation(location);



                        }
                    });



                    //CHECK IF LOCATION IS ENABLED
                    int off = 0;
                    try {
                        off = Settings.Secure.getInt(getContext().getContentResolver(), Settings.Secure.LOCATION_MODE);
                    } catch (Settings.SettingNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(off==0){
                        showGPSDiabledDialog();
                    }




                    String serviceString = Context.LOCATION_SERVICE;
                    final LocationManager locationManager;
                    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
                    criteria.setAltitudeRequired(false);
                    criteria.setBearingRequired(false);
                    criteria.setSpeedRequired(false);
                    criteria.setCostAllowed(true);


                    //GET THE BEST PROVIDER
                    final String bestProvider = locationManager.getBestProvider(criteria, true);

                    //CHECK FOR PERMISSION TO USE LOCATION
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {

                        mLocationPermissionGranted = true;

                    }else{
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }


                    //IF PERMISSION IS GRANTED *********************************************************************
                    //**********************************************************************************************
                     LatLng curentLocatn = new LatLng(lat, lng);
                    if (mLocationPermissionGranted==true){

                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                         location = locationManager.getLastKnownLocation(bestProvider);

                       // lat=location.getLatitude();
                       // lng=location.getLongitude();

                        // For zooming automatically to the location of the marker
                        // CameraPosition cameraPosition = new CameraPosition.Builder().target(curentLocatn).zoom(15).build();
                        // mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



                        //SET MINIMUM TIME AND DISTANCE THAT WILL TRIGGER LOCATION UPDATE
                        final int t = 300;     // milliseconds
                        final int distance = 1/2; // meters

                        //LISTENER FOR LOCATION CHANGES
                        LocationListener myLocationListener = new LocationListener() {
                            public void onLocationChanged(Location locatn) {    // Update application based on new location.
                                if (locatn != null) {
                                    lat = locatn.getLatitude();
                                    lng = locatn.getLongitude();
                                }
                                updateWithNewLocation(locatn);










                            }

                            @Override
                            public void onStatusChanged(String s, int i, Bundle bundle) {

                            }

                            @Override
                            public void onProviderEnabled(String s) {

                            }

                            public void onProviderDisabled(String provider) {    // Update application if provider disabled.

                                showGPSDiabledDialog();
                            }

                        };


                        //REQUEST LOCATION UPDATE
                        locationManager.requestLocationUpdates(bestProvider,t,distance,myLocationListener);



                        // For dropping a marker at a point on the Map
                         curentLocatn = new LatLng(lat, lng);

                        if (curentLocatn!=null){
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);


                            mMap.addMarker(new MarkerOptions().position(curentLocatn).title("Marker Title").snippet("Marker Description").icon(bitmapDescriptor));



                            // For zooming automatically to the location of the marker
                            CameraPosition   cameraPosition = new CameraPosition.Builder().target(curentLocatn).zoom(15).build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        }else{

                            lat=location.getLatitude();
                            lng=location.getLongitude();
                            curentLocatn=new LatLng(lat,lng);

                            // For zooming automatically to the location of the marker
                             CameraPosition cameraPosition = new CameraPosition.Builder().target(curentLocatn).zoom(15).build();
                             mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                        }



                    }//**************************************************************************************************
                    //***************************************************************************************************




                }
            });



        }else{//////////////////////////////////bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb

            NetworkConnTxt.setVisibility(View.VISIBLE);

        }
///////////////////////////////////////////////////////////////////////////////////////////////


        return rootView;
    }

    //UPDATE USER LOCATION ON THE SERVER AND SHOW THE CURENT LOCATION ON THE UI TEXTVIEW
    private void updateWithNewLocation(Location location) {



        String latLongString = "No location found";
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();


            if(isOnline){
                updLocatn updateLocatn = new updLocatn();
                Thread locationTask = new Thread(updateLocatn);
                locationTask.start();
            }



        latLongString =  lat + " " + lng;  }
        myLocationText.setText("Current Position: " +latLongString);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if ( googleMap == null) {
            return;
        }

    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            googleMap.setMyLocationEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    public class updLocatn implements Runnable{

        @Override
        public void run() {


            StringBuilder myXml = new StringBuilder();
            StringBuilder xml = new StringBuilder();
            try {

                String upLocatnURL = String.format("https://www.easyworld.com.ng/ewc.asmx/uploctn?email=%s&pwd=%s&lon=%s&lat=%s",Useremail,Userpass,lng,lat) ;

                URL ul = new URL(upLocatnURL);

                //Connect to the WebService
                HttpURLConnection clientt = (HttpURLConnection) ul.openConnection();


                //Read the data from the client

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientt.getInputStream()));


                String current;

                while ((current=(in.readLine())) != null) {

                    xml.append( current) ;
                }




                String s = Pattern.compile("&lt;").matcher(xml).replaceAll("<");
                String s2 = Pattern.compile("&gt;").matcher(s).replaceAll(">");

                myXml.append(s2);

                clientt.disconnect();

            } catch (MalformedURLException e) {
                Log.d(this.getClass().getSimpleName(), e.getMessage());
            } catch (IOException e) {
                Log.d(this.getClass().getSimpleName(), e.getMessage());
            }





        }
    }



    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = ( activeNetwork != null && activeNetwork.isConnectedOrConnecting());


        if (isConnected) {
            return true;    }    return false;
    }




    public void showGPSDiabledDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("GPS Disabled");
        builder.setCancelable(false);
        builder.setMessage("Gps is disabled, in order to use the application properly you need to enable GPS of your device");
        builder.setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_ENABLE_REQUEST);
            }
        }).setNegativeButton("No, Just Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        mGPSDialog = builder.create();
        mGPSDialog.show();
    }





    public class getUserByLocation extends AsyncTask<Double, Void, ArrayList<EasyUserProfile>> {




        @Override
        protected ArrayList<EasyUserProfile> doInBackground(Double... latlon) {
            try {
            //errTxt = (TextView)  getActivity().findViewById(R.id.errTxt);
            double minlat=0;
            double maxlat=0;
            double minlon=0;
            double maxlon=0;

            //LATITUDE AND LONGITUDE IN DECIMAL
            double minlatt=0;
            double maxlatt=0;
            double minlonn=0;
            double maxlonn=0;


            double meters = 900000;///100
            double coef = meters * 0.0000089;

            DecimalFormat df = new DecimalFormat("##.########");



            minlatt= latlon[0] - coef;
            minlat = Double.valueOf(df.format(minlatt));

            maxlatt = latlon[0] + coef;
            maxlat = Double.valueOf(df.format(maxlatt));

            minlonn = latlon[1] - coef / Math.cos(latlon[0] * 0.018);
            minlon = Double.valueOf(df.format(minlonn));

            maxlonn = latlon[1] + coef / Math.cos(latlon[0] * 0.018);
            maxlon = Double.valueOf(df.format(maxlonn));

            //Save the location in the SheraedPrefference
            userSession = getContext().getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = userSession.edit();
            editor.putFloat("MinLat", (float)minlat);
            editor.putFloat("MaxLat", (float)maxlat);
            editor.putFloat("MinLongi", (float)minlon);
            editor.putFloat("MaxLongi", (float)maxlon);
            editor.apply();

            //GET USERS BY LOCATION
            String URL = String.format("https://www.easyworld.com.ng/ewc.asmx/getUserByLocatn?minlat=%s&maxlat=%s&minlon=%s&maxlon=%s",minlat,maxlat,minlon,maxlon);


            GetFeedFromUrl getfeed = new GetFeedFromUrl();



            GetProfileObjectDataLst getProObjDataList = new GetProfileObjectDataLst();



                FeedObject FD = getfeed.GetFeedFromURL(URL);

                //CHECK IF THERE IS AN ERR FROM THE SERVER
                //IF THERE IS NO ERROR POPULATE THE LISTVIEW
                //ELSE DISPLAY A SERVER ERROR MESSAGE
                if (FD.getErr()){

                    easyUsers2 = getProObjDataList.GetObjectDataList(FD.getMyXml());//easyUser is an ArrayList containing EasyUsers
                }else{

                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



            return easyUsers2;
        }


        @Override
      protected void  onPostExecute(ArrayList<EasyUserProfile> result){


            //DISPLAY USERS ON THE MAP WITH THE GOOGLE MARKERS
            displayUserByLocation displayUser = new displayUserByLocation();

            displayUser.execute(result);


            adapter = new EasyProfileAdapter(result,getActivity());


           try{
               listView=(ListView) getActivity().findViewById(R.id.ConnectList);
               listView.setAdapter(adapter);

               //SHOW THE ACTION BUTTONS WHEN THE PEOPLE HAS BEEN LOADED---
               HideLstView.setVisibility(View.VISIBLE);
              // GeneralActionBtnContainer.setVisibility(View.VISIBLE);



            }catch(Exception e){
               String dommy;
            }


        }

    }


    //For user on the Map Markers
    public class displayUserByLocation extends AsyncTask<ArrayList<EasyUserProfile>,Void,ArrayList<EasyUserProfile>>{

        @Override
        protected ArrayList<EasyUserProfile> doInBackground(ArrayList<EasyUserProfile>... userlist) {

            ArrayList userData = userlist[0];

            return userData;
        }


        @Override
        protected void onPostExecute(ArrayList<EasyUserProfile> result){

            try{
          for(int x=0; x<result.size(); x++){



                  double userLat =Double.parseDouble(result.get(x).latitude);
                  double userLon =Double.parseDouble(result.get(x).longitude);
                  String BcPerson = result.get(x).fname;
                  String BcField1 = result.get(x).gender;
                  String BcField2 = result.get(x).country;
                  String BcField3 = result.get(x).emailaddress;


                  LatLng curentLocatn = new LatLng(userLat, userLon);
                  BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);


                  googleMap.addMarker(new MarkerOptions().position(curentLocatn).title(BcPerson).snippet(BcField1+" "+BcField2+" "+BcField3).icon(bitmapDescriptor));

              }



          }
            catch(Exception e){

                String dommy;
            }
        }


    }





    @Override
       public void onResume() {
            super.onResume();
        mMapView.onResume();

    }


    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public boolean StartService(){

        boolean flag = false;

        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {


                mBounded = true;
                EasyService.EasyBinder easyBinder = (EasyService.EasyBinder) service;
                mEasyService = easyBinder.getService();

                Toast.makeText(getContext(), "Service Connected to Activity!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

                mBounded = false;
                mEasyService = null;
                Toast.makeText(getContext(), "Service Disconnected from Activity!", Toast.LENGTH_LONG).show();
            }
        };

        //START THE SERVICE
        Intent ServiceTask = new Intent( this.getContext() ,EasyService.class);

       getContext().startService(ServiceTask);



       return true;
    }


    public void StartMap(){

    }


}

