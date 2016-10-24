package bulletmaster.Font;

import bulletmaster.gfx.SpriteSheet;



public class Font {
	private static String chars = ""      + 
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ"  + 
			"0123456789" 				  +
			".,!?'\"-+=/\\%()<>:;         ";
	
	
	private SpriteSheet spriteSheet;
	private int letterWidth = 16;
	private	int letterHeight = 32;


	public Font(SpriteSheet letterSheet){
		this.spriteSheet = letterSheet;
		dimCheck();
	}
	public Font(String spriteLocation){
		this.spriteSheet = new SpriteSheet(spriteLocation);
		dimCheck();
	}
	
	public Font(){
		this.spriteSheet = new SpriteSheet("img/fonts/monoTextBlack.png");		
		dimCheck();
	}

	
	private void dimCheck(){
		//Check if the specified letter dimensions fit the spriteSheet dimensions
		if(   (this.spriteSheet.getHeight()/(this.letterHeight*6))!=0){
			System.out.println("Font sprite sheet dimensions do not match to specied letter height!");
		}else if(   (this.spriteSheet.getWidth()/(this.letterWidth*6))!=0){
			System.out.println("Font sprite sheet dimensions do not match to specied letter width!");
		}
	}
	
	
}
	

//Menu contains x,y,w,h,textbox items,
//MenuTextBox contains xOff,yOff,w,h,font,write( call font render)
//Font contains characters, spritesheet, render


