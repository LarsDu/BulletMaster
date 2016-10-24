package bulletmaster.old;

import java.awt.Rectangle;

import bulletmaster.entity.Actor;
import bulletmaster.gfx.Screen;


public class testHero extends Actor{
	//Basic class for making an actor that can be controlled by the player
	//A cube that can me moved around!
	
	int color;


	public testHero(int x, int y){
		//Constructor	
		this.setX(x);
		this.setY(y);
		this.setBoundingBox(new Rectangle(64,64));
		this.x = x;
		this.y =  y-64; 
		this.speedY = 0;
		this.lastGroundY = this.y;
		this.speedX = 0;
		
		this.color = 0x00ff0000;
		this.hasFullCollision = false;
		this.swimming = true;
		this.landed = false;
		this.applyGravity = false;
		
		
	}


	public void init(){

	}

	public void update(){

	}

	public void render(Screen screen){
		//screen.drawSolidRect(this.getX(), this.getY(), (int) this.getBoundingBox().getWidth(), (int) this.getBoundingBox().getHeight(), this.color);
		screen.drawSolidRect(this.getX(), this.getY(), 64,64, this.color);

	}

}