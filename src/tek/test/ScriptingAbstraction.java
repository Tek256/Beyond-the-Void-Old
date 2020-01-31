package tek.test;

import tek.game.GameObject;
import tek.script.Script;

/*
 * This is an engine-headless test
 * Just testing if the abstraction methods for GameObject 
 * & scripting will work effectively. 
 */
public class ScriptingAbstraction {
	public static void main(String[] args){
		GameObject g = new GameObject();
		System.out.println(g.attachScript(TestScript.class));
		
		for(int i=0;i<5;i++)
			g.update(10);
		
		System.out.println(g.getScripts()[0]);
		
		g.destroyScript(TestScript.class);
	}
	
	public static class TestScript extends Script {
		//local parameters
		{
			name = "TestScript";
		}
		
		@Override
		public void start() {
			System.out.println("hello!");
		}

		@Override
		public void update(long delta) {
			System.out.println(delta + " x d test ");
		}

		@Override
		public void death() {
			
		}
	}
}
