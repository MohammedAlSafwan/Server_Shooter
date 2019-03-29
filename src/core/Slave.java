package core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import base.Bullet;
import base.Message;
import util.MessageType;

public class Slave extends Thread {

	public Slave(Socket server) throws IOException {
		String message = "";
		try {
			DataInputStream in = // Get the data from the client
					new DataInputStream(server.getInputStream());

			message = in.readUTF();
			if (CoreThread.debugger)
				System.out.println(message);

			Message incomingMsg = new Message();
			incomingMsg.toMessage(new JSONObject(message));

			digestMessages(incomingMsg, server);

		} catch (SocketTimeoutException s) {
			if (CoreThread.debugger)
				System.out.println("Socket timed out!");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void digestMessages(Message incomingMsg, Socket server) {

		long start = 0L;
		long end = 0L;

		switch (incomingMsg.getType()) {

		case ADD_PLAYER:
			addPlayer(server);
			break;

		case UPDATE_PLAYER:
			start = System.currentTimeMillis();
			updatePlayer(incomingMsg);
			sendEmptyMsg(incomingMsg, server);
			end = System.currentTimeMillis();
			break;

		case RECEIVE_PLAYERS:
			start = System.currentTimeMillis();
			receivePlayers(incomingMsg, server);
			end = System.currentTimeMillis();
			break;

		case SEND_BULLET:
			start = System.currentTimeMillis();
			receiveBullet(incomingMsg);
			sendEmptyMsg(incomingMsg, server);
			end = System.currentTimeMillis();
			break;
			
		case RECEIVE_BULLETS:
			start = System.currentTimeMillis();
			sendBullets(incomingMsg, server);
			end = System.currentTimeMillis();
			break;
		default:
			break;
		}

		if (CoreThread.debugger)
			System.out.println("time to RECEIVE_PLAYERS (" + incomingMsg.getSender() + ") = " + (end - start) + " ms");
	}

	private void sendBullets(Message incomingMsg, Socket server) {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	void sendBullets()
		//
		// Method parameters	:	args - the method permits String command line parameters to be entered
		//
		// Method return		:	void
		//
		// Synopsis				:   
		//							
		//
		// Modifications		:
		//							Date			    Developer				Notes
		//							  ----			      ---------			 	     -----
		//							Mar 29, 2019		    Mohammed Al-Safwan				Initial setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		JSONArray allBullets = Master.mResources.getAllBullets(Integer.parseInt(incomingMsg.getSender()),
				new Date(Long.parseLong(incomingMsg.getBody())));
		incomingMsg.setBody(allBullets.toString());
		sendMessage(incomingMsg, server);
	}

	private void receiveBullet(Message incomingMsg) {

		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	void receiveBullet()
		//
		// Method parameters	:	args - the method permits String command line parameters to be entered
		//
		// Method return		:	void
		//
		// Synopsis				:   
		//							
		//
		// Modifications		:
		//							Date			    Developer				Notes
		//							  ----			      ---------			 	     -----
		//							Mar 29, 2019		    Mohammed Al-Safwan				Initial setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		Bullet newBullet = new Bullet();
		newBullet.toBullet(new JSONObject(incomingMsg.getBody()));
		Master.mResources.addBullet(newBullet);
	}

	private void sendEmptyMsg(Message incomingMsg, Socket server) {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	void sendEmptyMsg()
		//
		// Method parameters	:	args - the method permits String command line parameters to be entered
		//
		// Method return		:	void
		//
		// Synopsis				:   
		//							
		//
		// Modifications		:
		//							Date			    Developer				Notes
		//							  ----			      ---------			 	     -----
		//							Mar 29, 2019		    Mohammed Al-Safwan				Initial setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		incomingMsg.setType(MessageType.NULL);
		incomingMsg.setBody("");
		sendMessage(incomingMsg, server);
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
