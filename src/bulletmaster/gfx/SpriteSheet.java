package bulletmaster.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	public int width, height;
	private int[] pixels;



	public SpriteSheet(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();
		pixels = image.getRGB(0, 0, width, height, null, 0, width);

	}
	
	
	public SpriteSheet(String filename) {

		loadImgToSpriteSheet(filename); // loads Sprite sheet to pixels

	}
	
	
	
	public int[] cutIntArray(int x0, int y0, int cutWidth, int cutHeight){
			
		//Cut out a region of the SpriteSheet 
		//into an int[] array
	
		//Initialize int[] array	
		int subImage[] = new int[cutWidth*cutHeight];
	
		if (pixels != null){
			//Extract specified pixels from SpriteSheet
				
			//x and y indices for sub image
		
					
			for (int y = y0; y < y0 + cutHeight; y++){
				
				for (int x = x0; x < x0 + cutWidth; x++){
		
					subImage[(x-x0)+(y-y0)*cutWidth] = pixels[x+y*this.width];
					
				}
				
			}
		}
		
		//subImage.setRGB(x0, y0, w, h, pixels, 0, w);
		return subImage;
	}
	
	
	public Sprite cutSprite(int x0, int y0, int cutWidth, int cutHeight){
		
		//Make a sprite out of a region of the sprite sheet.
	
		Sprite outputSprite = new Sprite(this, x0,y0,cutWidth,cutHeight);
		
		return outputSprite;
	}
	
	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}


	public int[] getPixels() {
		return pixels;
	}


	public void setPixels(int[] pixels) {
		this.pixels = pixels;
	}

	
	public void loadImgToSpriteSheet(String filename){
		//Load image as a sprite sheet
		try{
			String base = new File("").getCanonicalPath();
			
			File filePath = new File(base+"/img/"+filename);
			System.out.println("Image directory and file are :" +filePath.getCanonicalPath());
			
			BufferedImage sheetImage = ImageIO.read(filePath);
			if(sheetImage.getType()!=BufferedImage.TYPE_INT_ARGB){
				//System.out.println("Wrong image type! " +sheetImage);
				//Hack for non ARGB images
				BufferedImage newImage = new BufferedImage(sheetImage.getWidth(), sheetImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = newImage.createGraphics(); 
				g.drawImage(sheetImage, 0, 0, sheetImage.getWidth(), sheetImage.getHeight(), null);
				g.dispose();
				sheetImage= newImage;
				System.out.println(sheetImage);
			}
			
			
			this.setWidth(sheetImage.getWidth());
			this.setHeight(sheetImage.getHeight());
			this.setPixels(sheetImage.getRGB(0, 0, width, height, null, 0, width));
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Failure to read TileSet Image"+ filename);
		}
	}
	
	
}


//Note on color:
//You can extract color using bitmasks
//int blue = color & 0xff;
//int green = (color & 0xff00) >> 8;
//int red = (color & 0xff0000) >> 16;

//http://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
//http://gamedev.stackexchange.com/questions/75661/java-single-array-best-choice-for-accessing-pixels-for-manipulation


//Using a 1D array for bit manipulation has significant speed advantages!
//http://gamedev.stackexchange.com/questions/75661/java-single-array-best-choice-for-accessing-pixels-for-manipulation
//Access pixels like so:
//for (int y = 0; y < height; y++)
//{
//   for (int x = 0; x < width; x++)
//   {
//        pixels[x + y * width] = value;
//   }
//}


//Fiddling with minicraft source
//http://stackoverflow.com/questions/14952863/importing-a-sprite-from-a-sprite-sheet
