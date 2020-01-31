package tek.physics;

import org.joml.Vector3f;

import tek.runtime.PhysicsObject;

public class BoxCollider extends Collider {
	public Vector3f size;
	
	private Vector3f lsize, loffset; //for tracking
	private Vector3f min, max; //relative sizes 
	
	{
		size = new Vector3f();
		
		lsize = new Vector3f();
		loffset = new Vector3f();
	}
	
	public BoxCollider(PhysicsObject parent){
		super(parent);
		size.set(parent.size);
		
		recalc();
	}
	
	public BoxCollider(PhysicsObject parent, Vector3f size){
		super(parent);
		size.set(size);
		recalc();
	}
	
	public BoxCollider(PhysicsObject parent, Vector3f size, Vector3f offset){
		super(parent, offset);
		this.size.set(size);
		recalc();
	}
	
	public void update(){
		if(!lsize.equals(size) || !loffset.equals(offset))
			recalc();
	}
	
	protected void onMove(Vector3f vec){
		//simply update with the parent's moved offset
		min.add(vec);
		max.add(vec);
	}
	
	private void recalc(){
		Vector3f temp = new Vector3f(size);
		temp.div(2f);
		
		min.set(center);
		min.sub(temp);
		
		max.set(center);
		max.add(temp);
		
		lsize.set(size);
		loffset.set(offset);
	}
	
	public Vector3f getMin(){
		return min;
	}
	
	public Vector3f getMax(){
		return max;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(!o.getClass().equals(getClass())) return false;
		
		BoxCollider b = (BoxCollider)o;
		
		return b.parent == parent 
				&& b.size == size
				&& b.offset == offset;
	}
}
