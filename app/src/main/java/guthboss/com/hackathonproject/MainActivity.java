package guthboss.com.hackathonproject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends Activity {
    ListView notifications;
    ArrayList<String> notificationMessage;
    Button send, test;
    EditText userIn;
    ChatAdapter notifyAdapt;
    WifiP2pManager mManager;
    Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //create receiver
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        setContentView(R.layout.activity_main);
        notifications = (ListView)findViewById(R.id.notification_updates);
        notificationMessage = new ArrayList<>();
        notifyAdapt = new ChatAdapter(this);
        send = (Button)findViewById(R.id.send_notification);
        test = (Button)findViewById(R.id.test_connection);
        userIn = (EditText)findViewById(R.id.enter_notification);
        notifications.setAdapter(notifyAdapt);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextToChat(userIn.getText().toString());
                userIn.setText("");
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.discoverPeers( mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        addTextToChat("Success");
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        addTextToChat("failed: " + reasonCode);
                    }
                });
            }
        });
    }

    public void addTextToChat( String str){
        notificationMessage.add( str);
        notifyAdapt.notifyDataSetChanged();
    }

    public <E> void addListToChat( Collection<E> list){
        for( E e:list){
            notificationMessage.add( e.toString());
        }
        notifyAdapt.notifyDataSetChanged();
    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private class ChatAdapter extends ArrayAdapter<String>
    {
        public ChatAdapter(Context ctx)
        {
            super(ctx,0);

        }
        @Override
        public int getCount()
        {
            return notificationMessage.size();
        }

        @Override
        public String getItem(int position)
        {
            return notificationMessage.get(position);
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View result = null;
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();


            result = inflater.inflate(R.layout.notificationlayout,null);

            TextView message = (TextView)result.findViewById(R.id.notificationtextview);
            message.setText(getItem(position));

            return result;
        }

    }
}
