package base;

import util.Directions;

public class Bullet {
	private int ID; 
	private int Ref; 
	private Position Pos; 

	public Bullet(int ID, int PID, Position startPos)
	{
		this.ID = ID;
		this.Ref = PID; 
		this.Pos = startPos;
	}

	public int getID() 
	{
		return ID;
	}
	
	public int getRef() 
	{
		return Ref;
	}
	
	public Position getPos() 
	{
		return Pos;
	}
	
	public void Update() 
	{
		switch (Pos.getDirection()) 
		{
		
		case NORTH: 
			Pos.setY(Pos.getY() + Pos.getSpeed());
			break;
		
		case EAST:
			Pos.setX(Pos.getX() + Pos.getSpeed());
			break;
		
		case SOUTH:
			Pos.setY(Pos.getY() - Pos.getSpeed());
			break;
		
		case WEST:
			Pos.setX(Pos.getX() - Pos.getSpeed()); 
			break;
		}
			
	}
}
