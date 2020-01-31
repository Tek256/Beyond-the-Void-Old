package tek.physics;

import org.joml.Vector3f;

import tek.runtime.PhysicsObject;

public class SphereCollider extends Collider {
	public float radius = 1.0f;
	
	public SphereCollider(PhysicsObject obj){
		super(obj);
	}
	
	public SphereCollider(PhysicsObject obj, float radius){
		super(obj);
		this.radius = radius;
	}
	
	public SphereCollider(PhysicsObject obj, float radius, Vector3f offset){
		super(obj, offset);
		this.radius = radius;
	}
	
	public boolean contains(Vector3f point){
		return point.distance(center) <= radius; 
	}
	
	public boolean collidesWith(BoxCollider box){
		return false;
	}
	
	protected void onMove(Vector3f vec){ //do nothing, no min/max to recalculate
	}
}
