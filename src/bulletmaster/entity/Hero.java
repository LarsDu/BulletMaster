package bulletmaster.entity;
import java.util.ArrayList;

import bulletmaster.Animation;
import bulletmaster.Camera;
import bulletmaster.InputHandler;
import bulletmaster.collisions.CollidableActor;
import bulletmaster.collisions.CollisionBox;
import bulletmaster.collisions.CollisionHandler;
import bulletmaster.entity.Enemies.BoarEnemy;
import bulletmaster.gfx.SpriteSheet;
import bulletmaster.old.Projectile;

public class Hero extends HealthyActor implements CollidableActor{
    
	//Control
	private InputHandler input;
	
    //Constants 
	private final int JUMPSPEED = -20;
	private double speedGnd = 0;

	
	
	//Finite state machine enums and states
	private enum MoveState {
		JUMPING, DUCKING, RUNLEFT, RUNRIGHT, STOPPED
	}
	
	private enum WallState{
		GROUND, RIGHTWALL, LEFTWALL, CEILING, AIR, WATER, WALLJUMP
	}
	
	private MoveState moveState = MoveState.STOPPED;
	private WallState wallState = WallState.AIR;



	private double angle = 0; //The angle of the surface our hero is standing on.

	
	//The problem with retrieving the game collisionhandler is we do not know if the collisionhandler has been initialized yet!
	//private CollisionHandler collisionHandler = Game.getCollisionHandler();
	

	//Array list for weapon projectiles
	private ArrayList<Projectile> projectiles;

	private int ammoPoolSize;
	
	
	
	//Hero image variables
	private static SpriteSheet sheet = null;
	private static String imgSource = null;
	
	//Empty constructor
	public Hero(){

		
	}
	
	
	//Hero constructor	
	//Wall state variables



	private boolean wallJumping = false;
	public Hero(int X, int Y){ //centX and centY are the spawn points
		
		
				
		alive = true;
		lastGroundY = Y;
		speedX = 0;
		speedY = 0; 
		hasFullCollision = true;
		swimming = false;
		landed = false;
		applyGravity = true;
		drawSensors = true;
		baseSpeed = 8;
		//influenceRect= new Rectangle(X,Y, 196,196);
		//boundingBox = new Rectangle(X,Y,64,64);
		
		this.collisionBox = new CollisionBox(20,12,24,52,8,16,42);
		this.boundingBox = this.collisionBox.getMainBox();
		this.setX(X);
		this.setY(Y);
		
		if(Hero.imgSource==null ){
			Hero.setImgSource("animations/runCycleTest2.png");

		}
		if(Hero.sheet == null && Hero.imgSource != null){
			Hero.sheet = new SpriteSheet(getImgSource());
		}
		
		standAnim = new Animation(0, 0,64,64,this.sheet);
		standAnim.addNewFrame(sheet,64,64,0,5);
		moveAnim = new Animation(0, 0, 64, 64, this.sheet);
		moveAnim.addNewFrame(sheet,64,64,1,5);
		moveAnim.addNewFrame(sheet,64,64,2,5);
		moveAnim.addNewFrame(sheet,64,64,3,5);
		moveAnim.addNewFrame(sheet,64,64,4,5);
		moveAnim.addNewFrame(sheet,64,64,5,5);	
		moveAnim.addNewFrame(sheet,64,64,6,5);	
		moveAnim.addNewFrame(sheet,64,64,5,5);	
		moveAnim.addNewFrame(sheet,64,64,4,5);
		moveAnim.addNewFrame(sheet,64,64,3,5);
		moveAnim.addNewFrame(sheet,64,64,2,5);
		moveAnim.addNewFrame(sheet,64,64,1,5);
		
		
		ammoPoolSize = 20;
		//Init projectiles
		projectiles = new ArrayList<Projectile>(ammoPoolSize);
		for (int i =0; i<ammoPoolSize; i++){
//			projectiles.add(new BluebulletProjectile());
		}
		
		
		
	}
	
	
	public void init(InputHandler input, Camera camera){
		this.setInput(input);
		
		CollisionHandler.setHero(this);
		

		
	}
	
	public void update(){

		boolean keyPressed = false;
				
		if (input.attack.down){
			this.shoot();
			keyPressed = true;
		}
		
		if (input.left.down){
			this.moveLeft();
			keyPressed = true;
		}
		if(input.right.down){
			this.moveRight();
			keyPressed = true;
		}
			
		if (input.up.down){	
			this.moveUp();
			keyPressed = true;
		}
		if(input.down.down){
			this.moveDown();
			keyPressed = true;
		}
		if(input.space.down){
			moveJump();
			keyPressed = true;
		}
		if(!keyPressed){
			this.stop();
		}
		
		
		
		this.setX(this.getSpeedX()+this.getX() );
		

		///////////// Y Movement logic
		// Updates Y Position
	
		//Apply gravity
		if(applyGravity == true){
			this.setSpeedY(this.getSpeedY() + 1);	
		}else{
		//	speedY= 0;
		}
		
			
		this.setY(this.getSpeedY()+this.getY() );
		
		////Handle state transitions from jumping and wall collisions
		// Handles Jumping
		if (this.getSpeedY() < -1){
			this.moveState = MoveState.JUMPING;
		}else{
			//this.moveState = MoveState.STOPPED;

		}
		
		if (this.collisionBox.isBottomCollision()){
			if (this.moveState == MoveState.JUMPING){
				this.moveState = MoveState.STOPPED;
			}
			this.wallState = WallState.GROUND;
		}
		if (this.collisionBox.isTopCollision()){
		//	this.moveState = MoveState.STOPPED;
		}
		if (this.collisionBox.isLeftCollision()){
		//	this.moveState = MoveState.STOPPED;
		}

		//System.out.println("mapX is " + x);
		//System.out.println("mapY is " + y);
		

		//Handle 
		

		///////////// Collision shape update
		this.setLocation(x,y);
		this.collisionBox.resetCollisions();
		
		//this.checkOnCamera(camera); //DO not use. Hero should always update
		
		//System.out.println(this.moveState);
		System.out.println(this.wallState);
	}//end of update()	



	
	
	
	

	public void render(){
		//screen.drawSolidRect(this.getX()-camera.getX(), this.getY()-camera.getY(), 64,64, 0x00ff0000); //red rectangle
		moveAnim.update(1);
		standAnim.update(1);
		if(this.isMovingRight()){
			screen.renderSprite(this.getCameraX(camera), this.getCameraY(camera), moveAnim.getSprite());
		}else if(this.isMovingLeft()){
			screen.renderSprite(this.getCameraX(camera), this.getCameraY(camera),moveAnim.getSprite(),true, false, false);
			
		}else{
			//screen.renderSprite(this.getCameraX(camera), this.getCameraY(camera), animation.getSprite);
			//screen.drawSolidRect(this.getX()-camera.getX(), this.getY()-camera.getY(), 64,64, 0x00ff0000);
			if (this.isFacingRight()){
				screen.renderSprite(this.getCameraX(camera),this.getCameraY(camera),standAnim.getSprite());
			}else if (this.isFacingLeft()){
				screen.renderSprite(this.getCameraX(camera),this.getCameraY(camera),standAnim.getSprite(),true,false,false);
			}else{
				screen.renderSprite(this.getCameraX(camera),this.getCameraY(camera),standAnim.getSprite());
			}
			
		}
		
		if (drawSensors == true){
			//Need to change this to camera coordinates
			final int GREEN = 0xff00ff00;
			final int RED = 0xffff0000;
			final int PURPLE = 0xffff00ff;
			screen.renderRect(this.boundingBox,camera);
			screen.renderRect(this.collisionBox.getUlSensor(),camera);
			screen.renderRect(this.collisionBox.getUrSensor(),camera,PURPLE);
			screen.renderRect(this.collisionBox.getLlSensor(),camera);
			screen.renderRect(this.collisionBox.getLrSensor(),camera);
			screen.renderRect(this.collisionBox.getTopSensor(),camera,GREEN);
			screen.renderPoint(this.collisionBox.getGroundPoint(), camera,PURPLE);
			
		}
	}
	
	



	//Input methods
	public void setInput(InputHandler input) {
		this.input = input;
	}






	//Movement methods
	

	public void moveRight(){
		speedX = baseSpeed;
		setMovingRight(true);
		setMovingLeft(false);
		this.moveState = MoveState.RUNRIGHT;
	}

	public void moveLeft(){
		speedX = -baseSpeed;
		setMovingRight(false);
		setMovingLeft(true);
		this.moveState = MoveState.RUNLEFT;
		
	}

	public void moveUp(){
		speedY = -baseSpeed;
		setMovingUp(true);
		setMovingDown(false);
		this.moveState = MoveState.JUMPING;
		this.wallState = WallState.AIR;
	}

	public void moveDown(){
		setMovingDown(true);
		setMovingUp(false);
		speedY = baseSpeed;
		this.moveState = MoveState.DUCKING;
		
	}


	private void stop() {
		speedX = 0;


		
		if (isMovingUp()== false && isMovingDown() == false && applyGravity == false){
			speedY = 0;
		}


		//if (isMovingRight() == false && isMovingLeft() == true) {
		//	
		//}


		//if (isMovingRight() == true && isMovingLeft() == false) {
		//
		//}
		setMovingRight(false);
		setMovingLeft(false);
		setMovingUp(false);
		setMovingDown(false);
		
		this.moveState = MoveState.STOPPED;
	}
	
	public void moveJump(){
		if (this.moveState != MoveState.JUMPING){
			speedY = JUMPSPEED;
			this.moveState = MoveState.JUMPING;
			this.wallState = WallState.AIR;
		}
	}
	
	public void shoot(){
	//	if(readyToFire){
	//	BaseProjectile p = new Projectile(cameraX+37, cameraY+32);
	//	projectiles.add(p);
	//	}
	}
	
	

//	public boolean isJumped() {
//		return jumped;
//		
//	}


//	public void setJumped(boolean jumped) {
//		this.jumped = jumped;
//		if (jumped == true){
//			landed = false;
//		}
//	}


//	public boolean isDucked() {
//		return ducked;
//	}

//	public void setDucked(boolean ducked) {
//		this.ducked = ducked;
//	}


	
	//////////////////////////////////////////////////

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public void setProjectiles(ArrayList<Projectile> projectiles) {
		this.projectiles = projectiles;
	}




	public double getSpeedGnd() {
		return speedGnd;
	}


	public void setSpeedGnd(double speedGnd) {
		this.speedGnd = speedGnd;
	}


	

	


//	@Override
//	public int getSpeedX(){
//		this.getSpeedGnd() * Math.cos(this.getAngle());
//	}

//	@Override
//	public int getSpeedY(){
//		this.getSpeedGnd() * Math.sin(this.getAngle());
//	}
	

//	public double getAngle(){
//		return this.angle;
//	}
	
//	public double setAngle(double angle){
//		this.angle = angle;
//	}

	
	
	
	
	
	
	
	//Special static image holding methods (these cannot be inherited and must be written for each specific animated character

	public static SpriteSheet getSheet() {
		return Hero.sheet;
	}

	public static void setSheet(SpriteSheet sheet) {
		Hero.sheet = sheet;
	}

	public static String getImgSource() {
		return Hero.imgSource;
	}

	public static void setImgSource(String imgSource) {
		Hero.imgSource = imgSource;
	}

	
	
	
	
	
	
}//class



