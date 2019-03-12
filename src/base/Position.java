package base;

import util.Directions;

public class Position {
	private float x;
	private float y;
	private Directions direction;
	private float speed;
	
	public Position()
	{
		x = 0;
		y = 0;
		speed = 0;
		
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Directions getDir() {
		return direction;
	}

	public void setDir(Directions dir) {
		this.direction = dir;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	
}
