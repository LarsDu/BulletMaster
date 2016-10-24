package bulletmaster.entity;
import java.awt.Rectangle;

import bulletmaster.Camera;
import bulletmaster.LevelObject;
import bulletmaster.Tiles.BaseTile;
import bulletmaster.collisions.CollisionHandler;
import bulletmaster.gfx.Screen;


public class Entity extends LevelObject{
	
	protected static Camera camera;
	protected static Screen screen;
	
	public static final int MAX_SPEEDX =12;
	public static final int MAX_SPEEDY =24;

	//Coordinates

	//These x and y values are the global position of the object
	protected int x;
	protected int y;
	
	protected int speedX;
	protected int speedY;
	
	//NEVER SET camera coordinate values. These are always calculated from global x and y coordinates
	//and only when the Entity is onCamera
	protected int cameraX;
	protected int cameraY;

	//protected int width;
	//protected int height;
	
	protected boolean hasFullCollision = true;
	
	protected boolean applyGravity = true;
	
	//Movement state variables
	protected boolean movingUp = false;
	protected boolean movingDown = false;
	protected boolean movingLeft = false;
	protected boolean movingRight = false;
	
	protected boolean facingRight = false;
	protected boolean facingLeft = false;
	
	//Tells the engine if the object is onscreen
	protected boolean onCamera = true;

	protected boolean alive = true;

	
	
	//Bounding box for simple collisions
	protected Rectangle boundingBox;
	//protected Camera camera1 = StartingClass.getCamera1();

	
	
	//Get and set methods
	
	public void init(){
		
		
	}
	
	public void update(){
		
	}
	
	public void render(){
		
	}
	
	public static void setCamera(Camera camera){
		Entity.camera = camera;
	}
	
	
	public void checkOnCamera(Camera camera){
		// Checks if current entity is onscreen
		// Switches onscreen flag to true or false 
		// based on whether object is onscreen or not.
		if (this.boundingBox != null){ 
			if ( (int) this.boundingBox.getMaxX() < camera.getX() 
			 ||  (int) this.boundingBox.getX() > camera.getX()+camera.getWidth()
			 ||  (int) this.boundingBox.getMaxY() < camera.getY() 
			 ||  (int) this.boundingBox.getY() > camera.getY()+camera.getHeight() ){
				
				this.onCamera = false;
			}else{
				this.onCamera = true;
			}
		}
	}
	


	
	public int getCameraX(Camera camera) {
		cameraX = this.x - camera.getX();
		return cameraX;
	}



	public int getCameraY(Camera camera) {
		cameraY = this.y - camera.getY();
		return cameraY;
	}

	public int getTileIndX(){
		//Retrieve the x index of the tile that this object's x coordinate overlaps with
		int xInd = this.x/BaseTile.STDWIDTH;
		if(xInd<0){xInd = 0;}
		return xInd;
		
	}
	
	public int getTileIndY(){
		//Retrieves the y index of the tile that this object's x coordinate overlaps with.
		//System.out.println((this.y/BaseTile.STDWIDTH));
		
		int yInd = this.y/BaseTile.STDHEIGHT;
		if(yInd<0){yInd = 0;}
		return yInd;
		
	}

	//public int getWidth(){
	//	return this.width;
	//}
	
	//public int getHeight(){
	//	return this.height;
	//}
	
	public boolean isHasFullCollision() {
		return hasFullCollision;
	}

	public void setHasFullCollision(boolean hasFullCollision) {
		this.hasFullCollision = hasFullCollision;
	}

	public boolean isApplyGravity() {
		return applyGravity;
	}

	public void setApplyGravity(boolean applyGravity) {
		this.applyGravity = applyGravity;
	}

	public boolean isMovingUp() {
		return movingUp;
	}

	public void setMovingUp(boolean movingUp) {
		this.movingUp = movingUp;
	}

	public boolean isMovingDown() {
		return movingDown;
	}

	public void setMovingDown(boolean movingDown) {
		this.movingDown = movingDown;
	}

	public boolean isMovingLeft() {
		return movingLeft;
	}

	public void setMovingLeft(boolean movingLeft) {
		this.movingLeft = movingLeft;
		if (movingLeft == true){
			this.setFacingLeft(true);
			this.setFacingRight( false);
		}
	}

	public boolean isMovingRight() {
		return movingRight;
	}

	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
		if (movingRight == true){
			this.setFacingRight( true);
			this.setFacingLeft( false);
		}
	}

	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		if (speedX > Entity.MAX_SPEEDX){
			this.speedX = Entity.MAX_SPEEDX;
		}else{
			this.speedX = speedX;
		}
		
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setSpeedY(int speedY) {
		if (speedY > Entity.MAX_SPEEDY){
			speedY = Entity.MAX_SPEEDY;
		}else{
			this.speedY = speedY;
		}
	}
	
	//Bounding box methods

	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(Rectangle boundingBox) {
		this.boundingBox = boundingBox;
	}
	
	//Collision detect rect methods
	//this rect is used to determine if collision calculations need to be made
	


	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}



	public int getY() {
		return y;
	}


	public boolean isFacingRight() {
		return facingRight;
	}

	public void setFacingRight(boolean facingRight) {
		this.facingRight = facingRight;
	}

	public boolean isFacingLeft() {
		return facingLeft;
	}

	public void setFacingLeft(boolean facingLeft) {
		this.facingLeft = facingLeft;
	}

	public boolean isOnCamera() {
		return onCamera;
	}

	public void setOnCamera(boolean onCamera) {
		this.onCamera = onCamera;
	}

	public void setLocation(int X, int Y){
		//Reposition x and y (upper left corner) of entity
		//and reposition bounding box.
		this.setX(X);
		this.setY(Y);
		if (boundingBox != null){
			boundingBox.setLocation((int) X, (int) Y);
		}
	}

	public static void setScreen(Screen screen){
		Entity.screen = screen;
	}
	
	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	
	
}
