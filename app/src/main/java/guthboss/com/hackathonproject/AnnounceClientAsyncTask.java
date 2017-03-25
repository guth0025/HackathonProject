package guthboss.com.hackathonproject;

import android.os.AsyncTask;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class AnnounceClientAsyncTask extends AsyncTask<String,String,String> {

    @Override
    protected String doInBackground( String... params) {
        try (ServerSocket server = new ServerSocket(8888)){
            try (Socket client = server.accept()){
                try (PrintWriter output = new PrintWriter(client.getOutputStream())){
                    output.write( params[0]);
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