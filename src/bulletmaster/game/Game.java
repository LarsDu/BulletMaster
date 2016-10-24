package bulletmaster.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;

import bulletmaster.Camera;
import bulletmaster.InputHandler;
import bulletmaster.Level;
import bulletmaster.TMXReader;
import bulletmaster.collisions.CollisionHandler;
import bulletmaster.entity.Hero;
import bulletmaster.gfx.Screen;

public class Game extends Canvas implements Runnable{

	public static final int WIDTH = 720;
	public static final int HEIGHT = 480;
	
	private static final int SCALE =1;
	public static final String NAME ="GAME";
	
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	private Screen screen = null;
	private static CollisionHandler collisionHandler;

	
	private boolean running = false;

	//Game specific assets 
	private Level level = null;
	
	//FIX THIS
	//private static String basedir = new File("").getCanonicalPath();

	//Input handler
	private InputHandler input;
	
	//TEMPORARY TEST VARIABLES
	private Camera camera;
	private Hero player = null; 

	
	
	private void init(){
		//init() is Called under Game.run()
		input = new InputHandler(this);
		screen = new Screen((WIDTH*SCALE), (HEIGHT*SCALE));	
		level = loadLevelTmx("/home/ladu/Code/BulletMaster/levels/curved.tmx");
		camera = new Camera(WIDTH, HEIGHT, level.getWidth(), level.getHeight());
		
		//Tell us about the current level.
		//level.info();
		
		//Initialize the level and all children
		
		level.init(screen,camera); //each tile needs to be linked with camera and screen
		
		if (level.getHero()!=null){
			player= level.getHero(); //Each level should have a hero spawn
			player.init(input,camera); //Hero needs to be linked to control and camera
		}else{
			System.out.println("Failed to located player spawnpoint!");
		}
		
		//Initialize Hero

	}
	
	
	
	
	





	public void start(){
		running = true;
		new Thread(this).start();
		
		
	}
	
	public void stop(){
		running = false;
	}

	public void run(){
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000.0/60.0;
		int frames = 0;
		int ticks = 0;
		
		long lastFrameTimer = System.currentTimeMillis();
		//millisecond timer is used for calculating
		//frame rate.
		
		//unprocessed will keep the game from ticking
		//until 1 second passes.
		double unprocessed = 0;
		
		//Initialize game here!
		init();
		
		while (running){
			long now = System.nanoTime();
			unprocessed +=(now-lastTime)/nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			while(unprocessed>=1){
				ticks++;
				update();
				unprocessed -= 1; //reset
				shouldRender=true;
				
			}
			
			try{
				Thread.sleep(2);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			
			if(shouldRender){
				frames++;
				render();
			}
			
			
			if(System.currentTimeMillis()-lastFrameTimer>=1000){
				//Calculate frame rate
				lastFrameTimer += 1000;
				System.out.println(ticks +" ticks, frames "+frames);
				frames = 0;
				ticks = 0;
			}
			
		}
	}
	

	public void resetGame(){
	}
	
	
	public void update(){
		
		
		
		if (player!= null){
			player.update();
		}
		camera.update(player); //camera target is player

		level.update();
				
		CollisionHandler.update();
		
		
		
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}

		// retrieves a graphics object from the next in line buffer in the bufferstrategy,
		//this graphics object draws to that buffer
		Graphics g= bs.getDrawGraphics();
		
		//set all screen pixels to 0
		//screen.clear();
		level.render();
		//getPixels is where game things are actually drawn onto the screen
		//Pixels are retrieved from all objects painted onto the screen
		getPixels();
				
		
		//draw the bufferedImage to the next available buffer
		g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
      
		g.dispose();
        // orders the next in line buffer (which the graphics object g is tied to) 
        // to show its contents on the canvas 
        bs.show();
	
	}
	
	
	public static void main(String[] args) {
		Game game = new Game();
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		JFrame frame = new JFrame(Game.NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(game, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		game.start();
	}
	
	public void getPixels() {
	    for(int i = 0; i < pixels.length; i++)
	        pixels[i] = screen.pixels[i];
	}
	
	
	
	public Level loadLevelTmx (String filename){
		//Reads and parses TMX file
		//Returns level object
		//Updating the map will update all MapLayers>Tiles>Entities etc.
		TMXReader tmxRead = new TMXReader();
		Level level = null;
        try{

			//InputStream is = new BufferedInputStream(new FileInputStream("/home/ladu/Code/BulletMaster/levels/test1base64.tmx"));
        	InputStream is = new BufferedInputStream(new FileInputStream(filename));
	
			try {
				level = tmxRead.readLevel(is);
			} catch (Exception e) {
				
				e.printStackTrace();
				
			}
			
        }catch(IOException e){
        	
        }
        return level;
	}
	
	
	
	
	//Getters and setters	
	public InputHandler getInput() {
		return input;
	}

	public Camera getCamera() {
		return camera;
	}


	public static CollisionHandler getCollisionHandler() {
		return collisionHandler;
	}








	

	
	
	
	
	
	
}//Ends classdef



//For help check this boilerplate out:
//http://stackoverflow.com/questions/20852641/bufferedimage-int-pixels-and-rendering-how-do-they-work-they-work-together
