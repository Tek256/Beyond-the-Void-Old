package tek.asset;

import tek.asset.AssetDirectory.AssetType;

public class Asset {
	public final String contents;
	public final AssetType type;
	
	public Asset(String contents, AssetType type){
		this.contents = contents;
		this.type = type;
	}
	public Object instantiate(){ 
		//
		return null; 
	}
}
