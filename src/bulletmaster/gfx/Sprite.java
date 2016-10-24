package bulletmaster.gfx;

import java.awt.Rectangle;

public class Sprite {
	
	//The offsets of the sprite on its spritesheet
	private int xOffset, yOffset;
	
	
	private int width, height;
	
	//The sprite sheet this sprite can be found on.
	private SpriteSheet sheet;
	
	//Handle this later
	private int color;
	
	//Rotation flags
    private boolean horFlip=false;
    private boolean vertFlip=false;
    private boolean diagFlip=false;

	public Sprite(SpriteSheet sheet, int x, int y, int w, int h) {
		this.sheet = sheet;
		this.xOffset = x;
		this.yOffset = y;
		this.width = w;
		this.height = h;
		

	}

	public Sprite(){
	
	}
	//Getters and Setters
	

	

	public int [] getPixels(){
		return this.getSheet().cutIntArray(this.getxOffset(), this.getyOffset(), this.getWidth(), this.getHeight());
	}
	
	public int getxOffset() {
		return xOffset;
	}

	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
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

	public SpriteSheet getSheet() {
		return sheet;
	}

	public void setSheet(SpriteSheet sheet) {
		this.sheet = sheet;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	public void setRotationFlags(boolean [] flags){
		this.horFlip= flags[0];
		this.vertFlip = flags[1];
		this.diagFlip = flags[2];
	}
	
	
	public Rectangle rectFromAlpha(){
		//Finish me!
		//Fits a rectangle to the alpha channel of an sprite
		//This is used for automatic bounding box generation
		final int ALPHA_MASK = 0xff000000; 

		int [] pixels = this.getPixels();
		int w = this.getWidth();
		int h = this.getHeight();
	
		int minX =0;
		int minY =0;
		int maxX =0;
		int maxY =0;

		int curPix;
		
		for (int r = 0; r<h; ++r){
			for (int c = 0; c<w ; ++c){	
			
				//Mask the alpha channel
				//And shift the alpha channel to the right
				//(everything in the right 24 positions should be 0
				
				//int pix = c+r*w;
				curPix = ((pixels[c+r*w]& ALPHA_MASK)>>24); //Get the alpha value of each pixel
				//Locate the first pixel from the top 
				if(curPix !=0){
					if (minY == 0 ){
						minY = r;
						minX = c; //minX value will change
					}
					if(c < minX){ 
						//Find the solid pixel with the lowest x value 
						minX = c;
					}
					if(c >= maxX){
						//Find the solid pixel with the highest x value 
						maxX = c;
					}
					if(r >= maxY){
						maxY=r;
					}
				}
				
			}
		
		}
		
		return new Rectangle(minX,minY,maxX-minX,maxY-minY);
	}
	
	
	
}
