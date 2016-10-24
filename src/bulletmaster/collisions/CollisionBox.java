package bulletmaster.collisions;

import java.awt.Rectangle;

import bulletmaster.Point;
import bulletmaster.Tiles.BaseTile;
import bulletmaster.gfx.Sprite;

public class CollisionBox {
	
	private int x,y;
	private int width, height;

	
	//CollisionBox offset from upper left corner of object that has the collision box
	private int xoff = 0;
	private int yoff = 0;
	
	
	//Multi-point CollisionBox methods
	

	//The main bounding box
	protected Rectangle mainBox = new Rectangle(0,0,0,0);
	
	//Sensor rects for handling collisions
	protected Rectangle ulSensor = new Rectangle(0,0,0,0);
	protected Rectangle urSensor = new Rectangle(0,0,0,0);
	protected Rectangle llSensor = new Rectangle(0,0,0,0);
	protected Rectangle lrSensor = new Rectangle(0,0,0,0);	
	protected Rectangle topSensor = new Rectangle(0,0,0,0);
	
	//A single point at the bottom of the actors bounding box that determines 
	// actor's y position on sloped ground.
	protected Point groundPoint = new Point(0,0);
	protected Point topPoint = new Point(0,0);
	
	
	//Sensor offsets relative to bounding box x,y;

	protected int ulSensorXOff=0;
	protected int ulSensorYOff=0;
	protected int urSensorXOff=0;
	protected int urSensorYOff=0;
	protected int llSensorXOff=0;
	protected int llSensorYOff=0;
	protected int lrSensorXOff=0;
	protected int lrSensorYOff=0;
	protected int topSensorXOff=0;
	protected int topSensorYOff=0;
	protected int groundPointXOff=0;
	protected int groundPointYOff=0;
	protected int topPointXOff =0;
	protected int topPointYOff=0;
	
	protected boolean leftCollision = false;
	protected boolean rightCollision = false;
	protected boolean bottomCollision = false;
	protected boolean topCollision = false;
	
	
	
	public CollisionBox(int xOff, int yOff, int w, int h, int midLineXoff, int neckLineYoff, int footLineYoff){
		//midLine, neckLine, and footLine are all relative to mainBox x,y coordindate.
		//midLine value relative to mainBox x, neckLine and footline relative to mainBox y.
		this.xoff = xOff;
		this.yoff = yOff;
		this.mainBox.setBounds(xOff, yOff, w, h);
		
		this.setSensorsFromLines(midLineXoff,neckLineYoff,footLineYoff);
		
		this.width = w;
		this.height = h;
		
		
	}
	
	public CollisionBox(Sprite sprite,int midLineXoff, int neckLineYoff, int footLineYoff){
		//Constructor with parameters automatically set by sprite (except for sensor line locations)
		
		this.width = sprite.getWidth();
		this.height = sprite.getHeight();
		this.mainBox = sprite.rectFromAlpha();
		this.xoff = (int) mainBox.getX();
		this.yoff = (int) mainBox.getY();
		this.setSensorsFromLines(midLineXoff, neckLineYoff, footLineYoff);
	}
	
	public CollisionBox(Sprite sprite){
		//Constructor with parameters automatically set by sprite 
		
		this.mainBox = sprite.rectFromAlpha();
		//this.mainBox = this.rectFromAlpha(sprite);
		this.xoff = (int) mainBox.getX();
		this.yoff = (int) mainBox.getY();
		
		//Set sensors from lines based on proportions of main box.
		int midLineXoff =( (int) mainBox.getWidth()-16);
		int neckLineYoff = (int) mainBox.getHeight()/3;
		int footLineYoff = (int) (mainBox.getHeight() *.7);
		
		
		this.setSensorsFromLines(midLineXoff, neckLineYoff, footLineYoff);
	}
	
	
	
	
	public void setSensorsFromLines(int midLineXoff, int neckLineYoff, int footLineYoff){
		if (mainBox.isEmpty()){ return;}
		
		int feetEdgeOffset = 4;
		
		topSensorXOff = midLineXoff;
		topSensorYOff = 0;
		ulSensorXOff = 0;
		ulSensorYOff = neckLineYoff;
		urSensorXOff = midLineXoff;
		urSensorYOff = neckLineYoff;
		llSensorXOff = feetEdgeOffset;
		llSensorYOff = footLineYoff;
		lrSensorXOff = midLineXoff;
		lrSensorYOff = footLineYoff;
		groundPointXOff = (int) this.mainBox.getWidth()/2;
		groundPointYOff = (int) this.mainBox.getHeight();
		topPointXOff = (int) this.mainBox.getWidth()/2;
		topPointYOff = 0;
		int midSectionHeight = footLineYoff - neckLineYoff;
		int feetHeight = (int) mainBox.getHeight()-footLineYoff;
		int leftWidth = midLineXoff;
		int rightWidth = (int) mainBox.getWidth()- midLineXoff;
		
		topSensor.setBounds(this.x+topSensorXOff, this.y+topSensorYOff,2,neckLineYoff);
		ulSensor.setBounds(this.x+ulSensorXOff,this.y+ulSensorYOff,leftWidth,midSectionHeight);
		urSensor.setBounds(this.x+urSensorXOff, this.y+urSensorYOff,rightWidth,midSectionHeight);
		llSensor.setBounds(this.x+llSensorXOff, this.y+llSensorYOff,leftWidth,feetHeight);
		lrSensor.setBounds(this.x+lrSensorXOff,this.y+lrSensorYOff,rightWidth-feetEdgeOffset,feetHeight);
			
		groundPoint.setX(this.x+groundPointXOff);
		groundPoint.setY(this.y+groundPointYOff);
		topPoint.setX(this.x+topPointXOff);
		topPoint.setY(this.y+topPointYOff);
	}
	
	
	
	public int getX(){
		return this.x;
		//return (int) mainBox.getX();
	}
	public int getY(){
		return this.y;
		//return (int) mainBox.getY();
	}
	public int getHeight(){
		return this.height;
		//return (int) mainBox.getHeight();
	}
	public int getWidth(){
		return this.width;
		//return (int) mainBox.getWidth();
	}
	
	public int getMaxX(){
		//return this.x+this.width;
		return (int) mainBox.getMaxX();
	}
	
	public int getMaxY(){
		//return this.y + this.height;
		return (int) mainBox.getMaxY();
	}
	
	public int getXoff() {
		return xoff;
	}
	public void setXoff(int xoffset) {
		this.xoff = xoffset;
	}
	public int getYoff() {
		return yoff;
	}
	public void setYoff(int yoffset) {
		this.yoff = yoffset;
	}
	
	
	public Point getGroundPoint() {
		return groundPoint;
	}

	public void setGroundPoint(Point groundPoint) {
		this.groundPoint = groundPoint;
	}


	
	
	
	//Reposition by collisionBox coordinates (as opposed to sprite coordinates)
		
	
	public void setX(int x){
		this.x = x;
		this.mainBox.setLocation(this.x , (int)this.mainBox.getY());
		this.ulSensor.setLocation(x+ulSensorXOff, (int) ulSensor.getY());
		this.urSensor.setLocation(x+urSensorXOff, (int) urSensor.getY());
		this.llSensor.setLocation(x+llSensorXOff, (int) llSensor.getY());
		this.lrSensor.setLocation(x+lrSensorXOff, (int) lrSensor.getY());
		this.topSensor.setLocation(x+topSensorXOff, (int) topSensor.getY());
		this.groundPoint.setLocation(x+groundPointXOff, groundPoint.getY());
		this.topPoint.setLocation(x+topPointXOff, topPoint.getY());
	}
	
	public void setY(int y){
		this.y = y;
		this.mainBox.setLocation((int) this.mainBox.getX(),this.y);
		this.ulSensor.setLocation((int) ulSensor.getX(),y+ulSensorYOff);
		this.urSensor.setLocation((int) urSensor.getX(),y+urSensorYOff);
		this.llSensor.setLocation((int) llSensor.getX(),y+llSensorYOff);
		this.lrSensor.setLocation((int) lrSensor.getX(),y+lrSensorYOff);
		this.topSensor.setLocation((int) topSensor.getX(),y+topSensorYOff);
		this.groundPoint.setLocation(groundPoint.getX(),y+groundPointYOff);
		this.topPoint.setLocation(topPoint.getX(),y+topPointYOff);
	}
	
	public void setLocation(int x, int y){
		this.x = x;
		this.y=y;
		this.mainBox.setLocation(this.x, this.y);
		this.ulSensor.setLocation(x+ulSensorXOff,y+ulSensorYOff);
		this.urSensor.setLocation(x+urSensorXOff,y+urSensorYOff);
		this.llSensor.setLocation(x+llSensorXOff,y+llSensorYOff);
		this.lrSensor.setLocation(x+lrSensorXOff,y+lrSensorYOff);
		this.topSensor.setLocation(x+topSensorXOff,y+topSensorYOff);
		this.groundPoint.setLocation(x+groundPointXOff,y+groundPointYOff);
		this.topPoint.setLocation(x+topPointXOff, y+topPointYOff);
	}
	
	

	//Mirror the collisionBox along the X axis with offSet options.
	
	

	

	
	public int getTileIndX(){
		//Retrieve the x index of the tile that this object's x coordinate overlaps with
		int xInd = this.x/BaseTile.STDWIDTH;
		if(xInd<0){xInd = 0;}
		return xInd;
		
	}
	
	public int getTileIndY(){
		//Retrieves the y index of the tile that this object's x coordinate overlaps with.
		//System.out.println((this.y/BaseTile.STDWIDTH));
		
		int yInd = this.y/BaseTile.STDHEIGHT;
		if(yInd<0){yInd = 0;}
		return yInd;
		
	}



	//Get and set functions
	
	public int[] getLocalTilesX(){
		int padding = 1;
		//Returns upper and lower x indices of tiles near this collisionbox
		//This is used to help decide which  nearby tiles should be checked for collision
		//adding padding adds more surrounding tiles to check
		int [] tileRange = new int[2];
		tileRange[0] = this.getTileIndX()-padding;
		if (tileRange[0]<0){
			tileRange[0] = 0;
		}
		tileRange[1] = this.getTileIndX()+( this.width/BaseTile.STDWIDTH ) +padding;
		if (tileRange[1]<0){
			tileRange[1] = 0;
		}
		
		return tileRange;
	}
	
	public int[] getLocalTilesY(){
		int padding = 1;
		//Returns upper and lower y indices of tiles near this entity
		//This is used to help entities decide which tiles they should check collision for
		//adding padding adds more surrounding tiles to check
		int [] tileRange = new int[2];
		tileRange[0] = this.getTileIndY()-padding;
		if (tileRange[0]<0){
			tileRange[0] = 0;
		}
		tileRange[1] = this.getTileIndY()+( this.height/BaseTile.STDHEIGHT ) +padding;
		if (tileRange[1]<0){
			tileRange[1] = 0;
		}
		return tileRange;
	}


	//Autogen get/set for different sensors
	
	public void setWidth(int width) {
		//Only changes corresponding dimension for the main box.
		this.width = width;
		this.mainBox.setSize(width, (int)this.mainBox.getHeight());
		
	}
	public void setHeight(int height) {
		//Only changes corresponding dimension for the main box.
		this.height = height;
		this.mainBox.setSize((int) this.mainBox.getWidth(),height);
		
	}
	
	public Rectangle getMainBox(){
		return this.mainBox;
	}

	public Rectangle getUlSensor() {
		return ulSensor;
	}
	public void setUlSensor(Rectangle ulSensor) {
		this.ulSensor = ulSensor;
	}
	public Rectangle getUrSensor() {
		return urSensor;
	}
	public void setUrSensor(Rectangle urSensor) {
		this.urSensor = urSensor;
	}
	public Rectangle getLlSensor() {
		return llSensor;
	}
	public void setLlSensor(Rectangle llSensor) {
		this.llSensor = llSensor;
	}
	public Rectangle getLrSensor() {
		return lrSensor;
	}
	public void setLrSensor(Rectangle lrSensor) {
		this.lrSensor = lrSensor;
	}
	public Rectangle getTopSensor() {
		return topSensor;
	}
	public void setTopSensor(Rectangle topSensor) {
		this.topSensor = topSensor;
	}

	public boolean isLeftCollision() {
		return leftCollision;
	}

	public void setLeftCollision(boolean leftCollision) {
		this.leftCollision = leftCollision;
	}

	public boolean isRightCollision() {
		return rightCollision;
	}

	public void setRightCollision(boolean rightCollision) {
		this.rightCollision = rightCollision;

	}

	public boolean isBottomCollision() {
		return bottomCollision;
	}

	public void setBottomCollision(boolean bottomCollision) {
		this.bottomCollision = bottomCollision;
	}

	public boolean isTopCollision() {
		return topCollision;
	}

	public void setTopCollision(boolean topCollision) {
		this.topCollision = topCollision;
	}
	
	public void resetCollisions(){
		this.setBottomCollision(false);
		this.setTopCollision(false);
		this.setRightCollision(false);
		this.setLeftCollision(false);
	}

	public int getUlSensorXOff() {
		return ulSensorXOff;
	}

	public void setUlSensorXOff(int ulSensorXOff) {
		this.ulSensorXOff = ulSensorXOff;
	}

	public int getUlSensorYOff() {
		return ulSensorYOff;
	}

	public void setUlSensorYOff(int ulSensorYOff) {
		this.ulSensorYOff = ulSensorYOff;
	}

	public int getUrSensorXOff() {
		return urSensorXOff;
	}

	public void setUrSensorXOff(int urSensorXOff) {
		this.urSensorXOff = urSensorXOff;
	}

	public int getUrSensorYOff() {
		return urSensorYOff;
	}

	public void setUrSensorYOff(int urSensorYOff) {
		this.urSensorYOff = urSensorYOff;
	}

	public int getLlSensorXOff() {
		return llSensorXOff;
	}

	public void setLlSensorXOff(int llSensorXOff) {
		this.llSensorXOff = llSensorXOff;
	}

	public int getLlSensorYOff() {
		return llSensorYOff;
	}

	public void setLlSensorYOff(int llSensorYOff) {
		this.llSensorYOff = llSensorYOff;
	}

	public int getLrSensorXOff() {
		return lrSensorXOff;
	}

	public void setLrSensorXOff(int lrSensorXOff) {
		this.lrSensorXOff = lrSensorXOff;
	}

	public int getLrSensorYOff() {
		return lrSensorYOff;
	}

	public void setLrSensorYOff(int lrSensorYOff) {
		this.lrSensorYOff = lrSensorYOff;
	}

	public int getTopSensorXOff() {
		return topSensorXOff;
	}

	public void setTopSensorXOff(int topSensorXOff) {
		this.topSensorXOff = topSensorXOff;
	}

	public int getTopSensorYOff() {
		return topSensorYOff;
	}

	public void setTopSensorYOff(int topSensorYOff) {
		this.topSensorYOff = topSensorYOff;
	}

	public int getGroundPointXOff() {
		return groundPointXOff;
	}

	public void setGroundPointXOff(int groundPointXOff) {
		this.groundPointXOff = groundPointXOff;
	}

	public int getGroundPointYOff() {
		return groundPointYOff;
	}

	public void setGroundPointYOff(int groundPointYOff) {
		this.groundPointYOff = groundPointYOff;
	}

	public void setMainBox(Rectangle mainBox) {
		this.mainBox = mainBox;
	}

	public Point getTopPoint() {
		return topPoint;
	}

	public void setTopPoint(Point topPoint) {
		this.topPoint = topPoint;
	}

	public int getTopPointXOff() {
		return topPointXOff;
	}

	public void setTopPointXOff(int topPointXOff) {
		this.topPointXOff = topPointXOff;
	}

	public int getTopPointYOff() {
		return topPointYOff;
	}

	public void setTopPointYOff(int topPointYOff) {
		this.topPointYOff = topPointYOff;
	}
	
}
