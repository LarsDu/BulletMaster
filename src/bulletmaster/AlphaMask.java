package bulletmaster;
import java.lang.Math;

import bulletmaster.collisions.CollisionSurf;

public class AlphaMask {


	public static void main(String[] args) {
		int w = 3;
		int h = 3;
		//int [] img = new int[w*h];
		int [] img = {0,1,2,3,4,0xff000000,6,0xff000000,0xff000000};
		printIntArr(img,w,h);

		boolean[]solidMask = colHeightMaskFromAlpha(img,w,h);
		printBoolArr( solidMask,w,h);

		int [] heights = colHeightFromBoolMask(solidMask,w,h);
		printIntArr(heights,w,1);

		int[] heightDirect = colHeightFromAlpha(img,w,h);
		int[] widthDirect = rowWidthFromAlpha(img,w,h);

		printIntArr(heightDirect,w,1);
		printIntArr(rowWidthFromAlpha(img,w,h),w,1);

	}




	public static void printIntArr(int [] arr, int w, int h){
		for(int r=0; r<h ; ++r){
			for (int c=0; c<w; ++c){
				System.out.print(arr[r*w+c]+" ");
				if(c==w-1){
					System.out.print("\n");
				}
			}
		}
	}



	public static void printBoolArr(boolean [] arr, int w, int h){
		for(int r=0; r<h ; ++r){
			for (int c=0; c<w; ++c){
				System.out.print(arr[r*w+c]+" ");
				if(c==w-1){
					System.out.print("\n");
				}
			}
		}
	}




	public static  boolean [] colHeightMaskFromAlpha(int [] pixels, int w, int h){
		//This function takes the non-transparent regions of 
		//an alpha mask from an array of pixels, and converts
		//this region into a boolean mask with true representing solidity
		//and false representing empty space.

		//Mask for all but the first four bits (the alpha mask)
		//of an int array.
		//w and h are width and height of the input image
		final int ALPHA_MASK = 0xff000000; 

		boolean [] boolMask = new boolean[w*h];

		for (int r = 0; r<h; ++r){
			for (int c = 0; c<w ; ++c){
				//Mask the alpha channel, invert the bit value
				//And shift the alpha channel to the right
				//(everything in the right 24 positions should be 0
				int i = c+r*w;
				int curPix = (pixels[i]& ALPHA_MASK)>>24;

			//Ternary operator, look it up!
			boolMask[i] = (curPix != 0) ? true: false;
			//Recall that the boolean type in java is
			//not interconvertible with ints, and the size
			// of this type is 

			}
		}
		return boolMask;

	}

	public static int[] colHeightFromBoolMask(boolean [] mask, int w, int h){


		//This method takes a boolean solidity mask and recovers 
		//height of the top most true pixel in each column


		//Array will be initialized with default values of 0 (not null!)
		int [] heightArr = new int[w];

		for (int r = 0; r<h; ++r){
			for (int c = 0; c<w; ++c){
				if (mask[c+r*w] == true){
					//Set height for column
					if (heightArr[c] == 0 ){
						heightArr[c] = h-r;
					} 
				}
			}
		}
		return heightArr;
	}


	public static  int [] colHeightFromAlpha(int [] pixels, int w, int h){
		//This function takes the non-transparent regions of 
		//an alpha mask from an array of pixels, and finds the coordinate
		//measured from the bottom of the matrix to the first 
		//non-transparent pixel from the top.

		//This can be useful for making sloped or curved surfaces
		//for a Sonic the Hedgehog type platformer game.

		//Mask for all but the first four bits (the alpha mask)
		//of an int array.
		//w and h are width and height of the input image
		final int ALPHA_MASK = 0xff000000; 

		int [] heightArr = new int[w];

		for (int r = 0; r<h; ++r){
			for (int c = 0; c<w ; ++c){
				//Mask the alpha channel, invert the bit value
				//And shift the alpha channel to the right
				//(everything in the right 24 positions should be 0
				int i = c+r*w;
				int curPix = (pixels[i]& ALPHA_MASK)>>24;

			if(curPix != 0){ //If in solid area of image

				//Record height value for corresponding column
				if (heightArr[c] == 0){
					heightArr[c] = h-r;
				} 
			}
			//Ternary operator, look it up!

			//Recall that the boolean type in java is
			//not interconvertible with ints, and the size
			// of this type is 
			}

		}
		return heightArr;
	}




	public static  int [] rowWidthFromAlpha(int [] pixels, int w, int h){
		//This function takes the non-transparent regions of 
		//an alpha mask from an array of pixels, and finds the coordinate
		//measured from the left the matrix to the first 
		//non-transparent pixel from the right. 
		//This is useful for describing a curved wall from the left


		//This can be useful for making sloped or curved surfaces
		//for a Sonic the Hedgehog type platformer game.

		//Mask for all but the first four bits (the alpha mask)
		//of an int array.
		//w and h are width and height of the input image
		final int ALPHA_MASK = 0xff000000; 

		int [] widthArr = new int[h];

		for (int r = 0; r<h; ++r){
			for (int c = 0; c<w ; ++c){
				//Mask the alpha channel, invert the bit value
				//And shift the alpha channel to the right
				//(everything in the right 24 positions should be 0
				int i = c+r*w;
				int curPix = (pixels[i]& ALPHA_MASK)>>24;

			if(curPix != 0){ //If in solid area of image

				//Record height value for corresponding column
				if (widthArr[r] == 0){
					widthArr[r] = w-c;
				} 
			}
			//Ternary operator, look it up!

			//Recall that the boolean type in java is
			//not interconvertible with ints, and the size
			// of this type is 
			}

		}
		return widthArr;
	}










	public void pointRotationClockwise(int centX, int centY, int radius, int deg){
		//Center is A, rotated point is B

		int pX = centX+radius;
		int pY = centY;
		pX = (int) (pX*Math.cos(deg)+ pY*Math.sin(deg));
		pY = (int) (-pX*Math.sin(deg)+pY*Math.cos(deg));

		//return new Vector2(pointX,pointY);

	}




}




