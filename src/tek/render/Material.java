package tek.render;

import org.joml.Vector4f;

public class Material {
	public Vector4f color;
	public Texture texture;
	
	public int subTexture = -1;
	public boolean colorOnly;
	
	{
		color = new Vector4f(1f);
	}
	
	public Material(Texture texture){
		colorOnly = false;
		this.texture = texture;
	}
	
	public Material(Vector4f color){
		this.color.set(color);
		colorOnly = true;
	}
	
	public Material(Texture texture, Vector4f color){
		this.texture = texture;
		this.color.set(color);
		colorOnly = false;
	}
	
	public Material(){
		color = new Vector4f(1f);
		colorOnly = true;
	}
	
	public void setColor(float r, float g, float b, float a){
		color.x = (r > 1) ? r / 255 : r;
		color.y = (g > 1) ? g / 255 : g;
		color.z = (b > 1) ? b / 255 : b;
		color.w = (a > 1) ? a / 255 : a;
	}
	
	public Vector4f getColor(){
		return color;
	}
	
	public void setTexture(Texture texture){
		if(colorOnly && texture != null)
			colorOnly = false;
		else if(texture == null)
			colorOnly = true;
		this.texture = texture;
	}
	
	public Texture getTexture(){
		return texture;
	}
	
	public void setSubTexutre(int id){
		subTexture = id;
	}
	
	public void disableSubTexture(){
		subTexture = -1;
	}
	
	public boolean isSubTexture(){
		return subTexture != -1;
	}
}
