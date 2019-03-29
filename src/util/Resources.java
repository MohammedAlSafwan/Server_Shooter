package util;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import base.Bullet;
import base.Player;
import base.Position;

public class Resources {

	private Player[] mPlayers;
	private ArrayList<Bullet> mBullets;

	public Resources() {
		mPlayers = new Player[8];
	}

	public int addPlayer() {
		int newID = -1;
		for (int index = 0; index < mPlayers.length; index++) {
			if (null == mPlayers[index]) {
				mPlayers[index] = new Player(index, new Position(0, 0, Directions.NORTH), "Player" + index);
				newID = index;
				break;
			}
		}

		return newID;
	}

	public Player[] getPlayers() {
		return mPlayers;
	}

	synchronized public void updatePlayer(int index, JSONObject updatedPlayer) {
		mPlayers[index].toPlayer(updatedPlayer);
		//		System.err.println("updated player " + index);
		//		System.err.println(mPlayers[index].toPrettyString());

	}

	public JSONArray getAllPlayers(int senderID) {
		JSONArray allPlayers = new JSONArray();
		for (int index = 0; index < mPlayers.length; index++) {
			if (null != mPlayers[index] && senderID != index)
				allPlayers.put(mPlayers[index].toJSON());
		}
		return allPlayers;
	}

	synchronized public void addBullet(Bullet newBullet) {
		mBullets.add(newBullet);
	}

	public JSONArray getAllBullets(int playerID, Date lastBulletDate) {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	JSONArray getAllBullets()
		//
		// Method parameters	:	args - the method permits String command line parameters to be entered
		//
		// Method return		:	JSONArray
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
		JSONArray allBullets = new JSONArray();
		for (Bullet bullet : mBullets) {
			if (null != bullet && bullet.getReference() != playerID && bullet.compareTo(bullet) > 0)
				allBullets.put(bullet.toJSON());
		}

		return allBullets;
	}
}
