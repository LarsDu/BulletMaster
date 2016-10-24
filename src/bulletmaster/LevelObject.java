package bulletmaster;

import java.util.HashMap;

import bulletmaster.Tiles.TileType;
import bulletmaster.gfx.Screen;

public class LevelObject {


	
	private int id;
	private int gid;
	private int spawnX;
	private int spawnY;

    private boolean horFlip=false;
    private boolean vertFlip=false;
    private boolean diagFlip=false;
	
	private TileType tiletype;
	
	private HashMap<String,String> properties = new HashMap<String,String>();
	

 
	
	//Getters and setters 
	

	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public int getSpawnX() {
		return spawnX;
	}
	public void setSpawnX(int spawnX) {
		this.spawnX = spawnX;
	}
	public int getSpawnY() {
		return spawnY;
	}
	public void setSpawnY(int spawnY) {
		this.spawnY = spawnY;
	}
	public TileType getTiletype() {
		return tiletype;
	}
	public void setTiletype(TileType tiletype) {
		this.tiletype = tiletype;
	}
	public HashMap<String, String> getProperties() {
		return properties;
	}
	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}

	public void setRotationFlags(boolean [] flags){
		this.horFlip= flags[0];
		this.vertFlip = flags[1];
		this.diagFlip = flags[2];
	}
	
}
