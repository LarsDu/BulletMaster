package bulletmaster.old;


import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import bulletmaster.old.SimpleTile;


public class StartingClass extends Applet implements Runnable, KeyListener{

	//Make a hero 
	private static testHero bulletMonk;
	private Hornworm hrn1,hrn2;
	private Image image, currentHeroSprite, character, 
	characterCrouched, characterJumped, backgroundSky, backgroundGreen,hornworm,
	    bullet;
	public static Image orangeSolid, wave, blueSolid;

	private URL base;
	private Graphics second;
	private static Background bg1, bg2, bg3;
	private Level level1;
	private static Camera camera1;
	

	

    private ArrayList<BaseTile> tilearray = new ArrayList<BaseTile>();
	private ArrayList<Projectile> projectiles =  new ArrayList<Projectile>();
	

	
	//Note that init(),start(),stop(), and destroy are methods
	//inherited from Applet

	@Override
	public void init() {

		/*Initializing*/
		System.out.println("Initializing!\n");
		
			
		/*Set screen size*/
		setSize(1024,64*8);
		
		/*Set background*/
		setBackground(Color.BLACK);
		setFocusable(true);
		addKeyListener(this);
		/*Assign the applet window to the frame*/
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("Bullet Master");
		try{
			base = getCodeBase();
	
		}catch (Exception e){
			//TODO: handle exception
		}

		//Image setups
		character = getImage(base,"../img/proto.png");
		characterCrouched = getImage(base,"../img/protocrouch.png");
		characterJumped = getImage(base,"../img/proto.png");		
		currentHeroSprite = character; 		
		backgroundSky = getImage(base,"../img/background.png");
		backgroundGreen = getImage(base,"../img/background_green.png");
		hornworm = getImage(base,"../img/protobaddie.png");
		bullet = getImage(base,"../img/bullet.png");
		orangeSolid = getImage(base,"../img/64x64_orange.png");
		wave = getImage(base,"../img/64x64_wave.png");
		blueSolid = getImage(base,"../img/64x64_blue.png");
		//Test code
		
		


	}

	@Override
	public void start() {
		//Initialization hierarchy
		//To avoid code spaghettification always follow this order 
		//for loading
		//Level>Tiles>Camera>Target>Hero>Background> Enemies > Movable Widgets
		
		//Initialize empty map
		level1 = new Level();
		// Initialize Tiles
		try {
			level1.loadLevelSimple("../levels/testmap1.txt");
		}catch(IOException e){
			System.out.println("Level load error!");
			e.printStackTrace();
		}
		//Initialize camera
		camera1 = new Camera(level1); //Camera needs to know map bounds
		
		//Initialize Hero
		bulletMonk = new testHero(384,288); 
		bg1 = new Background(0,0);
		bg2 = new Background(3000,0);
		bg3 = new Background(0,480);
		hrn1 = new Hornworm(384,200);
		hrn2 = new Hornworm(2000,200);

		Thread thread = new Thread(this);
		thread.start();
	}//start

	
	

	@Override
	public void stop() {
		// TODO Auto-generated method stub
	
	}

	
	
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}


	@Override
	public void run() {

		while (true) {
			//Try to always follow this update order
			//cameraTarget>Hero>Enemy>Background>Camera
			//MovableWidgets>Enemies>Background>Hero>Target>Camera>Tiles>Map
		    //Draw entire scene
			bulletMonk.update();
			camera1.update(bulletMonk);	//Camera needs a target to track
			
			
			if(bulletMonk.isJumped()){
				currentHeroSprite = characterJumped;
			}else if (bulletMonk.isJumped() == false && bulletMonk.isDucked() == false){
					currentHeroSprite = character;
			}
			
			
			ArrayList projectiles = bulletMonk.getProjectiles();
			for (int i = 0; i < projectiles.size(); i++){
				Projectile p = (Projectile) projectiles.get(i);
				if (p.isVisible() == true){
					p.update();
				}else{
					projectiles.remove(i);
				}
			}
			updateTiles(camera1,bulletMonk);
			
			hrn1.update();
			hrn2.update();
			bg1.update(camera1);
			bg2.update(camera1);
			bg3.update(camera1);
			
			
			//Clear the screen
			repaint();
			try{
				Thread.sleep(17);
			} catch (InterruptedException e){
				e.printStackTrace();
			}
		}//while loop end
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Update graphics with double buffering system
	@Override
	public void update(Graphics g) {
		if (image == null) {
			image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
		}

		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);
		g.drawImage(image, 0, 0, this);

	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(backgroundSky, bg1.getBgX(),bg1.getBgY(),this);
		g.drawImage(backgroundGreen, bg2.getBgX(),bg2.getBgY(),this);
		g.drawImage(backgroundGreen,bg3.getBgX(),bg3.getBgY(),this);
		paintTiles(g);

		g.drawImage(hornworm, hrn1.getCameraX(camera1), hrn1.getCameraY(camera1), this);
		g.drawImage(hornworm, hrn2.getCameraX(camera1), hrn2.getCameraY(camera1), this);
		
		//Draw projectiles
		ArrayList projectiles = bulletMonk.getProjectiles();
		for (int i=0; i<projectiles.size(); i++){
			//Note: Parenthesis around Projectile is for explicit typecast
			Projectile p = (Projectile) projectiles.get(i);
			g.drawImage(bullet,p.getX(),p.getY(),this);
		}
		
		
		g.drawImage(currentHeroSprite,bulletMonk.getCameraX(camera1),bulletMonk.getCameraY(camera1),this);
		
		//g.drawRect((int)bulletMonk.boundingBox.getX(),
		//		(int)bulletMonk.boundingBox.getY(),
		//		(int)bulletMonk.boundingBox.getWidth(),
		//		(int)bulletMonk.boundingBox.getHeight());

		//g.drawRect((int)bulletMonk.topSensor.getX(),
		//		(int)bulletMonk.topSensor.getY(),
		//		(int)bulletMonk.topSensor.getWidth(),
		//		(int)bulletMonk.topSensor.getHeight());	
		
		//g.drawRect((int)bulletMonk.upperLeftSensor.getX(),
		//			(int)bulletMonk.upperLeftSensor.getY(),
		//			(int)bulletMonk.upperLeftSensor.getWidth(),
		//			(int)bulletMonk.upperLeftSensor.getHeight());
	}//paint

	
	
	
	private void updateTiles(Camera camera1, testHero hero) {
		for (int i = 0; i < level1.getTilearray().size(); i++) {
			SimpleTile t = (SimpleTile) level1.getTilearray().get(i);
			t.update(camera1, hero);
		}//for
	}//updateTiles()

	

    private void paintTiles(Graphics g) {
    	for (int i = 0; i < level1.getTilearray().size(); i++) {
	    	SimpleTile t = (SimpleTile) level1.getTilearray().get(i);
	    	g.drawImage(t.getTileImage(), t.getScreenTileX(camera1), t.getScreenTileY(camera1), this);
		}
    }//paintTiles()
	

	//Handle input
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			System.out.println("Move up");	
			break;

		case KeyEvent.VK_DOWN:
			System.out.println("Move down");
			currentHeroSprite = characterCrouched;
			if (bulletMonk.isJumped() == false){
			bulletMonk.setDucked(true);
			bulletMonk.setSpeedX(0);
			}
			break;

		case KeyEvent.VK_LEFT:
			System.out.println("Move left");
			bulletMonk.moveLeft();
			break;

		case KeyEvent.VK_RIGHT:
			System.out.println("Move right");
			bulletMonk.moveRight();
			break;

		case KeyEvent.VK_SPACE:
			System.out.println("Jump");
			bulletMonk.moveJump();
			break;
			
		
		case KeyEvent.VK_CONTROL:
			bulletMonk.setReadyToFire(true);
			bulletMonk.shoot();
			break;
		}
	}

	public void keyReleased(KeyEvent e) {

		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			System.out.println("Stop moving up");
			break;

		case KeyEvent.VK_DOWN:
			System.out.println("Stop moving down");
			currentHeroSprite = character;
			bulletMonk.setDucked(false);
			break;

		case KeyEvent.VK_LEFT:
			bulletMonk.stopLeft();
			System.out.println("Stop moving left");
			break;

		case KeyEvent.VK_RIGHT:
			bulletMonk.stopRight();
			System.out.println("Stop moving right");
			break;

		case KeyEvent.VK_SPACE:
			System.out.println("Stop jumping");
			break;

			
		case KeyEvent.VK_CONTROL:
			bulletMonk.setReadyToFire(false);
			break;
		}	    
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}



	//Get and set methods
	public static Background getBg1() {
		return bg1;
    }

		
    public static Background getBg2() {
        return bg2;
    }

	public static testHero getBulletMonk() {
		return bulletMonk;
	}
	
	public static Camera getCamera1() {
		return camera1;
	}



	public void setBulletMonk(testHero bulletMonk) {
		this.bulletMonk = bulletMonk;
	}





}
