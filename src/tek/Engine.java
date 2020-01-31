package tek;

import tek.game.GameInterface;
import tek.input.Keyboard;
import tek.input.Mouse;
import tek.render.Window;

public class Engine {
	public static int targetFPS = 60;
	public static int targetUPS = 60;
	
	public static int FPS = 0;
	public static int UPS = 0;
	
	public static final long SECOND = 1_000_000;
	
	private final long initialization;
	private boolean running = false;
	
	private Window window;
	
	private GameInterface game;
	
	public Engine(GameInterface game){
		long current = System.currentTimeMillis();
		initialization = current;
		
		this.game = game;
		
		init();
	}
	
	private void init(){
		window = new Window();
		window.setClearColor(0.13f, 0.13f, 0.13f);
		running = true;
		
		game.start();
		
		loop();
	}
	
	private void loop(){
		int frames = 0;
		int updates = 0;
		
		long timer = SECOND;
		
		long updateFrame = SECOND / targetUPS;
		long current = System.currentTimeMillis();
		long accumulation = updateFrame;
		long lastUpdate = current;
		
		long delta = 0L;
		
		while(running){
			current = System.currentTimeMillis();
			delta = current - lastUpdate;
			
			long tempCur, tempCur2;
			
			//cycle updates
			if(accumulation > 0){
				tempCur = System.currentTimeMillis();
				
				input(delta);
				update(delta);
				
				updates ++;
				
				//update accumulation time
				tempCur2 = System.currentTimeMillis();
				accumulation -= tempCur2 - tempCur;
				tempCur = tempCur2;
			}
			
			render(delta);
			frames ++;
			
			lastUpdate = current;
			
			if(timer >= 0){
				FPS = frames;
				UPS = updates;
				
				frames = 0;
				updates = 0;
				timer += SECOND;
			}
		}
		
		game.exit();
	}
	
	private void input(long delta){
		window.pollEvents();
		Keyboard.update(delta);
		Mouse.update();
		game.input(delta);
	}
	
	private void update(long delta){
		//exit the game if closing
		if(window.isCloseRequested())
			running = false;
		
		game.update(delta);
	}
	
	private void render(long delta){
		window.clear();
		
		game.render(delta);
		
		window.swapBuffers();
	}
	
	public long durationRunning(){
		return System.currentTimeMillis() - initialization;
	}
	
	public boolean isRunning(){
		return running;
	}
}
