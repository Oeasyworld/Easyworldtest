package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaCodecVideoEncoder;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.VideoCapturerAndroid;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Timer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;
import ng.com.easyworld.easyworld.RendererCommon.ScalingType;

import static ng.com.easyworld.easyworld.RendererCommon.ScalingType.SCALE_ASPECT_FIT;


////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class EasyVideoCall extends AppCompatActivity  {

    EasyService myServive;
    // private static final PeerConnectionClient instance = new PeerConnectionClient();
    private final PCObserver pcObserver = new PCObserver();
    private SdpObserver msdpObserver;
    private SdpObserver sdpObserver;
    private PeerConnectionFactory factory;
    private PeerConnection peerConnection;
    PeerConnectionFactory.Options options = null;
    private VideoSource videoSource;
    private boolean videoCallEnabled;
    private boolean preferIsac;
    private boolean preferH264;
    private boolean videoSourceStopped;
    private boolean isError;
    private Timer statsTimer;

    private VideoRenderer.Callbacks remote_view;
    private VideoRenderer.Callbacks local_view;

    //private VideoStreamHandler[] remoteRenders;
    //private SignalingParameters signalingParameters;
    private MediaConstraints pcConstraints;
    private MediaConstraints videoConstraints;
    private MediaConstraints audioConstraints;
    private MediaConstraints sdpMediaConstraints;
    // private PeerConnectionParameters peerConnectionParameters;
    private PeerConnectionEvents events;
    private boolean isInitiator;
    private SessionDescription localSdp; // either offer or answer SDP

    //Caller ant Callee
    private static String usernameToCall;
    private static String Caller;
    private static String Callee;
    private boolean isCaller;
    private String TargetUser;
    private static String From;
    private static SessionDescription offer;

    private static String pw;

    private static ArrayList msgIdList;

    //EasyMessage Array List
    private static ArrayList<EasyMessage> list_of_Messages;

    //ADAPTER TO CONVERT EASYMESSAGE LIST TO VIEW
    private EasyChatAdapter adapterr ;

    //Messages ListView
    private ListView chatMessageList;

    private PrivateMessage loginData;

    //Share Preference
    private SharedPreferences userSession;


    private  StringBuilder displayMsg ;


    private static Stack msgTask;

    private BroadcastReceiver networkStateReceiver;

    private TextView messageToSend;

    private String host;
    private HubProxy hub;
    private PrivateMessage pm;


    //fINAL STRINGS
    private  final String I_AM_CONNECTED_TO_CHAT = "IN";
    private  final String I_AM_NOT_CONNECTED_T0_CHAT = "OUT";
  private static  SessionDescription remoteSDP2;


    // Queued remote ICE candidates are consumed only after both local and
    // remote descriptions are set. Similarly local ICE candidates are sent to
    // remote peer after both local and remote description are set.
    private ArrayList<IceCandidate> queuedRemoteCandidates;
    // private PeerConnectionEvents events;

    private MediaStream audioMediaStream;
    private int numberOfCameras;
    // private VideoCapturerAndroid videoCapturer;
    // enableVideo is set to true if video should be rendered and sent.
    private boolean renderVideo;

    private VideoTrack remoteVideoTrack;
    private VideoTrack localVideoTrack;
    private MediaStream videoMediaStream;



    public static final String VIDEO_TRACK_ID = "videoPN";
    public static final String AUDIO_TRACK_ID = "audioPN";
    public static final String LOCAL_MEDIA_STREAM_ID = "localStreamPN";


    private VideoSource localVideoSource;
    private VideoRenderer localRender;
    private VideoRenderer remoteRender = null;

    private MediaStream localMediaStream;

    private static  PeerConnection localPeer;
    private static PeerConnection remotePeer;
    private  MediaConstraints sdpConstraint;

    private static String Person ="";
    private static String Person2="";
    private String targetUser;

    EasyService mEasyService;
    EasyService mEasyServive;
    private ServiceConnection mConnection;
    boolean mBounded;

  MyView  mVideoView ;

    private String connectionId;

     BroadcastReceiver receiver;
    private   IntentFilter filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVideoView = findViewById(R.id.gl_surface);
        mVideoView = new MyView(this);

        setContentView(mVideoView);

        mEasyServive = new EasyService();



/*
 //----------------------------------------------------
        //----Register Receive--------------------------------
         filter = new IntentFilter();
        filter.addAction("android.intent.action.SIGNAL_MESSAGE");

       receiver = new BroadcastReceiver() {
           @Override
           public void onReceive(Context context, Intent intent) {

               Uri data = intent.getData();
               String answer = intent.getStringExtra("ANSWER");
               String candidate = intent.getStringExtra("CANDIDATE");

               if(answer != null){
                  // handleAnswer(answer);
               }else if(candidate != null){

                  // handleCandidate(candidate);
               }else{}


           }
       };

        registerReceiver(receiver,filter);
       //-----------------------------------------------------

       */


        //LOAD USER INFORMATION---------------
        LoadUserInfo();


        //START THE CHAT SERVER
        ConnectToChatServer Connect_To_Chat = new ConnectToChatServer();



            //Start Chat
            Connect_To_Chat.Start_Chat();


        try{
            OfferObj offerObj = new OfferObj();
            Caller =  offerObj.getFrom();
            From = offerObj.getFrom();
            offer =  offerObj.getOffer();

            if(offer != null){

                ReceiveCall();
                OfferObj offerObjj = new OfferObj(null,null);
            }else{

                invite();
            }
        }catch (Exception exx){
exx.printStackTrace();
        }



    }


    //---------------SIGNALING CHANNEL-------------------------
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


            hub.on("AnswerResponse",new SubscriptionHandler1<PrivateMessage>()

            {
                @Override
                public void run (PrivateMessage data){


                    try {

                        switch (data.getTypee()) {



                            case "answer":
                                handleAnswer(data.getAnswer());

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

                    Toast.makeText(getBaseContext(), "Chat Hub Server Started Inside EasyVideoCall", Toast.LENGTH_LONG).show();
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



    public void handleAnswer(SessionDescription answer) {


/*
        OfferObj ans = new OfferObj();
        SessionDescription ansSdp = ans.getOffer();//This will contain Answer now

        Gson gson = new Gson();
        String SdpString = gson.toJson(answer);
*/
        //SessionDescription remoteSDP = new SessionDescription(SessionDescription.Type.OFFER,SdpString);
        String des = answer.description;
        SessionDescription answerr = new SessionDescription(answer.type,des);


        try {
            Log.d("Answer Received",answer.toString());
            localPeer.setRemoteDescription(sdpObserver,answerr);

        }catch (Exception e){

            e.printStackTrace();
        }


    }

    public void handleCandidate(IceCandidate candidate) {

        //IceCandidate iceCandidate = new IceCandidate()

        Log.d("Candidate Receive", candidate.toString());

        //Add the IceCandidate to the Queue
        addRemoteIceCandidate(candidate);


    }


    private void drainCandidates() {

        if (queuedRemoteCandidates != null) {
            Log.d("Candidate Queue", "Add " + queuedRemoteCandidates.size() + " remote candidates");

            for (IceCandidate candidate : queuedRemoteCandidates) {

                Log.d("IceCandidate",candidate.toString());
                if(localPeer != null){
                    localPeer.addIceCandidate(candidate);
                    Log.d("Candidate","Candidate From Remote Peer Added");
                }

                if(remotePeer != null){

                    remotePeer.addIceCandidate(candidate);
                    Log.d("Candidate","Candidate From Local Peer Added");
                }
               // peerConnection.addIceCandidate(candidate);
            }
            queuedRemoteCandidates = null;





        }
    }



    public void addRemoteIceCandidate(final IceCandidate candidate) {

        ExecuteMyTask executeMyTask  = new ExecuteMyTask(new Runnable() {
            @Override
            public void run() {
                if (remotePeer != null && !isError) {
                    if (queuedRemoteCandidates != null) {
                        queuedRemoteCandidates.add(candidate);
                    } else {
                        remotePeer.addIceCandidate(candidate);
                    }
                }

                if (localPeer != null && !isError) {
                    if (queuedRemoteCandidates != null) {
                        queuedRemoteCandidates.add(candidate);
                    } else {
                         localPeer.addIceCandidate(candidate);
                    }
                }

                Log.d("Candidate","Candidate From Local Peer Added");
            }
        });
        executeMyTask.execute();

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
                //Callee.setText(userSession.getString("selUserEmail", null));

            }

        } catch (Exception e) {
            String err = e.toString();

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


    //RECORD THE CONNECTION
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




        /////////START WEBRTC SESSION///////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void invite(){


        //CREATE PEER CONNECTION
        MakeCall();
        Log.d("Make Call","Making Call");
    }


    //------------------START A CALL-----------------------------
    public void MakeCall(){






        //This is the caller making call-----------
        Person = "Caller";
        TargetUser = usernameToCall;


        options = new PeerConnectionFactory.Options();

        PeerConnectionFactory.initializeAndroidGlobals(
                this,  // Context
                true,  // Audio Enabled
                true,  // Video Enabled
                true,  // Hardware Acceleration Enabled
                null);

        factory = new PeerConnectionFactory();


        // Creates a VideoCapturerAndroid instance for the device name
        VideoCapturerAndroid capturer =  VideoCapturerAndroid.create(VideoCapturerAndroid.getNameOfFrontFacingDevice(),null);


        // First create a Video Source, then we can make a Video Track
        localVideoSource = factory.createVideoSource(capturer, new MediaConstraints());
        VideoTrack localVideoTrack = factory.createVideoTrack(VIDEO_TRACK_ID, localVideoSource);
        localVideoTrack.setEnabled(true);

// First we create an AudioSource then we can create our AudioTrack
        AudioSource audioSource = factory.createAudioSource(new MediaConstraints());
        AudioTrack localAudioTrack = factory.createAudioTrack(AUDIO_TRACK_ID, audioSource);
        localAudioTrack.setEnabled(true);

        localMediaStream = factory.createLocalMediaStream(LOCAL_MEDIA_STREAM_ID);
        localMediaStream.addTrack(localAudioTrack);
        localMediaStream.addTrack(localVideoTrack);


        VideoRendererGui.setView(mVideoView, null);

        try {
            remoteRender = VideoRendererGui.createGui(0, 0, 100, 100, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
            localRender = VideoRendererGui.createGui(50, 50, 50, 50, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);

            localVideoTrack.addRenderer(localRender);

        } catch (Exception e) {
            e.printStackTrace();
        }




//Make the Call to the other peer-------------
        CreatePeerConnection();

        localPeer.addStream(localMediaStream);
        Log.d("Create Offer","Want to create offer");
    }


    //------------------RECEIVE A CALL-----------------------------
    public void ReceiveCall(){


        Log.d("Receive Call","Receiving Call");


        Person2 = "Callee";

        options = new PeerConnectionFactory.Options();

        PeerConnectionFactory.initializeAndroidGlobals(
                this,  // Context
                true,  // Audio Enabled
                true,  // Video Enabled
                true,  // Hardware Acceleration Enabled
                null);

        factory = new PeerConnectionFactory();


        videoConstraints = new MediaConstraints();
        creatvideoConstraints(videoConstraints);

        audioConstraints = new MediaConstraints();
        creataudioConstraints(audioConstraints);


        // Creates a VideoCapturerAndroid instance for the device name
        VideoCapturerAndroid capturer = (VideoCapturerAndroid) VideoCapturerAndroid.create(VideoCapturerAndroid.getNameOfFrontFacingDevice(),null);



        // First create a Video Source, then we can make a Video Track
        localVideoSource = factory.createVideoSource(capturer, videoConstraints);
        VideoTrack localVideoTrack = factory.createVideoTrack(VIDEO_TRACK_ID, localVideoSource);
        localVideoTrack.setEnabled(true);



// First we create an AudioSource then we can create our AudioTrack
        AudioSource audioSource = factory.createAudioSource(audioConstraints);
        AudioTrack localAudioTrack = factory.createAudioTrack(AUDIO_TRACK_ID, audioSource);
        localAudioTrack.setEnabled(true);

        localMediaStream = factory.createLocalMediaStream(LOCAL_MEDIA_STREAM_ID);
        localMediaStream.addTrack(localAudioTrack);
        localMediaStream.addTrack(localVideoTrack);


        VideoRendererGui.setView(mVideoView, null);

        try {
            remoteRender = VideoRendererGui.createGui(0, 0, 100, 100, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
            localRender = VideoRendererGui.createGui(50, 50, 50, 50, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);

            localVideoTrack.addRenderer(localRender);

        } catch (Exception e) {
            e.printStackTrace();
        }


        //Make the Call to the other peer-------------
        CreatePeerConnection();


        remotePeer.addStream(localMediaStream);
        Log.d("Adding Stream","Remote Peer Adding Stream");



    }






///-------------------------------------------------------------------------------------------------------
///----------------------------------------------------------------------------------------------------------------

    //---------------------CREATE PEER CONNECTION--------------------------------------
    private void CreatePeerConnection() {




        // Set ICE servers
        List<PeerConnection.IceServer> iceServers = new ArrayList<>();
        iceServers.add(new org.webrtc.PeerConnection.IceServer("stun:stun2.1.google.com:19302"));
        iceServers.add(new org.webrtc.PeerConnection.IceServer("turn:webrtcweb.com:7788", "muazkh", "muazkh"));

        //the peer connection----
        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServers);

        // TCP candidates are only useful when connecting to a server that supports
        // ICE-TCP.
        rtcConfig.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.ENABLED;
        rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.BALANCED;
        rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.NEGOTIATE;





        sdpConstraint = new MediaConstraints();
        sdpConstraint.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        sdpConstraint.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
        sdpConstraint.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));





        if(Person=="Caller"){
            //CREATING LOCAL PEER----------------------------
            localPeer = factory.createPeerConnection(rtcConfig, sdpConstraint, pcObserver);

        }



        queuedRemoteCandidates = new ArrayList<>();



        sdpObserver = new SdpObserver() {
            @Override
            public void onCreateSuccess(final SessionDescription origSdp) {

                if(origSdp.type == SessionDescription.Type.ANSWER && Person2=="Callee"){

                    String des = origSdp.description;
                    SessionDescription desc = new SessionDescription(origSdp.type,des);


                    //Set remote peer local description
                    remotePeer.setLocalDescription(sdpObserver,desc);


                    //Get the caller from the ShearedPref
                    GetFromSharedPrefs geta = new GetFromSharedPrefs(EasyVideoCall.this,"MYPREFS");
                    String callee = geta.GetVal("Email");

                    //Create a PrivateMessage Containing the Offer----
                    pm.setTypee("answer");                         //Set the message type
                    pm.setAnswer(origSdp);  //Set the message
                    pm.setFromm(callee);                      //Set the sender
                    pm.setToo(Caller);

                    //Send the PrivateMessage through the Signalling Channel to Remote Peer---
                    try {
                        hub.invoke("SendWebrtcConnData",pm).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    Log.d("Answer","Answer Sent");


                    //After sending answer back, Add the Ice Candidates
                    drainCandidates();

                }


                final String sdpDescription = origSdp.description;

                final SessionDescription sdp = new SessionDescription(origSdp.type, sdpDescription);
                localSdp = sdp;

////11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111
                if(origSdp.type == SessionDescription.Type.OFFER){///111111111111111111111111111111111111111111111111111111111111111111


                    if (localPeer != null && Person=="Caller") {
                        try {


                            localPeer.setLocalDescription(new SdpObserver() {
                                @Override
                                public void onCreateSuccess(SessionDescription sessionDescription) {
                                    Log.d("Set onCreateSuccess","Local description set Success");
                                }

                                @Override
                                public void onSetSuccess() {
                                    Log.d("Set Sdp","Local description set Success");

                                    //Get the caller from the ShearedPref
                                    GetFromSharedPrefs geta = new GetFromSharedPrefs(EasyVideoCall.this,"MYPREFS");
                                    String caller = geta.GetVal("Email");

                                    //Get the Use to call from the static data class
                                    UserToCall userToCall = new UserToCall();
                                    String callee = userToCall.getName();

                                    //Create a PrivateMessage Containing the Offer----
                                    pm.setTypee("offer");                         //Set the message type
                                    pm.setOffer(origSdp);  //Set the message
                                    pm.setFromm(caller);                      //Set the sender
                                    pm.setToo(callee);

                                    //Send the PrivateMessage through the Signalling Channel to Remote Peer---
                                    try {
                                        hub.invoke("SendWebrtcConnData",pm).get();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }

                                    Log.d("Offer","Offer Sent");


                                }

                                @Override
                                public void onCreateFailure(String s) {
                                    Log.d("Set onCreateFailure","Local onCreateFailure set Success");
                                }

                                @Override
                                public void onSetFailure(String s) {
                                    Log.d("Set onSetFailure","Local onSetFailure set Success");
                                }
                            }, sdp);




                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }/////111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111
///11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111









            }

            @Override
            public void onSetSuccess() {
                String dommy = "dommy";
                Log.d("1", "Can not start looper thread");


                //On setting the remote answer as remote sdp
                //Add the Ice Candidate and tell the other peer
                drainCandidates();
            }

            @Override
            public void onCreateFailure(String s) {
                Log.d("2", "Can not start looper thread");
            }

            @Override
            public void onSetFailure(String s) {
                String dommy = "dommy";

                Log.d("3", "Can not start looper thread");
            }
        };


        if (Person2 == "Callee") {

            //CREATING REMOTE PEER----------------------------
            remotePeer = factory.createPeerConnection(rtcConfig, sdpConstraint, pcObserver);
        }




        if(remotePeer != null && Person2=="Callee" & offer!=null)
        {
            try{

                Gson gson = new Gson();
                String SdpString = gson.toJson(offer);

                SessionDescription remoteSDP = new SessionDescription(SessionDescription.Type.OFFER,SdpString);
                String des = remoteSDP.description;
                remoteSDP2 = new SessionDescription(remoteSDP.type,des);

                Log.d("Try Set Remote Descript","At the Try Block");
                try{
                    remotePeer.setRemoteDescription(new SdpObserver() {
                    @Override
                    public void onCreateSuccess(SessionDescription sessionDescription) {


                        Log.d("onCreateSuccess", "Set RemoteDescripting ");
                    }

                    @Override
                    public void onSetSuccess() {

                        remotePeer.createAnswer(sdpObserver,sdpConstraint);

                        Log.d("onSetSuccess", "Set RemoteDescripting");
                    }

                    @Override
                    public void onCreateFailure(String s) {
                        Log.d("onCreateFailure", "Set RemoteDescripting");
                    }

                    @Override
                    public void onSetFailure(String s) {
                        Log.d("onSetFailure", "Set RemoteDescripting");
                    }
                }, offer);
                }
                catch(Exception eeee){
                    eeee.printStackTrace();
                }



            }
            catch(Exception eee){

                eee.printStackTrace();
            }

        }else{
            //Create the offer------------
            localPeer.createOffer( sdpObserver ,sdpConstraint);
        }



    }








    public void creatvideoConstraints(MediaConstraints videoConstraints) {

        String MAX_VIDEO_WIDTH_CONSTRAINT = "maxWidth";
        String MIN_VIDEO_WIDTH_CONSTRAINT = "minWidth";
        String MAX_VIDEO_HEIGHT_CONSTRAINT = "maxHeight";
        String MIN_VIDEO_HEIGHT_CONSTRAINT = "minHeight";
        String MAX_VIDEO_FPS_CONSTRAINT = "maxFrameRate";
        String MIN_VIDEO_FPS_CONSTRAINT = "minFrameRate";

        int videoWidth = 0;
        int videoHeight = 0;

        if ((videoWidth == 0 || videoHeight == 0) && true && MediaCodecVideoEncoder.isVp8HwSupported()) {
            videoWidth = 352;
            videoHeight = 288;
        }

        if (videoWidth > 0 && videoHeight > 0) {
            videoWidth = Math.min(videoWidth, 1280);
            videoHeight = Math.min(videoHeight, 1280);
            videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair(MIN_VIDEO_WIDTH_CONSTRAINT, Integer.toString(videoWidth)));
            videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair(MAX_VIDEO_WIDTH_CONSTRAINT, Integer.toString(videoWidth)));
            videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair(MIN_VIDEO_HEIGHT_CONSTRAINT, Integer.toString(videoHeight)));
            videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair(MAX_VIDEO_HEIGHT_CONSTRAINT, Integer.toString(videoHeight)));
        }

        int videoFps = 30;

        videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair(MIN_VIDEO_FPS_CONSTRAINT, Integer.toString(videoFps)));
        videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair(MAX_VIDEO_FPS_CONSTRAINT, Integer.toString(videoFps)));

    }
    public void creataudioConstraints(MediaConstraints pcConstraints) {
        pcConstraints.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));
        pcConstraints.optional.add(new MediaConstraints.KeyValuePair("RtpDataChannels", "true"));
        pcConstraints.optional.add(new MediaConstraints.KeyValuePair("internalSctpDataChannels", "true"));
    }













     //------------------------------------------------------------------------------
    // ------------------------------HANDLING PEER CONNECTION EVENTS-----------------


    public void HandleOnSignalingChange(PeerConnection.SignalingState signalingState) {

    }


    public void HandleOnIceConnectionChange(PeerConnection.IceConnectionState newState) {


        RunOnIceStateChanged runOnIceStateChanged = new RunOnIceStateChanged(newState);



    }

    class RunOnIceStateChanged implements  Runnable{

        PeerConnection.IceConnectionState newState = null;

        RunOnIceStateChanged(PeerConnection.IceConnectionState newState){
            this.newState  = newState;
        }

        @Override
        public void run() {

            Log.d("Ice Changed", "IceConnectionState: " + newState);
            if (newState == PeerConnection.IceConnectionState.CONNECTED) {
                events.onIceConnected();
            } else if (newState == PeerConnection.IceConnectionState.DISCONNECTED) {
                events.onIceDisconnected();
            } else if (newState == PeerConnection.IceConnectionState.FAILED) {
                Log.d("Error","ICE connection failed.");
            }


        }

    }





    public void HandleOnIceConnectionReceivingChange(boolean b) {

    }


    public void HandleOnIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {

    }


    public void HandleOnIceCandidate(IceCandidate candidate){
        try {

            try {
                userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);

                Map<String, ?> allPreferences = userSession.getAll();

                if (!allPreferences.isEmpty()) {

                    Caller = userSession.getString("Email", null);

                }

            } catch (Exception e) {
                String err = e.toString();

            }

            if(Person=="Caller"){
                UserToCall userToCall = new UserToCall();
                usernameToCall = userToCall.getName();
                //Create a PrivateMessage Containing the Offer----
                pm.setTypee("candidate");                         //Set the message type
                pm.setCandidate(candidate);  //Set the message
                pm.setFromm(Caller);                      //Set the sender
                pm.setToo(usernameToCall);
            }

            if(Person2=="Callee"){
                UserToCall userToCall = new UserToCall();
                usernameToCall = userToCall.getName();
                //Create a PrivateMessage Containing the Offer----
                pm.setTypee("candidate");                         //Set the message type
                pm.setCandidate(candidate);  //Set the message
                pm.setFromm(Caller);                      //Set the sender
                pm.setToo(From);
            }


            //Send the PrivateMessage through the Signalling Channel to Remote Peer---
            hub.invoke("SendWebrtcConnData",pm).get();

            Log.d("IceCandidate","Ice Sent");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void HandleOnAddStream(final MediaStream streamm) {

        RunAddStream runAddStream = new RunAddStream(streamm);
        runAddStream.run();

    }

    class RunAddStream implements  Runnable{

        MediaStream streamm = null;

        RunAddStream(MediaStream stream){
            this.streamm = stream;
        }

        @Override
        public void run() {

            if (localPeer == null && remotePeer == null){
                return;
            }

            if (streamm.audioTracks.size() > 1 || streamm.videoTracks.size() > 1 ){

                return;
            }

            if (streamm.videoTracks.size() == 1){

                remoteVideoTrack = streamm.videoTracks.get(0);
                remoteVideoTrack.setEnabled(true);


                remoteVideoTrack.addRenderer(new VideoRenderer(remote_view));
                VideoRendererGui.update(local_view, 75, 70, 60, 60,  VideoRendererGui.ScalingType.SCALE_ASPECT_FIT, true);
                VideoRendererGui.update(remote_view, 0, 0, 200, 200, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, false);



               // VideoRenderer.Callbacks remoteRender = null;

                //remoteVideoTrack.addRenderer(new VideoRenderer(remoteRender));
                Log.d("Remote Stream","Attaching remote stream");
            }

        }

    }





    public void HandleOnRemoveStream(MediaStream mediaStream) {

    }


    public void HandleOnDataChannel(DataChannel dataChannel) {

    }


    public void HandleOnRenegotiationNeeded() {

        Log.d("Renegotiation","Renegotiation has been called");

    }


    // Implementation detail: observe ICE & stream changes and react accordingly.
    private class PCObserver implements PeerConnection.Observer {
        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {

            HandleOnSignalingChange(signalingState);

        }

        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {

            HandleOnIceConnectionChange(iceConnectionState);

        }

        @Override
        public void onIceConnectionReceivingChange(boolean b) {

            HandleOnIceConnectionReceivingChange(b);

        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {

            HandleOnIceGatheringChange(iceGatheringState);

        }

        @Override
        public void onIceCandidate(final IceCandidate candidate) {

            HandleOnIceCandidate(candidate);
            Log.d("OnIceCandidate","On Ice Candidate Called");

        }


        @Override
        public void onAddStream(final MediaStream streamm) {

            HandleOnAddStream(streamm);

        }

        @Override
        public void onRemoveStream(MediaStream mediaStream) {

            HandleOnRemoveStream(mediaStream);

        }

        @Override
        public void onDataChannel(DataChannel dataChannel) {

            HandleOnDataChannel(dataChannel);

        }

        @Override
        public void onRenegotiationNeeded() {

            HandleOnRenegotiationNeeded();
            Log.d("Renegotiation","Renegotiation has been called");

        }


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


    @Override
    protected void onResume() {
        super.onResume();





    }


    @Override
    protected void onPause() {


        super.onPause();


    }

public class ExecuteMyTask{

        Runnable runnable = null;

    ExecuteMyTask(Runnable runnable){
        this.runnable = runnable;
    }

        public void execute(){

        runnable.run();
        }

}



}









