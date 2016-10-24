package bulletmaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bulletmaster.Tiles.TileSet;
import bulletmaster.Tiles.TileType;
import bulletmaster.entity.Entity;
import bulletmaster.entity.Hero;
import bulletmaster.entity.Enemies.BoarEnemy;




public class LevelObjectGroup {
	//Objects are just placeholders for spawning entities
	private ArrayList <LevelObject> objects;
	
	//Entities are enemies and things like movable platforms.
	public List<Entity> entities;
	
	private HashMap<String,Animation> animations;
	private ArrayList<TileType> tiletypes;

	private Hero hero;
	
	public void init(ArrayList<TileSet> allTileSets){

		
		entities = new ArrayList<Entity>();
		//For each object in this group,
		//check its gid against all existing tiletypes
		//eachTileSet.
		for(LevelObject eachObject: objects){
			for(TileSet eachTileSet: allTileSets){
				for (TileType eachTileType:	eachTileSet.getTileTypes()){
					if(eachTileType.getGid() == eachObject.getGid()){
						//If gid's match, check tileType properties
						HashMap<String,String> tTypeProps = eachTileType.getProperties();
							
							if(tTypeProps.containsKey("type")){
								String typeString = tTypeProps.get("type");
								System.out.println(typeString);
								if(typeString.equals( "hero_spawn")){
									//Make a hero at object position.
									//System.out.println("Hero initialized");
									hero = new Hero(eachObject.getSpawnX(),eachObject.getSpawnY());
									
							
								}else if (typeString.equals("boar_enemy")){
									entities.add(new BoarEnemy(eachObject.getSpawnX(),eachObject.getSpawnY()));
									
								}
								
							
								
							}
					}
				}
			}

		}
	}
	public void setAnimations(HashMap<String,Animation> animations){
		this.animations=animations;
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	public ArrayList<TileType> getTiletypes() {
		return tiletypes;
	}
	public void setTiletypes(ArrayList<TileType> tiletypes) {
		this.tiletypes = tiletypes;
	}
	public Hero getHero() {
		return hero;
	}
	public void setHero(Hero hero) {
		this.hero = hero;
	}
	public ArrayList<LevelObject> getObjects() {
		return objects;
	}
	public HashMap<String, Animation> getAnimations() {
		return animations;
	}
	public void addAnimation(String animName, Animation animation){
		this.animations.put(animName, animation);
	}

	public void setObjects(ArrayList<LevelObject> objects){//beware this overwrites!
		this.objects = objects;
	}

	public void addObjects(LevelObject anObject) {
		objects.add(anObject);
	}
	
	

	
	
}
