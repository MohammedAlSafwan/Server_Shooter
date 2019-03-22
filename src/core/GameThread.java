package core;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class GameThread extends Thread {

	public GameThread(Socket server) throws IOException {
		String message = "";
		try {
			DataInputStream in = // Get the data from the client
					new DataInputStream(server.getInputStream());
			
			message = in.readUTF();
			
			System.out.println(message);
			
		} catch (SocketTimeoutException s) {
			System.out.println("Socket timed out!");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
