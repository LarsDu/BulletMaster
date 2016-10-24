package bulletmaster.old;

import bulletmaster.entity.Enemies.Enemy;



public class Hornworm extends Enemy {

	public Hornworm(int X, int Y) {    
		
		x = X;
		y = Y;
		hasFullCollision = true;
		applyGravity = true; 
		swimming = false;
	}
	
	
	public void update(){
		x += speedX;
		y += speedY;
		
		
		///////////// Y Movement logic
		
		// Updates Y Position

		//Apply gravity
		if(applyGravity == true){
			speedY += 1;	
		}else{
			speedY= 0;
		}
		
		
		
		///////////// Collision shape update
		
		//Set collision iteration decision box
		influenceRect.setRect(x-128,y-128,256,256);
		
		
		//Set bounding boxes
		boundingBox.setRect(x,y,32,64);

		
		//Set collision sensor boxes
		upperLeftSensor.setRect(boundingBox.getMinX(),boundingBox.getMinY()+10,10,10);
		upperRightSensor.setRect(boundingBox.getMaxX()-10,boundingBox.getMinY()+10,10,10);
		lowerLeftSensor.setRect(boundingBox.getMinX()+4,boundingBox.getMaxY()-10,10,10);
		lowerRightSensor.setRect(boundingBox.getMaxX()-14,boundingBox.getMaxY()-10,10,10);
		topSensor.setRect(boundingBox.getMaxX()-21, boundingBox.getMinY(),10,10);
		
	}
	
	
	//default constructor, no parameters given
	public Hornworm(){

	}	
	
	
}