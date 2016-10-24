package bulletmaster;

public class TickTimer {
	//A class for keeping track of time by ticks.
	//This timer will update everytime the tick() method is called.
	//This is much less expensive than using SystemTimer which updates based on System.nanoTime();
	protected int ticks=0;
	
	//If paused, the timer will not tick, even if tick() is called.
	protected boolean pause = false;
	
	//Ticks will reset to 0 if this value is reached
	protected int maxTicks;

	
	
	public TickTimer(){
		ticks = 0;
		pause = false;
		maxTicks = 32000;
	}
		
	
	public void tick(){
		if (pause == false){
			this.ticks++;
		}
		if(ticks>=maxTicks){
			ticks = 0;
		}
	}
	
	public void pause(){
		pause = true;
	}
	
	public void unpause(){
		pause = false;
	}
	
	public void reset(){
		this.ticks = 0;
	}
	
	public void setMaxTicks(int maxTicks){
		this.maxTicks = maxTicks;
	}
	
	public int getTicks(){
		return ticks;
	}
	
	

}
