package bulletmaster.Tiles;


import java.awt.Rectangle;

import bulletmaster.Camera;
import bulletmaster.collisions.CollidableActor;
import bulletmaster.entity.Hero;
import bulletmaster.gfx.Screen;
import bulletmaster.gfx.Sprite;


public class BaseTile implements ITile {
	

		
    //Standard tile width/height
    final static public int STDWIDTH = 32;
    final static public int STDHEIGHT = 32;
	
    protected int xIndex, yIndex;
    
    //The x and y coordinates of a tile never change once initialized
    protected int x;
	protected int y;
    
    protected boolean drawOutline = false;
    
    protected boolean onCamera = true;
    protected boolean hasFullCollision = false;
    
    protected boolean horFlip=false;
    protected boolean vertFlip=false;
    protected boolean diagFlip=false;
 

    protected static Camera camera;
    protected static Hero hero;
    protected static Screen screen;

   
    protected Rectangle tileRect = null;
    
    
    protected float rotation;	
    protected int gid; //grid id for tmx files
    
    //Number corresponding to the tileDict tileType 
    //defaults to 0 (EMPTY)
    protected int typeId = TileType.EMPTY;
    
    //Reference to TileType object corresponding to this tile
    protected TileType tileType= null;
    


    
    public BaseTile(){
    	
    }
    
    public BaseTile(int xInd, int yInd, int gridId, TileType tileType,boolean hFlip,boolean vFlip, boolean dFlip) {
    //x and y represent tile indices, not coordinates!
    this.x = xInd * STDWIDTH;  
	this.y = yInd * STDHEIGHT;
	this.gid = gridId;
	this.tileType = tileType;
	this.typeId = tileType.getTypeId();
	this.tileRect = new Rectangle(x,y,STDWIDTH,STDHEIGHT);
	this.drawOutline = false;
	this.hasFullCollision = false;
	this.setHorFlip(hFlip);
	this.setVertFlip(vFlip);
	this.setDiagFlip(dFlip);
	
    }//Tile constructor


    public void init(Camera camera, Hero hero,Screen screen){
    	if(BaseTile.camera == null ){
    		BaseTile.camera = camera;
    	}
    	if (BaseTile.hero == null){
    		BaseTile.hero = hero;    		
    	}
    	if(BaseTile.screen == null){
    		BaseTile.screen = screen;
    	}
    	if(camera != null){
    	
    		checkOnCamera(camera);
    	}
    	
    	

    	
    	//if(hasFullCollision == true){
    	//	CollisionHandler.add(this);
    	//}
    	
    }
    
    
    

    



	public void update(){
       
    	if(camera != null){
    		checkOnCamera(camera);
    	}
    //	if (hasFullCollision==true){
  //  		checkCollision();
    	//}

    }//update
    
    
    public void render(Screen screen){
    	
    	//Draw tile onto screen
    	if (gid != 0 && onCamera){ //Do not render blank tiles or offcamera tiles
    	
    		screen.renderTile(this.getCameraX(camera), this.getCameraY(camera), this);
    		
    		if(drawOutline == true){
    			screen.renderRect(this.tileRect,camera);
    		}
    		
    	}
    }
    
    public void visit(){
    	this.setDrawOutline(true);
    }
    
	public void checkOnCamera(Camera camera){
		// Checks if current entity is onscreen
		// Switches onscreen flag to true or false 
		// based on whether object is onscreen or not.
		if (this.tileRect != null){ 
			if ( (int) this.tileRect.getMaxX() < camera.getX() 
			 ||  (int) this.tileRect.getX() > camera.getX()+camera.getWidth()
			 ||  (int) this.tileRect.getMaxY() < camera.getY() 
			 ||  (int) this.tileRect.getY() > camera.getY()+camera.getHeight() ){
				
				this.onCamera = false;
			}else{
					
				this.onCamera = true;
			}
		}
	}
    
    //Collision detection methods
    
    
    public void checkCollision(CollidableActor box){
    	//This shell method is necessary for interface
   	}
  
    

    
    
    

    
    //Getters and setters

       
	public int getCameraX(Camera camera) {
		int screenTileX= this.getX()-camera.getX();
		return screenTileX;
	}



	public int getCameraY(Camera camera) {
		int screenTileY= this.getY()-camera.getY();
		return screenTileY;
	}
	
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


	public int getxIndex() {
		return xIndex;
	}

	public void setxIndex(int xIndex) {
		this.xIndex = xIndex;
	}

	public int getyIndex() {
		return yIndex;
	}

	public void setyIndex(int yIndex) {
		this.yIndex = yIndex;
	}




	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}




	public int getGid() {
		return gid;
	}


	public void setGid(int gid) {
		this.gid = gid;
	}

	public boolean isHorFlip() {
		return horFlip;
	}

	public void setHorFlip(boolean horFlip) {
		this.horFlip = horFlip;
	}

	public boolean isVertFlip() {
		return vertFlip;
	}

	public void setVertFlip(boolean vertFlip) {
		this.vertFlip = vertFlip;
	}

	public boolean isDiagFlip() {
		return diagFlip;
	}

	public void setDiagFlip(boolean diagFlip) {
		this.diagFlip = diagFlip;
	}

	public void setRotationFlags(boolean [] flags){
		this.horFlip= flags[0];
		this.vertFlip = flags[1];
		this.diagFlip = flags[2];
	}

	public int[] getRotatedSpritePixels(){
		Sprite origSprite = this.getTileType().getSprite();
		int [] spritePixels = origSprite.getPixels();
		int w = origSprite.getWidth();
		int h = origSprite.getHeight();
		
		return Screen.rotateByFlags(spritePixels, w,h, isHorFlip(), isVertFlip(), isDiagFlip());
						
	}

	


    public int getTypeId() {
		return typeId;
	}


	public void setTypeId(int type) {
		this.typeId = type;
	}

	public TileType getTileType() {
		return tileType;
	}

	public void setTileType(TileType tileType) {
		this.tileType = tileType;
	}


	public Rectangle getTileRect() {
		return tileRect;
	}

	public void setTileRect(Rectangle tileRect) {
		this.tileRect = tileRect;
	}

	public boolean isOnCamera() {
		return onCamera;
	}

	public void setOnCamera(boolean onCamera) {
		this.onCamera = onCamera;
	}

	public boolean isDrawOutline() {
		return drawOutline;
	}

	public void setDrawOutline(boolean drawOutline) {
		this.drawOutline = drawOutline;
	}
	
}
