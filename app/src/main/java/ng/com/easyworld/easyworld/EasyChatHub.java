package ng.com.easyworld.easyworld;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.webrtc.SessionDescription;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

/**
 * Created by Oisrael on 1/24/2018.
 */

public class EasyChatHub {


    //Caller ant Callee
    private String mUsernameToCall;
    private String mCaller;

    private String connectionId;
    private HubProxy hub;

    //Message Object that is beening sent over signalr
    private PrivateMessage pm;
    private ImageButton sendMsgBtn;
    private TextView messageText;
    private TextView messageToSend;
    private TextView conid;

    private  StringBuilder displayMsg ;

    //fINAL STRINGS
    private  final String I_AM_CONNECTED_TO_CHAT = "IN";
    private  final String I_AM_NOT_CONNECTED_T0_CHAT = "OUT";

    //EasyMessage
    private static EasyMessage messages;

    //EasyMessage Array List
    private static ArrayList<EasyMessage> list_of_Messages;

    //EasyMessage
    private static EasyMessage mMessages;

    //EasyMessage Array List
    private ArrayList<EasyMessage> mList_of_Messages;

    //ADAPTER TO CONVERT EASYMESSAGE LIST TO VIEW
    private EasyChatAdapter mAdapterr ;

    //Messages ListView
    private ListView mChatMessageList;

    private  StringBuilder mDisplayMsg ;

    private TextView mMsgDisplay;

    private static Context mContext;


    /*
    private ChatControlActivity.ManageChatInstance mManageChatInstance;


    EasyChatHub(Context Context,ChatControlActivity.ManageChatInstance ManageChatInstance,
                ArrayList<EasyMessage> List_of_Message,
                EasyChatAdapter Adapterr,
                ListView ChatMessageList,
                TextView MsgDisplay,String Caller,String usernameToCall ) {
        mContext = Context;
        mList_of_Messages = List_of_Message;
        mAdapterr = Adapterr;
        mChatMessageList = ChatMessageList;
        mMsgDisplay = MsgDisplay;
        mManageChatInstance = ManageChatInstance;
        mCaller = Caller;
        mUsernameToCall = usernameToCall;
    }

*/
    //Start Chat Connection
    public void Start_Chat(String host){


        //signalr platform object
        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        final HubConnection connection = new HubConnection(host);
        hub =connection.createHubProxy("EasyHub");


        pm = new PrivateMessage();


        //Listen to MESSAGE EVENT from signalr hub
        hub.on("EasyMsg",new SubscriptionHandler2<String,String>()
        {
            @Override
            public void run (final String val1 ,final String val2){
                handleMsg(val1,val2);
            }
        },String.class,String.class);


        hub.on("LoginResponse",new SubscriptionHandler1<PrivateMessage>()

        {
            @Override
            public void run (PrivateMessage data){


                try {

                    switch (data.getTypee()) {

                        case "login":
                            handleLogin(data.getSuccess());
                            break;




                        default:
                            break;
                    }


                } catch (Exception e) {

                }


            }
        },PrivateMessage.class);


        //Start the SIGNALR CONNECTION
        SignalRFuture<Void> awaitConnection = connection.start(new ServerSentEventsTransport(connection.getLogger()));

        try

        {
            awaitConnection.get();

            connectionId = connection.getConnectionId();

            PrivateMessage loginData = new PrivateMessage();
            loginData.setTypee("login");
            loginData.setNamee(mCaller);

            //LOGIN TO CHAT
            hub.invoke("SendWebrtcConnData",loginData).get();

            //Register Chat Status
           // mManageChatInstance.Connected_To_Chat();//This References the ManageChatInstance class on the ChatControl Activity

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


    public void handleLogin(String success ) {

        displayMsg.append("You are logged in to chat!  ");
        mMsgDisplay.setText(displayMsg.toString());

    }

    public void handleOffer(SessionDescription success, String From) {



    }

    public void handleAnswer(String answer) {


    }

    public void handleCandidate(String candidate) {


    }

    public void handleMsg(final String msg,String name) {



        if (name == "Me"){
            messages.setMyMessage(msg);
        }
        else{
            messages.setOthersMessage(msg);
        }


        list_of_Messages = new ArrayList();
        list_of_Messages.add(messages);


        mAdapterr = new EasyChatAdapter(list_of_Messages, mContext.getApplicationContext());

        mChatMessageList.setAdapter(mAdapterr);


        //chatMessageList.setAdapter(adapterr);
        displayMsg.append("   " + msg);
        mMsgDisplay.setText(displayMsg.toString());

    }







}
