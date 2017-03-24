package guthboss.com.hackathonproject;

import android.app.Activity;
import android.content.Context;
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

public class MainActivity extends Activity {
    ListView notifications;
    ArrayList<String> notificationMessage;
    Button send;
    EditText userIn;
    ChatAdapter notifyAdapt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notifications = (ListView)findViewById(R.id.notificationupdates);
        notificationMessage = new ArrayList<String>();
        notifyAdapt = new ChatAdapter(this);
        send = (Button)findViewById(R.id.sendnotification);
        userIn = (EditText)findViewById(R.id.enternotification);
        notifications.setAdapter(notifyAdapt);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationMessage.add(userIn.getText().toString());
                notifyAdapt.notifyDataSetChanged();
            }
        });



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

            return result;//Change this in step 9
        }

    }
}
