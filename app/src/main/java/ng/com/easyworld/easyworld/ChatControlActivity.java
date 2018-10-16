package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

public class ChatControlActivity extends AppCompatActivity implements EasyService.ServiceCallbacks {


    //Caller ant Callee
    private String usernameToCall;
    private String Caller;
    private String pw;

    //Signalr Hub

    private String connectionId;
    private HubProxy hub;

    //Share Preference
    private SharedPreferences userSession;

    //Message Object that is beening sent over signalr
    private PrivateMessage pm;
    private ImageButton sendMsgBtn;
    private TextView messageText;
    private TextView messageToSend;
    private TextView msgDisplay;
    private TextView conid;
    private TextView timerTxt;

    private  StringBuilder displayMsg ;

    //Controls
    private TextView Callee;

    //fINAL STRINGS
    private  final String I_AM_CONNECTED_TO_CHAT = "IN";
    private  final String I_AM_NOT_CONNECTED_T0_CHAT = "OUT";

    //EasyMessage
    private static EasyMessage messages;

    //EasyMessage Array List
    private static ArrayList<EasyMessage> list_of_Messages;

    //ADAPTER TO CONVERT EASYMESSAGE LIST TO VIEW
    private EasyChatAdapter adapterr ;

    //Messages ListView
   private ListView chatMessageList;
   private TextView unsentMsg;

   private PrivateMessage loginData;

   private static ArrayList msgIdList;

   private static Stack msgTask;

    boolean mBounded;
    EasyService mEasyService;

    private BroadcastReceiver networkStateReceiver;

    private String host;
   private ServiceConnection mConnection;

    handleMessageAsync messageHandler;

        private  int Number_Of_UnsentMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_control);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //Reference the controls

        sendMsgBtn = (ImageButton) findViewById(R.id.SendMsgBtn);
        messageToSend = (TextView) findViewById(R.id.MessageContent);
        unsentMsg = (TextView) findViewById(R.id.UnsentMsg);
        timerTxt = (TextView) findViewById(R.id.TimerTxt);
        list_of_Messages = new ArrayList<EasyMessage>();

        //String builder to hold Display message
        displayMsg = new StringBuilder();


        messages = new EasyMessage();

        msgIdList = new ArrayList();


        //Handle voice call button
        ImageButton videoCall = (ImageButton) findViewById(R.id.VideoCallBtn);

        chatMessageList = (ListView) findViewById(R.id.ChatListt) ;




        videoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent videoCallIntent = new Intent(ChatControlActivity.this, EasyVideoCall.class);

                startActivity(videoCallIntent);


            }
        });


        //LOAD USER INFORMATION---------------
        LoadUserInfo();





        //Handle send message button
        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try{
                    if(mBounded){
                        RunService runService = new RunService();
                        runService.send();

                        messageToSend.setText("");
                    }
                }catch(Exception e){

                }

            }
        });










    }



    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService



    }

    @Override
    protected void onStop() {
        super.onStop();

    }







    public void LoadUserInfo() {


        try {
            userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);

            Map<String, ?> allPreferences = userSession.getAll();

            if (!allPreferences.isEmpty()) {

                Caller = userSession.getString("Email", null);
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
                Callee.setText(userSession.getString("selUserEmail", null));

            }

        } catch (Exception e) {
            String err = e.toString();

        }

    }



    public Boolean Connected_To_Chat() {


        Boolean chatFlag = false;
        try {
            userSession = getSharedPreferences("MYCURRENTCHAT", Activity.MODE_PRIVATE);

            Map<String, ?> allPreferences = userSession.getAll();

            if (!allPreferences.isEmpty()) {


                String chatStatus = userSession.getString("ChatStatus", null);

                if (chatStatus == I_AM_CONNECTED_TO_CHAT){

                    chatFlag = true;

                }

            }

        } catch (Exception e) {
            String err = e.toString();

        }

        return chatFlag;
    }

    @Override
    public void updateChat(String msg, String val) {
        handleMessageAsync handler = new handleMessageAsync();
        handler.execute(msg,val);
    }


    public class ConnectToChatServer implements Runnable{


        @Override
        public void run() {

            Start_Chat();

        }



        //Start Chat Connection
        public void Start_Chat(){

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



                    messageHandler = new handleMessageAsync();

                    messageHandler.execute(msg,val1);//val2 is the message


                }
            },String.class,String.class);


            //Start the SIGNALR CONNECTION
            SignalRFuture<Void> awaitConnection = connection.start(new ServerSentEventsTransport(connection.getLogger()));

            try

            {
                awaitConnection.get();

                connectionId = connection.getConnectionId();

                try{
                    //LOGIN TO CHAT
                    LoginMethodCaller login = new LoginMethodCaller();
                    Thread LoginTask = new Thread(login);
                    LoginTask.start();

                    //Register Chat Status
                    LogIntoChat();

                    Toast.makeText(getBaseContext(), "Chat Hub Server Started", Toast.LENGTH_LONG).show();
                }catch(Exception e){

                }



            } catch(
                    InterruptedException e)

            {
                e.printStackTrace();
            } catch(
                    ExecutionException e)

            {
                e.printStackTrace();
            }



        }




    }



    //Record the User chat login status
    public void LogIntoChat() {

        try {
            userSession = getSharedPreferences("MYCURRENTCHAT", Activity.MODE_PRIVATE);

            SharedPreferences.Editor editor = userSession.edit();
            editor.putString("ChatStatus", "IN");

            editor.apply();
        } catch (Exception e) {
            String err = e.toString();

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


    //LOGIN COMMAND EXECUTOR
    class LoginMethodCaller implements Runnable{


        @Override
        public void run() {
            //LOGIN TO CHAT
            try {

                loginData = new PrivateMessage();
                loginData.setTypee("login");
                loginData.setNamee(Caller);
                loginData.setPw(pw);

                hub.invoke("SendWebrtcConnData",loginData).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
    }



    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = ( activeNetwork != null && activeNetwork.isConnectedOrConnecting());


        if (isConnected) {
            return true;    }    return false;
    }



    class RunService{

        public void send() {
            mEasyService.CallMessageSender(messageToSend.getText().toString(),Caller,usernameToCall);
        }
    }





}