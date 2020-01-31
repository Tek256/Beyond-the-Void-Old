package tek.audio;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import tek.Application;

import static org.lwjgl.openal.AL10.*;

import java.nio.FloatBuffer;

public class Source {
	public final int id;
	private boolean dead = false;
	private FloatBuffer posbuffer;
	
	private boolean play = false, pause = false; //stopped = false false, paused = 0 + 1 or 1 + 1, playing = 1 + 0
	private boolean loop = false;
	
	public Vector3f position;
	
	{
		position = new Vector3f();
		posbuffer = BufferUtils.createFloatBuffer(3);
	}
	
	public Sound sound;
	
	protected Source(){
		id = alGenSources();
	}
	
	public Source(Sound sound){
		this.sound = sound;
		
		posbuffer.put(new float[]{
				position.x,
				position.y,
				position.z});
		posbuffer.flip();
		
		id = alGenSources();
		
		AL10.alSourcei(id, AL10.AL_BUFFER, sound.id);
		AL10.alSourcefv(id, AL10.AL_POSITION, posbuffer);
		
		if(AL10.alGetError() != AL10.AL_NO_ERROR)
			System.err.println("AL ERROR");
	}
	
	public void setSound(Sound sound){
		if(this.sound == sound)
			return;
		
		AL10.alSourcei(id, AL10.AL_BUFFER, sound.id);
	}
	
	public void play(){
		if(isPlaying())
			return;
		play = true;
		pause = false;
		alSourcePlay(id);
	}
	
	public void pause(){
		if(isPaused())
			return;
		pause = true;
		alSourcePause(id);
	}
	
	public void stop(){
		if(isStopped())
			return;
		pause = false;
		play = false;
		alSourceStop(id);
	}
	
	public boolean isPlaying(){
		return play && !pause;
	}
	
	public boolean isPaused(){
		return pause;
	}
	
	public boolean isStopped(){
		return !play && !pause;
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public void setLoop(boolean loop){
		this.loop = loop;
		alSourcei(id, AL_LOOPING, (loop) ? 1 : 0);
	}
	
	public boolean isLooping(){
		return loop;
	}
	
	public void destroy(){
		if(dead)
			return;
		
		alDeleteSources(id);
		dead = true;
	}
}
