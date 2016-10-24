package bulletmaster;

import bulletmaster.gfx.Screen;
import bulletmaster.gfx.Sprite;
import bulletmaster.gfx.SpriteSheet;


public class Background{
	
	private int x, y;
	private int speedX, speedY;
	private SpriteSheet sheet;
	private Sprite sprite;
	private Camera camera;
	public Background( int x, int y, String filename, Camera camera){
		this.x = x;
		this.y = y;
		sheet = new SpriteSheet(filename);
		sprite = sheet.cutSprite(0, 0, sheet.getWidth(), sheet.getHeight());
		this.camera = camera;
		speedX = 0;
		speedY = 0;
		
	}
		
	public void update(){

		//speedX = 20;
		speedX =-camera.getSpeedX()/4;
		this.x += speedX;
		if(this.getCameraX(camera)<-sprite.getWidth()){
			this.x =camera.getX();
		}else if(this.getCameraX(camera)>sprite.getWidth()){
			this.x = camera.getX();
		}


		}

	public void render(Screen screen){
		
		if( sheet!=null && screen!=null){
			
			//screen.drawSolidRect(0, 0, 640, 480, 0xff);
		
			//screen.renderScrollingBg(this.x-camera.getX(), 0, sprite, camera);
			screen.renderSprite(this.getCameraX(camera), 0, sprite);
			if(this.getCameraX(camera)>0){
				screen.renderSprite(this.getCameraX(camera)-this.sprite.getWidth(),0, sprite);
			}
			
			if(this.getCameraX(camera)<this.sprite.getWidth()-camera.getWidth()){
				screen.renderSprite(this.getCameraX(camera)+this.sprite.getWidth(), 0, sprite);
			}
		

		}
	}
	

	
	public int getCameraX(Camera camera){
		return this.x-camera.getX();
	}
	
	public int getCameraY(Camera camera){
		return this.y-camera.getY();
	}
	
		
		
}
