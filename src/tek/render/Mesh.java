package tek.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

public class Mesh {
	public static int method = GL_TRIANGLES;
	
	private int vao, vbo, vboi, vto, vno;
	private int vertexcount, faces;
	
	private Vector3f size;
		
	public Mesh(float[] verts, float[] texcoords, float[] normals, int[] indices){
		vertexcount = indices.length;
		
		faces = indices.length / 3;
		
		vao = glGenVertexArrays();
		
		glBindVertexArray(vao);
		
		vbo  = glGenBuffers();
		vto  = glGenBuffers();
		vno  = glGenBuffers();
		vboi = glGenBuffers();
		
		Vector3f min = new Vector3f(Float.POSITIVE_INFINITY);
		Vector3f max = new Vector3f(Float.NEGATIVE_INFINITY);
		
		for(int i=0; i<verts.length; i += 3){
			float x = verts[i];
			float y = verts[i+1];
			float z = verts[i+2];
			
			if(x < min.x){
				min.x = x;
			}else if(x > max.x){
				max.x = x;
			}
			
			if(y < min.y){
				min.y = y;
			}else if(y > max.y){
				max.y = y;
			}
			
			if(z < min.z){
				min.z = z;
			}else if(z > max.z){
				max.z = z;
			}
		}
		
		size = max.sub(min, new Vector3f());
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(verts.length);
		vertexBuffer.put(verts);
		vertexBuffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
		FloatBuffer texcoordBuffer = BufferUtils.createFloatBuffer(texcoords.length);
		texcoordBuffer.put(texcoords);
		texcoordBuffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vto);
		glBufferData(GL_ARRAY_BUFFER, texcoordBuffer, GL_STATIC_DRAW);
		
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		
		FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(normals.length);
		normalBuffer.put(normals);
		normalBuffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vno);
		glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
		
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
		
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboi);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
	}
	
	public Mesh(float[] verts, float[] texcoords, int[] indices){
		vertexcount = indices.length;
		
		faces = indices.length / 3;
		
		vao = glGenVertexArrays();
		
		glBindVertexArray(vao);
		
		vbo  = glGenBuffers();
		vto  = glGenBuffers();
		vno  = glGenBuffers();
		vboi = glGenBuffers();
		
		Vector3f min = new Vector3f(Float.POSITIVE_INFINITY);
		Vector3f max = new Vector3f(Float.NEGATIVE_INFINITY);
		
		float[] normals = new float[indices.length];
		
		for(int i=0;i<indices.length;i+=3){
			Vector3f v0 = new Vector3f(
					verts[(indices[i] * 3)],
					verts[(indices[i] * 3)+1],
					verts[(indices[i] * 3)+2]);
			Vector3f v1 = new Vector3f(
					verts[(indices[i+1] * 3)],
					verts[(indices[i+1] * 3)+1],
					verts[(indices[i+1] * 3)+2]);
			Vector3f v2 = new Vector3f(
					verts[(indices[i+2] * 3)],
					verts[(indices[i+2] * 3)+1],
					verts[(indices[i+2] * 3)+2]);
			
			
			Vector3f u = new Vector3f();
			u.x = v1.x - v0.x;
			u.y = v1.y - v0.y;
			u.z = v1.z - v0.z;
			
			Vector3f v = new Vector3f();
			v.x = v2.x - v0.x;
			v.y = v2.y - v0.y;
			v.z = v2.z - v0.z;
			
			
			normals[i  ] = (u.y * v.z) - (u.z * v.y); //x
			normals[i+1] = (u.z * v.x) - (u.x * v.z); //y
			normals[i+2] = (u.x * v.y) - (u.y * v.x); //z
		}
		
		for(int i=0; i<verts.length; i += 3){
			float x = verts[i];
			float y = verts[i+1];
			float z = verts[i+2];
			
			if(x < min.x){
				min.x = x;
			}else if(x > max.x){
				max.x = x;
			}
			
			if(y < min.y){
				min.y = y;
			}else if(y > max.y){
				max.y = y;
			}
			
			if(z < min.z){
				min.z = z;
			}else if(z > max.z){
				max.z = z;
			}
		}
		
		size = max.sub(min, new Vector3f());
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(verts.length);
		vertexBuffer.put(verts);
		vertexBuffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
		FloatBuffer texcoordBuffer = BufferUtils.createFloatBuffer(texcoords.length);
		texcoordBuffer.put(texcoords);
		texcoordBuffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vto);
		glBufferData(GL_ARRAY_BUFFER, texcoordBuffer, GL_STATIC_DRAW);
		
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		
		FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(normals.length);
		normalBuffer.put(normals);
		normalBuffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vno);
		glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
		
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
		
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboi);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
	}
	
	public void render(){
		glBindVertexArray(vao);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glDrawElements(method, vertexcount, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		
		glBindVertexArray(0);
	}
	
	public int getVertexCount(){
		return vertexcount;
	}
	
	public int getFaces(){
		return faces;
	}
	
	public Vector3f getSize(){
		return size;
	}
	
	public void destroy(){
		glDeleteBuffers(vbo);
		glDeleteBuffers(vboi);
		glDeleteBuffers(vno);
		glDeleteBuffers(vto);
		glDeleteVertexArrays(vao);
	}
}