package tek.runtime;

import org.joml.Matrix4f;

import tek.render.Material;
import tek.render.Mesh;
import tek.render.Shader;

public class Renderable {
	public Shader shader;
	public Mesh mesh;
	public Material material;
	public Matrix4f matrix;
	
	{ //local initialization
		matrix = new Matrix4f();
	}
	
	public Renderable(){
		material = new Material();
		mesh = null;
		shader = Shader.getShader("default");
	}
	
	public Renderable(Mesh mesh){
		this.mesh = mesh;
		material = new Material();
		shader = Shader.getShader("default");
	}
	
	public Renderable(Mesh mesh, Material material){
		this.mesh = mesh;
		this.material = material;
		shader = Shader.getShader("default");
	}
	
	public Renderable(Mesh mesh, Material material, Shader shader){
		this.mesh = mesh;
		this.material = material;
		if(shader != null)
			this.shader = shader;
		else
			shader = Shader.getShader("default");
	}
	
	public Renderable(Renderable r){
		this.shader = r.shader;
		this.mesh = r.mesh;
		this.material = r.material;
		this.matrix.set(r.matrix);
	}
	
	public void prep(Transform transform){
		matrix.identity();
		matrix.translate(transform.position);
		matrix.rotateX(toRad(transform.rotation.x));
		matrix.rotateY(toRad(transform.rotation.y));
		matrix.rotateZ(toRad(transform.rotation.z));
		matrix.scale(transform.scale);
	}
	
	private float toRad(float deg){
		return (float)(deg * (Math.PI / 180f));
	}
}
