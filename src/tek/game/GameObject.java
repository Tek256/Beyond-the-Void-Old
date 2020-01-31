package tek.game;

import java.util.ArrayList;
import java.util.Iterator;

import tek.runtime.*;
import tek.script.Script;

public class GameObject {
	public Transform transform;
	public Renderable renderable;
	public PhysicsObject physicsObject;
	public ArrayList<Script> scripts;
	
	{
		transform = new Transform();
		scripts = new ArrayList<Script>();
	}
	
	public GameObject(){
	}
	
	public GameObject(PhysicsObject physicsObject){
		this.physicsObject = physicsObject;
	}
	
	public GameObject(Renderable renderable){
		this.renderable = renderable;
	}
	
	public GameObject(Renderable renderable, PhysicsObject physicsObject){
		this.renderable = renderable;
		this.physicsObject = physicsObject;
	}
	
	public GameObject(GameObject g){
		this.transform = new Transform(g.transform);
		this.renderable = new Renderable(g.renderable);
		this.physicsObject = new PhysicsObject(g.physicsObject);
	}
	
	public void update(long delta){
		for(Script script : scripts){
			if(script.active)
				script.update(delta);
		}
	}
	
	public boolean attachScript(Class<?> c){
		try {
			if(!c.getSuperclass().equals(Script.class)){
				System.out.println("xd");
				return false;
			}
			
			Script instance = (Script)c.newInstance();
			
			if(instance == null)
				return false;
			
			scripts.add(instance);
			return true;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void destroyScript(int index){
		Iterator<Script> iter = scripts.iterator();
		int iterIndex = 0;
		while(iter.hasNext()){
			iter.next();
			
			if(iterIndex == index){
				iter.remove();
				break; //exit loop 
			}
			iterIndex ++;
		}
	}
	
	public void destroyScript(Class<?> scriptClass, boolean all){
		Iterator<Script> iter = scripts.iterator();
		while(iter.hasNext()){
			Script next = iter.next();
			
			if(next.getClass().equals(scriptClass)){
				iter.remove();
				if(!all) //if not going to destroy all
					break; //exit loop 
			}
		}
	}
	
	public void destroyScript(Class<?> scriptClass){
		destroyScript(scriptClass, false);
	}
	
	public void destroyAllScripts(Class<?> scriptClass){
		destroyScript(scriptClass, true);
	}
	
	public String[] getScripts(){
		String[] scriptNames = new String[scripts.size()];
		for(int i=0;i<scripts.size();i++){
			scriptNames[i] = scripts.get(i).getClass().getSimpleName();
		}
		return scriptNames;
	}
}
