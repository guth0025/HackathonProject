package guthboss.com.hackathonproject;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class AnnounceServerAsyncTask extends AsyncTask<String,String,String> {

    @Override
    protected String doInBackground( String... params) {
        try (ServerSocket server = new ServerSocket(8888)){
            try (Socket client = server.accept()){
                try (Connection connect = new Connection()){
                    connect.connectTo(client);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute( String result) {

    }

    @Override
    protected void onProgressUpdate(String... progress) {

    }
}