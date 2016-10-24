package bulletmaster.Tiles;

import java.awt.Rectangle;

import bulletmaster.collisions.CollidableActor;
import bulletmaster.collisions.CollisionBox;
import bulletmaster.gfx.Screen;
import bulletmaster.gfx.Sprite;


public class CurvedTile extends BaseTile implements ITile {
	private final double DEG_RAD_45 = 0.7853981633974483;
	
	private int[] heightmap;
	private int[] widthmap;	//Only necessary for wall states
	private double angle;
	
	

	public CurvedTile(int xInd, int yInd, int gridId, TileType tileType, boolean hFlip,boolean vFlip, boolean dFlip) {
		//x and y represent tile indices, not coordinates!
		this.x = xInd * STDWIDTH;  
		this.y = yInd * STDHEIGHT;
		this.gid = gridId;
		this.tileType = tileType;
		this.typeId = tileType.getTypeId();
		this.setHorFlip(hFlip);
		this.setVertFlip(vFlip);
		this.setDiagFlip(dFlip);
		
		this.tileRect = new Rectangle(x,y,STDWIDTH,STDHEIGHT);
		this.drawOutline = true;
		
		int [] rotatedTile = this.getRotatedSpritePixels();
		
		this.heightmap = colHeightFromAlpha(rotatedTile,CurvedTile.STDWIDTH, CurvedTile.STDHEIGHT);
		//Determine angle from heightmap (in radians)
		this.angle = Math.atan(   -((double)heightmap[heightmap.length-1]-(double)heightmap[0])/(double)heightmap.length    );
		
		
		this.widthmap = rowWidthFromAlpha(rotatedTile,CurvedTile.STDWIDTH, CurvedTile.STDHEIGHT,this.angle);
		
		
		
		
	
		
		
		
	}//Tile constructor

	@Override
    public void render(Screen screen){
    	
    	//Draw tile onto screen
    	if (gid != 0 && onCamera){ //Do not render blank tiles or offcamera tiles
    	
    		screen.renderSprite(this.getCameraX(camera), this.getCameraY(camera), this.getTileType().getSprite(),this.horFlip,this.vertFlip,this.diagFlip);
    		if(drawOutline == true){
    			//System.out.println(this.angle);
    			final int GREEN = 0xff00ff00;
    			final int YELLOW = 0xffffff00;
    			screen.renderRect(this.tileRect,camera,GREEN);
    			//screen.renderImgIntAlpha(this.getScreenX(camera), this.getScreenY(camera), CurvedTile.STDWIDTH, CurvedTile.STDHEIGHT,this.getTileType().getSprite().getPixels());
    			//screen.renderCurvedTileHeightMask(this.getCameraX(camera), this.getCameraY(camera),this,YELLOW);
    			
    			if(this.angle>0){    			
    				screen.renderCurvedTileWidthMask(this.getCameraX(camera), this.getCameraY(camera),this,YELLOW);
    			}else if (this.angle<0)
    				screen.renderCurvedTileWidthMask(this.getCameraX(camera), this.getCameraY(camera),this,0xff0000ff);
    		}
    		
    	}
    }
	
	
	public void checkCollision(CollidableActor actor){

		//Change in coordinates relative to the tile x y coordinates (upper left hand coord)
		CollisionBox box = actor.getCollisionBox();	
		if(!(box.getMainBox().intersects(this.getTileRect()))){
			return;
		}




		//These offsets are coordinates relative to the x and y coords of the this.
		//We will use these for repositioning our box


		//Bottom collisions
		if (box.getLrSensor().intersects(this.getTileRect()) || 
				box.getLrSensor().intersects(this.getTileRect())){
			if(box.getGroundPoint().getX()>=this.getX() && box.getGroundPoint().getX()<this.getX()+this.tileRect.getWidth()){


				int tileGroundY = this.heightmap[box.getGroundPoint().getX()-this.getX()];
				int tileGlobalGroundY = this.getY()+tileGroundY;
				int boxGlobalGroundY = (int) box.getGroundPoint().getY();
				//System.out.println("boxGroundY "+ boxGlobalGroundY );
				//System.out.println("tileGlobalGroundY = "+ tileGlobalGroundY);
				//System.out.println("Angle"+this.angle);

				if (boxGlobalGroundY>tileGlobalGroundY){
					actor.setCollisionBoxY(tileGlobalGroundY-(int)box.getMainBox().getHeight());

					if(tileGroundY !=CurvedTile.STDHEIGHT){
						actor.setSpeedY(0);
					}
				}
			}
			box.setBottomCollision(true);
		}
		//Top collisions
		if (box.getTopSensor().intersects(this.getTileRect())){
			//	actor.setSpeedY(0);
			//	actor.setCollisionBoxY( this.getY()+BlockTile.STDHEIGHT);
			//box.setTopCollision(true);
			
		}

		//Left collisions
		if (box.getUlSensor().intersects(this.getTileRect()) && !(box.getTopSensor().intersects(this.getTileRect())) ){
			//	actor.setSpeedX(0);
			//	actor.setCollisionBoxX( this.getX()+BlockTile.STDWIDTH);
			//box.setLeftCollision(true);
		}

		//Right collisions
		if(box.getUrSensor().intersects(this.getTileRect()) && !(box.getTopSensor().intersects(this.getTileRect())) ){
			//	actor.setSpeedX(0);
			//	actor.setCollisionBoxX( this.getX()+(int) -box.getWidth());
				//box.setRightCollision(true);
		}

		//System.out.println("tileX" +tile.getX());
		//System.out.println("deltaX"+deltaX);
		//System.out.println("deltaY"+deltaY);
		//Now reposition box to new coordinates relative to tile coordinates (moving all boundingboxes and collision sensors)


	}


	
	public static  int [] colHeightFromAlpha(Sprite sprite, int w, int h){
		return colHeightFromAlpha(sprite.getPixels(), w, h);
	}
	

	public static  int [] colHeightFromAlpha(int [] pixels, int w, int h){
		//This function takes the non-transparent regions of 
		//an alpha mask from an array of pixels, and finds the coordinate
		//measured from the bottom of the matrix to the first 
		//non-transparent pixel from the top.

		//This can be useful for making sloped or curved surfaces
		//for a Sonic the Hedgehog type platformer game.
		
		//Each position in array records distance from yMin to the surface!
		//To get actual height from up to down, heightArr[c] = h-r;

		//Mask for all but the first four bits (the alpha mask)
		//of an int array.
		//w and h are width and height of the input image
		final int ALPHA_MASK = 0xff000000; 

		int [] heightArr = new int[w];
		
		for (int r = 0; r<h; ++r){
			for (int c = 0; c<w ; ++c){	
			
				//Mask the alpha channel, invert the bit value
				//And shift the alpha channel to the right
				//(everything in the right 24 positions should be 0
				
				int i = c+r*w;
				int curPix = ((pixels[i]& ALPHA_MASK)>>24);
				
				//System.out.println(curPix);
				//Record height value for corresponding column
				if (curPix != 0 && heightArr[c] == 0 ){
					heightArr[c] = r;
					
				}else if ((r == h-1)&& curPix ==0){
					heightArr[c] =h;
				}

			}
		
		}
		
		
		//System.out.println(Arrays.toString(heightArr));
		
		return heightArr;
	}

	public int [] rowWidthFromAlpha(int[] img, int w, int h, double angle){
		
		int [] widthMap = new int[h];
		
		//Determine if the widthmap is right or left based on angle value
		if(angle > 0){ 
			//IF curving upwards right
			widthMap = rowWidthFromAlphaRight(img,w, h);
		}else if (this.angle <0){
			//If curving upwards left
			widthMap = rowWidthFromAlphaLeft(img,w,h);
		}
		
		
		return widthMap;
	}


	public static int [] rowWidthFromAlphaRight(int [] pixels, int w, int h){
		//This function takes the non-transparent regions of 
		//an alpha mask from an array of pixels, and finds the coordinate
		//measured from the left the matrix to the first 
		//non-transparent pixel from the right. 
		//This is useful for describing a curved wall from the left


		//This can be useful for making sloped or curved surfaces
		//for a Sonic the Hedgehog type platformer game.

		//Mask for all but the first four bits (the alpha mask)
		//of an int array.
		//w and h are width and height of the input image
		final int ALPHA_MASK = 0xff000000; 

		int [] widthArr = new int[h];

		for (int r = 0; r<h; ++r){
			for (int c = 0; c<w ; ++c){
				//Mask the alpha channel, invert the bit value
				//And shift the alpha channel to the right
				//(everything in the right 24 positions should be 0
				int i = c+r*w;
				int curPix = (pixels[i]& ALPHA_MASK)>>24;


			//Record height value for corresponding column
			if (widthArr[r] == 0 && curPix != 0){
				widthArr[r] = c;
			}else if ((c == w-1)&& curPix ==0){
				widthArr[r] =w;
			}

			//Ternary operator, look it up!

			//Recall that the boolean type in java is
			//not interconvertible with ints, and the size
			// of this type is 
			}

		}
		return widthArr;
	}

	public static  int [] rowWidthFromAlphaLeft(int [] pixels, int w, int h){
		//This function takes the non-transparent regions of 
		//an alpha mask from an array of pixels, and finds the coordinate
		//measured from the left the matrix to the first 
		//non-transparent pixel from the right. 
		//This is useful for describing a curved wall from the left


		//This can be useful for making sloped or curved surfaces
		//for a Sonic the Hedgehog type platformer game.

		//Mask for all but the first four bits (the alpha mask)
		//of an int array.
		//w and h are width and height of the input image
		final int ALPHA_MASK = 0xff000000; 

		int [] widthArr = new int[h];

		for (int r = 0; r<h; ++r){
			for (int c = 0; c<w ; ++c){
				//Mask the alpha channel, invert the bit value
				//And shift the alpha channel to the right
				//(everything in the right 24 positions should be 0
				int i = c+r*w;
				int curPix = (pixels[i]& ALPHA_MASK)>>24;


			//Record width value for corresponding row
			if (widthArr[r] == 0 && curPix == 0){
				widthArr[r] =w-c;
			}
			//else if ((c == w-1)&& curPix !=0){
			//	widthArr[r] =w;
			//}

			//Ternary operator, look it up!

			//Recall that the boolean type in java is
			//not interconvertible with ints, and the size
			// of this type is 
			}

		}
		return widthArr;
	}
	
	
	
	public double getAngle(){
		return this.angle;
	}
	
	public int[] getHeightmap() {
		return heightmap;
	}

	public void setHeightmap(int[] heightmap) {
		this.heightmap = heightmap;
	}

	public int[] getWidthmap() {
		return widthmap;
	}

	public void setWidthmap(int[] widthmap) {
		this.widthmap = widthmap;
	}
	
}
