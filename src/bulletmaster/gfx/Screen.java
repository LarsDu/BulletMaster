package bulletmaster.gfx;

import java.awt.Rectangle;
import java.util.Random;

import bulletmaster.Camera;
import bulletmaster.Point;
import bulletmaster.Tiles.BaseTile;
import bulletmaster.Tiles.CurvedTile;




public class Screen {
	//Render strategy:
	//The Screen provides a surface upon which to draw our tiles and entities
	//The screen also holds the sprite information for everything drawn to screen.
	
	private static int width, height;
	public int[] pixels;
	
	//Containers for images to be drawn to screen
	//private static ArrayList<TileType> tileTypes=new ArrayList<TileType>(); 
		
		

	
	public static int getWidth() {
		return width;
	}



	public static void setWidth(int width) {
		Screen.width = width;
	}



	public static int getHeight() {
		return height;
	}



	public static void setHeight(int height) {
		Screen.height = height;
	}



	


	public Screen(int w, int h) {

		Screen.width = w;
		Screen.height = h;
		pixels = new int[width * height];
	
	}
	
	

	public void clear() {
		for(int i = 0; i < pixels.length; i++)
			pixels[i] = 0;
	}

	//public void render(int xOffset, int yOffset) {
		
	//	for(int y = 0; y < height; y++) {
	//		yy = y + yOffset;
	//		for(int x = 0; x < width; x++) {
	//			xx = x + xOffset;
	//			tileIndex = (yy >> 4 & MAP_SIZE_MASK) * MAP_SIZE + (xx >> 4 & MAP_SIZE_MASK);
	//			pixels[y * width + x] = tiles[tileIndex];
	//		}
	//	}
	//}
	
	
	
	public void renderTile(int xPos, int yPos, BaseTile tile) {
		//System.out.println("Tile types"+ tileTypes.size());

		Sprite tileSprite = tile.getTileType().getSprite();
		if(tileSprite != null){
			renderSprite(xPos,yPos,tileSprite,tile.isHorFlip(),tile.isVertFlip(),tile.isDiagFlip());
		}


	}






	public void renderSprite(int xPos,int yPos, Sprite sprite) {
		//Get the pixels from the Sprite's Sprite Sheet
		//int [] spritePixels = sprite.getSheet().cutIntArray(sprite.getxOffset(), sprite.getyOffset(), 
		//		sprite.getWidth(), sprite.getHeight());
		//Remember, int[] is passed by reference, so we are not copying the original pixel data
		//from the sprite sheet.
		
		renderImgInt(xPos,yPos,sprite.getWidth(),sprite.getHeight(),sprite.getPixels());
			
	}	
	
	public void renderSprite(int xPos,int yPos, Sprite sprite, boolean hFlip, boolean vFlip, boolean dFlip) {
		//Render sprite with rotations and vertical, horizontal flips
		

		//Get the pixels from the Sprite's Sprite Sheet
		//int [] spritePixels = sprite.getSheet().cutIntArray(sprite.getxOffset(), sprite.getyOffset(), 
		//		sprite.getWidth(), sprite.getHeight());
		
		//Remember, int[] is passed by reference, so we are not copying the original pixel data
		//from the sprite sheet.
		int [] spritePixels = sprite.getPixels();
		spritePixels = rotateByFlags(spritePixels,sprite.getWidth(),sprite.getHeight(), hFlip, vFlip, dFlip);
		
		renderImgInt(xPos,yPos,sprite.getWidth(),sprite.getHeight(),spritePixels);
			
	}	
	
	public void renderSpriteAlpha(int xPos,int yPos, Sprite sprite, boolean hFlip, boolean vFlip, boolean dFlip) {
		//Render sprite with rotations and vertical, horizontal flips
		

		//Get the pixels from the Sprite's Sprite Sheet
		int [] spritePixels = sprite.getSheet().cutIntArray(sprite.getxOffset(), sprite.getyOffset(), 
				sprite.getWidth(), sprite.getHeight());
		//Remember, int[] is passed by reference, so we are not copying the original pixel data
		//from the sprite sheet.
		
		spritePixels = rotateByFlags(spritePixels,sprite.getWidth(),sprite.getHeight(), hFlip, vFlip, dFlip);
		
		renderImgIntAlpha(xPos,yPos,sprite.getWidth(),sprite.getHeight(),spritePixels);
			
	}	
	

	public void renderSheet(int xPos,int yPos, int w, int h, SpriteSheet sheet) {
		renderImgInt(xPos,yPos,sheet.getWidth(),sheet.getHeight(),sheet.getPixels());
	}	


	public void renderScrollingBg(int bgX, int bgY, Sprite sprite, Camera camera){
		//Does not work yet!
		
		int deltaX = camera.getX()-bgX; //background x relative to camera
		for (int r=0; r < sprite.getHeight(); ++r){
			if (r < 0 || r  >= Screen.height) continue;
			for (int c=0; c < sprite.getWidth(); ++c){
				if (c < 0 || c  >= Screen.width) continue;
				
			
				//int colOff = sprite.getWidth()-Screen.width-(camera.getX()-bgX);
				if (bgX > camera.getX()){
					deltaX =0;
				}else if(bgX+sprite.getWidth()<camera.getX()+camera.getWidth()){
					deltaX=0;
				}
				this.pixels[c + r * Screen.width] =sprite.getPixels()[c+deltaX+r*sprite.getWidth()];
				
				//if(bgX<camera.getX()){
				//	int colOff = wDiff-(camera.getX()-bgX);
				//	this.pixels[c + r * Screen.width] =sprite.getPixels()[c+colOff+r*sprite.getWidth()];
				//}else if(bgX>camera.getX()){
				
			//	}else if(bgX==camera.getX()){
					//this.pixels[c + r * Screen.width] =sprite.getPixels()[c+r*sprite.getWidth()];
				//}
				
			}
		}
	}
	
	
	public void renderImgInt(int xPos, int yPos,int imgWidth,int imgHeight, int[] imgInt){
		//draw imgInt[] onto the screen pixel matrix

		for (int y = yPos; y < (yPos+imgHeight); ++y){
			if (y < 0 || y  >= Screen.height) continue;
			
			for (int x = xPos; x < (xPos+imgWidth); ++x){
				if (x < 0 || x  >= Screen.width) continue;
				
				//this.width is the screen width and is actually the indexing scanwidth
				//for the one dimensional array
				//this.pixels[x + y * this.width] = imgInt[(x-xPos)+(y-yPos)*imgWidth];
				int curPixel = imgInt[(x-xPos)+(y-yPos)*imgWidth];
				if((curPixel & 0xff000000) != 0){ //alpha masking
					this.pixels[x + y * Screen.width] =curPixel;
				//}else{
					//this.pixels[x + y * Screen.width]=0xff0000ff;
				}
			}
		}
	}
	

	public void renderImgIntAlpha(int xPos, int yPos,int imgWidth,int imgHeight, int[] imgInt){
		//draw imgInt[] onto the screen pixel matrix

		for (int y = yPos; y < (yPos+imgHeight); ++y){
			if (y < 0 || y  >= Screen.height) continue;
			
			for (int x = xPos; x < (xPos+imgWidth); ++x){
				if (x < 0 || x  >= Screen.width) continue;
				
				//this.width is the screen width and is actually the indexing scanwidth
				//for the one dimensional array
				//this.pixels[x + y * this.width] = imgInt[(x-xPos)+(y-yPos)*imgWidth];
				int curPixel = imgInt[(x-xPos)+(y-yPos)*imgWidth];
				if((curPixel & 0xff000000) == 0){ //alpha masking
					this.pixels[x + y * Screen.width] =0xff0000ff;
					//this.pixels[x + y * Screen.width] =0xff;
				}
			}
		}
	}
	

	
	
	public void renderCurvedTileHeightMask(int xPos, int yPos, CurvedTile ctile, int color){
		//x and y positions are upper left hand corner of rectangle
		//int lineWidth = 1;
		
		
		
		int w = ctile.getWidthmap().length;
		int h = ctile.getHeightmap().length;

		for (int j = yPos; j<h+yPos;j++){
			if (j < 0 || j  >= Screen.height) continue;
			for (int i =xPos; i<w+xPos;i++){
				if (i < 0 || i  >= Screen.width) continue;
				//if(i > xPos && i < xPos+w && j> yPos && j< yPos+h){ //If inside tile bounding box
					if((j-yPos)> ctile.getHeightmap()[i-xPos]){
						
						this.pixels[i+j*Screen.width] = color; //draw orange
					}
				//}
			}
		}
	}
	
	
	
	public void renderCurvedTileWidthMask(int xPos, int yPos, CurvedTile ctile, int color){
		//x and y positions are upper left hand corner of rectangle
		//int lineWidth = 1;
		
		
		double angle = ctile.getAngle();
		int w = ctile.getWidthmap().length;
		int h = ctile.getHeightmap().length;

		if (angle > 0){

			for (int j = yPos; j<h+yPos;j++){
				if (j < 0 || j  >= Screen.height) continue;
				for (int i =xPos; i<w+xPos;i++){
					if (i < 0 || i  >= Screen.width) continue;
					//if(i > xPos && i < xPos+w && j> yPos && j< yPos+h){ //If inside tile bounding box
					if((i-xPos)> ctile.getWidthmap()[j-yPos]){

						this.pixels[i+j*Screen.width] = color; //draw orange
					}
					//}
				}
			}

		}else if (angle<0){		
			for (int j = yPos; j<h+yPos;j++){
				if (j < 0 || j  >= Screen.height) continue;
				for (int i =xPos; i<w+xPos;i++){
					if (i < 0 || i  >= Screen.width) continue;
					//if(i > xPos && i < xPos+w && j> yPos && j< yPos+h){ //If inside tile bounding box
					if((i-xPos)< w-ctile.getWidthmap()[j-yPos]){

						this.pixels[i+j*Screen.width] = color; //draw orange
					}
					//}
				}
			}


		}
		
	}
	
	
	
	
	

	
	public static int[] rotateByFlags(int [] sImg, int sWidth, int sHeight, 
			boolean hFlip, boolean vFlip, boolean dFlip){
		//Transform an integer array image based on 
		//horizontal, vertical, and diagonal flags
		
		//This is my favorite method!
		
		int arrSize = sWidth*sHeight;
		if (sImg.length != (arrSize)){
			System.out.println("Array length mismatch!");
			return sImg;
		}

		int[] dImg = new int[arrSize];

		//Destination parameters
		int dCol;
		int dRow;
		int dWidth = sWidth;
	
		for (int sRow = 0; sRow < sHeight; ++sRow){
			for (int sCol = 0; sCol < sWidth; ++sCol){
				//Default variables
				dCol = sCol;
				dRow = sRow;
				dWidth = sWidth;
				//Overwrite variables
				if (dFlip){
					//Swap rows and columns for a diagonal flip
					dRow = sCol; //Set dRow to dCol
					dCol = sRow; //Set dCol to dRow (or sCol if no h or v flips performed)
					
				}	
				
				if( hFlip){
					//Reverse columns for horizontal flip
					dCol = sWidth - 1 - dCol;
				}
				if(vFlip){
					//Reverse rows for vertical flip
					dRow = sHeight-1-dRow;
				}

				
				dImg[dCol+dRow*dWidth] = sImg[sCol+sRow*sWidth];
			}
		}


		return dImg;
	}
	
	
	
	
	
	public void drawSolidRect(int xPos, int yPos, int w, int h, int color){
		//x and y positions are upper left hand corner of rectangle
		
		
		for (int j = yPos; j< h+yPos; j++){
			if (j < 0 || j  >= Screen.height) continue;
			for (int i = xPos; i<w+xPos;i++){
				if (i < 0 || i  >= Screen.width) continue;
				this.pixels[i+j*Screen.width] = color;
			}
		}
	}

	
	public void renderRect(Rectangle rect, int color){
		//x and y positions are upper left hand corner of rectangle
		int lineWidth = 1;
		int xPos = (int) rect.getX();
		int yPos = (int) rect.getY();
		int w= (int) rect.getWidth();
		int h = (int) rect.getHeight();

		for (int j = yPos; j<h+yPos;j++){
			if (j < 0 || j  >= Screen.height) continue;
			for (int i =xPos; i<w+xPos;i++){
				if (i < 0 || i  >= Screen.width) continue;
				if(i <= (xPos+lineWidth) || i >= (xPos+w-lineWidth)|| j <= (yPos+lineWidth) ||j >= (yPos+h-lineWidth)){
					this.pixels[i+j*Screen.width] = color; //draw blue bounding box
				}
			}
		}
	}
	
	public void renderRect(Rectangle rect){
		//x and y positions are upper left hand corner of rectangle
		int lineWidth = 1;
		int xPos = (int) rect.getX();
		int yPos = (int) rect.getY();
		int w= (int) rect.getWidth();
		int h = (int) rect.getHeight();

		for (int j = yPos; j<h+yPos;j++){
			if (j < 0 || j  >= Screen.height) continue;
			for (int i =xPos; i<w+xPos;i++){
				if (i < 0 || i  >= Screen.width) continue;
				if(i <= (xPos+lineWidth) || i >= (xPos+w-lineWidth)|| j <= (yPos+lineWidth) ||j >= (yPos+h-lineWidth)){
					this.pixels[i+j*Screen.width] = 0x0000ff; //draw blue bounding box
				}
			}
		}
	}
	
	
	public void renderRect(Rectangle rect, Camera camera){
		//x and y positions are upper left hand corner of rectangle
		int lineWidth = 1;
		int xPos = (int)(rect.getX()-camera.getX());
		int yPos = (int)(rect.getY()-camera.getY());
		
		int w = (int) rect.getWidth();
		
		
		int h = (int) rect.getHeight();

		for (int j = yPos; j<h+yPos;j++){
			if (j < 0 || j  >= Screen.height) continue;
			for (int i =xPos; i<w+xPos;i++){
				if (i < 0 || i  >= Screen.width) continue;
				if(i <= (xPos+lineWidth) || i >= (xPos+w-lineWidth)|| j <= (yPos+lineWidth) ||j >= (yPos+h-lineWidth)){
					this.pixels[i+j*Screen.width] = 0xff0000ff; //draw blue bounding box
				}
			}
		}
	}
	
	public void renderRect(Rectangle rect, Camera camera, int color){
		//x and y positions are upper left hand corner of rectangle
		int lineWidth = 1;
		int xPos = (int)(rect.getX()-camera.getX());
		int yPos = (int)(rect.getY()-camera.getY());
		
		int w = (int) rect.getWidth();
		
		
		int h = (int) rect.getHeight();

		for (int j = yPos; j<h+yPos;j++){
			if (j < 0 || j  >= Screen.height) continue;
			for (int i =xPos; i<w+xPos;i++){
				if (i < 0 || i  >= Screen.width) continue;
				if(i <= (xPos+lineWidth) || i >= (xPos+w-lineWidth)|| j <= (yPos+lineWidth) ||j >= (yPos+h-lineWidth)){
					this.pixels[i+j*Screen.width] = color; 
				}
			}
		}
	}
	
	public void renderPoint(Point point, Camera camera, int color){
		//x and y positions are upper left hand corner of rectangle
		int padding = 2;
		int xPos = (int)(point.getX()-camera.getX());
		int yPos = (int)(point.getY()-camera.getY());
		


		for (int j = yPos-padding; j<yPos+padding;j++){
			if (j < 0 || j  >= Screen.height) continue;
			for (int i =xPos-padding; i<xPos+padding;i++){
				if (i < 0 || i  >= Screen.width) continue;
				if(i <= (xPos-padding) || i >= (xPos-padding)|| j <= (yPos-padding) ||j >= (yPos+padding)){
					this.pixels[i+j*Screen.width] = color; 
				}
			}
		}
	}
	
	
	public void renderPoint(Point point, Camera camera){
		//x and y positions are upper left hand corner of rectangle
		int padding = 2;
		int xPos = (int)(point.getX()-camera.getX());
		int yPos = (int)(point.getY()-camera.getY());
		


		for (int j = yPos-padding; j<yPos+padding;j++){
			if (j < 0 || j  >= Screen.height) continue;
			for (int i =xPos-padding; i<xPos+padding;i++){
				if (i < 0 || i  >= Screen.width) continue;
				if(i <= (xPos-padding) || i >= (xPos-padding)|| j <= (yPos-padding) ||j >= (yPos+padding)){
					this.pixels[i+j*Screen.width] = 0xff00ff00; 
				}
			}
		}
	}
	
	
	public void renderPoint2(Point point, Camera camera){
		//x and y positions are upper left hand corner of rectangle
	
		int i = (int)(point.getX()-camera.getX());
		int j = (int)(point.getY()-camera.getY());
		if(i >=0 && i<=Screen.width && j >= 0 && j<=Screen.height){
			this.pixels[i+j*Screen.width] = 0xff00ff00; //draw green point
		}
	}
	
	public void renderPoint2(Point point, Camera camera, int color){
		//x and y positions are upper left hand corner of rectangle
	
		int i = (int)(point.getX()-camera.getX());
		int j = (int)(point.getY()-camera.getY());
		if(i >=0 && i<=Screen.width && j >= 0 && j<=Screen.height){
			this.pixels[i+j*Screen.width] = color; 		}
	}
	
	
	public void testPattern(){
		Random r;
		int [] tiles = new int[64 * 64];
		r = new Random(0xffffff);
		
		//Render a colorful test pattern
		for(int i = 0; i < tiles.length; i++) {
			//tiles[i] = r.nextInt();
			tiles[i] = r.nextInt();
		}
		tiles[0] = 0;
	}
	
	
}