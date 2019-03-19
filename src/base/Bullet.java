package base;

public class Bullet {

	private int id;
	private int reference;
	private Position position;

	public Bullet(int ID, int PID, Position startPos) {
		this.id = ID;
		this.reference = PID;
		this.position = startPos;
	}

	public int getID() {
		return id;
	}

	public int getRef() {
		return reference;
	}

	public Position getPos() {
		return position;
	}

	public void Update() {
		switch (position.getDirection()) {

		case NORTH:
			position.setY(position.getY() + position.getSpeed());
			break;

		case EAST:
			position.setX(position.getX() + position.getSpeed());
			break;

		case SOUTH:
			position.setY(position.getY() - position.getSpeed());
			break;

		case WEST:
			position.setX(position.getX() - position.getSpeed());
			break;
		}

	}
}
