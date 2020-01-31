package tek.render;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Camera {
	public Matrix4f projection, view;
	public Vector3f position;
	public Quaternionf rotation;
	private float fov, near, far, aspect;
	
	public boolean autoUpdate = false; //auto update projection & view matrix
	
	{
		projection = new Matrix4f();
		view       = new Matrix4f();
		position   = new Vector3f();
		rotation   = new Quaternionf();
	}
	
	public Camera(float fov, float aspect){
		this(fov, aspect, 0.01f, 100f);
	}
	
	public Camera(float fov, float aspect, float near, float far){
		this.fov = fov;
		this.aspect = aspect;
		this.near = near;
		this.far = far;
		
		init();
	}
	
	private void init(){
		updateView();
		updateProjection();
	}
	
	public void set(Vector3f position, Vector3f rotation){
		this.position.set(position);
		this.rotation.set(rotation.x, rotation.y, rotation.z);
		updateView();
	}
	
	public void set(Vector3f position, Quaternionf rotation){
		this.position.set(position);
		this.rotation.set(rotation);
		updateView();
	}
	
	public void updateView(){
		view.identity();
		
		view.rotateX((float)(Math.toRadians(rotation.x)));
		view.rotateY((float)(Math.toRadians(rotation.y)));
		view.rotateZ((float)(Math.toRadians(rotation.z)));
		
		view.translate(-position.x, -position.y, -position.z);
		
		view.scale(1f, 1f, 1f);
	}
	
	public void updateProjection(){
		float len = far - near;
		float aspect = Window.getWindowWidth() / Window.getWindowHeight();
		float yscale = 1 / (float)Math.tan(Math.toRadians(fov / 2));
		float xscale = yscale / aspect;
		
		if(projection == null)
			projection = new Matrix4f();
		
		projection.identity();
		projection.m00 = xscale;
		projection.m11 = yscale;
		projection.m22 = -((far +  near) / len);
		projection.m23 = -1;
		projection.m32 = -((2 * near * far) / len);
		projection.m33 = 0;
	}
	
	public void setAspect(float aspect){
		this.aspect = aspect;
		updateProjection();
	}
	
	public void setFOV(float fov){
		this.fov = fov;
		if(autoUpdate)
			updateProjection();
	}
	
	public float getFOV(){
		return fov;
	}
	
	public void setNear(float near){
		this.near = near;
	}
	
	public float getNear(){
		return near;
	}
	
	public void setFar(float far){
		this.far = far;
	}
	
	public float getFar(){
		return far;
	}
	
	public float getAspect(){
		return aspect;
	}
	
}
