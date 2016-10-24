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
import java.util.Comparator;

public class MapLayer implements Serializable, Comparator<MapLayer>, Comparable<MapLayer>{
	private static final long serialVersionUID = -8788888728011401784L;

	private int width=0;
	private int height=0;
	private boolean active=true;
	private int priority=0;
	
	private Tile[][] tiles;
	
	public MapLayer(){
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

	public Tile getTile(int x,int y){
		return tiles[x][y];
	}
	
	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int compare(MapLayer m1, MapLayer m2) {
		return m1.compareTo(m2);
	}

	public int compareTo(MapLayer m) {
		return (this.priority-m.getPriority());
	}
}
