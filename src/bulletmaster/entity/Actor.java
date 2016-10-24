package bulletmaster.entity;
import bulletmaster.Animation;
import bulletmaster.TickTimer;
import bulletmaster.collisions.CollidableActor;
import bulletmaster.collisions.CollisionBox;
import bulletmaster.collisions.CollisionHandler;



public class Actor extends Entity implements CollidableActor{
	



	protected final static int DEFAULT_MOVESPEED_X =2;
	protected final static int DEFAULT_MOVESPEED_Y = 2;
	
	

	protected Animation standAnim;
	protected Animation moveAnim;



	protected boolean swimming;
	protected boolean landed;  //only access with set functions
	protected int lastGroundY; //Records coordinates of the
	                            //last solid surface the Actor was standing on
	                            //only called when landed= true; 
	
	//Rendering option variables
	protected boolean drawSensors = false;
	
	//CollisionBox (contains sensors, influence rectangle)
	protected CollisionBox collisionBox;
	
	protected TickTimer timer = null;

	
	protected int baseSpeed;

	public void update(){
		timer.tick();
		
	}
	
	public void die(){
		System.out.println("Overhere!");
		this.setAlive(false);
	}
	
	public void render(){
		
	}

	public void visit(){
		//Triggers an action if called
		//ie: change turn drawSensors to true!
	}
	
	

	//State methods
	public boolean isSwimming() {
		return swimming;
	}

	public void setSwimming(boolean swimming) {
		this.swimming = swimming;
	}

	public boolean isLanded() {
		return landed;
	}

	public void setLanded(boolean landed) {
		this.landed = landed;
		if (landed){
			this.lastGroundY = this.y;
		}
	}

	public int getLastGroundY() {
		return lastGroundY;
	}
	
	
	
	public void setX(int x){
		this.x = x;
		this.collisionBox.setX(x+collisionBox.getXoff());
	}
	
	public void setY(int y){
		this.y = y;
		this.collisionBox.setY(y+collisionBox.getYoff());
	}
	
	
	public void checkCollision(Hero hero){
		
	}
	
	public void checkCollision(CollidableActor actor){
	
	}
	
	public void setLocation(int x, int y){
		this.x = x;
		this.collisionBox.setX(x+collisionBox.getXoff());
		this.y = y;
		this.collisionBox.setY(y+collisionBox.getYoff());
	}
	
	public void setCollisionBoxX(int x){
		//Moves the actor collision box to a new x coordinate, and 
		//moves the actor along with the collision box.
		//this.collisionBox.setX(x);
		this.setX(x- collisionBox.getXoff());
		}
	
	public void setCollisionBoxY(int y){
		//Moves the actor collision box to a new y coordinate, and 
		//moves the actor along with the collision box.
		//this.collisionBox.setY(y);
		this.setY(y-collisionBox.getYoff());
	}
	
	public void setCollisionBoxLocation(int x, int y){
		//Moves the actor collision box to a new x,y coordinate, and 
		//moves the actor along with the collision box.
		//this.collisionBox.setX(x);
		this.setX(x- collisionBox.getXoff());
		//this.collisionBox.setY(y);
		this.setY( y-collisionBox.getYoff());
	}
	
	
	public boolean isDrawSensors() {
		return drawSensors;
	}

	public void setDrawSensors(boolean drawSensors) {
		this.drawSensors = drawSensors;
	}

	public CollisionBox getCollisionBox() {
		return collisionBox;
	}

	public void setCollisionBox(CollisionBox collisionBox) {
		this.collisionBox = collisionBox;
	}

	public void checkCollisions(){
		if(this.onCamera){
		//Call the collisionHandler
			CollisionHandler.add(this);
		}
	}
	

	public Animation getStandAnim() {
		return this.standAnim;
	}

	public void setStandAnim(Animation standAnim) {
		this.standAnim = standAnim;
	}

	public Animation getMoveAnim() {
		return moveAnim;
	}

	public void setMoveAnim(Animation moveAnim) {
		this.moveAnim = moveAnim;
	}



	public void setLastGroundY(int lastGroundY) {
		this.lastGroundY = lastGroundY;
	}
	


	
	
}
