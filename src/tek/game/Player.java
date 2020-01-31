package tek.game;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Player {
	public Vector3f position;
	public Quaternionf rotation;
	public float movespeed = 1f;
	
	{
		position = new Vector3f();
		rotation = new Quaternionf();
	}
	
	public Player(){ //TODO add more constructors
	}
	
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
		rotation.x += dx;
		rotation.y += dy;
		
		float remainder = 0f;
		
		//negative
		if(rotation.x < 0){
			if(rotation.x < 360)
				remainder = rotation.x % 360;
			else
				remainder = rotation.x;
			rotation.x = 360 + remainder;
		}else if(rotation.x >= 360){
			remainder = rotation.x % 360;
			rotation.x = remainder;
		}
		
		if(rotation.y < 0){
			if(rotation.y < 360)
				remainder = rotation.y % 360;
			else
				remainder = rotation.y;
			rotation.y = 360 + remainder;
		}else if(rotation.y >= 360){
			remainder = rotation.y % 360;
			rotation.y = remainder;
		}
	}
}
