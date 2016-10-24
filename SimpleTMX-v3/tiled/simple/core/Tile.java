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

public class Tile implements Serializable{
	private static final long serialVersionUID = -5060395762974647415L;

	private int Tn=-1;
	
	private int n=-1;
	
	public Tile(){
	}


	public int getN() {
		return n;
	}


	public void setN(int n) {
		this.n = n;
	}


	public int getTn() {
		return Tn;
	}


	public void setTn(int Tn) {
		this.Tn = Tn;
	}
}
