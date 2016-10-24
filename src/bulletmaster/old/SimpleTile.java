package bulletmaster.old;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.HashMap;

import bulletmaster.Camera;
import bulletmaster.StartingClass;
import bulletmaster.entity.Actor;


public class SimpleTile {

	//Indices for directionality
	final int TOP = 0;
	final int BOTTOM = 1;
	final int LEFT =2;
	final int RIGHT = 3;
		
	
    //Indices for tile types
	private final static int EMPTY = 0;
    private final static int SOLID = 1;
    private final static int JUMPTHROUGH =2; //Solid on top but can be jumped through on bottom
    private final static int WATER = 3;
    private final static int SPRING1 =10;
    private final static int SPRING2 = 11;
    private final static int ANTIGRAV1 = 20;
    private final static int ANTIGRAV2 = 21;
    

    
    //Map for tile types
    //used for translating string values to 
    //tile type indices
    private static final HashMap<String, Integer> tileDict;
    static{
    	tileDict = new HashMap<String,Integer>();
    	tileDict.put("EMPTY", EMPTY);
    	tileDict.put("SOLID", SOLID);
    	tileDict.put("JUMPTHROUGH", JUMPTHROUGH);
    	tileDict.put("WATER", WATER);
    	tileDict.put("SPRING1", SPRING1);
    	tileDict.put("SPRING2", SPRING2);
    	tileDict.put("ANTIGRAV1", ANTIGRAV1);
    	tileDict.put("ANTIGRAV2", ANTIGRAV2);
    	
    }
    
    
    //Standard tile width/height
    final int STDWIDTH = 64;
    final int STDHEIGHT = 64;

        
    private int screenTileX,screenTileY;
    private int mapTileX, mapTileY;
    private int speedX,speedY; 

    
// type refers to whether tile is ocean or dirt
//tileX and tileY are indices for tile positioning (each tile is 64x64)
    private Rectangle tileRect;
    private Image tileImage;

    //Values from tmx 
    //private String imageSource;
    private float rotation;	
    private String layerId;
    private int layerIndex;
    private int type;
    private int gid; //grid id for tmx files
    private int tileId;
    private int tileType;

    //Empty constructor
    public SimpleTile (){
    
    }
    
    
    
    //Constructor for simple map loads
    public SimpleTile(int x, int y, int typeInt) {
    //x and y represent tile indices, not coordinates!
	//mapTileX and mapTileY are upper left corner coordinates of tile in the map.
    //These mapTile coordinates stay the same	
    //screenTileX and screenTileY start off with mapTile coordinates, but will change as the camera moves
    		
  	
    mapTileX = x * STDWIDTH;  
	mapTileY = y * STDHEIGHT;
	screenTileX = mapTileX;
	screenTileY = mapTileY;
	type = typeInt;
	tileRect = new Rectangle();
	
		if (type == SOLID){
	    	tileImage = StartingClass.orangeSolid;
		}else if (type == JUMPTHROUGH){
			tileImage = StartingClass.blueSolid;
		}else{
			type = EMPTY;
		}
    }//Tile constructor

    
    //TODO: Camera contains list of objects in view (determined by intersection with 
    //collisionDetectRects
    //pass camera objects to tile for collision detection
    public void update(Camera camera1, testHero movableObj){
    
    	//For tiles, camera1 gives information on how to move across screen
    	//in the form of speed information.
    	//The tile if will call the movableObj to do work
    	// (ie: collision, bouncing, etc)
    	
    	speedX = camera1.getSpeedX();
    	speedY = camera1.getSpeedY();
    	screenTileX += speedX;
       	screenTileY += speedY;
       	tileRect.setBounds(mapTileX, mapTileY, STDWIDTH, STDHEIGHT);
       
       	if( movableObj instanceof testHero && movableObj.isHasFullCollision()==true){
        	if (tileRect.intersects(movableObj.getCollisionDetectRect())&& type != EMPTY){
        		checkHeroCollision(movableObj);	
        	}
        	

        }//if collisionRect intersects tile
    }//update
   
    
    
    
    
    //Collision detection methods
    public void checkHeroCollision(testHero hero){
    	//Note that each of these sensors are Rects
    	
    	//Bottom collisions
    	if (hero.getLowerRightSensor().intersects(tileRect) || 
    			hero.getLowerLeftSensor().intersects(tileRect)){
    		hero.setSpeedY(0);
     		hero.setJumped(false);
    		hero.setMapY(mapTileY-63); //adjust map coordinate of hero
    		hero.setLanded(true);
    	}
    	//Up collisions
    	if (hero.getTopSensor().intersects(tileRect)){
    		hero.setMapY(mapTileY+STDHEIGHT);
    		//System.out.println("Top collision!");
    	}
    	//Left collisions
    	if (hero.getUpperLeftSensor().intersects(tileRect) && !(hero.getTopSensor().intersects(tileRect)) ){
    		hero.setSpeedX(0);
    		hero.setMovingLeft(false);
    		hero.setMapX(mapTileX+STDWIDTH);
    		//System.out.println("left collision!");
    	}
    	
    	//Right collisions
    	if(hero.getUpperRightSensor().intersects(tileRect) && !(hero.getTopSensor().intersects(tileRect)) ){
    		hero.setSpeedX(0);
    		hero.setMovingRight(false);
    		//hero.setScreenX(screenTileX-31);
    		hero.setMapX(mapTileX-31);
    		
    	}
    	
    }//Check collision method

    
    public void checkHeroCollision(Actor actor){
    	//Note that each of these sensors are Rects
    	
    	//Bottom collisions
    	if (actor.getLowerRightSensor().intersects(tileRect) || 
    			actor.getLowerLeftSensor().intersects(tileRect)){
    		actor.setSpeedY(0);
     		//actor.setJumped(false);
      		actor.setMapY(mapTileY-actor.getBoundingBox().height-1); //adjust map coordinate of actor
      		//actor.setLanded(true);
    	}
    	//Up collisions
    	if (actor.getTopSensor().intersects(tileRect)){
    		//actor.setScreenY(screenTileY+STDHEIGHT);
    		actor.setMapY(mapTileY+STDHEIGHT);
    		//System.out.println("Top collision!");
    	}
    	//Left collisions
    	if (actor.getUpperLeftSensor().intersects(tileRect) && !(actor.getTopSensor().intersects(tileRect)) ){
    		actor.setSpeedX(0);
    		actor.setMovingLeft(false);
    		//actor.setScreenX(screenTileX+STDWIDTH);
    		actor.setMapX(mapTileX+STDWIDTH);
    		//System.out.println("left collision!");
    	}
    	
    	//Right collisions
    	if(actor.getUpperRightSensor().intersects(tileRect) && !(actor.getTopSensor().intersects(tileRect)) ){
    		actor.setSpeedX(0);
    		actor.setMovingRight(false);
    		//actor.setScreenX(screenTileX-31);
    		actor.setMapX(mapTileX-actor.getBoundingBox().width-1);
    		
    	}
    	
    }//Check collision method
    

    
    public void setTileTypeByString(String property){
    	//Look up string value in dictionary,
    	//set tileType to value in dictionary.
    	
    	try{
    		this.tileType = tileDict.get(property);
    	}catch(NullPointerException e){
    		System.out.println("Tile type key not found!");
    	}
    	
    }
    
    
    

    
    //Getters and setters

       
	public int getScreenTileX(Camera camera) {
		screenTileX= this.getMapTileX()-camera.getX();
		return screenTileX;
	}



	public int getScreenTileY(Camera camera) {
		screenTileY= this.getMapTileY()-camera.getY();
		return screenTileY;
	}


	public int getMapTileX() {
		return mapTileX;
	}



	public int getMapTileY() {
		return mapTileY;
	}


	//More methods
	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Image getTileImage() {
		return tileImage;
	}

	public void setTileImage(Image tileImage) {
		this.tileImage = tileImage;
	}


	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public int getLayerIndex() {
		return layerIndex;
	}

	public void setLayerIndex(int layerIndex) {
		this.layerIndex = layerIndex;
	}

	public int getGid() {
		return gid;
	}


	public void setGid(int gid) {
		this.gid = gid;
	}


	public int getTileId() {
		return tileId;
	}


	public void setTileId(int tileId) {
		this.tileId = tileId;
	}



    

}
