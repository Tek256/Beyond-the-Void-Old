package tek.runtime;

import java.util.ArrayList;

import tek.game.GameObject;

public class World {
	public ArrayList<PhysicsObject> objects;
	private ArrayList<GameObject> sharedList;
	
	{
		objects = new ArrayList<PhysicsObject>();
	}
	
	public World(){
	}
	
	public World(ArrayList<GameObject> sharedList){
		this.sharedList = sharedList;
	}
	
	public void gatherFrom(GameObject ... gameObjects){
		for(GameObject gameObject : gameObjects){
			if(gameObject.physicsObject != null){
				objects.add(gameObject.physicsObject);
			}
		}
	}
	
	public void update(long delta){
		
	}
	
	public void destroy(){
		objects.clear();
	}
}
