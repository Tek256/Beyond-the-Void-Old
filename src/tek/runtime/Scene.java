package tek.runtime;

import java.util.ArrayList;
import java.util.HashMap;

import tek.render.Camera;
import tek.render.Shader;
import tek.render.TextureSheet;

/* THIS CLASS IS FOR RENDERING OBJECTS */

public class Scene {
	public ArrayList<Renderable> renderables;
	public Camera camera;
	
	private HashMap<Shader, ArrayList<Renderable>> shadermap;
	
	{
		renderables = new ArrayList<Renderable>();
		shadermap = new HashMap<Shader, ArrayList<Renderable>>();
	}
	
	public Scene(){
	}
	
	public void add(Renderable r){
		if(r.shader != null){
			if(shadermap.containsKey(r.shader)){
				if(!shadermap.get(r.shader).contains(r)){
					shadermap.get(r.shader).add(r);
				}
			}else{
				ArrayList<Renderable> list = new ArrayList<Renderable>();
				list.add(r);
				shadermap.put(r.shader, list);
			}
		}else{
			renderables.add(r);
		}
	}
	
	public void remove(Renderable r){
		if(r.shader != null){
			if(shadermap.containsKey(r.shader)){
				shadermap.get(r.shader).remove(r);
			}
		}
		renderables.remove(r);
	}
	/* TODO - Deferred Rendering
	private void sortPass(){
		
	}
	
	private void geomPass(){
		
	}
	
	private void lightingPass(){
		
	}*/
	
	public void render(){
		/* SHADER PREP */
		HashMap<Shader, ArrayList<Renderable>> merge = new HashMap<Shader, ArrayList<Renderable>>();
		
		//organize any changes
		for(Shader shader : shadermap.keySet()){
			ArrayList<Renderable> rlist = shadermap.get(shader);
			for(Renderable r : rlist){
				if(!r.shader.equals(shader)){
					if(!merge.containsKey(r.shader))
						merge.put(r.shader, new ArrayList<Renderable>());
					
					merge.get(r.shader).add(r);
					//remove invalid call
					rlist.remove(r);
				}
			}
		}
		
		for(Shader m : merge.keySet()){
			if(shadermap.containsKey(m)){
				shadermap.get(m).addAll(merge.get(m));
			}else{
				shadermap.put(m, merge.get(m));
			}
		}
		
		merge.clear();
		
		for(Shader shader : shadermap.keySet()){
			//GLOBAL SHADER VARIABLES
			shader.bind();
			Camera camera = this.camera;
			shader.setUniform("projection", camera.projection);
			shader.setUniform("view", camera.view);
			
			ArrayList<Renderable> rlist = shadermap.get(shader);
		
			for(Renderable renderable : rlist){
				if(renderable.matrix != null)
					shader.setUniform("model", renderable.matrix);
				
				if(renderable.material != null){
					shader.setUniform("color", renderable.material.color);
					if(renderable.material.colorOnly){
						shader.setUniform("color_only", true);
					}else{
						shader.setUniform("color_only", false);
						if(renderable.material.texture != null){
							if(renderable.material.isSubTexture()){
								shader.setUniform("sub_texture", true);
									
								TextureSheet sheet = TextureSheet.getSheet(renderable.material.texture);
									
								shader.setUniform("sub_offset", sheet.getOffset(renderable.material.subTexture));
								shader.setUniform("sub_size", sheet.subSize);
							}
							
							renderable.material.texture.bind();
						}else{
							shader.setUniform("color_only", true);
						}
					}
					//only reachable if material isn't null
					if(renderable.mesh != null)
						renderable.mesh.render();
				}
			}
			
		}
		
	}
}
