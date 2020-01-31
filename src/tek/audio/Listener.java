package tek.audio;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

public class Listener {
	private Vector3f lastPosition, lastRotation, lastVelocity;
	public Vector3f position, rotation, velocity;
	private FloatBuffer posbuffer, oribuffer, velbuffer;
	
	{
		lastPosition = new Vector3f();
		lastRotation = new Vector3f();
		lastVelocity = new Vector3f();
		
		position = new Vector3f();
		rotation = new Vector3f();
		velocity = new Vector3f();
		
		posbuffer = BufferUtils.createFloatBuffer(3);
		oribuffer = BufferUtils.createFloatBuffer(6);
		velbuffer = BufferUtils.createFloatBuffer(3);
	}
	
	public Listener(Vector3f position){
		this.position.set(position);
	}
	
	public Listener(Vector3f position, Vector3f rotation){
		this.position.set(position);
		this.rotation.set(rotation);
	}
	
	public void update(){
		if(lastPosition != position){
			posbuffer.clear();
			posbuffer.put(new float[]{
				position.x,
				position.y,
				position.z
			});
			lastPosition.set(position);
		}
		
		if(lastRotation != rotation){
			oribuffer.clear();
			oribuffer.put(new float[] {
				(float)(Math.toRadians(rotation.x)),
				(float)(Math.toRadians(rotation.y)),
				(float)(Math.toRadians(rotation.z)),
				0f,
				1f,
				0f
			});
			
			lastRotation.set(rotation);
		}
		
		if(lastVelocity != velocity){
			velbuffer.clear();
			velbuffer.put(new float[]{
				velocity.x,
				velocity.y,
				velocity.z,
			});
			lastVelocity.set(velocity);
		}
			
		
		AL10.alListenerfv(AL10.AL_POSITION,    posbuffer);
		AL10.alListenerfv(AL10.AL_ORIENTATION, oribuffer);
		AL10.alListenerfv(AL10.AL_VELOCITY,    velbuffer);
	}

	public void setPosition(Vector3f position){
		this.position.set(position);
		posbuffer.clear();
		posbuffer.put(new float[]{
			position.x,
			position.y,
			position.z
		});
	}
	
	public void setRotation(Vector3f rotation){
		this.rotation.set(rotation);
		oribuffer.clear();
		oribuffer.put(new float[] {
			(float)(Math.toRadians(rotation.x)),
			(float)(Math.toRadians(rotation.y)),
			(float)(Math.toRadians(rotation.z)),
			0f,
			1f,
			0f
		});
	}
	
	public void setVelocity(Vector3f velocity){
		this.velocity.set(velocity);
		velbuffer.clear();
		velbuffer.put(new float[]{
			velocity.x,
			velocity.y,
			velocity.z,
		});
	}
	
	public void destroy(){
		posbuffer.clear();
		oribuffer.clear();
		velbuffer.clear();
	}
}
