package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.SessionDescription;
import org.webrtc.VideoTrack;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

import static org.chromium.base.ThreadUtils.runOnUiThread;

/**
 * Created by Oisrael on 2/1/2018.
 */

public class EasyService extends Service implements SensorEventListener {

    // Binder given to clients
    private final IBinder mEasyBinder = new EasyBinder();


    //Caller ant Callee
    private static String usernameToCall;
    private static String Caller;
    private static String pw;

    private static ArrayList msgIdList;

    //EasyMessage Array List
    private static ArrayList<EasyMessage> list_of_Messages;
    private  ArrayList<EasyFeedMessage> feed_list_of_Messages;

    //ADAPTER TO CONVERT EASYMESSAGE LIST TO VIEW
    private EasyChatAdapter adapterr ;

    //Messages ListView
    private ListView chatMessageList;

    private PrivateMessage loginData;

    //Share Preference
    private SharedPreferences userSession;


    private  StringBuilder displayMsg ;


    private static Stack msgTask1;


    private BroadcastReceiver networkStateReceiver;

    private TextView messageToSend;

    private String host;
    private static HubProxy hub;
    private PrivateMessage pm;
    private PrivateMessage trafficPm;
    private PrivateMessage trafficAnswerPm;


    //fINAL STRINGS
    private  final String I_AM_CONNECTED_TO_CHAT = "IN";
    private  final String I_AM_NOT_CONNECTED_T0_CHAT = "OUT";



    // Queued remote ICE candidates are consumed only after both local and
    // remote descriptions are set. Similarly local ICE candidates are sent to
    // remote peer after both local and remote description are set.
    private LinkedList<IceCandidate> queuedRemoteCandidates;
    // private PeerConnectionEvents events;
    private boolean isInitiator;
    private SessionDescription localSdp; // either offer or answer SDP
    private MediaStream audioMediaStream;
    private int numberOfCameras;
    // private VideoCapturerAndroid videoCapturer;
    // enableVideo is set to true if video should be rendered and sent.
    private boolean renderVideo;
    private VideoTrack localVideoTrack;
    private MediaStream videoMediaStream;


    private WebView browser;
    private ProgressDialog progressDialog;
    String url;

    private String device = "Android";
    private String UsernameToCal;
    private String name;
    private String i;//pw
    private String o;//offer
    Handler h;
    CountDownTimer myTimer;

    private  int Number_Of_UnsentMsg;
    private ServiceCallbacks serviceCallbacks;
    private IceCallback iceCallback;
    private  AnswerCallback answerCallback;


    private DbaseHelper db;

    //Sensor
    private SensorManager sensorManager;
    private boolean color = false;
    private View view;
    private long lastUpdate;

    private String email;

    BroadcastReceiver receiver;
    private IntentFilter filter;

    private  ConnectToChatServer  Connect_To_Chat;
//*************************************************************************************************
    @Override
    public void onCreate() {
        super.onCreate();    // TODO: Actions to perform when service is created.




    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mEasyBinder;
    }

    //BINDING THE SERVICE TO ACTIVITY
    public class EasyBinder extends Binder {
        EasyService getService()
        {
            return EasyService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.




        //----------------------------------------------------
        //----Register Receive--------------------------------
        filter = new IntentFilter();

        filter.addAction("android.intent.action.SENDTRAFFICQUERRY");
        filter.addAction("android.intent.action.SENDTRAFFICANSWER");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
              if(action=="android.intent.action.SENDTRAFFICQUERRY"){
                    try{
                        Bundle extras = intent.getExtras();

                        String msgId = extras.getString("MSGID");
                        double minlat = extras.getDouble("MINLAT");
                        double maxlat = extras.getDouble("MAXLAT");
                        double minlongi = extras.getDouble("MINLONGI");
                        double maxlongi = extras.getDouble("MAXLONGI");



                        SendTrafficQuestion trafficQ = new SendTrafficQuestion(msgId);// here you invoke service method
                        trafficQ.execute( minlat,maxlat,minlongi,maxlongi);
                    }
                    catch(Exception eee){


                    }
                }else{}



            }
        };




        registerReceiver(receiver,filter);

        try{
            if(isOnline()){
                msgTask1 = new Stack();
                msgIdList = new ArrayList();


                RunServiceTask runServiceTask = new RunServiceTask(intent);
                runServiceTask.run();

                //GET THE LIST OF WALL POST AND SAVE TO DATABASE
                LoadWallPost loadWallPost = new LoadWallPost();
                loadWallPost.execute();
            }
        }
        catch(Exception e){
            Log.d("Service Connection","There was a network problem");
        }


        //SENSOR----------------------------------------------------
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
        //----------------------------------------------------------


        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

        Toast.makeText(this, "Service Destroyed",
                Toast.LENGTH_LONG).show();
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
                           // GetFromSharedPrefs getFromSharedPrefs = new GetFromSharedPrefs(EasyService.this, "MPREFS");

                            String msgId = userSession.getString("FROMTRAFFIC", null);
                             //msgId = getFromSharedPrefs.GetVal("MSGID");

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

//*************************************************************************************************


    //----------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------
    //RECORD THE CONNECTION
    public void CallMessageSender(String msg,String from,String to){

        MessageSender sender = new MessageSender(msg,from,to);
        sender.run();
    }


    public class ConnectToChatServer implements Runnable{


        @Override
        public void run() {

            Start_Chat();

        }




        //Start Chat Connection
        public void Start_Chat(){

            try{



                host = "https://www.easyworld.com.ng";

                //signalr platform object
                Platform.loadPlatformComponent(new AndroidPlatformComponent());
                final HubConnection connection = new HubConnection(host);
                hub =connection.createHubProxy("EasyHub");


                pm = new PrivateMessage();



                //Listen to MESSAGE EVENT from signalr hub
                hub.on("EasyMsg",new SubscriptionHandler2<String,String>()
                {
                    @Override
                    public void run (final String val1 ,final String msg){



                        //  setCallbacks(serviceCallbacks);
                        try{
                           // serviceCallbacks.updateChat(msg,val1);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }




                    }
                },String.class,String.class);



                //Listen to MESSAGE EVENT from signalr hub
                hub.on("TrafficQuestion",new SubscriptionHandler1<PrivateMessage>()
                {
                    @Override
                    public void run (PrivateMessage data ){//data contains---type from to fromConn toConn

                        NotificationID notificationID = new NotificationID();
                        int notiId = notificationID.getID();

                        //SAVE THE FROM AND TO IN SHAREDPREFERENCE-----
                        userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userSession.edit();
                        editor.putString("FROMTRAFFIC", data.getFromm());
                        editor.putString("TOTRAFFIC", data.getToo());//
                        // editor.putString("MSGID", data.getMsgId());//
                        editor.apply();


                        NotificationID ids = new NotificationID();
                        int idds = ids.getID();
                        String idString = Integer.toString(idds);


                        Intent intent = new Intent(getBaseContext(), NotificationReceiver.class);
                        intent.putExtra("MSGID",data.getMsgId());
                        PendingIntent pIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);



                        int notiIdd = 111;
                        String idd = Integer.toString(notiIdd);

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                            CharSequence name =  "Traffic";
                            String description = "How is traffic in your area";
                            int importance = NotificationManager.IMPORTANCE_DEFAULT;
                            NotificationChannel channel = new NotificationChannel(idString,name,importance);
                            channel.setDescription(description);
                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                            notificationManager.createNotificationChannel(channel);

                        }

                        NotificationCompat.Builder mBuilder =new  NotificationCompat.Builder(EasyService.this,idString);

                        mBuilder .setSmallIcon(R.drawable.traficreportredsmall)
                                .setContentTitle("Traffic Request?")
                                .setContentText("How is the Traffic ")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pIntent)
                                .setAutoCancel(true) // cancel the notification when clicked
                                .addAction(R.drawable.traficreportredsmall, "YES", pIntent) ;//add a btn to the Notification with a corresponding intent

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(EasyService.this);
                        notificationManager.notify(notiIdd, mBuilder.build());




                        editor.putInt("NOTIID",notiId);
                        editor.apply();


                        //Play sound
                        try {

                            // Uri notification =  Uri.parse("android.resource://"+getPackageName()+"/Music/Akeregbe");
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                            r.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //  handleTrafficQuestionAsyn handler = new handleTrafficQuestionAsyn();

                        //  handler.execute();


                    }
                },PrivateMessage.class);

                //Listen to MESSAGE EVENT from signalr hub
                hub.on("TrafficAnswer",new SubscriptionHandler1<PrivateMessage>()
                {
                    @Override
                    public void run (PrivateMessage data ){//data contains---type from to fromConn toConn


                        NotificationID ids = new NotificationID();
                        int idds = ids.getID();
                        String idString = Integer.toString(idds);

                        //Calculate the number of answer and save it in SharedPreference----
                        handleTrafficMessageAsync handler = new handleTrafficMessageAsync();
                        handler.execute(data.getTrafficAnswer(),data.getMsgId(),data.getTypee());


                        //SAVE THE FROM AND TO IN SHAREDPREFERENCE-----
                        userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userSession.edit();
                        editor.putString("FROMTRAFFIC", data.getFromm());
                        editor.putString("TOTRAFFIC", data.getToo());//
                        editor.apply();

                        Intent intent = new Intent(getBaseContext(), TrafficResponseReceiver.class);
                        PendingIntent pIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);

                        int notiIdd = 111;
                        String idd = Integer.toString(notiIdd);

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                            CharSequence name =  "Traffic";
                            String description = "How is traffic in your area";
                            int importance = NotificationManager.IMPORTANCE_DEFAULT;
                            NotificationChannel channel = new NotificationChannel(idString,name,importance);
                            channel.setDescription(description);
                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                            notificationManager.createNotificationChannel(channel);

                        }

                        NotificationCompat.Builder mBuilder =new  NotificationCompat.Builder(EasyService.this,idString);

                        mBuilder .setSmallIcon(R.drawable.traficreportgreensmall)
                                .setContentTitle("Traffic Response?")
                                .setContentText("Result of your Traffic request ")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pIntent)
                                .setAutoCancel(true) // cancel the notification when clicked
                                .addAction(R.drawable.traficreportgreensmall, "YES", pIntent) ;//add a btn to the Notification with a corresponding intent

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(EasyService.this);
                        notificationManager.notify(idds, mBuilder.build());




                        //Play sound
                        try {

                            // Uri notification =  Uri.parse("android.resource://"+getPackageName()+"/Music/Akeregbe");
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                            r.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //  handleTrafficQuestionAsyn handler = new handleTrafficQuestionAsyn();

                        //  handler.execute();


                    }
                },PrivateMessage.class);


                hub.on("LoginResponse",new SubscriptionHandler1<PrivateMessage>()

                {
                    @Override
                    public void run (PrivateMessage data){


                        try {

                            switch (data.getTypee()) {

                                case "login":
                                    handleLogin(data.getSuccess());
                                    break;

                                case "offer":
                                    handleOffer(data.getOffer(), data.getFromm());
                                    OfferObj offerObj = new OfferObj(data.getOffer(),data.getFromm());
                                    break;




                                case "openV":
                                    handleOpenV(data);
                                    break;


                                default:
                                    break;
                            }


                        } catch (Exception e) {

                            String err = e.toString();

                        }


                    }
                },PrivateMessage.class);

                hub.on("AnswerResponse",new SubscriptionHandler1<PrivateMessage>()

                {
                    @Override
                    public void run (PrivateMessage data){


                        try {

                            switch (data.getTypee()) {



                                case "answer":
                                    handleAnswer(data.getAnswer());
                                    OfferObj answerObj = new OfferObj(data.getAnswer(),"Answer");
                                    break;

                                case "candidate":
                                    handleCandidate(data.getCandidate());
                                    break;

                                default:
                                    break;
                            }


                        } catch (Exception e) {

                            String err = e.toString();

                        }


                    }
                },PrivateMessage.class);



                try

                {
                    try{
                        //Start the SIGNALR CONNECTION
                        SignalRFuture<Void> awaitConnection = connection.start(new ServerSentEventsTransport(connection.getLogger()));

                        awaitConnection.get();
                    }
                    catch(Exception eee){


                    }


                    //connectionId = connection.getConnectionId();
                    try{
                        loginData = new PrivateMessage();
                        loginData.setTypee("login");
                        loginData.setNamee(Caller);
                        loginData.setPw(pw);

                        //LOGIN TO CHAT
                        LoginMethodCaller login = new LoginMethodCaller(hub,loginData);
                        Thread LoginTask = new Thread(login);
                        LoginTask.start();

                        //Register Chat Status
                        LogIntoChat();

                    }
                    catch(Exception eee){


                    }


                /*
                Home home = new Home();
                home.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "Chat Hub Server Started", Toast.LENGTH_LONG).show();
                    }
                });
*/

                } catch(Exception e) {
                    e.printStackTrace();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }




    }

    private static class GenerateMsgId{
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        String randomString( int len ){
            StringBuilder sb = new StringBuilder( len );
            for( int i = 0; i < len; i++ )
                sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
            return sb.toString();
        }
    }

    public void handleLogin(String success ) {

        //displayMsg.append("You are logged in to chat!  ");
        //msgDisplay.setText(displayMsg.toString());





    }

    //HANDLE THE INCOMING CALL
    public void handleOffer(SessionDescription offer,String from) {
        sendMessage();
       // Log.d("Handle Offer","Handle Offer has been called");
    }

    public void handleAnswer(SessionDescription answer) {


        // answerCallback.setAnswer(answer);

        Gson gson = new Gson();
        String ans = gson.toJson(answer);

        Intent intent = new Intent("android.intent.action.SIGNAL_MESSAGE");
        intent.putExtra("ANSWER", ans);
        sendBroadcast(intent);
        Log.d("Answer ","Answer Received");

    }

    public void handleCandidate(IceCandidate candidate) {


        //iceCallback.setIceCandidate(candidate);

        Gson gson = new Gson();
        String can = gson.toJson(candidate);

        Intent intent = new Intent("android.intent.action.SIGNAL_MESSAGE");
        intent.putExtra("CANDIDATE", can);
        sendBroadcast(intent);

    }

    public void handleOpenV(PrivateMessage data) {

        //If a remote peer want you to open a browser to do video chat
        if (data.getOpenV() != null){

            //Open the browser
            SetCallReceivingDatails(data.getFromm());

            ReceiveVideoCall();
        }

    }

    //HANDLE MESSAGE ARRANGEMENT INTO THE LISTVIEW
    class handleMessageAsync extends AsyncTask<String,String,ArrayList> {




        @Override
        protected ArrayList doInBackground(String... data) {

            EasyMessage message = new EasyMessage();



            try{
                if (msgIdList.contains(data[0].toString())) {

                    message.setMyMessage(data[1].toString());

                } else {
                    message.setOthersMessage(data[1].toString());
                }
            }
            catch(Exception e){
                message.setOthersMessage(data[1].toString());
            }




            list_of_Messages.add(message);


            return list_of_Messages;
        }

        @Override
        protected void  onPostExecute(ArrayList result){


            adapterr = new EasyChatAdapter(list_of_Messages, getBaseContext());

            chatMessageList.setAdapter(adapterr);
            chatMessageList.setSelection(adapterr.getCount() - 1);




        }



    }

    public class handleTrafficQuestionAsyn extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            //Pop up Traffic Dialog


            return null;
        }

        @Override
        public void onPostExecute(String val){

            // dialogTrafficQuestion dialog = new dialogTrafficQuestion();
            //Home home = new Home();
            // dialog.showDialog(home);

        }

    }

    public class handleTrafficMessageAsync extends AsyncTask<String,Void,String>{

        int val = 0 ;

        @Override
        protected String doInBackground(String... vals) {

            db = new DbaseHelper(getBaseContext());

            switch (vals[0])
            {
                case "Very Free" :
                    val = 0;
                    try {
                        val=db.getVeryFree(vals[1]);
                        val += 1;
                        String v =Integer.toString(val);
                        boolean res =  db.updateVeryFree(vals[1],v);
                        Log.d("RES", Boolean.toString(res));
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    break;

                case "Moving Slowly" :
                    val = 0;
                    try {
                        val=db.getMovingSlowly(vals[1]);
                        val += 1;
                        String v =Integer.toString(val);
                        db.updateMovingslowly(vals[1],v);

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    break;

                case "Standstill" :

                    val = 0;
                    try {
                        val=db.getStandstill(vals[1]);
                        val += 1;
                        String v =Integer.toString(val);
                        db.updateStandstill(vals[1],v);

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    break;

                default:break;
            }


            return null;
        }

        protected  void onPostExecute(String val){
            // Home home =  new Home();
            // home.ShowDialogg();
        }


    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = ( activeNetwork != null && activeNetwork.isConnectedOrConnecting());


        if (isConnected) {
            return true;    }    return false;
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

    //Record the User chat login status
    public void LogoutFromChat() {

        try {
            userSession = getSharedPreferences("MYCURRENTCHAT", Service.MODE_PRIVATE);

            SharedPreferences.Editor editor = userSession.edit();
            editor.putString("ChatStatus", "OUT");

            editor.apply();
        } catch (Exception e) {
            String err = e.toString();

        }

    }

    //LOGIN COMMAND EXECUTOR
    class LoginMethodCaller implements Runnable{
        private HubProxy hub;
        private PrivateMessage loginData;
        LoginMethodCaller(HubProxy hub,PrivateMessage loginData){
            this.hub = hub;
            this.loginData = loginData;
        }
        @Override
        public void run() {
            //LOGIN TO CHAT
            try {
                hub.invoke("SendWebrtcConnData",loginData).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
    }

    public void LoadUserInfo() {


        try {
            userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);

            Map<String, ?> allPreferences = userSession.getAll();

            if (!allPreferences.isEmpty()) {

                Caller = userSession.getString("Email", null);
                name = Caller;
                pw = userSession.getString("Pw", null);

            }

        } catch (Exception e) {
            String err = e.toString();

        }

        try {
            userSession = getSharedPreferences("MYCURRENTCHAT", Activity.MODE_PRIVATE);

            Map<String, ?> allPreferences = userSession.getAll();

            if (!allPreferences.isEmpty()) {

                usernameToCall = userSession.getString("selUserEmail", null);
                //Callee.setText(userSession.getString("selUserEmail", null));

            }

        } catch (Exception e) {
            String err = e.toString();

        }

    }

    class MessageSender implements Runnable{


        private String msg ;
        private String from;
        private String to;

        MessageSender(String msg,String from,String to){
            this.msg = msg;
            this.from = from;
            this.to = to;
        }


        @Override
        public void run() {
            StartMessageTask();
        }


        public void StartMessageTask(){

            //GENERATE A RANDOM MESSAGE ID
            GenerateMsgId msgIDGen = new GenerateMsgId();
            String msgID = msgIDGen.randomString(8);

            //SAVE THE MESSAGE ID IN THE MESSAGE ID LIST
            msgIdList.add(msgID);




            if(isOnline())
            {
                try {
                    //SET THE REQUIRED FEED
                    pm = new PrivateMessage();
                    pm.setTypee("msg");                         //Set the message type
                    pm.setMsg(msg);  //Set the message
                    pm.setFromm(from);                      //Set the sender
                    pm.setToo(to);
                    pm.setMsgId(msgID);
                    //Execute sendmessage

                    hub.invoke("SendWebrtcConnData",pm).get();
                } catch (InterruptedException e) {
                    //SAVE THE TASK TO BE SENT LATER

                    msgTask1.push(pm);

                    Number_Of_UnsentMsg += msgTask1.size();
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    //SAVE THE TASK TO BE SENT LATER

                    msgTask1.push(pm);

                    Number_Of_UnsentMsg += msgTask1.size();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //unsentMsg.setText(Number_Of_UnsentMsg);
                        }
                    });
                    e.printStackTrace();
                }
            }else{

                //SAVE THE TASK TO BE SENT LATER
                msgTask1 = new Stack();
                msgTask1.push(pm);

                Number_Of_UnsentMsg += msgTask1.size();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //unsentMsg.setText(Number_Of_UnsentMsg);
                    }
                });
            }

        }


    }

    public class RunServiceTask implements Runnable{

        Intent intent;

        RunServiceTask (Intent intent){
            this.intent = intent;
        }

        @Override
        public void run() {
            String action = intent.getAction();

            LoadUserInfo();
            if (action == null) {


                //START THE CHAT SERVER
                  Connect_To_Chat = new ConnectToChatServer();


                        try{
                                myTimer =  new CountDownTimer(10000, 10000) {

                                    public void onTick(long millisUntilFinished) {

                                             }

                                                     public void onFinish() {



                                                          try{

                                                              Connect_To_Chat.Start_Chat();
                                                                  Log.d("Alarm","Finished");

                                                                  myTimer.cancel();
                                                             }catch(Exception e){

                                                      }



                                                 }
                                         }.start();
                                }catch (Exception ee){
                                         Log.d("Alarm","Failed");
                                            ee.printStackTrace();
                                                        }

                networkStateReceiver=new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo ni = manager.getActiveNetworkInfo();

                        if(ni.isConnected()){
                            //Resend Messages that were not sent
                            ResendMsg();
                        }

                    }
                };


                Log.d("Service","Service Started");



            }


            else if(action.equals("SEND_TRAFFIC_ANSWER")) {

                try{
                    Bundle extras = intent.getExtras();
                    String answer = extras.getString("ANSWER");

                    String From = null;
                    String To = null;
                    String MsgId = null;

                    try {
                        userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);

                        Map<String, ?> allPreferences = userSession.getAll();

                        if (!allPreferences.isEmpty()) {

                            From = userSession.getString("Email", null);
                            To = userSession.getString("FROMTRAFFIC", null);
                            MsgId = userSession.getString("MSGID", null);



                            //Get the current time and add 10 min to it
                            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
                            Calendar now = Calendar.getInstance();
                            now.add(Calendar.MINUTE, 10);

                            String teenMinutesFromNow = null;

                            try{
                                teenMinutesFromNow = ft.format(now.getTime());
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }

                            SendTrafficAnswerAsy trafficAns = new SendTrafficAnswerAsy();
                            trafficAns.execute(answer,From,To,MsgId,teenMinutesFromNow.toString());
                        }

                    } catch (Exception e) {
                        String err = e.toString();

                    }

                }
                catch(Exception eee){


                }



            }


            else{}
        }
    }

    public void ReceiveVideoCall(){





        //Uri uri = Uri.parse( String.format("https://www.easyworld.com.ng/Easy Mobile.aspx?device=%s&UsernameToCal=%s&name=%s&i=%s&o=%s",device,UsernameToCal,name,i,o));
        // Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        Intent intent = new Intent(EasyService.this, EasyVideoCallView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);



    }

    public void ResendMsg(){


        if (!msgTask1.empty()){

            //Execute ResendMessage
            try {
                hub.invoke("SendWebrtcConnData",msgTask1.pop()).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        }


    }

    public void StartMessageTask(PrivateMessage pm){


        //SAVE THE MESSAGE ID IN THE MESSAGE ID LIST
        msgIdList.add(pm.getMsgId());



        if(isOnline())
        {
            //Execute sendmessage
            try {
                hub.invoke("SendWebrtcConnData",pm).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }else{

            //SAVE THE TASK TO BE SENT LATER
            msgTask1 = new Stack();
            msgTask1.push(pm);


        }





    }

    public void SendOffer(SessionDescription offer){

        //Execute ResendMessage
        try {
            hub.invoke("SendWebrtcConnData",offer.toString()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void SetCallReceivingDatails(String frommm){

        try {
            userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);

            Map<String, ?> allPreferences = userSession.getAll();

            if (!allPreferences.isEmpty()) {

                name = userSession.getString("Email", null);
                i = userSession.getString("Pw", null);
                o = "opened";
                UsernameToCal = frommm;
                device = "Android";


            }

        } catch (Exception e) {
            String err = e.toString();

        }

    }

    public class SendTrafficQuestion extends AsyncTask<Double,Void,Void>{

        String msgId="";

        SendTrafficQuestion(String msgId){
            this.msgId=msgId;
        }

        @Override
        protected Void doInBackground(Double... vals) {

            //GET CALLER FRO SHAREDPREFERENCE----

            LoadUserInfo();

            //Get the current time and add 10 min to it
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
            Calendar now = Calendar.getInstance();

            String teenMinutesFromNow = null;

            try{
                teenMinutesFromNow = ft.format(now.getTime());
            }
            catch(Exception e){
                e.printStackTrace();
            }

            String From = Caller;
            double minlat = vals[0];
            double maxlat = vals[1];
            double minlongi = vals[2];
            double maxlongi = vals[3];


            trafficPm = new PrivateMessage();
            trafficPm.setTypee("trafficQuestion");
            trafficPm.setFromm(From);//Set the message type
            trafficPm.setMsgId(msgId);
            trafficPm.setMinLat(minlat);                      //Set the sender
            trafficPm.setMaxLat(maxlat);
            trafficPm.setMinLongi(minlongi);
            trafficPm.setMaxLongi(maxlongi);
            trafficPm.setTim(teenMinutesFromNow);
            //Execute sendmessage

            try {
                hub.invoke("SendWebrtcConnData",trafficPm).get();
                String dmmy = "";
            }catch (InterruptedException e){

            }
            catch (ExecutionException e){

            }

            return null;
        }
    }

    public class SendTrafficAnswerAsy extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... vals) {


            trafficAnswerPm = new PrivateMessage();
            trafficAnswerPm.setTypee("trafficAnswer");                         //Set the message type - trafficAnswer
            trafficAnswerPm.setTrafficAnswer(vals[0]);  //very free, moving slowly or standstill
            trafficAnswerPm.setFromm(vals[1]);//The person to receive the message
            trafficAnswerPm.setToo(vals[2]);//The
            trafficAnswerPm.setMsgId(vals[3]);//The message id to identify the msg
            trafficAnswerPm.setTim(vals[4]);//The time the message was sent plus 10min
            //Execute sendmessage

            try {
                hub.invoke("SendWebrtcConnData",trafficAnswerPm).get();
            }catch (InterruptedException e){

            }
            catch (ExecutionException e){

            }
            return null;
        }
    }

    private void sendMessage() {
       // Intent intent = new Intent(this.);
        // add data
       // intent.putExtra("message", "data");
       // LocalBroadcastManager.getInstance(this).sendBroadcast(intent);


        Intent regPage = new Intent(EasyService.this,EasyVideoCall.class);
        regPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(regPage);

    }

    public interface ServiceCallbacks {
        void updateChat(String msg,String val);
    }

    public void sendMsg() {
        Intent intent = new Intent("my-event");
        // add data
        intent.putExtra("message", "data");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }



    public interface IceCallback {
        void setIceCandidate(String candidate);
    }
    public void setIceCallbacks(IceCallback callbacks) {
        iceCallback = callbacks;
    }
    public interface AnswerCallback {
        void setAnswer(String answer);
    }
    public void setAnswerCallbacks(AnswerCallback callbacks) {
        answerCallback = callbacks;
    }
    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------


    //*******************LOAD WALLPOST*************************************
    public class LoadWallPost extends AsyncTask<Void,Void,ArrayList<EasyFeedMessage>>{


        LoadWallPost(){

        }

        @Override
        protected ArrayList<EasyFeedMessage> doInBackground(Void... voids) {


            SharedPreferences user = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);

            Map<String, ?> allPreferences = user.getAll();

            String  emaill = null;
            if (!allPreferences.isEmpty()) {

                 emaill= user.getString("Email", null);

            }

            ArrayList<EasyFeedMessage> data = new ArrayList<EasyFeedMessage>();

            //Get the Time the last Item was inserted into the dfatabase
            DbaseHelper db = new DbaseHelper(getBaseContext());
            DateAndTime dt = new DateAndTime();

            dt  = db.getLastItemTime();


            String cote = dt.getDatee();
            String datee = cote;

            //CREATE OBJECT TO GET THE FEED FROM THE WEBSERVICE
            GetFeedFromUrl getfeed = new GetFeedFromUrl();
            GetFeedFromUrlByPostMethod getfeed2 = new GetFeedFromUrlByPostMethod();
            //DATASET TO CONTAIN THE FEED FROM THE SERVICE
            GetMsgFeedObjectDataList getFeedObjDataList = new GetMsgFeedObjectDataList();

            if(datee == ""||datee == null){//888888888888888888888888888888888888888888888888888888888888888888888888888888888888
                //Get the data from the server
                url = String.format("https://www.easyworld.com.ng/ewc.asmx/GetWallPostByEmail?email=%s",emaill);//load email from sharedpref

                //GET THE FEED FROM THE DATASET AND SAVE IT IN ARRAYLIST(List_of_Messages)
                try {
                    FeedObject FD = getfeed.GetFeedFromURL(url);

                    //CHECK IF THERE IS AN ERR FROM THE SERVER
                    //IF THERE IS NO ERROR POPULATE THE LISTVIEW
                    //ELSE DISPLAY A SERVER ERROR MESSAGE
                    if (FD.getErr()){
                        getFeedObjDataList.GetMsgFeedObjectDataList(FD.getMyXml(),getBaseContext());//List_of_Messages is an ArrayList containing EasyFeedMessages
                        String d = "";
                    }else{

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }


            }else if(datee != ""&&datee != null){//8888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888
                //Get the data from the server
                url = String.format("https://www.easyworld.com.ng/ewc.asmx/GetWallPostByTime?");//load email from sharedpref


                //GET THE FEED FROM THE DATASET AND SAVE IT IN ARRAYLIST(List_of_Messages)
                try {
                    FeedObject FD2 = getfeed2.GetFeedFromUrlByPostMethod(url,emaill,datee);

                    //CHECK IF THERE IS AN ERR FROM THE SERVER
                    //IF THERE IS NO ERROR POPULATE THE LISTVIEW
                    //ELSE DISPLAY A SERVER ERROR MESSAGE
                    if (FD2.getErr()){
                        getFeedObjDataList.GetMsgFeedObjectDataList(FD2.getMyXml(),getBaseContext());//List_of_Messages is an ArrayList containing EasyFeedMessages
                        String d = "";
                    }else{

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }



            }//8888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888






            return null;
        }


    }




    public class SentAnswerReceiver extends  BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {



            String action = intent.getAction();
            if(action=="android.intent.action.SENDTRAFFICANSWER"){

                try{
                    Bundle extras = intent.getExtras();
                    String answer = extras.getString("ANSWER");

                    String From = null;
                    String To = null;
                    String MsgId = null;

                    try {
                        userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);

                        Map<String, ?> allPreferences = userSession.getAll();

                        if (!allPreferences.isEmpty()) {

                            From = userSession.getString("Email", null);
                            To = userSession.getString("FROMTRAFFIC", null);
                            MsgId = userSession.getString("MSGID", null);



                            //Get the current time and add 10 min to it
                            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
                            Calendar now = Calendar.getInstance();
                            now.add(Calendar.MINUTE, 10);

                            String teenMinutesFromNow = null;

                            try{
                                teenMinutesFromNow = ft.format(now.getTime());
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }

                            SendTrafficAnswerAsy trafficAns = new SendTrafficAnswerAsy();
                            trafficAns.execute(answer,From,To,MsgId,teenMinutesFromNow.toString());
                        }

                    } catch (Exception e) {
                        String err = e.toString();

                    }

                }
                catch(Exception eee){


                }

            }



        }
    }

    //*******************LOAD WALLPOST END*********************************

}
