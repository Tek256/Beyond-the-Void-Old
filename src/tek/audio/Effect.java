package tek.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.EXTEfx.*;

import java.util.ArrayList;

import org.lwjgl.openal.AL10;

import tek.Application;

public class Effect {
	public static ArrayList<EffectSupport> supported;
	
	static {
		supported = new ArrayList<EffectSupport>();
	}
	
	private EffectType type;
	public int id;
	
	public Effect(int type){
		this(EffectType.get(type));
	}
	
	public Effect(EffectType type){
		if(isEffectSupported(type)){
			id = alGenEffects();
			alEffecti(id, AL_EFFECT_TYPE, type.al);
			
			if(alGetError() != AL_NO_ERROR)
				Application.error("Unsupported");
			
			this.type = type;
		}else{
			System.out.println("Unsupported error");
		}
	}
	
	public void set(int param, float ... val){
		if(val.length == 1)
			alEffectf(id, param, val[0]);
		else
			alEffectfv(id, param, val);
	}
	
	public void set(int param, int... val){
		if(val.length == 1)
			alEffecti(id, param, val[0]);
		else
			alEffectiv(id, param, val);
	}
	
	public EffectType getType(){
		return type;
	}
	
	public void destroy(){
		alDeleteEffects(id);
	}
	
	public static boolean isEffectSupported(EffectType type){
		boolean support = false;
		
		for(EffectSupport s : supported){
			if(s.type == type){
				return s.support;
			}
		}
		
		if(!support){
			EffectSupport s = new EffectSupport(type, type.isSupported());
			supported.add(s);
			support = s.support;
		}
		
		return support;
	}
		
	public static class EffectSupport {
		public final boolean support;
		public final EffectType type;
		
		public EffectSupport(EffectType type, boolean support){
			this.type = type;
			this.support = support;
		}
	}
	
	public static enum EffectType {
		REVERB(AL_EFFECT_REVERB),
		CHORUS(AL_EFFECT_CHORUS),
		DISTORTION(AL_EFFECT_DISTORTION),
		ECHO(AL_EFFECT_ECHO),
		FLANGER(AL_EFFECT_FLANGER),
		FREQUENCY_SHIFTER(AL_EFFECT_FREQUENCY_SHIFTER),
		EQUALIZER(AL_EFFECT_EQUALIZER);
		
		int al;
		
		EffectType(int AL_VALUE){
			al = AL_VALUE;
		}
		
		public boolean isSupported(){
			boolean supported = false;
			
			int error;
			int object = 0;
			try{
				object = alGenEffects();
				error = alGetError();
			}catch(RuntimeException e){
				if(e.getMessage().contains("OUT_OF_MEMORY"))
					error = AL_OUT_OF_MEMORY;
				else
					error = AL_INVALID_OPERATION;
			}
			
			if(error == AL_NO_ERROR){
				alGetError();
				int setError;
				try{
					alEffecti(object, AL_EFFECT_TYPE, al);
					setError = alGetError();
				}catch(RuntimeException e){
					setError = AL_INVALID_VALUE;
				}
				
				if(setError == AL_NO_ERROR)
					supported = true;
				
				try{
					alDeleteEffects(object);
				}catch(RuntimeException e){
				}
			} else if(error == AL_OUT_OF_MEMORY){
				throw new RuntimeException(AL10.alGetString(error));
			}
			
			return supported;
		}
		
		public static EffectType get(int AL_VALUE){
			switch(AL_VALUE){
			case AL_EFFECT_REVERB:
				return REVERB;
			case AL_EFFECT_CHORUS:
				return CHORUS;
			case AL_EFFECT_DISTORTION:
				return DISTORTION;
			case AL_EFFECT_ECHO:
				return ECHO;
			case AL_EFFECT_FLANGER:
				return FLANGER;
			case AL_EFFECT_FREQUENCY_SHIFTER:
			return FREQUENCY_SHIFTER;
			case AL_EFFECT_EQUALIZER:
				return EQUALIZER;
			default:
				throw new IllegalArgumentException("Invalid AL_VALUE");
			}
		}
	}
	
}
