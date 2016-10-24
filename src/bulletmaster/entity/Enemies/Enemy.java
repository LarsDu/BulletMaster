package bulletmaster.entity.Enemies;

import bulletmaster.Animation;
import bulletmaster.collisions.CollidableActor;
import bulletmaster.entity.HealthyActor;
import bulletmaster.gfx.SpriteSheet;


public class Enemy extends HealthyActor implements CollidableActor{
	

	protected boolean jumped = false;
	
	

	public void update(){
		if (!alive){
			return;
		}
		x += speedX;
		y += speedY;
	}
	
	
	public void moveRight(){
		speedX = (int) baseSpeed;
		setMovingRight(true);
		setMovingLeft(false);
		setFacingRight(true);
		setFacingLeft(false);
	}

	public void moveLeft(){
		speedX = (int) -baseSpeed;
		setMovingRight(false);
		setMovingLeft(true);
		setFacingRight(false);
		setFacingLeft(true);
	}
	
	public void moveUp(){
		speedY = (int) -baseSpeed;
		setMovingUp(true);
		setMovingDown(false);
	}
	public void moveDown(){
		speedY = (int) baseSpeed;
		setMovingDown(true);
		setMovingUp(false);
	}

	public void moveJump(){
		if (jumped == false){
			speedY = -12;
			jumped = true;
			landed = false;
		}
	}
	
	
	//Behavioral methods
	public void die(){
	}

	
	
}



