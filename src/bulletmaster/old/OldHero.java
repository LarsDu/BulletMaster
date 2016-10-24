package bulletmaster.old;
import java.awt.Rectangle;
import java.util.ArrayList;

import bulletmaster.entity.Actor;
import bulletmaster.gfx.Screen;

public class OldHero extends Actor{
    

    //Constants 
	private final int JUMPSPEED = -18;
	private final int MOVESPEED = 5;
	private final int GROUND = 448; //Touch this and disappear or die
	private final int HERO_SCREENX = 300; //Default camera position of Hero on screen in x axis
	private final int HERO_SCREENY = 350; //Default camera position of hero on screen in y axis
	

    
	//Movement state variables
	
	private boolean jumped = false;
	private boolean ducked = false;
	private boolean readyToFire;


	
	
	//Array list for weapon projectiles
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	
	//Hero constructor
	public OldHero(int X, int Y){ //centX and centY are the spawn points
		
		
		x = X;
		y = Y;
		lastGroundY = Y;
		speedX = 0;
		speedY = 0; 

		hasFullCollision = true;
		swimming = false;
		landed = false;
		applyGravity = true;
		
		
	}
	
	
	
	
	public void update(){
		//X movement logic
		
		//Camera collision
		//if (camera1.isLeftCollision()||camera1.isRightCollision()){
			//If camera has left or right collision
			//character should be free to move around
		//	System.out.println("Camera collision!");
		//	screenCenterX += speedX;

		//}
		//if (speedX != 0 && screenX <= 448 && screenX >= 320){
		//	screenX += speedX;
		//	if (screenX < 320){
		//		screenX = 320;
		//	}else if (screenX > 448){
		//		screenX = 448;
		//	}	
			
		//}//392 376

		//Update the map centerX 
		//(track every displacement of the Hero to determine current map location)
		
		x += speedX; 
		
				
		
		///////////// Y Movement logic
		
		// Updates Y Position


		
		//Apply gravity
		if(applyGravity == true){
			speedY += 1;	
		}else{
			speedY= 0;
		}
		
			
		y += speedY;
		
		// Handles Jumping
		if (speedY > 1){
			this.setJumped (true);
	
			
		}else{
			this.setJumped(false) ;

		}

		System.out.println("mapX is " + x);
		System.out.println("mapY is " + y);
		


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
		

	}//end of update()	

	//Movement methods


	public void render(Screen screen){
		//screen.drawSolidRect(this.getX(), this.getY(), (int) this.getBoundingBox().getWidth(), (int) this.getBoundingBox().getHeight(), this.color);
		screen.drawSolidRect(this.getX(), this.getY(), 64,64, this.color);

	}




	public void moveRight(){
		speedX = MOVESPEED;
	}

	public void moveLeft(){
		speedX = -MOVESPEED;
	}
	

	public void stopRight() {
		setMovingRight(false);
		stop();
	}

	public void stopLeft() {
		setMovingLeft(false);
		stop();
	}

	private void stop() {
		if (isMovingRight() == false && isMovingLeft() == false) {
			speedX = 0;
		}


		if (isMovingRight() == false && isMovingLeft() == true) {
			moveLeft();
		}


		if (isMovingRight() == true && isMovingLeft() == false) {
			moveRight();
		}

	}
	
	public void moveJump(){
		if (jumped == false){
			speedY = JUMPSPEED;
			jumped = true;
			landed = false;
		}
	}
	
	public void shoot(){
		if(readyToFire){
		Projectile p = new Projectile(cameraX+37, cameraY+32);
		projectiles.add(p);
		}
	}
	
	

	public boolean isJumped() {
		return jumped;
		
	}


	public void setJumped(boolean jumped) {
		this.jumped = jumped;
		if (jumped == true){
			landed = false;
		}
	}


	public boolean isDucked() {
		return ducked;
	}

	public void setDucked(boolean ducked) {
		this.ducked = ducked;
	}

	


	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public void setProjectiles(ArrayList<Projectile> projectiles) {
		this.projectiles = projectiles;
	}


	public boolean isReadyToFire() {
		return readyToFire;
	}


	public void setReadyToFire(boolean readyToFire) {
		this.readyToFire = readyToFire;
	}







}//class



