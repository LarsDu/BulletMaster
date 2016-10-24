package bulletmaster;


import java.util.ArrayList;

import bulletmaster.gfx.Sprite;
import bulletmaster.gfx.SpriteSheet;

public class Animation {

	
	
		//Class variables
		//Note the imageHeight%height = 0 and imageWidth%=0, else through an error
		private int x;
		private int y;
		private int width; //width of each frame
		private int height; //height of each frame
		private String imgSource; //The image source for this Animation's sprite sheet
		private SpriteSheet sheet; //
		
		
		//private AnimFrame[] frames;
		private ArrayList<AnimFrame> frames;
		private int currentFrame; //index of current frame
		private int animTime;
		private int totalDuration;
	
		
		//Constructor with x and y paramters
		public Animation(int x, int y, int w, int h, String imgSource){
			//Constructor from imgSource
			this.x = x;
			this.y = y;
			this.width = w;
			this.height = h;
			this.imgSource = imgSource;
			
			this.sheet = new SpriteSheet(imgSource);
		
			//Initialize frames arraylist with size(optional)
			int numFrames = (int) (this.sheet.getWidth()*this.sheet.getHeight() )/(this.width*this.height);
			frames = new ArrayList <AnimFrame>(numFrames);
			//frames = new AnimFrame[numFrames];
			totalDuration = 0;
			
			
			synchronized (this){
				animTime = 0;
				currentFrame = 0;
			}
		}
		
		public Animation(int x, int y, int w, int h, SpriteSheet sheet){
			//Constructor from spritesheet
			this.x = x;
			this.y = y;
			this.width = w;
			this.height = h;
		
			
			this.sheet =sheet;
			
			//Initialize frames arraylist with size(optional)
			int numFrames = (int) (this.sheet.getWidth()*this.sheet.getHeight() )/(this.width*this.height);
			frames = new ArrayList <AnimFrame>(numFrames);
			//frames = new AnimFrame[numFrames];
			totalDuration = 0;
			
			
			synchronized (this){
				animTime = 0;
				currentFrame = 0;
			}
		}
		
		
		//Constructor no parameters
		public Animation(){
			frames =null;
			totalDuration = 0;
			
			
			synchronized (this){
				animTime = 0;
				currentFrame = 0;
			}
		}
		
		
		
		
		//Append AnimFrame object to frames ArrayList
		public synchronized void addNewFrame(Sprite sprite, int duration){
			this.totalDuration += duration;
			frames.add(new AnimFrame(sprite, totalDuration));
		}
		
		public synchronized void addNewFrame(SpriteSheet spriteSheet,int frameWidth,
				                        int frameHeight, int frameIndex, int duration){
			//Dimension check
			if(this.sheet.getWidth()%frameWidth != 0 || this.sheet.getHeight()%frameHeight != 0){
				System.out.println("frameWidth: "+frameWidth+"\nframeHeight "+frameHeight);
				System.out.println("sheet.width: "+sheet.getWidth()+"\nsheetHeight "+sheet.getHeight());
				System.out.println("Dimensions of spriteSheet and frame height or width are not divisible!");
				return;
			}
			
			
			Sprite sprite = null;
			int framesPerRow = this.sheet.getWidth()/frameWidth;
			int framesPerCol = this.sheet.getHeight()/frameHeight;
			for(int i=0; i<framesPerRow;++i){
				for( int j=0;j<framesPerCol;++j){
					if (frameIndex == (i+j*framesPerRow)){
						sprite = this.sheet.cutSprite(i*frameWidth, j*frameHeight, frameWidth, frameHeight);
						this.addNewFrame(sprite, duration);
					}
				}
			}
			
			this.totalDuration += duration;
			if (sprite !=null){
				frames.add(new AnimFrame(sprite, totalDuration));
			}	
		}
		
		
		public void autoFrames(int frameWidth,int frameHeight, int standardDuration ){
			//Dimension check
			if(this.sheet.getWidth()%frameWidth != 0 || this.sheet.getHeight()%frameHeight != 0){
				System.out.println("frameWidth: "+frameWidth+"\nframeHeight "+frameHeight);
				System.out.println("sheet.width: "+sheet.getWidth()+"\nsheetHeight "+sheet.getHeight());
				System.out.println("Dimensions of spriteSheet and frame height or width are not divisible!");
				return;
			}
			//Generates frames automatically from SpriteSheet dimensions
			//New frames are created from left to right, up to down
			int framesPerRow = this.sheet.getWidth()/frameWidth;
			int framesPerCol = this.sheet.getHeight()/frameHeight;
			
			for(int i=0; i<framesPerRow;++i){
				for( int j=0;j<framesPerCol;++j){
					Sprite tempSprite = this.sheet.cutSprite(i*frameWidth, j*frameHeight, frameWidth, frameHeight);
					this.addNewFrame(tempSprite, standardDuration);
					
				}
			}
		}
		
		public void autoFramesFromRow(int frameWidth,int frameHeight,int columnIndex, int standardDuration){
			//Dimension check
			if(this.sheet.getWidth()%frameWidth != 0 || this.sheet.getHeight()%frameHeight != 0){
			
				System.out.println("Dimensions of spriteSheet and frame height or width are not divisible!");
				return;
			}
			
			//Generates frames automatically from a row of images in a spritesheet.
			//Specify the proper row in columnIndex
			int framesPerRow = this.sheet.getWidth()/frameWidth;
				
			for(int i=0; i<framesPerRow;++i){
				
					Sprite tempSprite = this.sheet.cutSprite(i*frameWidth,columnIndex*frameHeight, frameWidth, frameHeight);
					this.addNewFrame(tempSprite, standardDuration);
			}
		}
		
		
		//Call this to switch frames 
		public synchronized void update(int elapsedTime){
			if (frames.size()>1){ //If there is more than 1 frame
				animTime += elapsedTime; //Add elapsedTime to animTime
				if (animTime >= totalDuration){
					animTime = animTime % totalDuration;
					currentFrame = 0;
				}
				
				while (animTime > getFrame(currentFrame).endTime) {
					currentFrame++;
				}
			}
		}
		//Get and set methods
		
		//Return image that belongs to currentFrame
		public synchronized Sprite getSprite() {
			if (frames.size()== 0) {
				return null;
				} else {
					return getFrame(currentFrame).sprite;
				}
		}		
		
		
		private AnimFrame getFrame(int i) {
			return (AnimFrame) frames.get(i);
		}
		
		
		
		//Nested AnimFrame class for encapsulating image frames
		private class AnimFrame{
			Sprite sprite;
			int endTime;
			
			public AnimFrame(Sprite sprite, int endTime){
				this.sprite = sprite;
				this.endTime = endTime;
			}
		}//AnimFrame class end
				
				
	
}
