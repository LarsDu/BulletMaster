package bulletmaster.collisions;

import bulletmaster.entity.Hero;



public interface CollidableActor {
	
	//Must have this object!
	public CollisionBox getCollisionBox();
	public void setCollisionBox(CollisionBox collisionBox);
	
	
	public void setX(int x);
	public void setY(int y);
	public void setLocation(int x,int y);
	public void setCollisionBoxX(int x);
	public void setCollisionBoxY(int y);
	public void setCollisionBoxLocation(int x, int y);
	public void checkCollision(CollidableActor actor);
	public void checkCollision(Hero hero);
	
	public void die();
	public int getX();
	public int getY();
	
	//Used for triggering events upon collision
	public void visit();
	
	

	//Alter speed of collidable object
	public int getSpeedX();
	public void setSpeedX(int speedX);
	public int getSpeedY();
	public void setSpeedY(int speedY);
	
	//State variables
	public boolean isHasFullCollision();
	public void setHasFullCollision(boolean hasFullCollision);
	public boolean isApplyGravity();
	public void setApplyGravity(boolean applyGravity);
	public boolean isAlive();




}
