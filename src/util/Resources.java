package util;

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
}
