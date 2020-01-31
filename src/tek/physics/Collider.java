package tek.physics;

import org.joml.Vector3f;

import tek.runtime.PhysicsObject;

public abstract class Collider {
	public PhysicsObject parent;
	
	public Vector3f offset;
	public Vector3f center;
	
	//check if anything has happened
	private boolean hasMoved = false;
	
	{
		offset = new Vector3f();
		center = new Vector3f();
	}
	
	public Collider(PhysicsObject parent){
		this.parent = parent;
	}
	
	public Collider(PhysicsObject parent, Vector3f offset){
		this.parent = parent;
		this.offset.set(offset);
		this.center.set(parent.position).add(offset);
	}

	protected void move(Vector3f vec){
		center.add(vec);
		onMove(vec);
	}
	
	protected abstract void onMove(Vector3f offset);
	
	public boolean hasMoved(){
		if(hasMoved){
			hasMoved = false;
			return true;
		}
		return false;
	}
	
	public void destroy(){
		//dereference parent for GC
		parent = null;
	}
}
