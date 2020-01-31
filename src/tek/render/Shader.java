package tek.render;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import tek.Application;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
	public static ArrayList<Shader> shaders; 
	public static Shader bound = null;
	
	static {
		shaders = new ArrayList<Shader>();
	}
	
	public ArrayList<Uniform> uniforms;
	
	public final int id;
	public final String name;
	private boolean dead = false;
	
	private FloatBuffer matBuffer;
	
	public Shader(String name, String vertex, String fragment){
		this.name = name;
		
		int gen = glCreateProgram();
		
		int vert = createShader(vertex, GL_VERTEX_SHADER);
		int frag = createShader(fragment, GL_FRAGMENT_SHADER);
		
		glAttachShader(gen, vert);
		glAttachShader(gen, frag);
		
		glLinkProgram(gen);
		
		if(glGetProgrami(gen, GL_LINK_STATUS) == GL11.GL_FALSE){
			Application.error(glGetProgramInfoLog(gen, 1024));
			dead = true;
			glDeleteProgram(gen);
		}else{
			uniforms = new ArrayList<Uniform>();
			matBuffer = BufferUtils.createFloatBuffer(16);
		}
		
		id = gen;
	}
	
	private int createShader(String text, int type){
		int id = glCreateShader(type);
		glShaderSource(id, text);
		glCompileShader(id);
		if(glGetShaderi(id, GL_COMPILE_STATUS) == GL11.GL_FALSE)
			Application.error(glGetShaderInfoLog(id, 1024));
		
		return id;
	}
	
	public void bind(){
		if(bound != null)
			if(bound.id == id || dead) //faster comparator
				return;
		
		bound = this;
		glUseProgram(id);
	}
	
	public static void unbind(){
		bound = null;
		glUseProgram(0);
	}
	
	public void destroy(){
		if(dead) //check for prior destruction
			return;
		dead = true;
		glDeleteProgram(id);
		shaders.remove(this);
	}

	public int getUniformLocation(String uniform){
		for(Uniform u : uniforms)
			if(u.name.equals(uniform))
				return u.location;
		
		int location = GL20.glGetUniformLocation(id, uniform);
		uniforms.add(new Uniform(uniform, location));
		return location;
	}
	
	public void setUniform(String uniform, int... ints){
		int loc = getUniformLocation(uniform);
		if(ints.length == 0)
			return;
		else if(ints.length == 1){
			glUniform1i(loc, ints[0]);
		}else if(ints.length == 2){
			glUniform2i(loc, ints[0], ints[1]);
		}else if(ints.length == 3){
			glUniform3i(loc, ints[0], ints[1], ints[2]);
		}else if(ints.length == 4){
			glUniform4i(loc, ints[0], ints[1], ints[2], ints[3]);
		}
	}
	
	public void setUniform(String uniform, float... floats){
		int loc = getUniformLocation(uniform);
		if(floats.length == 0)
			return;
		else if(floats.length == 1){
			glUniform1f(loc, floats[0]);
		}else if(floats.length == 2){
			glUniform2f(loc, floats[0], floats[1]);
		}else if(floats.length == 3){
			glUniform3f(loc, floats[0], floats[1], floats[2]);
		}else if(floats.length == 4){
			glUniform4f(loc, floats[0], floats[1], floats[2], floats[3]);
		}
	}
	
	public void setUniform(String uniform, Vector2f vec){
		int loc = getUniformLocation(uniform);
		glUniform2f(loc, vec.x, vec.y);
	}
	
	public void setUniform(String uniform, Vector3f vec){
		int loc = getUniformLocation(uniform);
		glUniform3f(loc, vec.x, vec.y, vec.z);
	}
	
	public void setUniform(String uniform, Vector4f vec){
		int loc = getUniformLocation(uniform);
		glUniform4f(loc, vec.x, vec.y, vec.z, vec.w);
	}
	
	public void setUniform(String uniform, Matrix4f mat){
		int loc = getUniformLocation(uniform);
		GL20.glUniformMatrix4fv(loc, false, mat.get(matBuffer));
	}
	
	public void setUniform(String uniform, boolean bool){
		int loc = getUniformLocation(uniform);
		GL20.glUniform1i(loc, bool ? 1 : 0);
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public boolean isBound(){
		return (bound != null) ? bound.id == id : false;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(!o.getClass().equals(getClass())) return false;
		
		Shader s = (Shader)o;
		return s.id == id;
	}
	
	public static Shader getShader(String name){
		for(Shader s : shaders)
			if(s.name.toLowerCase().equals(name.toLowerCase()))
				return s;
		return null;
	}

	public static class Uniform {
		public final String name;
		public final Integer location;

		public Uniform(String name, Integer location){
			this.name = name;
			this.location = location;
		}
	}
}
