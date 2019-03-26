package util;

import org.json.JSONArray;
import org.json.JSONObject;

import base.Player;
import base.Position;

public class Resources {

	private Player[] mPlayers;

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

	public void updatePlayer(int index, JSONObject updatedPlayer) {
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
}
