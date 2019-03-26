package core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.json.JSONArray;
import org.json.JSONObject;

import base.Message;
import base.Player;
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

			digestMessages(incomingMsg, server);

		} catch (SocketTimeoutException s) {
			System.out.println("Socket timed out!");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void digestMessages(Message incomingMsg, Socket server) {

		switch (incomingMsg.getType()) {
		case ADD_PLAYER:
			addPlayer(server);
			break;
		case UPDATE_PLAYER:
			updatePlayer(incomingMsg);
			incomingMsg = new Message();
			incomingMsg.setType(MessageType.NULL);
			sendMessage(incomingMsg, server);
			break;
		case RECEIVE_PLAYERS:
			receivePlayers(incomingMsg, server);
			break;
		default:
			break;
		}
	}

	private void sendMessage(Message msg, Socket server) {
		OutputStream outToServer = null;
		DataOutputStream out = null;

		try {
			outToServer = server.getOutputStream();
			out = new DataOutputStream(outToServer);
			out.writeUTF(msg.toString());
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
			} catch (IOException e) {
				System.out.println("Client couldn't flush correctly");
				e.printStackTrace();
			}
		}
	}

	private void addPlayer(Socket server) {

		Message outgoingMsg = new Message();
		int newID = Master.mResources.addPlayer();

		outgoingMsg.setType(MessageType.ADD_PLAYER);
		outgoingMsg.setBody(newID + "");
		sendMessage(outgoingMsg, server);

	}

	private void updatePlayer(Message incomingMsg) {
		int index = Integer.parseInt(incomingMsg.getSender());
		Master.mResources.updatePlayer(index, new JSONObject(incomingMsg.getBody()));
	}

	private void receivePlayers(Message incomingMsg, Socket server) {
		JSONArray allPlayers = Master.mResources.getAllPlayers(Integer.parseInt(incomingMsg.getSender()));
		incomingMsg.setBody(allPlayers.toString());
		sendMessage(incomingMsg, server);
	}
}
