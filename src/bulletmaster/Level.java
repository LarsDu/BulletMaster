package bulletmaster;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bulletmaster.Tiles.TileSet;
import bulletmaster.Tiles.TileType;
import bulletmaster.collisions.CollisionHandler;
import bulletmaster.entity.Entity;
import bulletmaster.entity.Hero;
import bulletmaster.gfx.Screen;


public class Level {
	//Default parameters; only relevant for Tiled map editor
	private String version;
	private String orientation;
	private String renderorder;
	
	//MapX and MapY are coordinates for upper left corner of map
	private int x= 0;
	private int y=0;
	private int tileWidth;
	private int tileHeight;
	
	private int widthInTiles;
	private int heightInTiles;
	private int width, height;
	
	private int nextobjectid;


	private Background background;
	
	private Screen screen;
	private Camera camera;
	
	
	private Hero hero;
		


	//Map objects (based on Tiled formats)
    private ArrayList<TileSet> levelTileSets = new ArrayList<TileSet>();
    private HashMap<String,LevelObjectGroup> levelObjectGroups = new HashMap<String,LevelObjectGroup>();
    private HashMap<String,LevelLayer> levelLayers = new HashMap<String,LevelLayer>();
	private HashMap<String,String> levelProperties = new HashMap<String,String>();
	
	private ArrayList<TileType> tileTypes = null;
	


	
	public void init(Screen screen, Camera camera){
		CollisionHandler.setLevel(this);
		Entity.setCamera(camera);
		Entity.setScreen(screen);
		this.screen = screen;
		this.camera = camera;

		background = new Background(0,0,"/backgrounds/smallclouds.png",camera);
		
	
		//NOTE: ALL TILESET INITIALIZATION MOVED TO TMX READER
		//Initialize tilesets
		//for(TileSet eachTileSet:levelTileSets){
			//The tileSet had most values initialized from TMX file, but needs to load images
			//into SpriteSheet
		//	eachTileSet.init();
		//}
		

	
		//Initialize objects in the entities layer
		//(this depends on tileSets already being initialized!!!!)
		LevelObjectGroup entitiesObjGroup = levelObjectGroups.get("Entities");
		if (entitiesObjGroup != null){
			//Match gid to existing tiletype
			//Check properties of corresponding tiletype
			entitiesObjGroup.init(levelTileSets);
			//Retrieve our hero!!!!!
			
			if (entitiesObjGroup.getHero() != null){
				this.hero = entitiesObjGroup.getHero();
				System.out.println("Hero loaded!");
			}


		}else{
			this.hero = new Hero(100,100); //Default hero spawn if no spawnpoint specified
		}
		
	
		

		
		//At this point, the tile
		if (TileType.getAllTileTypes()!= null){
			this.tileTypes = TileType.getAllTileTypes();
		}else{
			System.out.println("tileTypes not set on Screen!");
		}
		
		for(Map.Entry<String, LevelLayer> layerEntry:levelLayers.entrySet()){
			//System.out.println(layerEntry.getKey() +" :: "+ layerEntry.getValue());
			
			//Camera and hero are passed to each tile at this level!
			layerEntry.getValue().init(camera, hero, screen); 
		}
		
		//Pass the tiles in the collision layer to the collision handler
		CollisionHandler.setTiles(   ( levelLayers.get("Collision").getTiles() )    );
		
	}
	
	
	public void update(){

		background.update();
		//Using EntrySet to iterate through HashMap
		for(Map.Entry<String, LevelLayer> layerEntry:levelLayers.entrySet()){
			//System.out.println(layerEntry.getKey() +" :: "+ layerEntry.getValue());
			layerEntry.getValue().update();
		}
		
		//Update level objects
		
		LevelObjectGroup levelEntities = levelObjectGroups.get("Entities");
		for (Entity eachEntity :levelEntities.getEntities()){
			eachEntity.update();
		}
		
		
		
		
	}
	
	


	public void render(){
		
		//Render each MapLayer
		//for(Map.Entry<String, LevelLayer> layerEntry:levelLayers.entrySet()){
		//	//System.out.println(layerEntry.getKey() +" :: "+ layerEntry.getValue());
		//	layerEntry.getValue().render(screen);
		//}
		background.render(screen);
		levelLayers.get("Background").render(screen); //not the scrolling background!
		levelLayers.get("Collision").render(screen);
		hero.render();
		
		//Render all other entities.
		LevelObjectGroup levelEntities = levelObjectGroups.get("Entities");
		for (Entity eachEntity :levelEntities.getEntities()){
			
			eachEntity.render();
		}
		
		levelLayers.get("Foreground").render(screen);
		
		
		
		
		
	}
	
	
	
	public void info(){
		//Prints out information to the commandline about the current map
		//Useful for testing purposes
	}		
	
	
	
	
	
	
	
	
	
	//Add objects to hash maps methods

	public void addLayer(String name,LevelLayer levelLayer){
           levelLayers.put(name, levelLayer);
    
	}

	public void addTileSet(TileSet tileSet){
		levelTileSets.add(tileSet);
	}

	public void addObjectGroup(String name,LevelObjectGroup objectGroup){
		levelObjectGroups.put(name, objectGroup);
	}

	
	
//Getters and setters

	public Hero getHero() {
		return hero;
	}
	public void setHero(Hero hero){
		this.hero = hero;
	}
	
	public int getX() {
		return x;
	}




	public void setX(int X) {
		this.x = X;
	}




	public int getY() {
		return y;
	}




	public void setY(int Y) {
		this.y = Y;
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




	public int getWidthInTiles() {
		return widthInTiles;
	}
	public void setWidthInTiles(int widthInTiles) {
		this.widthInTiles = widthInTiles;
	}
	public int getHeightInTiles() {
		return heightInTiles;
	}
	public void setHeightInTiles(int heightInTiles) {
		this.heightInTiles = heightInTiles;
	}
	public int getTileWidth() {
		return tileWidth;
	}


	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}


	public int getTileHeight() {
		return tileHeight;
	}


	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}


	public int getMaxX(){
		return this.x+this.width;
	}
	
	public int getMaxY(){
		return this.y+this.height;
		
	}







	public HashMap<String, LevelObjectGroup> getLevelObjectGroups() {
		return levelObjectGroups;
	}


	public void setLevelObjectGroups(HashMap<String, LevelObjectGroup> levelObjectGroups) {
		this.levelObjectGroups = levelObjectGroups;
	}


	public HashMap<String, LevelLayer> getLevelLayers() {
		return levelLayers;
	}


	public void setLevelLayers(HashMap<String, LevelLayer> levelLayers) {
		this.levelLayers = levelLayers;
	}


	public HashMap<String, String> getLevelProperties() {
		return levelProperties;
	}


	public void setLevelProperties(HashMap<String, String> levelProperties) {
		this.levelProperties = levelProperties;
	}
		
}
