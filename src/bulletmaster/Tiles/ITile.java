package bulletmaster.Tiles;

import bulletmaster.Camera;
import bulletmaster.collisions.CollidableActor;
import bulletmaster.entity.Hero;
import bulletmaster.gfx.Screen;


public interface ITile {

    public void init(Camera camera, Hero hero,Screen screen);
    public void update();
    public void visit();
    public void render(Screen screen);
    public void checkOnCamera(Camera camera);
    //Collision detection methods
    public void checkCollision(CollidableActor box);
    
    //Getters and setters
	public int getCameraX(Camera camera);
	public int getCameraY(Camera camera);
	public void setX(int x);
	public void setY(int y);
	public int getX();
	public int getY();
	public int getxIndex();
	public void setxIndex(int xIndex);
	public int getyIndex();
	public void setyIndex(int yIndex);
	public float getRotation();
	public void setRotation(float rotation);
	public int getGid();
	public void setGid(int gid);
	public boolean isHorFlip();
	public void setHorFlip(boolean horFlip);
	public boolean isVertFlip();
	public void setVertFlip(boolean vertFlip);
	public boolean isDiagFlip();
	public void setDiagFlip(boolean diagFlip);
	public void setRotationFlags(boolean [] flags);
	public boolean isOnCamera();

	public void setOnCamera(boolean onCamera);

    public int getTypeId();

	public void setTypeId(int type);

	public TileType getTileType();
	
	public void setTileType(TileType tileType);

	public boolean isDrawOutline();

	public void setDrawOutline(boolean drawOutline);
}
