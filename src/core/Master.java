package core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import util.Resources;

public class Master extends Thread {

	private ServerSocket serverSocket;
	private int port = 6066; // Establish port 6066 as a hard-coded port override

	private String message = "";
	public static Resources mResources;

	public Master() throws IOException {
		serverSocket = new ServerSocket(port); // Instantiates the server socket
		serverSocket.setSoTimeout(0); // Sets the timeout for the socket. A timeout of 0 establishes
		mResources = new Resources();
	} // an infinite timeout

	public void run() {
		while (true) {
			try {

				Socket server = serverSocket.accept(); // Wait for a connection from the client

				Thread thread = new Slave(server); // when we get a connection, start a new thread
				thread.start();
			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
}
