package tek.script;

import tek.physics.Collider;

public abstract class Script {
	public String name;
	public boolean active = true;
	
	public abstract void start();
	public abstract void update(long delta);
	public abstract void death();
	
	//somewhat messy implementation of optional methods
	public void onCollision(Collider src, Collider dst){}
	public void fixedUpdate(long delta){} 
}
