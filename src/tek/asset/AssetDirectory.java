package tek.asset;

import java.io.UnsupportedEncodingException;

/* 
 * Primarily experrimental for blob files/directories
 * 
 */

public class AssetDirectory {
	public static final String AUDIO_TOKEN = "A";
	public static final String SONG_TOKEN  = "SN";
	public static final String SHADER_TOKEN = "SH";
	public static final String TEXTURE_TOKEN = "T";
	public static final String TEXTURE_MAP_TOKEN = "TM";
	public static final String PREFAB_TOKEN = "PFB";
	
	public final String raw;
	public String[] directory;
	public Asset[] assets;
	
	public AssetDirectory(String raw){
		this.raw = raw;
		this.directory = raw.split(";")[0].split("\n"); //each line new asset
		
		assets = new Asset[directory.length];
		
		for(String direction : directory){
			String uppr = direction.toUpperCase();
			AssetType t = getType(uppr);
			
			switch(t){
			case SHADER:
				
				break;
			case TEXTURE:
				break;
			case TEXTURE_MAP:
				break;
			case AUDIO:
				break;
			case SONG:
				break;
			case PREFAB:
				
				break;
			case UNKNOWN:
				break;
			}
		}
	}
	
	public static AssetType getType(String uppercase_string){
		if(uppercase_string.contains(":"))
			uppercase_string = uppercase_string.split(":")[0];
		
		for(AssetType t : AssetType.values())
			if(t.getPrefix().equals(uppercase_string))
				return t;
		return AssetType.UNKNOWN;
	}
	
	public static final String decode(byte[] b){
		try {
			return new String(b, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static final byte[] encode(String str){
		try{
			str.getBytes("UTF-8");
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		
		return new byte[0];
	}
	
	public static enum AssetType {
		SHADER,
		TEXTURE,
		TEXTURE_MAP,
		PREFAB,
		AUDIO,
		SONG,
		UNKNOWN;
		
		public String getPrefix(){
			switch(this){
			case SHADER:
				return SHADER_TOKEN;
			case TEXTURE:
				return TEXTURE_TOKEN;
			case TEXTURE_MAP:
				return TEXTURE_MAP_TOKEN;
			case PREFAB:
				return PREFAB_TOKEN;
			case AUDIO:
				return AUDIO_TOKEN;
			case SONG:
				return SONG_TOKEN;
			default:
				return "";
			}
		}
	}
	
}
