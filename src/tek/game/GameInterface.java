package tek.game;

public interface GameInterface {
	public void start();
	public void exit();
	
	public void input(long delta);
	public void update(long delta);
	public void render(long delta);
}
