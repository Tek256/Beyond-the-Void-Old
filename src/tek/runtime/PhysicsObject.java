package tek.runtime;

import java.util.ArrayList;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import tek.physics.Collider;

public class PhysicsObject {
	private Vector3f lpos, lsize;
	public Vector3f position, size, velocity;
	public Quaternionf rotation;
	public Vector3f min, max;
	public ArrayList<Collider> colliders;
	
	{
		position = new Vector3f();
		rotation = new Quaternionf();
		size     = new Vector3f();
		velocity = new Vector3f();
		
		lpos = new Vector3f();
		lsize = new Vector3f();
		
		min = new Vector3f();
		max = new Vector3f();
		
		colliders = new ArrayList<Collider>();
	}
	
	public PhysicsObject(){
	}
	
	public PhysicsObject(Vector3f position){
		this.position.set(position);
		
		recalc();
	}
	
	public PhysicsObject(Vector3f position, Vector3f rotation){
		this.position.set(position);
		this.rotation.set(rotation.x, rotation.y, rotation.z);
		
		recalc();
	}
	
	public PhysicsObject(Vector3f position, Vector3f rotation, Vector3f size){
		this.position.set(position);
		this.rotation.set(rotation.x, rotation.y, rotation.z);
		this.size.set(size);
		
		recalc();
	}
	
	public PhysicsObject(PhysicsObject p){
		this.position.set(p.position);
		this.rotation.set(p.rotation);
		this.size.set(p.size);
		
		recalc();
	}
	
	public void update(float delta){
		//less operations to find squared length
		if(velocity.lengthSquared() != 0)
			position.add(velocity.mul(delta, new Vector3f()));
		
		if(!lpos.equals(position) || !lsize.equals(size)) 
			recalc();
		
		//check for any dead colliders
		for(Collider collider : colliders)
			if(collider.parent != this)
				colliders.remove(collider);
	}
	
	public void addCollider(Collider collider){
		//ensure we're looking at the right collider
		if(collider.parent != this)
			return;
		colliders.add(collider);
	}
	
	private void recalc(){
		if(lpos.equals(position) && lsize.equals(size)) return;
		if(min == null) min = new Vector3f();
		if(max == null) max = new Vector3f();
		
		Vector3f hsize = size.div(2, new Vector3f());
		
		min.set(position);
		max.set(position);
		
		min.sub(hsize);
		max.add(hsize);
	}
}
