package bulletmaster.Tiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import bulletmaster.gfx.Sprite;

public class TileType {
	//TileTypes should be accessed from screen.getTileTypes()
	
	//A simple container for representing tileTypes
	HashMap<String,String> properties;
	
	
	int gid;
	//Gid = local id+firstgid
	
	//The type integer tells how this type of tile will react to the engine
	int typeId = TileType.EMPTY;
	
	//The Image for this TileType
	//int [] image;
	Sprite sprite;
	
	int width,height;
	
	//Tile properties
	//Each tileType is represented as an integer
	//Property values in the TMX map files are represented as strings
	//Here we translate from strings to int
	
	//A list of ALL tiletypes
	//Initialized by TMXReader
	private static ArrayList<TileType> allTileTypes = new ArrayList<TileType>();


	//TODO: replace with enum
	//Indices for tile types
	public final static int EMPTY = 0;
    public final static int SOLID = 1;
    public final static int BLOCK = 1;
    public final static int JUMPTHROUGH =2; //Solid on top but can be jumped through on bottom
    public final static int WATER = 3;
    public final static int CURVED = 4;
    public final static int SPRING =10;
 
    public final static int ANTIGRAV = 20;

	
    //Map for tile types
    //used for translating string values to 
    //tile type indices
    public static final HashMap<String, Integer> tileDict;
    static{
    	tileDict = new HashMap<String,Integer>();
    	tileDict.put("curved", CURVED);
    	tileDict.put("empty", EMPTY);
    	tileDict.put("block", BLOCK);
    	tileDict.put("solid", SOLID);
    	tileDict.put("jumpthrough", JUMPTHROUGH);
    	tileDict.put("water", WATER);
    	tileDict.put("spring", SPRING);
    	tileDict.put("antigrav", ANTIGRAV);
    	tileDict.put("hero_spawn", EMPTY);
    	tileDict.put("boar_enemy", EMPTY);
    	tileDict.put("hornworm", EMPTY);

    }
	
    
    //Empty constructor
    public TileType(){
    	this.gid = -1;
    	//Set default property
    	//This will be overwritten
    	//if the tile has a different properties.
    	typeId = EMPTY;
    	sprite = null;
    	
    }
    
    public TileType(int gridId){
    	this.gid = gridId;
    	//Set default property
    	//This will be overwritten
    	//if the tile has a different properties.
    	typeId = EMPTY;
    	sprite = null;
    	
    }
    
    
	public void init(){
		//Remember, TileType objects were initialized on the 
		//initialization of each tileSet
		//The main screen should also have an arraylist of TileTypes
		
		//Set each tile type according to its last listed property
		//For now tiles will only have a single property,
		//but in the future, tiles may have multiple switchable properties
		//for(String typeString : properties.keySet()) {
				//type = tileDict.get(typeString);		
				//System.out.println("Tiletype: "+typeString);
				//System.out.println("Tiletype: "+type);
		//}
			
		//System.out.println("Initializing tileType "+this.gid);

		
		//Get loaded into screen
		checkPropertiesForType();
		//Add current tileType to global list
		TileType.add(this);
		
		
	}
	
	
	
	
	
	
	
	public void add(HashMap<String,String> props){
		
		this.properties = props;
		checkPropertiesForType();
	}
	
	public HashMap<String, String> getProperties() {
		
		return properties;
	}
	
	public void setProperties(HashMap<String, String> properties) {
		
		this.properties = properties;
		checkPropertiesForType();
	}
	
	public void checkPropertiesForType(){
		if (this.properties != null){
			if (properties.containsKey("type")){
				try{
					String typeString = properties.get("type");
					this.setTypeId( tileDict.get(typeString));
				}catch(NullPointerException e){
					System.out.println("Cannot find this type of tile!");
				}
			}else{
				this.setTypeId ( EMPTY);
			}
		}
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gridId) {
		this.gid = gridId;
	}
	
    public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int type) {
		this.typeId = type;
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

	
	public static void add(TileType [] tileTypes){
		
		ArrayList<TileType>convertedTileTypes = new ArrayList<TileType>(Arrays.asList(tileTypes));
		TileType.allTileTypes.addAll(convertedTileTypes);
	}
	

	public static ArrayList<TileType> getAllTileTypes() {
		
		return allTileTypes;
	}

	public static void setTileTypes(ArrayList<TileType> tileTypes) {
		
		TileType.allTileTypes = tileTypes;
	}
	

	
	public static void add(TileType tileType){

		TileType.allTileTypes.add(tileType);
	}
	
	public static void clearAllTileTypes(){
		//Clears global allTileTypes list
		TileType.allTileTypes.clear();
	}

}
