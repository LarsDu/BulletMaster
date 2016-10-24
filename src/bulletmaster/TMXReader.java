package bulletmaster;
/*
 * Copyright [2012] [Sergey Mukhin]
 *
 * Licensed under the Apache License, Version 2.0 (the License); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an AS IS BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
*/
/*http://code.google.com/p/simpletmx/source/browse/SimpleTMX/
 * 
 * 
 */
/*Modified by Larry Du*/
//https://code.google.com/p/simpletmx/wiki/GWTIntegration

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import bulletmaster.Tiles.BaseTile;
import bulletmaster.Tiles.BlockTile;
import bulletmaster.Tiles.CurvedTile;
import bulletmaster.Tiles.ITile;
import bulletmaster.Tiles.TileSet;
import bulletmaster.Tiles.TileType;



public class TMXReader {
	//global class parameters
	private DefaultHandler currentHandler = new TMXHandler();
	//Temporary map object
	private Level level=null;

	
		
	public Level readLevel(InputStream IS) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		saxParser.parse(IS,new SAXStub());
		return(level);
	}
	
	
		
	
	
	// SAX library don't have SAXParser.setHandler(DefaultHandler) method, thats why we will use this workaround stub to parse hierarchy 
	private class SAXStub extends DefaultHandler{
		public void startElement(String uri, String name,String qName, Attributes attributes) throws SAXException {
			currentHandler.startElement(uri,name,qName,attributes);
		}
		public void endElement(String uri,String name,String qName) throws SAXException {
			currentHandler.endElement(uri,name,qName);
		}
		public void characters(char[] ch, int start, int length) throws SAXException {
			currentHandler.characters(ch,start,length);
		}

	}
	
	
	
	
	private class TMXHandler extends DefaultHandler {

		public void startElement(String uri, String name,String qName, Attributes atts) throws SAXException {
			if (qName.equalsIgnoreCase("map")) {
				level = new Level();
				//TODO: move all this into a constructor
				level.setWidthInTiles(getInteger(atts,"width"));
				level.setHeightInTiles(getInteger(atts,"height"));
				level.setTileWidth(getInteger(atts,"tilewidth"));
				level.setTileHeight(getInteger(atts,"tileheight"));
				level.setWidth(level.getWidthInTiles()*level.getTileWidth());
				level.setHeight(level.getHeightInTiles()*level.getTileHeight());
				currentHandler=new LevelHandler(this,level);
			}
		}
	
	}

	
	
	
	private class LevelHandler extends DefaultHandler {
		private DefaultHandler previousHandler;
		
		//Constructor
		public LevelHandler(DefaultHandler previousHandler, Level level){
			this.previousHandler=previousHandler;
		}
		
		public void startElement(String uri, String name,String qName, Attributes atts) throws SAXException {
			
			if(qName.equalsIgnoreCase("tileset")) {
									TileSet tileSet = new TileSet();
									tileSet.setFirstgid(getInteger(atts,"firstgid"));
									tileSet.setName(getString(atts,"name"));
									tileSet.setTileWidth(getInteger(atts,"tilewidth"));
									tileSet.setTileHeight(getInteger(atts,"tileheight"));
									tileSet.setSpacing(getInteger(atts,"spacing"));
									tileSet.setMargin(getInteger(atts,"margin"));
									level.addTileSet(tileSet);
									currentHandler=new TileSetHandler(this,tileSet);

			} else if(qName.equalsIgnoreCase("objectgroup")) {
									LevelObjectGroup objectGroup = new LevelObjectGroup();
									level.addObjectGroup(getString(atts,"name"), objectGroup);
									currentHandler=new ObjectGroupHandler(this,objectGroup);

			} else if(qName.equalsIgnoreCase("layer")) {
									LevelLayer levelLayer = new LevelLayer();
									levelLayer.setWidth(getInteger(atts,"width"));
									levelLayer.setHeight(getInteger(atts,"height"));
									level.addLayer(getString(atts,"name"), levelLayer);
									currentHandler=new LevelLayerHandler(this,levelLayer);

			} else {
				throw new SAXException("unknown tag:"+qName);
			}
		
		}

		public void endElement(String uri, String name,String qName) throws SAXException {
			if (qName.equalsIgnoreCase("map")) currentHandler=previousHandler;
		}
	}

	
	
	private class LevelLayerHandler extends DefaultHandler {
		private DefaultHandler previousHandler;
		private LevelLayer levelLayer;
		
		public LevelLayerHandler(DefaultHandler previousHandler,LevelLayer levelLayer){
			this.previousHandler=previousHandler;
			this.levelLayer=levelLayer;
		}
		
		
		public void startElement(String uri, String name,String qName, Attributes atts) throws SAXException {
			if(qName.equalsIgnoreCase("data")) {
				currentHandler=new LayerDataHandler(this,levelLayer,getString(atts,"encoding"),getString(atts,"compression"));
			} else {
				throw new SAXException("unknown tag:"+qName);
			}
		}
		
		public void endElement(String uri, String name,String qName) throws SAXException {
			if (qName.equalsIgnoreCase("layer")) currentHandler=previousHandler;
		}
	}
	
	
	
	private class LayerDataHandler extends DefaultHandler {
		private DefaultHandler previousHandler;
		private LevelLayer levelLayer;
		private String encoding;
		private String compression;
		private String data="";
		
		public LayerDataHandler(DefaultHandler previousHandler,LevelLayer levelLayer,String encoding,String compression){
			this.previousHandler=previousHandler;
			this.levelLayer=levelLayer;
			this.encoding=encoding;
			this.compression=compression;

		}
		
		public void characters(char[] ch, int start, int length) throws SAXException {
			data=data+new String(ch,start, length);
			//Add data contents into a single string
		}

		public void endElement(String uri, String name,String qName) throws SAXException {
			//System.out.println("Data is" + data);
			try {

				
				levelLayer.setTiles(data2tilesArray(data,encoding,compression,levelLayer));
			} catch (IOException e) {
				throw new SAXException("unknown tag:"+qName);
			}
			if (qName.equalsIgnoreCase("data")) currentHandler=previousHandler;
		}
	}
	
	
	
	
	
	
	

	
	//Index that keeps track of how many tileSets there are per level
	private int tileSetIndex=0;
	
	private class TileSetHandler extends DefaultHandler {
		private DefaultHandler previousHandler;
		private TileSet tileSet;
		private TileType tileType;
		

		public TileSetHandler(DefaultHandler previousHandler,TileSet tileSet){
			this.previousHandler=previousHandler;
			this.tileSet=tileSet;
		
			
			
		}
		
		public void startElement(String uri, String name,String qName, Attributes atts) throws SAXException {
			if(qName.equalsIgnoreCase("image")) {
								tileSet.setImgSource(getString(atts,"source"));
								//tileSet.setTilesPerRow((getInteger(atts,"width") - 2 * tileSet.getMargin() + tileSet.getSpacing()) / (tileSet.getTileWidth() + tileSet.getSpacing()));
								//This information is passed on to the data2tilesarray parser
								tileSet.setImgWidth(getInteger(atts,"width"));
								tileSet.setImgHeight(getInteger(atts,"height"));
								tileSet.setTileCount(); //calculate tileCount from tileSet dimensions
								//Initialize size of the TileType array							
								tileSet.setTileTypes(new TileType [tileSet.getTileCount()]);
								//tileSetsFirstgids.put(tileSet.getFirstgid(),tileSetIndex);
				            	tileSetIndex++;
				            	
			}else if(qName.equalsIgnoreCase("tile")){
				//Pass tileSet and id to TileHandler
				long longTileTypeId= getLong(atts,"id"); //Need to use long in case of bit flags in the first few bits.
				int tileTypeId = clearRotationFlag(longTileTypeId);
				tileType = new TileType(tileTypeId+tileSet.getFirstgid()); //Initialize tileType and set its global id
				//Set your TileType in the TileTypes Arraylist at the index specified by the tileId
				//The blank spaces between indices will be filled with tilesTypes
				//that are not specifically declared in the tmx file
				//(See endElement below)
				tileSet.setTileType(tileTypeId,tileType);
				currentHandler = new TileTypeHandler(this,tileType);
				
				//Note: The <tile> tag is only used under tileset, when a given tile has custom
				//properties specified.
				//If the map maker does not specify a special property, this tag does not show up,
				//and a TileType will not be initialized.
				//In TileSet.init(), TileTypes that are not initialized here, will get initialized,
			
				
						
				
  	  	    } else {
				throw new SAXException("unknown tag:"+qName);
			}
		
		}

		public void endElement(String uri, String name,String qName) throws SAXException {
			//Create tileTypes that did not have an entry in the tmx file
			
			//First check if tileTypes size == to number of positions in the tileSet
			int numTileTypes = tileSet.getTileTypes().length;
			if (numTileTypes != tileSet.getTileCount()){
				throw new Error("Mismatch between number of tileTypes allocated and calculated tileCount ");
			}
			for (int i=0;i<numTileTypes; ++i){
				//
				if (tileSet.getTileTypes()[i] == null){
					//Create new tiles
					
					tileSet.setTileType(i, new TileType(i+tileSet.getFirstgid()));
				}
			}
			
			tileSet.init();
			if (qName.equalsIgnoreCase("tileset")){ 
					currentHandler=previousHandler;
				}
		}
		
		
		
		
	}

	
	
	
	private class TileTypeHandler extends DefaultHandler {
		private DefaultHandler previousHandler;
		//private HashMap<String,String> properties;
		private TileType myTileType;
		private HashMap<String,String> properties;
		
		public TileTypeHandler(DefaultHandler previousHandler,TileType tileType){
			this.previousHandler=previousHandler;
			this.myTileType = tileType;
		}
		
		public void startElement(String uri, String name,String qName, Attributes atts) throws SAXException {
			if(qName.equalsIgnoreCase("properties")) {
				properties = new HashMap<String,String>();
				myTileType.add(properties);
				currentHandler = new PropertiesHandler(this,properties);
				
  	  	    } else {
				throw new SAXException("unknown tag:"+qName);
			}
		
		}

		public void endElement(String uri, String name,String qName) throws SAXException {
			if (qName.equalsIgnoreCase("tile")) currentHandler=previousHandler;
		}
		
	}
	
	


//private ArrayList<Vertex> getPoints(String pointsString){
//		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
//		
//		String pointsArrayString[]=pointsString.split("\\ ");
//		for(int i =0; i < pointsArrayString.length ; i++){
//			String pointCoordsArrayString[]=pointsArrayString[i].split("\\,");
//			double x=Double.valueOf(pointCoordsArrayString[0]);
//			double y=Double.valueOf(pointCoordsArrayString[1]);
//			vertices.add(new Vertex(x,y));
//		}
//		return vertices;
//	}

	
	
	
	private class PropertiesHandler extends DefaultHandler {
		private DefaultHandler previousHandler;
		private HashMap<String,String> properties;

		public PropertiesHandler(DefaultHandler previousHandler,HashMap<String,String> properties){
			this.previousHandler=previousHandler;
			this.properties=properties;
		}

		public void startElement(String uri, String name,String qName, Attributes atts) throws SAXException {
			if(qName.equalsIgnoreCase("property")) {
				//System.out.println("Properties:"+  this.properties);
				properties.put(getString(atts,"name"), getString(atts,"value"));
			} else {
				throw new SAXException("unknown tag:"+qName);
			}
		}
		
		public void endElement(String uri, String name,String qName) throws SAXException {
			if (qName.equalsIgnoreCase("properties")) currentHandler=previousHandler;
		}
	}
	
	

	
	
	private class LevelObjectHandler extends DefaultHandler {
		private DefaultHandler previousHandler;
		private LevelObject levelObject;

		public LevelObjectHandler(DefaultHandler previousHandler, LevelObject levelObject){
			this.previousHandler=previousHandler;
			this.levelObject=levelObject;
		}

		public void startElement(String uri, String name,String qName, Attributes atts) throws SAXException {
//			if(qName.equalsIgnoreCase("polygon")) {
//									Polygon polygon = new Polygon(getPoints(getString(atts,"points")));
//									mapObject.add(polygon);
//			} else if(qName.equalsIgnoreCase("polyline")){
//									Polyline polyline = new Polyline(getPoints(getString(atts,"points")));
//									mapObject.add(polyline);
//		}else
		 if(qName.equalsIgnoreCase("properties")){
									HashMap<String,String> properties = new HashMap<String,String>();
									levelObject.setProperties(properties);
									currentHandler=new PropertiesHandler(this,properties);
			} else {
				throw new SAXException("unknown tag:"+qName);
			}
		}
		
		public void endElement(String uri, String name,String qName) throws SAXException {
			if (qName.equalsIgnoreCase("object")) currentHandler=previousHandler;
		}
	}

	
	
	
	
	
	private class AnimationHandler extends DefaultHandler {
		private DefaultHandler previousHandler;
		private Animation animation;
		private HashMap<String,String> properties;
		
		public AnimationHandler(DefaultHandler previousHandler,Animation animation){
			this.previousHandler=previousHandler;
			this.animation=animation;
		}
		
		public void startElement(String uri, String name,String qName, Attributes atts) throws SAXException {
			if (qName.equalsIgnoreCase("properties")){
							properties = new HashMap<String,String>();
							currentHandler=new PropertiesHandler(this,properties);
//			} else if(qName.equalsIgnoreCase("polygon")){
//							Polygon polygon = new Polygon(getPoints(getString(atts,"points")));
//							animation.setCenters(polygon.getVertices());
			} else {
				throw new SAXException("unknown tag:"+qName);
			}
		}
		
		public void endElement(String uri, String name,String qName) throws SAXException {
			if (qName.equalsIgnoreCase("object")) currentHandler=previousHandler;
		}
	}
	
	private class ObjectGroupHandler extends DefaultHandler {
		private DefaultHandler previousHandler;
		private LevelObjectGroup objectGroup;
		private HashMap<String,Animation> animations = new HashMap<String,Animation>();
		private ArrayList<LevelObject> objects = new ArrayList<LevelObject>();
		
		public ObjectGroupHandler(DefaultHandler previousHandler,LevelObjectGroup objectGroup){
			this.previousHandler=previousHandler;
			this.objectGroup=objectGroup;
		}

		public void startElement(String uri, String name,String qName, Attributes atts) throws SAXException {
			if ((qName.equalsIgnoreCase("object"))&&(getString(atts,"type").equalsIgnoreCase("animation"))){
								//	Animation animation=new Animation(getInteger(atts,"x"),getInteger(atts,"y"));
								//	animations.put(getString(atts,"name"), animation);
								//	currentHandler=new AnimationHandler(this,animation);
				
			} else if(qName.equalsIgnoreCase("object")) {
									LevelObject levelObject=new LevelObject();
									//The id is unique for every object within the object layer
									//and possibly every object (I need to check this out).
									//This is a useful value for keeping track of objects that die.
									levelObject.setId(getInteger(atts,"id"));
									//The gid tells us which tileType the object corresponds to
									
									long objGid = getLong(atts,"gid");
									//Retrieve rotation flags
									boolean [] rotationFlags = isRotated(objGid);
									levelObject.setRotationFlags(rotationFlags);
									//Clear object rotation flag
									objGid = clearRotationFlag(objGid);
									//System.out.println(objGid);
									levelObject.setGid((int) objGid);
									levelObject.setSpawnX((int) getFloat(atts,"x"));
									levelObject.setSpawnY((int) getFloat(atts,"y"));
									
									
									
									objects.add(levelObject);
									currentHandler=new LevelObjectHandler(this,levelObject);
			} else {
				throw new SAXException("unknown tag:"+qName);
			}
		
		}

		public void endElement(String uri, String name,String qName) throws SAXException {
			if (qName.equalsIgnoreCase("objectgroup")) currentHandler=previousHandler;

			//build all animation sprite sets
			//Iterator<Entry<String, Animation>> iA = animations.entrySet().iterator();
			//while (iA.hasNext()) {
			//	Entry<String, Animation> e = iA.next();
			//	Animation animation = e.getValue();
			//	for(LevelObject obj: objects){
			//		//animation.correlate(obj);
			//	}

			//}


			//objectGroup.setAnimations(animations);
			objectGroup.setObjects(objects);
		}

	}
	
	private int getInteger(Attributes atts,String name){
		String value=atts.getValue(name);
		if(value!=null){
			return(Integer.valueOf(value));
		}
		return 0;
	}
	
	private long getLong(Attributes atts,String name){
		String value=atts.getValue(name);
		if(value!=null){
			return Long.valueOf(value);
		}
		return 0;
	}
	
	
	
	private float getFloat(Attributes atts,String name){
		String value=atts.getValue(name);
		if(value!=null){
			return Float.valueOf(value);
		}
		return 0;
	}
	
/*
	private int getInteger(String value){
		if(value!=null){
			return(Integer.valueOf(value));
		}
		return 0;
	}
*/
	
	private String getString(Attributes atts,String name) {
		String value=atts.getValue(name);
		if(value==null){
			return ("");
		}
		return value;
	}
	
	
	
	
	
	final int FLIPPED_HORIZONTALLY_FLAG = 0x80000000;
	final int FLIPPED_VERTICALLY_FLAG   = 0x40000000;
	final int FLIPPED_DIAGONALLY_FLAG   = 0x20000000;
	
	private boolean[] isRotated(int tileGid){
		//Returns an bool array of three values:

		//horizontal flip state (true for yes, false for no)
		//vertical flip state
		//diagonal flip state

		boolean hFlip = ((tileGid & FLIPPED_HORIZONTALLY_FLAG)!=0) ? true:false;
		boolean vFlip = ((tileGid & FLIPPED_VERTICALLY_FLAG)!=0) ? true:false;
		boolean dFlip = ((tileGid & FLIPPED_DIAGONALLY_FLAG)!=0) ? true:false;

		boolean [] returnArr= { hFlip,vFlip,dFlip};
		return returnArr;
		
	}
	
	
	private boolean[] isRotated(long tileGid){
		//Returns an bool array of three values:

		//horizontal flip state (true for yes, false for no)
		//vertical flip state
		//diagonal flip state

		boolean hFlip = ((tileGid & FLIPPED_HORIZONTALLY_FLAG)!=0) ? true:false;
		boolean vFlip = ((tileGid & FLIPPED_VERTICALLY_FLAG)!=0) ? true:false;
		boolean dFlip = ((tileGid & FLIPPED_DIAGONALLY_FLAG)!=0) ? true:false;

		boolean [] returnArr= { hFlip,vFlip,dFlip};
		return returnArr;
		
	}
	
	private int clearRotationFlag(int tileGid)	{
	//Return value cleared of rotation flag.
		tileGid &= ~(FLIPPED_HORIZONTALLY_FLAG | FLIPPED_VERTICALLY_FLAG | FLIPPED_DIAGONALLY_FLAG);
		return tileGid;
	}
	
	private int clearRotationFlag(long tileGid)	{
	//Return value cleared of rotation flag.
		tileGid &= ~(FLIPPED_HORIZONTALLY_FLAG | FLIPPED_VERTICALLY_FLAG | FLIPPED_DIAGONALLY_FLAG);
		return (int)tileGid;
	}
	
	
	
		
	private ITile[][] data2tilesArray(String data,String encoding,String compression,LevelLayer levelLayer) throws IOException {


		ITile[][] tiles=new ITile[levelLayer.getWidth()][levelLayer.getHeight()];
		if (encoding != null && "base64".equalsIgnoreCase(encoding)) {
			if (data != null) {
				char[] enc = data.trim().toCharArray();
				byte[] dec = Base64.decode(enc);
				ByteArrayInputStream bais = new ByteArrayInputStream(dec);
				InputStream is;

				if ("gzip".equalsIgnoreCase(compression)) {
					is = new GZIPInputStream(bais);
				} else if ("zlib".equalsIgnoreCase(compression)) {
					is = new InflaterInputStream(bais);
				} else {
					is = bais;
				}

				for (int y = 0; y < levelLayer.getHeight(); y++) {
					for (int x = 0; x < levelLayer.getWidth(); x++) {
						int tileGid = 0;


						//Decompose the read byte into 4 parts
						tileGid |= is.read();   //titleId = titleID bitwise OR is.read()
						//System.out.println("Input 1:" + tileGid);
						tileGid |= is.read() <<  8; //The first (rightmost) 8 bits
						tileGid |= is.read() << 16;
						tileGid |= is.read() << 24; //The leftmost 8 bits?

						boolean [] rotationFlags = isRotated(tileGid);
						boolean hFlip = rotationFlags[0];
						boolean vFlip = rotationFlags[1];
						boolean dFlip = rotationFlags[2];
								
						//Clear the rotation flags (set left most 3 bits to 0);
						tileGid &= ~(FLIPPED_HORIZONTALLY_FLAG | FLIPPED_VERTICALLY_FLAG | FLIPPED_DIAGONALLY_FLAG);
				
						
						ITile tile = null;
			
						
						for( TileType currTileType: TileType.getAllTileTypes()){
							currTileType.checkPropertiesForType(); //HACK initialize TileTypeValues prematurely!
							//System.out.println("Current tileTypeId"+ currTileType.getGid());
							if(currTileType.getGid()==tileGid){
								
									
								if(currTileType.getTypeId() == TileType.BLOCK||currTileType.getTypeId() == TileType.SOLID){  
									//System.out.println("BlockTile made!");
									tile=new BlockTile(x,y,tileGid, currTileType,hFlip,vFlip,dFlip);
								}else if(currTileType.getTypeId() == TileType.CURVED){
									//System.out.println("CurvedTile made!");
									tile = new CurvedTile(x,y,tileGid,currTileType,hFlip,vFlip,dFlip);
								}else {
									tile = new BaseTile(x,y,tileGid, currTileType,hFlip,vFlip,dFlip);
									//System.out.println("BaseTile made!");
								}
								
								
							}
						
							
						} 
						
					

					

						//Set tile rotation state
						if (tile != null){
							tile.setRotationFlags(rotationFlags);
							//Put tile in appropriate position in the tiles array
							if (tileGid >0){
								tiles[x][y]=tile;
							}
						}


					}//interating through all tiles
				}
			}
		}else if(encoding == null){
			//Assume data is encoded as a vanilla XML file.


		}
		return(tiles);

	}
	
	
	
	
}
	

