package bulletmaster;

public class SystemTimer {
	//A strong timer class that relies on System.nanoTime()
	//This class is not used. I recommend using TickTimer instead
	
	
	//Clock time when timer starts
	private long startTicks;
	
	//The ticks stored when the timer was paused
	private long pausedTicks;
	
	
	//The timer status
	private boolean paused;
	private boolean started;
	
	//Nanoseconds per tick
	public static double nsPerTick = 1000000000.0/60;
	
	//Ticks
	private int ticks = 0;
	
			
	//Constructor
	public void Timer(){
		ticks = 0;
		startTicks = 0;
		pausedTicks = 0;
		paused = false;
		started = false;
	}
	
	//methods
	public void start(){
		started = true;
		paused = false;
		//Get the current system time
		startTicks = System.nanoTime();
	}
	
	public void stop(){
		started = false;
		paused = false;
	}
	
	public void pause(){
		if (started == true && paused ==false){
			paused = true;
			pausedTicks =  System.nanoTime()- startTicks;
		}
	}
	
	public void unpause(){
		if (paused == true){
			//Unpause timer
			paused = false;
			
			//Reset starting ticks
			startTicks = System.nanoTime()-pausedTicks;
			
			//Reset paused ticks
			pausedTicks = 0;
		}
	}

	public void tick(){
		
	}
	

	public boolean isStarted() {
		return started;
	}

	public boolean isPaused() {
		return paused;
	}

	

	//Notes: From studying minicraft source code:
	//This code should be in the primary while loop
	//for handling game ticks
				
	
	//while (running) {
	//	long now = System.nanoTime();
	//	unprocessed += (now - lastTime) / nsPerTick;
	//	lastTime = now;
	//	boolean shouldRender = true;
	//	while (unprocessed >= 1) {
	////When unprocess >= 1, a full tick has occured, and rendering can begin
	//		ticks++;
	//		tick();
	//		unprocessed -= 1;
	//		shouldRender = true;
	//	}
	//try {
	//	Thread.sleep(2);
	//} catch (InterruptedException e) {
	//	e.printStackTrace();
	//}
	//if (shouldRender) {
	//	frames++;
	//	render(); //replace render with update() in bulletmaster game
	//}
	
}

