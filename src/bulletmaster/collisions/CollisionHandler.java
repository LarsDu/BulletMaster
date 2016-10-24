package bulletmaster.collisions;

import java.util.ArrayList;

import bulletmaster.Camera;
import bulletmaster.Level;
import bulletmaster.Tiles.BaseTile;
import bulletmaster.Tiles.BlockTile;
import bulletmaster.Tiles.ITile;
import bulletmaster.entity.Hero;

//Everything in this class is static!
//The collision handler is essentially global and accessible by all classes

/*
Collision detection works the following way:
1. Only entities and tiles get flagged as onCamera if they are on camera. This flag decides whether they get updated
and whether they end up in the collision handler's lists.

2. Collision handler will compare every pair of onCamera hasCollision==true objects and 
will reposition every object

*/

public class CollisionHandler {
	//Lists for holding onscreen objects
	//On each update cycle these lists are cleared.
	//http://eclipsesource.com/blogs/2014/04/11/3-good-reasons-to-avoid-arrays-in-java-interfaces/
	//public static ArrayList<Tile> tiles = new ArrayList<Tile>();
	public static ITile[][] tiles;
	public static ArrayList<CollidableActor> actors = new ArrayList<CollidableActor>();

	//The player hero is not in the entities list
	public static CollidableActor hero;
	
	//Level is needed for handling collisions with the level boundaries
	//Set by level initializer
	public static Level level;
	

	public static Camera camera;

	

	
	public static void update(){
		handleCollisions();
	
		
		//Clear lists
		//actors.clear();
		//For now, don't clear the tiles list!
		
	}
	
	
	
	
	
	
	
	public static void handleCollisions(){
		if(tiles == null){return;}
		//Replace this with a request to each object for their surrounding tiles
			if (hero !=null && hero.isHasFullCollision()==true){
					
					CollisionHandler.checkLocalTilesForCollision((CollidableActor) hero);
					CollisionHandler.keepInLevelBounds((CollidableActor) hero);
			}
			for (CollidableActor eachActor: actors){
				if(eachActor == null) continue;
				CollisionHandler.keepInLevelBounds((CollidableActor) eachActor);
				if(eachActor.isHasFullCollision()==true && eachActor.isAlive()){
					CollisionHandler.checkLocalTilesForCollision((CollidableActor)eachActor);
					//CollisionHandler.checkHeroActorCollision((CollidableActor) eachActor, (CollidableActor) hero);
					
					eachActor.checkCollision((Hero) hero);
				}
			}
	}
	
	
	
	//public static void checkHeroActorCollision(CollidableActor actor, CollidableActor hero){
	//	actor.checkCollision(hero);
	//}

	public static void keepInLevelBounds(CollidableActor actor){
		if(level == null) return;
		
		if(actor.getCollisionBox().getX()<level.getX()){
			actor.setCollisionBoxX(level.getX());
			actor.getCollisionBox().setLeftCollision(true);
		}
		if(actor.getCollisionBox().getMaxX()>level.getMaxX()){
			actor.setCollisionBoxX(level.getMaxX()-actor.getCollisionBox().getWidth());
			actor.getCollisionBox().setRightCollision(true);
		}
		if(actor.getCollisionBox().getY()<level.getY()){
			actor.setCollisionBoxY(level.getY());
			actor.getCollisionBox().setTopCollision(true);
		}
		if(actor.getCollisionBox().getY()>level.getMaxY()){
			//actor.setCollisionBoxY(level.getMaxY()-actor.getCollisionBox().getHeight());
			//actor.getCollisionBox().setBottomCollision(true);
			actor.die();
		
		}
		
		
	}
	
	public static void checkLocalTilesForCollision(CollidableActor actor){
		//Visit tiles near object of interest and check for collision.
		int[] tileRangeX =	actor.getCollisionBox().getLocalTilesX();
		int[] tileRangeY = actor.getCollisionBox().getLocalTilesY();
		
		if(tileRangeX[1]>=tiles.length-1){
			//Outside of level bounds right
			tileRangeX[1]=tiles.length-1;
		
		}
		if (tileRangeX[0]<=0){
			//Outside of level bounds left
			tileRangeX[0] = 0;
		
		}
		if(tileRangeY[1]>=tiles[0].length-1){
			//Outside of level bounds bottom
			tileRangeY[1]=tiles[0].length-1;
		}
		if (tileRangeY[0]<=0){
			//Outside of level bounds top
			tileRangeY[0] = 0;
		}
		//System.out.println("Tile range:"+tileRangeY[0]+" "+tileRangeY[1]);
		for(int i=tileRangeX[0]; i<=tileRangeX[1]; ++i){
			if(tileRangeX[0]<0||tileRangeX[1]>=tiles.length) continue;
			for(int j = tileRangeY[0]; j<=tileRangeY[1]; ++j){
				if(tileRangeY[0]< 0||tileRangeY[1]>= tiles[0].length) continue;
				if(CollisionHandler.tiles[i][j]!=null){
					CollisionHandler.tiles[i][j].checkCollision(actor);
					//CollisionHandler.tiles[i][j].visit();
					
				}
			
			}
		}

	}

	

	
	//public static void checkCollision(CollisionBox box, Tile tile){
		
	//}
	
	
	
	
	
	
	//public void checkCameraCollision(){
		
		//if (!levelRect.contains(camRect)){
		//	return;
		//}

	//	if (camera.getX()< level.getX()){
	//		camera.setX((int) level.getX());
	//	}else if (camera.getX()+camera.getWidth() > level.getMaxX()){
	//		camera.setX( (int)  level.getMaxX() - camera.getWidthX());
	//	}
	//	if(camera.y < levelRect.getMinY()){
	//		y = (int) levelRect.getMinY();
	//	}else if (camera.y +camera.height > levelRect.getMaxY()){
	//		y = (int) levelRect.getMaxY() - camera.height;
	//	}
	//}
	
	
	
	

	public static void setTiles(ITile [][] tiles){
		CollisionHandler.tiles = tiles;
	}
	
	public static void setLevel(Level level){
		CollisionHandler.level = level;
	}

	public static void add(CollidableActor actor){
		actors.add(actor);
	}
	
	//Get set methods
	public static void setHero(Hero hero){
		if(hero!=null){
			CollisionHandler.hero = hero;
		}
	}
	
}
