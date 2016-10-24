/*
 * Copyright [2012] [Sergey Mukhin]
 *
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
*/

package tiled.simple.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class Map implements Serializable{

	private static final long serialVersionUID = -4418881735109587622L;

	private int width=0;
	private int height=0;
	private int tileWidth=0;
	private int tileHeight=0;
	
	private HashMap<Integer,TileSet> tileSets = new HashMap<Integer,TileSet>();
	private HashMap<String,ObjectGroup> objectGroups = new HashMap<String,ObjectGroup>();
	private HashMap<String,MapLayer> mapLayers = new HashMap<String,MapLayer>();

	public Map(){
	}
	
	public MapLayer getLayer(String name){
		return mapLayers.get(name);
	}

	// render all map using TMXRenderer interface
	public void render(Renderer ts){
		int w=this.width*this.tileWidth-1;
		int h=this.height*this.tileHeight-1;
		
		render(ts,0,0,w,h,0,0);
		/*
		ArrayList<MapLayer> c = new ArrayList<MapLayer>(mapLayers.values());
		Collections.sort(c);
		
		Iterator<MapLayer> itr = c.iterator();
		while (itr.hasNext()) {
	        MapLayer mapLayer = (MapLayer) itr.next();
	        
	        if(mapLayer.isActive()){
		        for(int y=0;y<mapLayer.getHeight();y++){
			        for(int x=0;x<mapLayer.getWidth();x++){

			        	Tile tile=mapLayer.getTile(x,y);

			        	TileSet tileSet=tileSets.get(tile.getTn());
			        	if((tile.getN()!=-1)&&(tile.getTn()!=-1)){
			        		int sx=(tile.getN()%tileSet.getTilesPerRow())*(tileSet.getTileWidth()+tileSet.getSpacing())+tileSet.getMargin();
			        		int sy=(tile.getN()/tileSet.getTilesPerRow())*(tileSet.getTileHeight()+tileSet.getSpacing())+tileSet.getMargin();
				        	ts.putImage(tileSet.getSource(), sx, sy, tileSet.getTileWidth(), tileSet.getTileHeight(), x*getTileWidth(), y*getTileHeight(), getTileWidth(), getTileHeight());
			        	}
			        }
		        }
	        }
	    }*/
	}
	
	// render a part of map (x,y,width,height) using TMXRenderer interface
	public void render(Renderer ts,int x,int y,int width,int height,int deltaX,int deltaY){
	
		ArrayList<MapLayer> c = new ArrayList<MapLayer>(mapLayers.values());
		Collections.sort(c);
		
		Iterator<MapLayer> itr = c.iterator();
		while (itr.hasNext()) {
	        MapLayer mapLayer = (MapLayer) itr.next();
	
	        if(mapLayer.isActive()){
		        int startX=(int) Math.floor((double)x/(double)tileWidth);
		        int startY=(int) Math.floor((double)y/(double)tileHeight);
		        int endX=(int) Math.floor((double)(x+width)/(double)tileWidth);
		        int endY=(int) Math.floor((double)(y+height)/(double)tileHeight);
		        
		        for(int Y=startY;Y<=endY;Y++){
			        for(int X=startX;X<=endX;X++){
			        	
			        	if((X>=0)&&(Y>=0)&&(X<width)&&(Y<height)){
				        	Tile tile=mapLayer.getTile(X,Y);
				        	TileSet tileSet=tileSets.get(tile.getTn());
				        	
				        	if((tile.getN()!=-1)&&(tile.getTn()!=-1)){
				        		
				        		int sw1=tileSet.getTileWidth()+tileSet.getSpacing();
				        		int dw1=getTileWidth();

				        		int sh1=tileSet.getTileHeight()+tileSet.getSpacing();
				        		int dh1=getTileHeight();
				        		
				        		// oh my brain boils! it is really crazy! 
		        		
				        		int sx1=(tile.getN()%tileSet.getTilesPerRow())*(tileSet.getTileWidth()+tileSet.getSpacing());
				        		
				        		int dx1=X*getTileWidth();
				        		int dx2=x;
				        		int dx=Math.max(dx1,dx2);
				        		
				        		int sx=sx1+Math.max(dx-dx1,0);
				        		
				        		int sy1=(tile.getN()/tileSet.getTilesPerRow())*(tileSet.getTileHeight()+tileSet.getSpacing());
				        		
				        		int dy1=Y*getTileHeight();
				        		int dy2=y;
				        		int dy=Math.max(dy1,dy2);

				        		// I don't realize how all this shit works, but it works... just hope there has no bugs....
				        		
				        		int sy=sy1+Math.max(dy-dy1,0);

				        		int sw=sw1-Math.max((dx1+dw1)-(x+width) ,0)-Math.max(dx-dx1,0);
				        		int dw=dw1-Math.max((dx1+dw1)-(x+width) ,0)-Math.max(dx-dx1,0);
				        		
				        		int sh=sh1-Math.max((dy1+dh1)-(y+height),0)-Math.max(dy-dy1,0);
				        		int dh=dh1-Math.max((dy1+dh1)-(y+height),0)-Math.max(dy-dy1,0);
				        		
				        		sx=sx+tileSet.getMargin();
				        		sy=sy+tileSet.getMargin();

				        		dx-=x;
				        		dy-=y;
								if((sw!=0)&&(sh!=0)&&(dw!=0)&&(dh!=0)){
					        		ts.putImage(tileSet.getSource(), sx, sy, sw, sh, dx-deltaX, dy-deltaY, dw, dh);
								}
				        	}
			        	}
			        }
		        }
	        }
		}
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

	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	public void add(String name,MapLayer mapLayer){
		mapLayers.put(name, mapLayer);
	}

	public void add(TileSet tileSet){
		tileSets.put(tileSets.size(), tileSet);
	}

	public void add(String name,ObjectGroup objectGroup){
		objectGroups.put(name, objectGroup);
	}

	public ObjectGroup getObjectGroup(String name){
		return objectGroups.get(name);
	}

}
