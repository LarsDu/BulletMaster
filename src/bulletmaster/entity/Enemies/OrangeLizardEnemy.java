package bulletmaster.entity.Enemies;

import bulletmaster.Animation;
import bulletmaster.TickTimer;
import bulletmaster.collisions.CollisionBox;
import bulletmaster.collisions.CollisionHandler;
import bulletmaster.entity.Entity;
import bulletmaster.entity.Hero;
import bulletmaster.gfx.SpriteSheet;

public class OrangeLizardEnemy extends Enemy{
	
	private static SpriteSheet sheet = null;
	private static String imgSource = null;

	private boolean jumped;
	
	public OrangeLizardEnemy(int x, int y){
		alive = true;
		baseSpeed = 4;
		swimming = false;
		landed = false;
		applyGravity = true;
		drawSensors = false;
		hasFullCollision = true;
		
	
		
		if(OrangeLizardEnemy.imgSource==null ){
			OrangeLizardEnemy.setImgSource("/boar.png");

		}
		if(OrangeLizardEnemy.sheet == null && OrangeLizardEnemy.imgSource != null){
			OrangeLizardEnemy.sheet = new SpriteSheet(getImgSource());
		}

		this.standAnim = new Animation(0, 0,64,64,sheet);
		this.standAnim.addNewFrame(sheet,64,64,0,5);

		this.moveAnim = new Animation(0, 0,64,64,sheet);
		this.moveAnim.addNewFrame(sheet,64,64,0,5);
		this.moveAnim.addNewFrame(sheet,64,64,1,5);
		this.moveAnim.addNewFrame(sheet,64,64,2,5);
		this.moveAnim.addNewFrame(sheet,64,64,3,5);
		

		
		//TODO: add more frames of animation!
		
		
		
		this.collisionBox = new CollisionBox(standAnim.getSprite(),20,12,25);
		this.getCollisionBox().getTopSensor().setSize(collisionBox.getWidth()-20, 8);
		this.getCollisionBox().getTopSensor().setLocation((int)this.getCollisionBox().getMainBox().getX()+8,(int) this.getCollisionBox().getMainBox().getY());
		this.getCollisionBox().setTopSensorXOff(4);
		this.boundingBox = this.collisionBox.getMainBox();
		
		this.setX(x);
		this.setY(y);
		if (hasFullCollision == true){
			CollisionHandler.add(this);
		}
		
		timer = new TickTimer();
		timer.setMaxTicks(20);		
		moveLeft();
		
	}
	
	

	
	@Override
	public void update(){
		
		if (!alive){
			return;
		}
		
		timer.tick();
		//if(timer.getTicks()==10 && hasFullCollision == true){
		//	speedY = -6;
		//	//moveJump();
		//}

		
				
		if(collisionBox.isLeftCollision()){
			moveRight();
		}
		if(collisionBox.isRightCollision()){
			moveLeft();
		}
		if( speedX == 0){
			moveLeft();
		}
		
		x += speedX;
		
		
		//Apply gravity
		if(applyGravity == true){
			speedY += 1;	
		}
		y += speedY;

		

		//Speed limiter

		if (speedX > Entity.MAX_SPEEDX){
			speedX = Entity.MAX_SPEEDX;
		}		
		if (speedY > Entity.MAX_SPEEDY){
			speedY = Entity.MAX_SPEEDY;
		}



		this.setLocation(x,y);
		//reset collision bool values
		collisionBox.resetCollisions();
	}
	
	public void die(){
		
		this.setAlive(false);
		this.setOnCamera(false);
		this.setHasFullCollision(false);
		
		
	}
	
	
	@Override
	public void render(){
		if (!alive){
			return;
		}
	
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
			screen.renderPoint(this.collisionBox.getTopPoint(), camera,PURPLE);
			
		}
	}
	
	
	public void checkCollision(Hero hero){
		
		if(!alive){
			return;
		}

			CollisionBox enemyBox = this.getCollisionBox();
			CollisionBox heroBox = hero.getCollisionBox();
			if(!(enemyBox.getMainBox().intersects(heroBox.getMainBox()))){
				return;
			}



			//Normal Bottom collisions
			//if (box.getLlSensor().intersects(this.getTileRect()) ){
			//		box.getLrSensor().intersects(this.getTileRect())){

			//if(heroBox.getGroundPoint().getX()>=enemyBox.getX() && heroBox.getGroundPoint().getX()<enemyBox.getX()+enemyBox.getWidth() 
			//		&& heroBox.getGroundPoint().getY()>enemyBox.getY() && heroBox.getGroundPoint().getY()<enemyBox.getY()+enemyBox.getHeight()){

			//if(heroBox.getGroundPoint().isInside(enemyBox.getTopSensor())){
			//if(heroBox.getMainBox().intersects(enemyBox.getTopSensor())){
			if (heroBox.getLlSensor().intersects(enemyBox.getTopSensor()) || heroBox.getLrSensor().intersects(enemyBox.getTopSensor()) ){
				//hero.setSpeedY(hero.getSpeedY()-26);
				hero.setSpeedY(-18);
				this.setSpeedY(-8);
				hero.setCollisionBoxY(this.getY()-(int) enemyBox.getMainBox().getHeight());
				heroBox.setBottomCollision(true);
				this.setHasFullCollision(false);
				
			}else{heroBox.setBottomCollision(false);}
			

			
			
			
			//	//Top collisions
			//	if (box.getTopSensor().intersects(this.getTileRect())){
			//		actor.setSpeedY(0);
			//		actor.setCollisionBoxY(this.getY()+BlockTile.STDHEIGHT);
			//		box.setTopCollision(true);
			//	}else{box.setTopCollision(false);}
				//Left collisions
				if (heroBox.getUlSensor().intersects(enemyBox.getUrSensor()) && heroBox.isBottomCollision()==false && heroBox.isTopCollision()==false){
					hero.setSpeedX(this.getSpeedX());
					//hero.setCollisionBoxX(enemyBox.getX()+(int)enemyBox.getMainBox().getWidth());
					//heroBox.setLeftCollision(true);
				}else{
					//heroBox.setLeftCollision(false);
					
				}

			//Right collisions
				if (heroBox.getUrSensor().intersects(enemyBox.getUlSensor())&& heroBox.isBottomCollision()==false && heroBox.isTopCollision()==false ){
					hero.setSpeedX(this.getSpeedX());
					//hero.setCollisionBoxX(enemyBox.getX()-(int)heroBox.getMainBox().getWidth());
					//heroBox.setRightCollision(true);
				}else{
					//heroBox.setRightCollision(false);
					}
	}

	
	
	
	


	//Special static image holding methods (these cannot be inherited and must be written for each specific animated character

	public static SpriteSheet getSheet() {
		return OrangeLizardEnemy.sheet;
	}

	public static void setSheet(SpriteSheet sheet) {
		 OrangeLizardEnemy.sheet = sheet;
	}

	public static String getImgSource() {
		return  OrangeLizardEnemy.imgSource;
	}

	public static void setImgSource(String imgSource) {
		 OrangeLizardEnemy.imgSource = imgSource;
	}

	
	
	
	
	
}
