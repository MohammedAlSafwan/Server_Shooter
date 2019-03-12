package base;

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
		switch (Pos.Direction) 
		{
		
		case Direction.NORTH: 
			Pos.y += Pos.speed;
			break;
		
		case Direction.EAST:
			Pos.x += Pos.speed;
			break;
		
		case Direction.SOUTH:
			Pos.y -= Pos.speed;
			break;
		
		case Direction.WEST:
			Pos.x -= Pos.speed; 
			break;
		}
			
	}
}
