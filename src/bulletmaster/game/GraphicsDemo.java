package bulletmaster.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import bulletmaster.gfx.Screen;



//Good resource
//http://stackoverflow.com/questions/11163925/drawing-your-own-buffered-image-on-frame
public class GraphicsDemo extends Canvas implements Runnable{
	public static final int HEIGHT = 480;
	public static final int WIDTH = 640;
	public static final int SCALE =1;
	public static final String NAME ="Graphics demo";
	
	private BufferedImage image = new BufferedImage(WIDTH,HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	public static JFrame frame;
	
	
	
	
	
	private boolean running = false;

	
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
		
		while (running){
			long now = System.nanoTime();
			unprocessed +=(now-lastTime)/nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			while(unprocessed>=1){
				ticks++;
				tick();
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
				//System.out.println(ticks +" ticks, frames "+frames);
				frames = 0;
				ticks = 0;
			}
			
		}
	}
	
	public void tick(){
		//Tick the input,level,etc
	}
	
	public void resetGame(){
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}
		
		
		
		Color myColor = new Color(255,200,100,0); //colors
	
		int rgbInt = myColor.getRGB();
		
		System.out.println("Int value:" +rgbInt);
		
		//drawColorRect(100,100,100,100,rgbInt); //Color1
		
		BufferedImage protoImg = loadImg("proto.png");
		
		int ih = protoImg.getHeight();
		int iw = protoImg.getWidth();
		
		int [] protoPix = protoImg.getRGB(0, 0, iw, ih, null, 0, iw);
		
		int [] protoPix2 = renderEffectsTest(protoImg);
		
		
		//protoPix =rotateIntArray90(protoPix,iw,ih,0);
		//protoPix = rotateByFlags(protoPix,iw,ih,true,false,false);
				//protoPix = renderEffectsTest(protoImg);
		
		//renderImgIntAlpha(0,0,iw,ih,protoPix);
		
		image.setRGB(100, 100,iw,ih,protoPix2,0,iw);
		
		//for (int y = 0; y < height; y++)
		//{
		 //  for (int x = 0; x < width; x++)
		  // {
		  //     image[x + y * width] = value;
		   //}
		//}
		
		
		
		
		Graphics g2= bs.getDrawGraphics();
        g2.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
        clear(image);
        g2.dispose();
        bs.show();
	}
	
	public void drawColorRect(int x0, int y0, int w, int h, int color){
		for (int j = y0; j<h+y0;j++){
			for (int i = x0; i<h+x0;i++){
				image.setRGB(i,j,color);
			}
		}
	}
	
	
	public void clear(BufferedImage image){
		
		for (int j = 0; j<image.getHeight();j++){
			for (int i =0; i<image.getWidth();i++){
				image.setRGB(i,j,0xFFFF0000);
			}
		}
	}
	

	public void renderImgIntAlpha(int xPos, int yPos,int imgWidth,int imgHeight, int[] imgInt){
		//draw imgInt[] onto the screen pixel matrix

		for (int y = yPos; y < (yPos+imgHeight); ++y){
			if (y < 0 || y  >= imgHeight) continue;
			
			for (int x = xPos; x < (xPos+imgWidth); ++x){
				if (x < 0 || x  >= imgWidth) continue;
				
				//this.width is the screen width and is actually the indexing scanwidth
				//for the one dimensional array
				//this.pixels[x + y * this.width] = imgInt[(x-xPos)+(y-yPos)*imgWidth];
				int curPixel = imgInt[(x-xPos)+(y-yPos)*imgWidth];
				if(((curPixel & 0xff000000) >> 24) != 0){ //alpha masking
				
					//this.pixels[x + y * Screen.width] = ((curPixel&0xff000000) >>24);
					this.pixels[x + y * imgWidth] = 0xff; //blue
				}
			}
		}
	}
	

	public int[] rotateIntArray90(int [] sImg, int sWidth, int sHeight, 
			int numClockwiseRotations){
		//Rotate an integer array image with a given width and height
		//by 90 180 or 270 degrees counter-clockwise
		//and output the rotated array
		int arrSize = sWidth*sHeight;
		if (sImg.length != (arrSize)){
			System.out.println("Array length mismatch!");
			return sImg;
		}

		int[] dImg = new int[arrSize];

		numClockwiseRotations = numClockwiseRotations%4;

		switch (numClockwiseRotations){
		case 0: //no rotation
			break;
		case 1: //90 degree rotation

			for (int r = 0; r < sHeight; ++r){
				for (int c = 0; c < sWidth; ++c){
					//perform diagonal flip with column reversal
					dImg[r+(sWidth-1-c)*sHeight] = sImg[c+r*sWidth];
				}
			}
			
			break;
		case 2: //180 degree rotation
			for (int r = 0; r < sHeight; ++r){
				for (int c = 0; c < sWidth; ++c){
					//Row and column reversal
					dImg[(sWidth-1-c)+(sHeight-1-r)*sWidth] = sImg[c+r*sWidth];
				}
			}

			break;
		case 3: //270 degree rotation
			for (int r = 0; r < sHeight; ++r){
				for (int c = 0; c < sWidth; ++c){
					//Diagonal flip with column and row reversal
					dImg[(sHeight-1-r)+c*sHeight] = sImg[c+r*sWidth];
				}
			}
			break;
		}
		return dImg;
	}
	
	public int[] rotateByFlags(int [] sImg, int sWidth, int sHeight, 
			boolean hFlip, boolean vFlip, boolean dFlip){
		//Transform an integer array image based on 
		//horizontal, vertical, and diagonal flags
		
		//This is my favorite method!
		
		int arrSize = sWidth*sHeight;
		if (sImg.length != (arrSize)){
			System.out.println("Array length mismatch!");
			return sImg;
		}

		int[] dImg = new int[arrSize];

		//Destination parameters
		int dCol;
		int dRow;
		int dWidth = sWidth;
		int dSwap; //temp variable for perfoming diagonal swap
		for (int sRow = 0; sRow < sHeight; ++sRow){
			for (int sCol = 0; sCol < sWidth; ++sCol){
				//Default variables
				dCol = sCol;
				dRow = sRow;
				dWidth = sWidth;
				//Overwrite variables
				if (dFlip){
					//Swap rows and columns for a diagonal flip
					dRow = sCol; //Set dRow to dCol
					dCol = sRow; //Set dCol to dRow (or sCol if no h or v flips performed)
					
				}	
				
				if( hFlip){
					//Reverse columns for horizontal flip
					dCol = sWidth - 1 - dCol;
				}
				if(vFlip){
					//Reverse rows for vertical flip
					dRow = sHeight-1-dRow;
				}

				
				dImg[dCol+dRow*dWidth] = sImg[sCol+sRow*sWidth];
			}
		}


		return dImg;
	}
	

	
	
	
	
	
	
	public BufferedImage loadImg(String filename){
		//Load image as a sprite sheet
		BufferedImage myImage =null;
		try{
			String base = new File("").getAbsolutePath();
			String leveldir  = new File(base,"/img").getCanonicalPath();
			File filePath = new File(leveldir,filename);

			
			myImage = ImageIO.read(filePath);
			
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Failure to read TileSet Image"+filename);
		}
		
		return myImage;
	}

	
	
	
	public int[] bufferedImageToInt(BufferedImage drawImg){
		int w = drawImg.getWidth();
		int h = drawImg.getHeight();
		int[] imagePixels = drawImg.getRGB(0, 0, w, h, null, 0, w);
		for (int i = 0; i < imagePixels.length; i++) {
			imagePixels[i] = (imagePixels[i] & 0xff) / 64;
		}
		return imagePixels;
	}
	
	public int[] renderEffectsTest(BufferedImage drawImg){
		int w = drawImg.getWidth();
		int h = drawImg.getHeight();
		
		int[] imagePixels = drawImg.getRGB(0, 0, w, h, null, 0, w);
		for (int i = 0; i < imagePixels.length; i++) {
			
			if (  ( (imagePixels[i] & 0xff000000)   >> 24) !=0 ) {
				imagePixels[i]=imagePixels[i];
			}
			
			//imagePixels[i] = ~((imagePixels[i] &  0xff000000) >> 24);//alpha channel (but it looks blue) transparency is 1 not 0!
			//imagePixels[i] = (imagePixels[i] &  0x00ff0000) >> 16;//red channel(but it looks blue)
			//imagePixels[i] = (imagePixels[i] &  0x0000ff00) >> 8;//green channel (but it looks blank)
			//imagePixels[i] = (imagePixels[i] &  0x000000ff); //blue channel
			
			//Shift blue to green
			//imagePixels[i] = (imagePixels[i] &   0x000000ff) << 8; 
			//Invert colors
			//imagePixels[i] = (0x00ffffff - imagePixels[i]); 
			//Make everything neon looking!
			//imagePixels[i] = (0b11110000111100001111 - imagePixels[i]); 
			//Fire Red!
			//imagePixels[i] = (imagePixels[i]>>8); 
			
			
			
			//Invert colors
			
		}
		return imagePixels;
	}
	
	public int[] renderEffectsTestIntArr(int [] imagePixels){

		for (int i = 0; i < imagePixels.length; i++) {
			
			//imagePixels[i] = (imagePixels[i] &  0xff000000) >> 24;//alpha channel (but it looks blue)
			//imagePixels[i] = (imagePixels[i] &  0x00ff0000) >> 16;//red channel(but it looks blue)
			//imagePixels[i] = (imagePixels[i] &  0x0000ff00) >> 8;//green channel (but it looks blank)
			//imagePixels[i] = (imagePixels[i] &   0x000000ff); //blue channel
			
		}
		return imagePixels;
	}
	
	
	public static void main(String[] args) {
		GraphicsDemo demo = new GraphicsDemo();
		demo.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		demo.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		demo.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		JFrame frame = new JFrame(GraphicsDemo.NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(demo, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		demo.start();
	}
	
	
	
}//Ends classdef

