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
	private int[] mKills;
	private final int MAX_PLAYERS = 8;
	private final int MAX_KILLS = 8;
	public boolean isGameOver = false;

	public Resources() {
		mPlayers = new Player[MAX_PLAYERS];
		mBullets = new ArrayList<>();
		mKills = new int[MAX_PLAYERS];
	}

	synchronized public void addKill(int killerID) {
		mKills[killerID]++;
		isGameOver = mKills[killerID] >= MAX_KILLS;
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
		// System.err.println("updated player " + index);
		// System.err.println(mPlayers[index].toPrettyString());

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
		JSONArray allBullets = new JSONArray();
		if (!mBullets.isEmpty())
			for (Bullet bullet : mBullets) {
				if (null != bullet && bullet.getReference() != playerID
						&& bullet.getCreationDate().after(lastBulletDate))
					allBullets.put(bullet.toJSON());
			}

		return allBullets;
	}

	public String getTop3Players() {
		StringBuilder top3Players = new StringBuilder();
		int checks = 3;

		for (int highestScore = MAX_KILLS; highestScore > 0; highestScore--) {
			for (int playerID = 0; playerID < MAX_PLAYERS; playerID++) {
				int playerScore = mKills[playerID];

				if (highestScore == playerScore) {
					checks--;
					top3Players.append(playerID + " " + playerScore + " ");
				}

				if (checks == 0)
					return top3Players.toString();
			}
		}
		return top3Players.toString();
	}
}
