package syncClient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	Socket socket;
	String device;
	
	//Has to do do that auth protocol with 
	public Client(String username, String password, boolean register) throws UnknownHostException, IOException {
		socket = new Socket("", 5555);//SHOULD BE CONSTANTS
	}

	//call back from somebody else, liek a file has been chagned, need totell the server!
	public void update(String info){
		
	}
	
	//closes the connection, goes to offline mode!
	public void logoff(){

	}
	
}
