package syncServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	private ServerSocket serverSocket;
	private User currentUser;
	private ArrayList<User> userList;
	public Server(int port) throws IOException {
	     serverSocket = new ServerSocket(port);
	}
	public static void main(String arg[]) throws IOException{
		Server server = new Server(5555);
		server.start();
	}
	private void start() throws IOException {
		while(true){
			Socket clientSocket = serverSocket.accept();
			if(newConnection(clientSocket)) {
				
			}
			else{
				continue;
			}
			handleUpdate(clientSocket);
		}
			
	}
	//LIstens on a port after user/device is set and everyiem an update is sent it will
	//push the changes to the client
	private void handleUpdate(Socket clientSocket) {

	}
	
	
	/*
	 * runs an Auth protocol with the user two options:
	 * Login: checks username and password
	 * Register: creates new FS and adds the currentUser to a list
	 * 
	 * sets current user to the current user.
	 * 
	 * brings the new device to current state.
	 */
	public boolean newConnection(Socket socket){
		return false;
		
	}
	
	//Notifies the client it is on a new device, assigns that device uid so that it can 
	//be identified in the future
	public void newDevice() {
		
	}
}
