package tek.audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.EXTEfx;

import tek.Application;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.EXTEfx.*;

public class Mixer {
	public float musicGain = 0.6f;
	public float sfxGain = 0.7f;
	public float masterGain = 0.8f;
	
	public AuxiliaryEffect[] effects;
	
	private long device, context;
	
	public Mixer(){
		initialize();
	}
	
	private void initialize(){
		device = alcOpenDevice((ByteBuffer)null);
		if(device == 0L){
			Application.error("Unable to open default audio device");
			return;
		}
		
		ALCCapabilities deviceCaps = ALC.createCapabilities(device);
		
		if(!deviceCaps.OpenALC10){
			Application.error("OpenALC10 Unsupported");
			return;
		}
		
		ContextAttributes attribs = new ContextAttributes();
		attribs.put(ALC_REFRESH, 60);
		attribs.put(ALC_SYNC, ALC_FALSE);
		attribs.put(EXTEfx.ALC_MAX_AUXILIARY_SENDS, 2);
		attribs.finalize();
		
		context = alcCreateContext(device, attribs.attribList);
		if(context == 0L){
			Application.error("Unable to create ALC Context");
			return;
		}
		ALC10.alcMakeContextCurrent(context);
		AL.createCapabilities(deviceCaps);
		
		effects = new AuxiliaryEffect[2];
		for(int i=0;i<effects.length;i++)
			effects[i] = new AuxiliaryEffect();
	}
	
	public void checkError(){
		int error = AL10.alGetError();
		if(error == AL10.AL_NO_ERROR)
			return;
		Application.error(AL10.alGetString(error));
	}
	
	public void add(Effect effect, Music music){
		int slot = add(effect);
		AL11.alSource3i(music.source, EXTEfx.AL_AUXILIARY_SEND_FILTER, slot, 0, EXTEfx.AL_FILTER_NULL);
	}
	
	public int add(Effect effect){
		boolean added = false;
		for(int i=0;i<effects.length;i++){
			if(effects[i].effect == null){
				effects[i].attach(effect);
				added = true;
				return i;
			}
		}
		
		if(!added)
			Application.error("unable to add effect");
		return 0;
	}
	
	public void remove(Effect effect){
		
	}
	
	public int getInt(int val){
		return ALC10.alcGetInteger(device, val);
	}
	
	public void destroy(){
		alcMakeContextCurrent(0L); //close out context
		alcDestroyContext(context);
		//close out devices
		alcCloseDevice(device);
		//destroy anything else
		ALC.destroy();
	}
	
	public static class ContextAttributes {
		public IntBuffer attribList;
		
		{
			attribList = BufferUtils.createIntBuffer(16);
		}
		
		public void put(int target, int value){
			attribList.put(target);
			attribList.put(value);
		}
		
		public void finalize(){
			attribList.put(0);
			attribList.flip();
		}
	}
	
	public static class AuxiliaryEffect {
		public final int id;
		public Effect effect;
		
		public AuxiliaryEffect(){
			id = EXTEfx.alGenAuxiliaryEffectSlots();
			EXTEfx.alAuxiliaryEffectSlotf(id, AL_EFFECTSLOT_GAIN, 1.0f);
			EXTEfx.alAuxiliaryEffectSloti(id, EXTEfx.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL10.AL_TRUE);
		}
		
		public  void attach(Effect effect){
			if(this.effect == null){
				EXTEfx.alAuxiliaryEffectSloti(id, AL_EFFECTSLOT_EFFECT, effect.id);
				this.effect = effect;
			}
		}
		
		public void detach(){
			if(effect != null){
				EXTEfx.alAuxiliaryEffectSloti(id, AL_EFFECTSLOT_EFFECT, 0);
				effect = null;
			}
		}
		
		public void destory(){
			EXTEfx.alDeleteAuxiliaryEffectSlots(id);
			effect.destroy();
		}
	}
	
	public static class AudioDevice {
		public final long handle;
		public final String name;
		public final ALCCapabilities caps;
		
		public AudioDevice(long handle){
			this.handle = handle;
			name = ALC10.alcGetString(handle, ALC10.ALC_DEVICE_SPECIFIER);
			caps = ALC.createCapabilities(handle);
		}
	}

}
