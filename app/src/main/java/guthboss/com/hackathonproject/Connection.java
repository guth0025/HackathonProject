package guthboss.com.hackathonproject;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * primary class for sending and receiving text from client or server
 */
public class Connection implements Closeable{
	
	private Socket socket = null;
	private InputStream input;
	private PrintWriter output;
	private InetSocketAddress address;
	private volatile boolean isConnected = false;

	/**
	 * state of the current connection
	 * @return true if connectTo() has been successfully called
	 * 			and false if disconnect has been called
	 */
	public boolean isConnected(){
		return isConnected;
	}
	
	@Override
	public String toString(){
		return socket.toString();
	}
	
	/**
	 * connect to a specific IP and port; and create in out streams
	 * @param ip - destination IP address
	 * @param port - destination port
	 * @return true if all streams are created and connection established, otherwise false
	 * @throws IOException - any caught exception will be thrown again
	 */
	public boolean connectTo( String ip, int port) throws IOException{
		if( ip==null || port<=0){
			throw new IllegalArgumentException( "IP address cannot be null, port must be a positive none zero integer");
		}
		try {
			address = new InetSocketAddress( ip, port);
			socket = new Socket();
			socket.connect( address);
			connectTo( socket);
		} catch( IOException e){
			disconnect();
			throw e;
		}
		return isConnected;
	}
	
	/**
	 * connect to a specific socket and create in out streams
	 * @param socket destination socket containing valid address
	 * @return true if all streams are created and connection established, otherwise false
	 * @throws IOException - any caught exception will be thrown again
	 */
	public boolean connectTo( Socket socket) throws IOException{
		if( socket==null){
			throw new IllegalArgumentException( "Socket cannot be null");
		}
		this.socket = socket;
		try {
			output = new PrintWriter( socket.getOutputStream());
            input = socket.getInputStream();
			isConnected = true;
		} catch( IOException e){
			disconnect();
			throw e;
		}
		return isConnected;
	}
	
	/**
	 * send a message to destination
	 * @param message - information to be sent as string
	 * @return true if information has been sent, else false
	 */
	public void send( String message) throws IOException{
		if( isConnected){
			output.print( message);
			output.flush();
		}else{
			throw new IOException( "No connection is available");
		}
	}
	
	/**
	 * Receive response from destination in form of a String
	 * @return a String if message is received, otherwise null
	 */
	public String recieve() throws IOException, ClassNotFoundException{
		if( isConnected){
			String message = null;
			Scanner scan = new Scanner( input);
			if( !scan.hasNext()){
				isConnected = false;
			}
			return scan.next();
		}else{
			throw new IOException( "No connection is avalibale");
		}
	}

	@Override
	public void close(){
		disconnect();
	}

	/**
	 * close all streams and socket
	 */
	public void disconnect() {
		if( socket!=null){
			if( input!=null){
				try {
					input.close();
				} catch ( IOException e) {
					e.printStackTrace();
				}
			}
			if( output!=null){
				output.close();
			}
			if( socket!=null){
				try {
					socket.close();
				} catch ( IOException e) {
					e.printStackTrace();
				}
			}
			input = null;
			output = null;
			socket = null;
		}
		isConnected = false;
	}
}