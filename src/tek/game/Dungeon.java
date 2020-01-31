package tek.game;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import tek.render.Mesh;
import tek.render.TextureSheet;

public class Dungeon {
	public Room[] rooms;
	
	public Dungeon(Room... rooms){
		this.rooms = rooms;
	}
	
	public static class Room {
		public Vector3f position, size;
		public RoomWall[] walls;
		
		public Room(Vector3f position, Vector3f size, RoomWall[] walls){
			this.position = position;
			this.size = size;
			this.walls = walls;
		}
		
		public Mesh toMesh(){
			return null;
		}
	}
	
	public static class RoomConstructor {
		public Vector3f position, size;
		
		public Vector3f min, max;
		
		public ArrayList<RoomWall> walls; 
		
		public RoomConstructor(){
			position = new Vector3f();
			size = new Vector3f();
			walls = new ArrayList<RoomWall>();
			
			min = new Vector3f();
			max = new Vector3f();
		}
		
		public void add(RoomWall wall){
			walls.add(wall);
			
			Face.getMin(wall, min);
			Face.getMax(wall, max);
		}
		
		public RoomWall makeWall(Face face, Vector3f offset, Vector2f size){
			RoomWall wall = new RoomWall(face, offset, size);
			walls.add(wall);
			return wall;
		}
		
		public void remove(RoomWall wall){
			walls.remove(wall);
			calculate();
		}
		
		private void calculate(){
			min.set(0);
			max.set(0);
			
			for(RoomWall wall : walls){
				Face.getMin(wall, min);
				Face.getMax(wall, max);
			}
		}
		
		public void clear(){
			position.set(0);
			size.set(0);
			min.set(0);
			max.set(0);
			walls.clear();
		}
		
		public Room toRoom(){
			RoomWall[] walls = new RoomWall[this.walls.size()];
			
			for(int i=0;i<walls.length;i++){
				walls[i] = this.walls.get(i);
			}
			
			clear();
			
			return new Room(position, size, walls);
		}
	}
	
	public static class RoomWall {
		public Face face;
		public WallPattern pattern;
		public Vector2f size;
		public Vector3f offset;
		
		public RoomWall(Face face){
			this.face = face;
			this.size = new Vector2f();
			this.offset = new Vector3f();
		}
		
		public RoomWall(Face face, Vector3f offset, Vector2f size){
			this.face = face;
			this.offset = offset;
			this.size = size;
		}
	}
	
	public static class WallPattern {
		public int CLEAR_TILE = 0;
		
		public final TextureSheet sheet;
		public final int width, height;
		public int[] tiles;
		
		public WallPattern(TextureSheet sheet, int width, int height){
			this.sheet = sheet;
			this.width = width;
			this.height = height;
			
			tiles = new int[width * height];
		}
		
		public void fill(int value){
			for(int i=0;i<tiles.length;i++)
				tiles[i] = value;
		}
		
		public void clear(){
			for(int i=0;i<tiles.length;i++)
				tiles[i] = CLEAR_TILE;
		}
		
		public WallPattern resize(int width, int height){
			WallPattern p = new WallPattern(sheet, width, height);
			for(int i=0;i<Math.min(p.tiles.length, tiles.length);i++)
				p.tiles[i] = tiles[i];
			return p;
		}
		
		public void set(int x, int y, int value){
			tiles[x + width * y] = value;
		}
		
		public int get(int x, int y){
			return tiles[x + width * y];
		}
		
		public void set(int id, int value){
			tiles[id] = value;
		}
		
		public int get(int id){
			return tiles[id];
		}
	}
	
	public static enum Face {
		NORTH,
		SOUTH,
		EAST,
		WEST,
		CEIL,
		FLOOR;
		
		public static void getMin(RoomWall wall, Vector3f dst){
			Vector2f size = wall.size;
			switch(wall.face){
			case NORTH:
			case SOUTH:
				dst.x = Math.min(size.x, dst.x);
				dst.y = Math.min(size.y, dst.y);
				break;
			case WEST:
			case EAST:
				dst.z = Math.min(size.x, dst.z);
				dst.y = Math.min(size.y, dst.y);
				break;
			case CEIL:
			case FLOOR:
				dst.z = Math.min(size.y, dst.z);
				dst.x = Math.min(size.x, dst.x);
				break;
			}
		}
		
		public static void getMax(RoomWall wall, Vector3f dst){
			Vector2f size = wall.size;
			switch(wall.face){
			case NORTH:
			case SOUTH:
				dst.x = Math.max(size.x, dst.x);
				dst.y = Math.max(size.y, dst.y);
				break;
			case WEST:
			case EAST:
				dst.z = Math.max(size.x, dst.z);
				dst.y = Math.max(size.y, dst.y);
				break;
			case CEIL:
			case FLOOR:
				dst.z = Math.max(size.y, dst.z);
				dst.x = Math.max(size.x, dst.x);
				break;
			}
		}
	}
}
