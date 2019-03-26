package core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.json.JSONObject;

import base.Message;
import util.MessageType;

public class Slave extends Thread {

	public Slave(Socket server) throws IOException {
		String message = "";
		try {
			DataInputStream in = // Get the data from the client
					new DataInputStream(server.getInputStream());

			message = in.readUTF();
			System.out.println(message);

			Message incomingMsg = new Message();
			incomingMsg.toMessage(new JSONObject(message));

			switch (incomingMsg.getType()) {
			case ADD_PLAYER:
				addPlayer(server);
				break;

			default:
				break;
			}
		} catch (SocketTimeoutException s) {
			System.out.println("Socket timed out!");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void addPlayer(Socket server) {
		OutputStream outToServer = null;
		DataOutputStream out = null;

		try {
			outToServer = server.getOutputStream();
			out = new DataOutputStream(outToServer);

			Message outgoingMsg = new Message();
			int newID = Master.mResources.addPlayer();
			// if (newID >= 0) {
			outgoingMsg.setType(MessageType.ADD_PLAYER);
			outgoingMsg.setBody(newID + "");

			out.writeUTF(outgoingMsg.toString());
			//
			// } else {
			// // send an error msg
			// }

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
				outToServer.flush();
				outToServer.close();
				server.close();
			} catch (NullPointerException e1) {
				System.out.println("Client object is null");
				// e1.printStackTrace();
			} catch (IOException e) {
				System.out.println("Client couldn't flush correctly");
				e.printStackTrace();
			}
		}
	}
}
