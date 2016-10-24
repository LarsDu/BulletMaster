package bulletmaster.Tiles;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
//This class is for loading images as tilesets. Each tileset
//is am image that is chopped up to make up maps in the Tiled map editor

import bulletmaster.gfx.Screen;
import bulletmaster.gfx.Sprite;
import bulletmaster.gfx.SpriteSheet;

//Tiled can be downloaded at http://www.mapeditor.org/
//http://gamedevelopment.tutsplus.com/tutorials/parsing-and-rendering-tiled-tmx-format-maps-in-your-own-game-engine--gamedev-3104
public class TileSet {

	private int firstgid;
	private int lastgid;
	private String name;
	private String imgSource;
	private int tileHeight;
	private int tileWidth;
	private int imgWidth;
	private int imgHeight;
	private int spacing;
	private int margin;
	
	//The number of TileTypes that should be in this tileset
	private int tileCount; 
	                       
	
	private SpriteSheet sheet;
	
	//A dictionary that tells us what property the 

	TileType [] tileTypes; 
	
	
	public TileSet(){

	}
	
	
	public TileSet(int firstgid,String tileName, int tileWidth, int tileHeight ){
		this.firstgid = firstgid;
		this.name = tileName;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		spacing = 0;
		margin =0;
	}
	
	public TileSet(int firstgid, String tileName, int tileWidth, int tileHeight, int spacing, int margin  ){
		this.firstgid = firstgid;
		this.name = tileName;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.spacing = spacing;
		this.margin = margin;
	}

	
	
	

	
	public void init(){

		//Initialize values not set from TMX reader
		//System.out.println("Initializing tileSet "+firstgid);
		
		//Create tileTypes not initialized in TMX reader;
	
		//Cut up sprite sheet
		int tilesPerRow = (int) imgWidth/tileWidth;
		int tilesPerCol = (int) imgHeight/tileHeight;
		
		//Tile gid is (first gid for given tileset)+(id)
		
		int currentIndex = 0;
		for(int i=0;i<tilesPerCol;i++){
			
			for (int j= 0; j<tilesPerRow; j++){		
				
		
				
				Sprite tileTypeSprite = sheet.cutSprite(j*tileWidth, i*tileHeight, tileWidth, tileHeight);
				//Iterate through Arraylist of TileType objects
				for(int k=0;k<tileTypes.length; ++k){
					
										
					//Left to right up to down
					
					if(tileTypes[k].getGid()-firstgid==currentIndex){

					tileTypes[k].setSprite(tileTypeSprite);
					tileTypes[k].setWidth(tileWidth);
					tileTypes[k].setHeight(tileHeight);
									
					//System.out.println(currentIndex);
					
					}
					
				}
				currentIndex++;			
			}
		}

		////  Initialize all tileTypes.
		//Their IDs and props are set by TMXReader, but these props need to be translated into
		//Integer values
		for (TileType tType : tileTypes) {
			//System.out.println("Tiletype initialized: "+ tType);
			tType.init();
		}
		
		

		
	}
	
	
	
	
	
	
	
	//Load tile image
	//public void loadImgToSpriteSheet(String filename){
	//	//Load image as a sprite sheet
	//	try{
	//		String base = new File("").getAbsolutePath();
	///		String leveldir  = new File(base,"/levels").getCanonicalPath();
	//		File filePath = new File(leveldir,filename);
	//		System.out.println("Image directory and file are :" +filePath.getCanonicalPath());
	//		
	//		BufferedImage sheetImage = ImageIO.read(filePath);
	//		sheet = new SpriteSheet(sheetImage);
	//	}catch(IOException e){
	//		e.printStackTrace();
	//		System.out.println("Failure to read TileSet Image"+ filename);
	//	}
	//}
	

	
	
	//Getters and setters

	public int getFirstgid() {
		return firstgid;
	}




	public String getName() {
		return name;
	}

	public void setFirstgid(int firstgid) {
		this.firstgid = firstgid;
	}


	public int getLastgid() {
		return lastgid;
	}


	public void setLastgid(int lastgid) {
		this.lastgid = lastgid;
	}


	public String getImgSource() {
		return imgSource;
	}


	public int getTileHeight() {
		return tileHeight;
	}


	public int getTileWidth() {
		return tileWidth;
	}


	public int getImgWidth() {
		return imgWidth;
	}


	public int getImgHeight() {
		return imgHeight;
	}





	public void setName(String name) {
		this.name = name;
	}




	
	
	public int getSpacing() {
		return spacing;
	}







	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}







	public int getMargin() {
		return margin;
	}







	public void setMargin(int margin) {
		this.margin = margin;
	}







	public void setImgSource(String imgSource) {
		this.imgSource = imgSource;
		try{
			//Set the spritesheet sheet for this tileSet
			this.setSheet(new SpriteSheet(this.imgSource));
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Failure initialize tileSet from imgSource");
		}
		
	}







	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}







	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}







	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}







	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}







	public void setTileCount() {

		this.tileCount = (int) (imgHeight/tileHeight)*(imgWidth/tileWidth);
		
	}

	public int getTileCount(){
		return this.tileCount;
	}

	public TileType [] getTileTypes() {
		return tileTypes;
	}


	public void setTileTypes(TileType [] tileTypes) {
		this.tileTypes = tileTypes;
	}



	
	public void setTileType(int index, TileType tileType){
		//Sets a TileType at specified index in tileTypes array
		this.tileTypes[index] = tileType;
	}


	public SpriteSheet getSheet() {
		return sheet;
	}


	public void setSheet(SpriteSheet sheet) {
		this.sheet = sheet;
	}


}
