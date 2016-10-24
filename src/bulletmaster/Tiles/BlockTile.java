package bulletmaster.Tiles;

import java.awt.Rectangle;

import bulletmaster.collisions.CollidableActor;
import bulletmaster.collisions.CollisionBox;
import bulletmaster.collisions.CollisionHandler;

public class BlockTile extends BaseTile implements ITile {

	

	public BlockTile(int xInd, int yInd, int gridId, TileType tileType,boolean hFlip,boolean vFlip, boolean dFlip) {
		//x and y represent tile indices, not coordinates!
		this.x = xInd * STDWIDTH;  
		this.y = yInd * STDHEIGHT;
		this.gid = gridId;
		this.tileType = tileType;
		this.typeId = tileType.getTypeId();
		this.tileRect = new Rectangle(x,y,STDWIDTH,STDHEIGHT);
		this.setHorFlip(hFlip);
		this.setVertFlip(vFlip);
		this.setDiagFlip(dFlip);
		
		this.drawOutline = false;
		this.hasFullCollision = true;
		
	}//Tile constructor


	//@Override
    //public void checkCollision(){
    //	//if(onCamera//){
    //		CollisionHandler.add(this);
    //	//}
    //}
    


	public void checkCollision2(CollidableActor actor){

		//Change in coordinates relative to the tile x y coordinates (upper left hand coord)

		//Check if box is in collision detection sensor range of the this.
		//If not, exit and do nothing!
		CollisionBox box = actor.getCollisionBox();	
		if(!(box.getMainBox().intersects(this.getTileRect()))){
			return;
		}

		


		//Bottom collisions
		if (box.getLlSensor().intersects(this.getTileRect()) || 
					box.getLrSensor().intersects(this.getTileRect())){
			
		//if(box.getGroundPoint().getX()>=this.getX() && box.getGroundPoint().getX()<this.getX()+this.tileRect.getWidth() 
		//		&& box.getGroundPoint().getY()>this.getY() && box.getGroundPoint().getY()<this.getY()+BlockTile.STDHEIGHT){
			
			actor.setSpeedY(0);
			actor.setCollisionBoxY(this.getY()-(int) box.getHeight());
			
		}
		//Top collisions
		if (box.getTopSensor().intersects(this.getTileRect())){
			actor.setSpeedY(0);
			actor.setCollisionBoxY(this.getY()+BlockTile.STDHEIGHT);
			
		}
		//Left collisions
		if (box.getUlSensor().intersects(this.getTileRect())
				&& !(box.getTopSensor().intersects(this.getTileRect())) ){
			actor.setSpeedX(0);
			actor.setCollisionBoxX( this.getX()+BlockTile.STDWIDTH);
			
		}

		//Right collisions
		if(box.getUrSensor().intersects(this.getTileRect()) && !(box.getTopSensor().intersects(this.getTileRect())) ){
			actor.setSpeedX(0);
			actor.setCollisionBoxX( this.getX()-(int)box.getWidth());
		}

		//System.out.println("tileX" +tile.getX());
		//System.out.println("deltaX"+deltaX);
		//System.out.println("deltaY"+deltaY);
		//Now reposition box to new coordinates relative to tile coordinates (moving all boundingboxes and collision sensors)
		//box.reposition(tile.getX()+deltaX, tile.getY()+deltaY);

	}
	
	
	public void checkCollision(CollidableActor actor){
		CollisionBox box = actor.getCollisionBox();	
		if(!(box.getMainBox().intersects(this.getTileRect()))){
			return;
		}
		

		
		//Bottom collisions
		//if (box.getLlSensor().intersects(this.getTileRect()) ){
			//		box.getLrSensor().intersects(this.getTileRect())){

		if(box.getGroundPoint().getX()>=this.getX() && box.getGroundPoint().getX()<this.getX()+this.tileRect.getWidth() 
				&& box.getGroundPoint().getY()>this.getY() && box.getGroundPoint().getY()<this.getY()+BlockTile.STDHEIGHT){
			
			actor.setSpeedY(0);
			actor.setCollisionBoxY(this.getY()-(int) box.getMainBox().getHeight());
			box.setBottomCollision(true);
		}else{box.setBottomCollision(false);}
		//Top collisions
		if (box.getTopSensor().intersects(this.getTileRect())){
			actor.setSpeedY(0);
			actor.setCollisionBoxY(this.getY()+BlockTile.STDHEIGHT);
			box.setTopCollision(true);
		}else{box.setTopCollision(false);}
		//Left collisions
		if (box.getUlSensor().intersects(this.getTileRect())
				&& box.isTopCollision()==false && box.isBottomCollision() == false){
			actor.setSpeedX(0);
			actor.setCollisionBoxX( this.getX()+BlockTile.STDWIDTH);
			box.setLeftCollision(true);
		}

		//Right collisions
		if(box.getUrSensor().intersects(this.getTileRect()) && box.isTopCollision()==false && box.isBottomCollision() == false ){
			actor.setSpeedX(0);
			actor.setCollisionBoxX( this.getX()-(int)box.getMainBox().getWidth());
			box.setRightCollision(true);
		}

		

	}
	

}