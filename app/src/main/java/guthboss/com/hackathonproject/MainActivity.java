package guthboss.com.hackathonproject;

import android.app.Activity;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    ListView notifications;
    ArrayList<String> notificationMessage;
    Button send, test;
    EditText userIn;
    ChatAdapter notifyAdapt;
    WiFiDirectBroadcastReceiver wifiDirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //create receiver
        WifiP2pManager manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        Channel channel = manager.initialize(this, getMainLooper(), null);
        wifiDirect = new WiFiDirectBroadcastReceiver(manager, channel, this);

        setContentView(R.layout.activity_main);
        notifications = (ListView)findViewById(R.id.notification_updates);
        notificationMessage = new ArrayList<>();
        notifyAdapt = new ChatAdapter(this);
        send = (Button)findViewById(R.id.send_notification);
        test = (Button)findViewById(R.id.test_connection);
        userIn = (EditText)findViewById(R.id.enter_notification);
        notifications.setAdapter(notifyAdapt);

        notifications.setOnItemClickListener( new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = ((TextView) view.findViewById( R.id.notificationtextview)).getText().toString();
                selected = selected.split("\n")[1];
                wifiDirect.connectTo( selected);
            }
        });

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
                wifiDirect.discoverPeers();
            }
        });

        test.setOnLongClickListener( new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }

    public void clearChat(){
        notificationMessage.clear();
        notifyAdapt.notifyDataSetChanged();
    }

    public void addTextToChat( String ... strings){
        for( String str:strings){
            notificationMessage.add( str);
        }
        notifyAdapt.notifyDataSetChanged();
    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(wifiDirect, wifiDirect.getIntenets());
    }

    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiDirect);
    }

    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx) {
            super(ctx,0);

        }
        @Override
        public int getCount() {
            return notificationMessage.size();
        }

        @Override
        public String getItem(int position) {
            return notificationMessage.get(position);
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();


            View result = inflater.inflate(R.layout.notificationlayout,null);

            TextView message = (TextView)result.findViewById(R.id.notificationtextview);
            message.setText(getItem(position));

            return result;
        }
    }
}
