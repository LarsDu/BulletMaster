package bulletmaster;

import java.awt.Rectangle;
import java.util.ArrayList;

import bulletmaster.entity.Hero;

public class Camera {

	
	
	//Camera center values have no bearing on camera position (yet)
	private int x; 
	private int y;
	

	
	private int speedX;
	private int speedY;
	private int width;
	private int height;

	//Camera will attempt to keep subject centered at these coordinates 
	//on the screen
	private int camTrackX;
	private int camTrackY;
	

	
	//Coordinates for the upper left corners of the rectangular camera
	//Set default Collision states
	private boolean leftCollision = false;
	private boolean rightCollision = false;
	private boolean topCollision = false;
	private boolean bottomCollision = false;
	
	//Rectangle encompassing camera dimensions
	private Rectangle camRect;
	private Rectangle levelRect;
	
	
	//Parameters for trackTargetPlatform mode and camera panning
	private static int PANSPEEDX = 8;
	private static int PANSPEEDY = 8;
	private int trackingDeltaY; //Difference between Target.mapCenterY and Camera.mapCenterY
	


	
	public Camera(int camWidth, int camHeight, int levelWidth, int levelHeight){
		//System.out.println("Lights, camera, action!");
		this.width = camWidth;
		this.height = camHeight;
		camRect = new Rectangle(0,0,camWidth,camHeight);
		levelRect = new Rectangle(0,0,levelWidth, levelHeight);
		x = 0;  
		y = 0;
		speedX=0;
		speedY= 0;
		camTrackX = getCenterX(); //this might be bugged. fix later...
		camTrackY = getCenterY();
	
		
	}
	
	

	public void update(Hero target){
		
		//Target tracking
		//For Heroes (human players) camera behavior adjust contextually.
		// If Target is swimming, camera tracks target perfectly 

		
		trackTargetPerfect(target);
		//trackTargetPerfect(target);
		//will keep the camera centered on the last platform the player jumped on
		//trackTargetPlatform(Target);
		
		
		//Check map boundaries.
		//The camera must never move outside the boundaries of the map
		//This is prevented by a form of collision detection.
		
		checkCameraCollision();
		
		
		
		//Set camera bounds based on tracking
		
		
	
		
	}

	
	//Camera functions.

	
	
	public void trackTargetSimple(Hero Target){
		//Set camera displacement to character displacement
		speedX = Target.getSpeedX();
		//System.out.println("Target speed Y is " + Target.getSpeedY());
		if(Target.isLanded() == true){
			speedY = 0;	
		}else{
			speedY = Target.getSpeedY();
			//System.out.println("Camera tracking Y"); 
		}
		
		
		x += speedX;
		y += speedY;
		
		
	
		camRect.setLocation(x,y);

	}
	
	
	
	public void trackTargetPerfect(Hero Target){
		final int PAN_MARGIN = 32; //number of pixels the target moves in 
		                           //any direction before tracking starts
		//Set camera displacement to character displacement
		speedX = Target.getSpeedX();
		speedY = Target.getSpeedY();

		if (speedY == 1){
			speedY =0;
		}
		//If the character steps outside of designated center, move camera to recenter around character
	
		int curCamTrackX = camTrackX+x;
		int curCamTrackY = camTrackY+y;
		int deltaX = curCamTrackX - Target.getX();
		int deltaY = curCamTrackY - Target.getY();
		int modDeltaX = deltaX%PANSPEEDX;
		int modDeltaY = deltaY%PANSPEEDY;
		//Move camera by delta 
		//System.out.println("**");
		//System.out.println("deltaX is"+deltaX);		
		//System.out.println("modDeltaX is"+modDeltaX);		
	
		if (deltaX<-PAN_MARGIN){
			if(modDeltaX!=0){
				speedX -= modDeltaX;
			}else{
				speedX = PANSPEEDX;		
			}
		}else if (deltaX>PAN_MARGIN){
			if(modDeltaX!=0){
				speedX = -modDeltaX;
			}else{
				speedX = -PANSPEEDX;
			}
		}
		
		
		//System.out.println("deltaY is"+deltaY);		
		//System.out.println("modDeltaY is"+modDeltaY);		
			

		if(deltaY <-PAN_MARGIN){
			
			if(modDeltaY!=0){
				//speedY -= modDeltaY;
				//speedY -=20;
			}else{
				
				speedY = PANSPEEDY;	
			}
		}else if (deltaY>PAN_MARGIN){
			if(modDeltaY!=0){
				speedY = -modDeltaY;
			}else{
				speedY = -PANSPEEDY;
			}
		}



		x += speedX;
		y += speedY;
		camRect.setLocation(x,y);
	}

	
	
	public void trackTargetPlatform(Hero Target){
		//Perfect target tracking
		speedX = -Target.getSpeedX();//Match camera speed to target speed
		x -= speedX;
		
		
		//Move camera toward Target's groundCenter
		trackingDeltaY = y - (Target.getLastGroundY()-256);
		
		//Tracking modulo makes sure camera step increments are divisible by the panspeed
		//This prevents "vibrations" around the zero point
		final int trackingModulo = trackingDeltaY%PANSPEEDY;
		//In Java % has same sign as dividend, while Math.floormod has same sign as divisor
		//Dividend = numerator
		
		//System.out.println("trackingDeltaY " + trackingDeltaY);
		//System.out.println("trackingModulo " + trackingModulo);
		
		
		if(Target.isLanded() == true){
			if(trackingModulo != 0){
				speedY = trackingModulo;
			}else{
				if(trackingDeltaY == 0){
					speedY = 0;
				}else if(trackingDeltaY < 0){
					speedY = -PANSPEEDY; //Move the camera up (-)
					
				}else if(trackingDeltaY > 0){
					speedY = PANSPEEDY; //Move the camera down
		
				}
			}
					
					
		}else{
			//System.out.println("False landing");
		}
		y -= speedY;
		
		
		//speedY = 0;
		
		//System.out.println("Hero mapX " + Target.getMapX());
		//System.out.println("Hero mapY " + Target.getMapY());
		
		//System.out.println("Landed is " + Target.isLanded());
		//System.out.println("Target speed Y is " + Target.getSpeedY());
		//System.out.println("Ground center Y is " + Target.getGroundY());
		//System.out.println("Cam center Y is " + mapCenterY);
		//System.out.println("Target mapCenterY is " + Target.getMapY());
	}
	

	public void checkCameraCollision(){
		
		//if (!levelRect.contains(camRect)){
		//	return;
		//}

		if (this.x< levelRect.getMinX()){
			x = (int) levelRect.getMinX();
			this.speedX = 0;
		}else if (this.x+this.width > levelRect.getMaxX()){
			x = (int) (  levelRect.getMaxX() - this.width);
			this.speedX =0;
		}
		if(this.y < levelRect.getMinY()){
			y = (int) levelRect.getMinY();
			this.speedY = 0;
		}else if (this.y +this.height >= levelRect.getMaxY()){
			y = (int) levelRect.getMaxY() - this.height;
			this.speedY = 0;
		}
	}
	
	
	
	
	
	
	
	
	

	
	
	
	
	public void updateOnScreenList(){
		//Update the list of objects that are currently onscreen
		
	
	}
		
	
	
	
	
	
	
	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int xCoord) {
		x = xCoord;
	}

	public int getY() {
		return y;
	}

	public void setY(int yCoord) {
		y = yCoord;
	}

	public Rectangle getCamRect() {
		return camRect;
	}

	public void setCamRect(Rectangle camRect) {
		this.camRect = camRect;
	}

	public Rectangle getMapRect() {
		return levelRect;
	}

	public void setMapRect(Rectangle mapRect) {
		this.levelRect = mapRect;
	}



	public boolean isLeftCollision() {
		return leftCollision;
	}

	public void setLeftCollision(boolean leftCollision) {
		this.leftCollision = leftCollision;
	}

	public boolean isRightCollision() {
		return rightCollision;
	}

	public void setRightCollision(boolean rightCollision) {
		this.rightCollision = rightCollision;
	}

	public boolean isTopCollision() {
		return topCollision;
	}

	public void setTopCollision(boolean topCollision) {
		this.topCollision = topCollision;
	}

	public boolean isBottomCollision() {
		return bottomCollision;
	}

	public void setBottomCollision(boolean downCollision) {
		this.bottomCollision = downCollision;
	}

	public int getCamTrackX() {
		return camTrackX;
	}

	public void setCamTrackX(int camTrackX) {
		this.camTrackX = camTrackX;
	}

	public int getCamTrackY() {
		return camTrackY;
	}

	public void setCamTrackY(int camTrackY) {
		this.camTrackY = camTrackY;
	}

	public int getCenterX(){
		return (int) this.x + (this.width/2) - 64;
	}
	
	public int getCenterY(){
		return (int) this.y + (this.height/2)+20;
	}


}