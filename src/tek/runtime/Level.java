package tek.runtime;

import java.util.ArrayList;

import tek.game.GameObject;

/*
 * THIS CLASS IS FOR PHYSICAL REPRESENTATION OF OBJECTS
 */

public class Level {
	//physics is coupled with transforms in methods, I really just ran out of more words
	public World world;
	
	public ArrayList<GameObject> gameObjects;
	
	{
		gameObjects = new ArrayList<GameObject>();
		world = new World(gameObjects);
	}
	
	public Level(){
		initialize();
	}
	
	private void initialize(){
		
	}
	
	public void update(long delta){
		gameObjects.forEach((object) -> {
		});
	}
}
