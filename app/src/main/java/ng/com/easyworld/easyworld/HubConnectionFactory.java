package ng.com.easyworld.easyworld;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.webrtc.SessionDescription;

import java.util.ArrayList;

import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.InvalidStateException;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

/**
 * Created by Oisrael on 1/17/2018.
 */

public class HubConnectionFactory {


    private Context mContext;
    private static HubConnectionFactory mInstance= null;
    private HubConnection mConnection;
    private HubProxy mChat;

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




    //////////////////////////////////////////////////
    //CONSTRUCTOR
    protected HubConnectionFactory() {}
    //////////////////////////////////////////////////



    //////////////////////////////////////////////////
    //CONSTRUCTOR
    public void HubConnectionSet_References(Context Context,
                                   ArrayList<EasyMessage> List_of_Message,
                                   EasyChatAdapter Adapterr,
                                   ListView ChatMessageList,
                                   TextView MsgDisplay )
    {
        mContext = Context;
        mList_of_Messages = List_of_Message;
        mAdapterr = Adapterr;
        mChatMessageList = ChatMessageList;
        mMsgDisplay = MsgDisplay;
    }
    //////////////////////////////////////////////////


    public static synchronized HubConnectionFactory getInstance(){
        if(null == mInstance){
            mInstance = new HubConnectionFactory();
        }
        return mInstance;
    }

    public HubConnection getHubConnection() {
        return mConnection;
    }

    public HubProxy getChatHub() {
        return mChat;
    }

    public SignalRFuture<Void> connect(String url) {
        final SignalRFuture<Void> future = new SignalRFuture<Void>();
        createObjects(url, future);

        return future;
    }

    public void createObjects(String url, final SignalRFuture<Void> future){

        mConnection = new HubConnection(url, "", true, new Logger() {

            @Override
            public void log(String message, LogLevel level) {
                if (level == LogLevel.Critical) {
                    Log.d("SignalR", level.toString() + ": " + message);
                }
            }
        });


        try {
            mChat = mConnection.createHubProxy("ChatHub");
        } catch (InvalidStateException e) {
            Log.d("SignalR", "Error getting creating proxy: " + e.toString());
            future.triggerError(e);
        }




        //Listen to MESSAGE EVENT from signalr hub
        mChat.on("EasyMsg",new SubscriptionHandler2<String,String>()
        {
            @Override
            public void run (final String val1 ,final String val2){

                            handleMsg(val1,val2);

            }
        },String.class,String.class);




        mChat.on("LoginResponse",new SubscriptionHandler1<PrivateMessage>()

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
                            break;





                        default:
                            break;
                    }


                } catch (Exception e) {

                }


            }
        },PrivateMessage.class);





        //ClientTransport transport = new LongPollingTransport(mConnection.getLogger()); // works as expected
        ClientTransport transport = new ServerSentEventsTransport(mConnection.getLogger()); // Works on WiFi, never connects on 3G, no error is thrown either

        //ClientTransport transport = new WebsocketTransport(mConnection.getLogger()); // Never connects, not error is thrown
        SignalRFuture<Void> connectionFuture = mConnection.start(transport);


        mConnection.connected(new Runnable() {
            @Override
            public void run() {

                future.setResult(null);
            }
        });



        mConnection.error(new ErrorCallback() {
            @Override
            public void onError(Throwable error) {
                Log.d("SignalR", "Connection error: " + error.toString());

                if (!future.isDone()) {
                     future.triggerError(error);
                }
            }
        });
    }

    public void disconnect() {
        mChat = null;
        mConnection.stop();
    }






    public void handleLogin(String success ) {

       // displayMsg.append("You are logged in to chat!  ");
       // msgDisplay.setText(displayMsg.toString());

    }

    public void handleOffer(SessionDescription success, String From) {



    }

    public void handleAnswer(String answer) {


    }

    public void handleCandidate(String candidate) {


    }

    public void handleMsg(final String msg,String name) {



        if (name == "Me"){
            mMessages.setMyMessage(msg);
        }
        else{
            mMessages.setOthersMessage(msg);
        }


        mList_of_Messages = new ArrayList();
        mList_of_Messages.add(mMessages);


        mAdapterr = new EasyChatAdapter(mList_of_Messages, mContext.getApplicationContext());

        mChatMessageList.setAdapter(mAdapterr);


        //chatMessageList.setAdapter(adapterr);
        mDisplayMsg.append("   " + msg);
        mMsgDisplay.setText(mDisplayMsg.toString());

    }








}
