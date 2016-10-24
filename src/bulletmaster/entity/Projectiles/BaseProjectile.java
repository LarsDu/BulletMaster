package bulletmaster.entity.Projectiles;

import java.awt.Rectangle;

import bulletmaster.Animation;
import bulletmaster.entity.Entity;
import bulletmaster.gfx.SpriteSheet;

public class BaseProjectile extends Entity{
	//Constructor
	
	protected Animation anim;
	protected int width, height;
	
	//protected SpriteSheet sheet;
	//protected static String imgSource;
	
	protected int baseSpeed;
	
	//Defines how far the projectile moves before disappearing
	protected int xLim;
	protected int yLim;
	
	//This is used to handle collisions
	protected Rectangle rect;
	
	//x and y offsets for the collision rect relative to the x and y of the sprite.
	protected int xOff,yOff;
	
	public BaseProjectile(int startX,int startY,int w, int h) {
		//Developer variables
		baseSpeed = 6;
		
		//Constructor and derived variables
		this.x = startX;
		this.y = startY;
		this.width = w;
		this.height = h;
		this.height = h;
		//BaseProjectile.setImgSource("");		
		this.setAlive(true);
		//this.sheet = new SpriteSheet(imgSource);
		//this.anim = new Animation(this.x,this.y,w,h,this.sheet);
		//this.anim.addNewFrame(sheet,w,h,0,0);
		
		//Construct collision rect from first animation frame.
		this.rect = anim.getSprite().rectFromAlpha();
	}

	public void update(){
		
		moveRight();
		
		if(x>x+xLim){
			this.setAlive(false);
		}
		if (y>y+yLim){
			this.setAlive(false);
		}
		
		this.rect.setLocation(this.x, this.y);
			
	}
	

	
	public void render(){
		if (!alive){
			return;
		}
	
		//screen.drawSolidRect(this.getX()-camera.getX(), this.getY()-camera.getY(), 64,64, 0x00ff0000); //red rectangle
		anim.update(1);

		screen.renderRect(this.rect);
		
		if (this.isFacingRight()){
			//screen.renderSprite(this.getCameraX(camera),this.getCameraY(camera),anim.getSprite());
		}else if (this.isFacingLeft()){
			//screen.renderSprite(this.getCameraX(camera),this.getCameraY(camera),anim.getSprite(),true,false,false);
		}else{
			//screen.renderSprite(this.getCameraX(camera),this.getCameraY(camera),anim.getSprite());
		}
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

	
	//Behavioral methods
	public void die(){
	}
	
	
	
	
}