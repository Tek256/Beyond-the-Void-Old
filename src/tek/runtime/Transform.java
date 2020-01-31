package tek.runtime;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Transform {
	public Vector3f position, rotation, scale, velocity;
	
	{ //local creation
		position = new Vector3f();
		rotation = new Vector3f();
		scale    = new Vector3f(1f);
		velocity = new Vector3f();
	}
	
	public Transform(){	}
	
	public Transform(Vector3f position) {
		this.position.set(position);
	}
	
	public Transform(Vector3f position, Vector3f rotation) {
		this.position.set(position);
		this.rotation.set(rotation);
	}
	
	public Transform(Vector3f position, Vector3f rotation, Vector3f scale){
		this.position.set(position);
		this.rotation.set(rotation);
		this.scale = scale;
	}
	
	public Transform(Transform t){
		this.position.set(t.position);
		this.rotation.set(t.rotation);
		this.scale.set(t.scale);
		this.velocity.set(t.velocity);
	}
	
	/* ------ UTILITY ------ */
	public void move(float x, float y, float z){
		z *= -1;
		y *= -1;
		if(z != 0){
			position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1f * z;
			position.z += (float)Math.cos(Math.toRadians(rotation.y)) * z;
		}
		if(x != 0){
			position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1f * x;
			position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * x;
		}
		position.y += y;
	}
	
	public void rotate(float dx, float dy){
		if(rotation.y + dy > 360){
			rotation.y = rotation.y + dy - 360;
		}else if(rotation.y + dy < 0){
			rotation.y = 360 - dy;
		}
		
		if(rotation.x + dx > 360){
			rotation.x = rotation.x + dx - 360;
		}else if(rotation.x + dx < 0){
			rotation.x = 360 - rotation.x + dx;
		}
	}
	
	/*  ------ POSITION ------*/
	
	public void setX(float x){
		position.x = x;
	}
	
	public void setY(float y){
		position.y = y;
	}
	
	public void setZ(float z){
		position.z = z;
	}
	
	public float getX(){
		return position.x;
	}
	
	public float getY(){
		return position.y;
	}
	
	public float getZ(){
		return position.z;
	}
	
	public void setPosition(Vector2f vec){
		this.position.x = vec.x;
		this.position.y = vec.y;
	}
	
	public void setPosition(float x, float y){
		this.position.x = x;
		this.position.y = y;
	}
	
	public void setPosition(Vector3f vec){
		this.position.set(vec);
	}
	
	public void setPosition(float x, float y, float z){
		this.position.set(x,y,z);
	}
	
	public void setPosition(Vector4f vec){
		this.position.set(vec.x, vec.y, vec.z);
	}
	
	/*  ------ ROTATION ------*/
	
	public void setRX(float x){
		rotation.x = x;
	}
	
	public void setRY(float y){
		rotation.y = y;
	}
	
	public void setRZ(float z){
		rotation.z = z;
	}
	
	public float getRX(){
		return rotation.x;
	}
	
	public float getRY(){
		return rotation.y;
	}
	
	public float getRZ(){
		return rotation.z;
	}
	
	public void setRotation(Vector2f vec){
		this.rotation.x = vec.x;
		this.rotation.y = vec.y;
	}
	
	public void setRotation(float x, float y){
		this.rotation.x = x;
		this.rotation.y = y;
	}
	
	public void setRotation(Vector3f vec){
		this.rotation.set(vec);
	}
	
	public void setRotation(float x, float y, float z){
		this.rotation.set(x,y,z);
	}
	
	public void setRotation(Vector4f vec){
		this.rotation.set(vec.x, vec.y, vec.z);
	}
	
	/*  ------ SCALE ------*/
	
	public void setSX(float x){
		scale.x = x;
	}
	
	public void setSY(float y){
		scale.y = y;
	}
	
	public void setSZ(float z){
		scale.z = z;
	}
	
	public float getSX(){
		return scale.x;
	}
	
	public float getSY(){
		return scale.y;
	}
	
	public float getSZ(){
		return scale.z;
	}
	
	public void setScale(Vector2f vec){
		this.scale.x = vec.x;
		this.scale.y = vec.y;
	}
	
	public void setScale(float x, float y){
		this.scale.x = x;
		this.scale.y = y;
	}
	
	public void setScale(Vector3f vec){
		this.scale.set(vec);
	}
	
	public void setPositon(float x, float y, float z){
		this.scale.set(x,y,z);
	}
	
	public void setScale(Vector4f vec){
		this.scale.set(vec.x, vec.y, vec.z);
	}
	
	/*  ------ VELOCITY ------*/
	
	public void setVX(float x){
		velocity.x = x;
	}
	
	public void setVY(float y){
		velocity.y = y;
	}
	
	public void setVZ(float z){
		velocity.z = z;
	}
	
	public float getVX(){
		return velocity.x;
	}
	
	public float getVY(){
		return velocity.y;
	}
	
	public float getVZ(){
		return velocity.z;
	}
	
	public void setVelocity(Vector2f vec){
		this.velocity.x = vec.x;
		this.velocity.y = vec.y;
	}
	
	public void setVelocity(float x, float y){
		this.velocity.x = x;
		this.velocity.y = y;
	}
	
	public void setVelocity(Vector3f vec){
		this.velocity.set(vec);
	}
	
	public void seVelocity(float x, float y, float z){
		this.velocity.set(x,y,z);
	}
	
	public void setVelocity(Vector4f vec){
		this.velocity.set(vec.x, vec.y, vec.z);
	}
}