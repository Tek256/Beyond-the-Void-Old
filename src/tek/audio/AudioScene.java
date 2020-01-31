package tek.audio;

import java.util.ArrayList;

import org.joml.Vector2f;

public class AudioScene {
	
	public ArrayList<AudioRoom> rooms;
	
	public Listener listener;
	
	// LOCAL INITIALIZATION
	{
		rooms = new ArrayList<AudioRoom>();
	}
	
	public AudioScene(){
	}
	
	public void addRoom(AudioRoom room){
		if(!rooms.contains(room))
			rooms.add(room);
	}
	
	public void removeRoom(AudioRoom room){
		if(rooms.contains(room))
			rooms.remove(room);
	}
	
	public static class AudioRoom {
		public final float width, height, x, y;
		public final Vector2f[] doors;
		
		public AudioRoom(float x, float y, float w, float h, Vector2f... doors){
			this.width = w;
			this.height = h;
			this.x = x;
			this.y = y;
			this.doors = doors;
		}
		
		@Override
		public boolean equals(Object o){
			if(o == null) return false;
			if(!o.getClass().equals(getClass())) return false;
			
			AudioRoom a = (AudioRoom)o;
			
			return a.x == x && a.y == y &&
					a.width == width && a.height == height;
		}
	}
}
