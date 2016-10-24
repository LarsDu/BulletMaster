package bulletmaster;

import bulletmaster.Tiles.BaseTile;
import bulletmaster.Tiles.ITile;
import bulletmaster.entity.Hero;
import bulletmaster.gfx.Screen;

public class LevelLayer {
	private int width;
	private int height;
	private String encoding;
	private String compression;
	private String data;
	private int layerIndex; //set this at level!
	
	private ITile[][] tiles;

	
	public void init(Camera camera, Hero hero, Screen screen){
		
		
		//Initialize all tiles
		
		for(int i=0 ; i<tiles.length ; i++){
			for(int j=0 ; j<tiles[0].length ; j++){
				if(tiles[i][j] != null){
					tiles[i][j].init(camera, hero, screen);
				}
				//Maybe add layerIndex property to tile?
				
			}
		}
		
	}
	
    public void setTiles(ITile[][] tiles) {
        this.tiles = tiles;
    }	
    
    
    public ITile[][] getTiles() {
		return tiles;
	}

	public void render(Screen screen){
    	//Render each tile
    	//Look up tileSet information for current layer, then render each tile
    	//accordingly
		for(int i=0 ; i<tiles.length ; ++i){
			for(int j=0 ; j<tiles[0].length ; ++j){
				//System.out.println("Rendering tile"+ tiles[i][j]);
				if(tiles[i][j] != null){
					tiles[i][j].render(screen);
				}
			}
		}
   	 	//System.out.println(tiles[0].length);
		//System.out.println(tiles[7][4].getxIndex());
		
    }
    
    
    public void update(){
		for(int i=0 ; i<tiles.length ; ++i){
			for(int j=0 ; j<tiles[0].length ; ++j){
				//System.out.println("Rendering tile"+ tiles[i][j]);
				if(tiles[i][j] != null){
					tiles[i][j].update();
				}
			}
		}
    }
	
	//Getters and setters
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


	
	
}
