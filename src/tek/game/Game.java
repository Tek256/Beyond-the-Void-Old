package tek.game;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.openal.EXTEfx;

import tek.Application;
import tek.ResourceLoader;
import tek.audio.Mixer;
import tek.audio.Music;
import tek.audio.Effect;
import tek.audio.Effect.EffectType;
import tek.input.Keyboard;
import tek.input.Mouse;
import tek.render.Camera;
import tek.render.Material;
import tek.render.Mesh;
import tek.render.MeshBuilder;
import tek.render.Shader;
import tek.render.Texture;
import tek.render.TextureSheet;
import tek.render.Window;
import tek.runtime.Renderable;
import tek.runtime.Scene;

public class Game implements GameInterface {
	//basic transform (only missing scale)
	Vector3f pos, rotation;
	
	//renderable object information
	Matrix4f model;
	Shader shader;
	Mesh mesh;
	Texture texture;
	
	//generic global rendering system thing
	Camera camera;
	
	Mixer mixer;
	Music music;
	
	Scene scene;
	Renderable renderable;
	
	Player player;
	
	TextureSheet sheet;
	
	@Override
	public void start() {
		Window.instance.setIcon("textures/icon16.png", "textures/icon32.png");
		
		Keyboard.setupButton("horizontal", false, Keyboard.KEY_D, Keyboard.KEY_RIGHT, Keyboard.KEY_A, Keyboard.KEY_LEFT);
		Keyboard.setupButton("vertical", false, Keyboard.KEY_W, Keyboard.KEY_UP, Keyboard.KEY_S, Keyboard.KEY_DOWN);
		
		/* this is all just arbitrary code for testing */
		
		String vert = ResourceLoader.getText("shaders/simple.vert");
		String frag = ResourceLoader.getText("shaders/simple.frag");
		shader = new Shader("test", vert, frag);
		
		/* represents a renderable object */
		model = new Matrix4f();
		texture = new Texture("textures/texturesheet.png");
		sheet = new TextureSheet(texture, 16, 16);
		
		//model mesh creation
		MeshBuilder builder = new MeshBuilder();
		builder.addCube(Vector3f.ZERO, new Vector3f(1f), Vector3f.ZERO);
		mesh = builder.toMesh();
		
		/* represents transform */
		pos = new Vector3f(0f, 0f, -2f);
		rotation = new Vector3f(0f, 45f, 0f);

		
		model = new Matrix4f(); //matrix
		model.translate(pos);
		model.rotateX((float)(Math.toRadians(rotation.x)));
		model.rotateY((float)(Math.toRadians(rotation.y)));
		model.rotateZ((float)(Math.toRadians(rotation.z)));
		
		
		player = new Player();
		
		/* actually useful - global rendering variable */
		camera = new Camera(100f, 1280f / 720f);
				
		Material mat = new Material(texture, new Vector4f(0.8f, 0.8f, 0.8f, 1f));
		mat.subTexture = 20;
		
		renderable = new Renderable(mesh, mat);
		renderable.matrix = model; //set the correct matrix
		renderable.shader = shader;
		
		scene = new Scene();
		scene.camera = camera;
		scene.add(renderable);
		
		mixer = new Mixer();
		
		music = new Music("audio/bg.ogg");
		music.setLoop(true);
		music.setGain(1f);
		//music.play();
		
		Effect e = new Effect(EffectType.REVERB);
		e.set(EXTEfx.AL_REVERB_DENSITY, 0.9f);
		e.set(EXTEfx.AL_REVERB_GAIN, 0.8f);
		e.set(EXTEfx.AL_REVERB_DECAY_TIME, 10f);
		
		mixer.add(e, music);
		mixer.checkError();
		
		/* let the console be alive*/
		Application.log("start of game");
	}

	@Override
	public void exit() {
		shader.destroy();
		mesh.destroy();
		texture.destroy();
	}
	
	StringBuilder message = new StringBuilder();
	@Override
	public void input(long delta) {
		//enable / disable character tracking
		if(Keyboard.isClicked(GLFW.GLFW_KEY_TAB)){
			Keyboard.setCharTracking(!Keyboard.isCharTracking());
			Application.log("Character tracking: " + (Keyboard.isCharTracking()? "enabled" : "disabled"));
		}
		
		if(Mouse.isGrabbed()){
			float y = 0f;
			if(Keyboard.isDown(Keyboard.KEY_SPACE)){
				if(Keyboard.isDown(Keyboard.KEY_LEFT_CONTROL))
					y = -1;
				else
					y = 1;
			}
			player.rotate((float)Mouse.getDY() * 0.01f, (float)Mouse.getDX() * 0.01f);
			player.move(Keyboard.getButton("horizontal") * delta * 0.01f, y * delta * 0.01f, Keyboard.getButton("vertical") * delta * 0.01f);
		}
		
		if(Keyboard.isClicked('p'))
			music.play();
		else if(Keyboard.isClicked('o'))
			music.pause();
		else if(Keyboard.isClicked('i'))
			music.stop();
		
		//if character tracking
		if(Keyboard.isCharTracking()){
			//get each character typed
			ArrayList<Character> next = Keyboard.getChars();
			if(next.size() != 0){
				for(char c : next)
					//add it to the message
					message.append(c);
				next.clear();
			}
			
			//if backspace, remove the last character
			if(Keyboard.isDown(GLFW.GLFW_KEY_BACKSPACE))
				if(message.length() > 0)
					message.setLength(message.length() - 1);
			
			//if enter, output the message and clear the message buffer
			if(Keyboard.isClicked(GLFW.GLFW_KEY_ENTER) && message.length() > 0){
				Application.log(message.toString());
				message.setLength(0);
			}
		}
		
		if(Mouse.isClicked(Mouse.LEFT_BUTTON)){
			Mouse.setCursorMode(0);
		}
		
		if(Keyboard.isClicked(GLFW.GLFW_KEY_ESCAPE)){
			Mouse.setCursorMode(2);
		}
	}

	@Override
	public void update(long delta) {
		camera.set(player.position, player.rotation);
		music.update();
	}

	@Override
	public void render(long delta) {
		scene.render();
	}

}
